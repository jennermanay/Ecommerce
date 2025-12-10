/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConexionSQL;
import ecommerce.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Jenner Jordy
 */
public class PedidoCRUD {
    private ProductoCRUD productoCRUD = new ProductoCRUD(); 

    public int obtenerUltimoIdPedido() {
 Connection conn = null;
    int ultimoId = 0;
    String sql = "SELECT ISNULL(MAX(idPedido), 0) AS ultimoId FROM PEDIDOS";
try {
             conn = SQLConection.getConnection();
            conn.setAutoCommit(false);
    try (PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        if (rs.next()) {
            ultimoId = rs.getInt("ultimoId");
        }
    }
    } catch (SQLException e) { try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback fallido: " + ex.getMessage());
            }
            System.err.println("Error al guardar pedido: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) { }
        }

    return ultimoId;
}
    public boolean guardarPedido(Pedido pedido) {
        
        String SQL_PEDIDO = "INSERT INTO PEDIDOS (idPedido, idCliente, idMetodoPago, total, estadoPedido, direccionEntrega) VALUES (?, ?, ?, ?, ?, ?)";
        String SQL_DETALLE = "INSERT INTO DETALLE_PEDIDO (idPedido, idProducto, cantidad, precioUnitario) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        boolean exito = false;
        try {
            conn = SQLConection.getConnection();
            conn.setAutoCommit(false); 

            try (PreparedStatement pstmtPedido = conn.prepareStatement(SQL_PEDIDO)) {
                pstmtPedido.setInt(1, pedido.getIdPedido());
                pstmtPedido.setInt(2, pedido.getIdCliente());
                pstmtPedido.setInt(3, pedido.getIdMetodoPago());
                pstmtPedido.setDouble(4, pedido.getTotal());
                pstmtPedido.setString(5, "POR_ENTREGAR"); 
                pstmtPedido.setString(6, pedido.getDireccion()); 
                pstmtPedido.executeUpdate();
            }

            for (DetallePedido dp : pedido.getDetalles()) {
                try (PreparedStatement pstmtDetalle = conn.prepareStatement(SQL_DETALLE)) {
                    pstmtDetalle.setInt(1, pedido.getIdPedido());
                    pstmtDetalle.setInt(2, dp.getProducto().getIdProducto());
                    pstmtDetalle.setInt(3, dp.getCantidad());
                    pstmtDetalle.setDouble(4, dp.getPrecioUnitario());
                    pstmtDetalle.executeUpdate();
                }

                productoCRUD.descontarStock(dp.getProducto().getIdProducto(), dp.getCantidad(), conn); 
            }
            
            conn.commit(); 
            exito = true;
            System.out.println("Pedido No. " + pedido.getIdPedido() + " guardado con exito.");

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback fallido: " + ex.getMessage());
            }
            System.err.println("Error al guardar pedido: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) { }
        }
        return exito;
    }

    public boolean confirmarEntrega(int idPedido, int idDelivery) throws SQLException {
        String SQL = "UPDATE PEDIDOS SET estadoPedido = 'ENTREGADO', idDelivery = ?, horaEntrega = ? WHERE idPedido = ? AND estadoPedido = 'POR_ENTREGAR'";
       
        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, idDelivery);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, idPedido);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
        public List<String> obtenerPedidosPendientes() {
        List<String> pendientes = new ArrayList<>();
        String SQL = "SELECT P.idPedido, P.direccionEntrega, U.telefono, U.nombre AS nombreCliente, P.total FROM PEDIDOS P JOIN USUARIOS U ON P.idCliente = U.idUsuario WHERE P.estadoPedido = 'POR_ENTREGAR'";
        
        try (Connection conn = SQLConection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            
            while(rs.next()) {
                String linea = String.format("Pedido ID: %d | Cliente: %s | Total: $%.2f | Direccion: %s | Telefono: %d", 
                                             rs.getInt("idPedido"), 
                                             rs.getString("nombreCliente"), 
                                             rs.getDouble("total"),
                                             rs.getString("direccionEntrega"),
                                             rs.getInt("telefono"));
                pendientes.add(linea);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener pedidos pendientes: " + e.getMessage());
        }
        return pendientes;
    }
    
    public List<String> obtenerPedidosCliente(int idCliente) {
        List<String> historial = new ArrayList<>();
        String SQL = "SELECT P.idPedido, P.total, P.estadoPedido, P.horaEntrega, P.idDelivery, U.nombre, U.telefono FROM PEDIDOS P left join USUARIOS U on P.idDelivery=U.idUsuario WHERE idCliente = ? ORDER BY idPedido DESC";
        
        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, idCliente);
            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    String estado = rs.getString("estadoPedido");
                    String entrega = rs.getTimestamp("horaEntrega") != null ? rs.getTimestamp("horaEntrega").toString() : "N/A";
                    String deliveryId = rs.getObject("idDelivery") != null ? rs.getString("idDelivery") : "N/A";
                    String nombre = rs.getObject("nombre") != null ? rs.getString("nombre") : "N/A";
                    String telefono = rs.getObject("telefono") != null ? rs.getString("telefono") : "N/A";

                    String linea = String.format("ID: %d | Total: $%.2f | Estado: %s | ID Delivery: %s |Nombre Delivery: %s | Telefono: %s | Entrega: %s", 
                                                 rs.getInt("idPedido"), 
                                                 rs.getDouble("total"), 
                                                 estado, 
                                                 deliveryId,
                                                 nombre,
                                                 telefono, 
                                                 entrega);
                    historial.add(linea);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener pedidos del cliente: " + e.getMessage());
        }
        return historial;
    }
    
    public List<String> obtenerTodasOrdenes() {
        List<String> todas = new ArrayList<>();
        String SQL = "SELECT idPedido, idCliente, total, estadoPedido, idDelivery FROM PEDIDOS ORDER BY idPedido DESC";
        
        try (Connection conn = SQLConection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            
            while(rs.next()) {
                String deliveryId = rs.getObject("idDelivery") != null ? rs.getString("idDelivery") : "N/A";
                String linea = String.format("ID: %d | Cliente ID: %d | Total: $%.2f | Estado: %s | Delivery ID: %s", 
                                             rs.getInt("idPedido"), 
                                             rs.getInt("idCliente"),
                                             rs.getDouble("total"), 
                                             rs.getString("estadoPedido"), 
                                             deliveryId);
                todas.add(linea);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las ordenes: " + e.getMessage());
        }
        return todas;
    }
}
