/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConexionSQL;

/**
 *
 * @author Jenner Jordy
 */
import ecommerce.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoCRUD {
    
    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        String SQL = "SELECT idProducto, nombreProducto, descripcion, precio, stock, idCategoria FROM PRODUCTOS";
        
        try (Connection conn = SQLConection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            
            while (rs.next()) {
                productos.add(new Producto(
                    rs.getInt("idProducto"),
                    rs.getString("nombreProducto"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getInt("idCategoria")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los productos: " + e.getMessage());
        }
        return productos;
    }

    public Producto obtenerPorId(int id) {
        String SQL = "SELECT idProducto, nombreProducto, descripcion, precio, stock, idCategoria FROM PRODUCTOS WHERE idProducto = ?";
        Producto producto = null;
        
        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    producto = new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("nombreProducto"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getInt("idCategoria")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener producto por ID: " + e.getMessage());
        }
        return producto;
    }
    
    public void agregarProducto(Producto p) throws SQLException {
        String SQL = "INSERT INTO PRODUCTOS (nombreProducto, descripcion, precio, stock, idCategoria) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, p.getNombreProducto());
            pstmt.setString(2, p.getDescripcion());
            pstmt.setDouble(3, p.getPrecio());
            pstmt.setInt(4, p.getStock());
            pstmt.setInt(5, p.getIdCategoria());
            
            pstmt.executeUpdate();
            System.out.println("Producto " + p.getNombreProducto() + " agregado con exito.");
        } catch (SQLException e) {
            System.err.println("Error al agregar producto: " + e.getMessage());
            throw e;
        }
    }
    
    public void aumentarStock(int idProducto, int cantidad) throws SQLException {
        String SQL = "UPDATE PRODUCTOS SET stock = stock + ? WHERE idProducto = ?";
        
        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, idProducto);
            
            if (pstmt.executeUpdate() > 0) {
                 System.out.println("Stock actualizado para producto ID " + idProducto);
            } else {
                 System.out.println("Producto ID " + idProducto + " no encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Error al aumentar stock: " + e.getMessage());
            throw e;
        }
    }

    public void descontarStock(int idProducto, int cantidad, Connection conn) throws SQLException {
        String SQL = "UPDATE PRODUCTOS SET stock = stock - ? WHERE idProducto = ? AND stock >= ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, idProducto);
            pstmt.setInt(3, cantidad);

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("Error de stock: No hay suficiente stock para el producto ID: " + idProducto);
            }
        }
    }
    
        public void editarProducto(Producto p) throws SQLException {
        String SQL = "UPDATE PRODUCTOS SET nombreProducto = ?, descripcion = ?, precio = ?, stock = ?, idCategoria = ? WHERE idProducto = ?";

        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, p.getNombreProducto());
            pstmt.setString(2, p.getDescripcion());
            pstmt.setDouble(3, p.getPrecio());
            pstmt.setInt(4, p.getStock());
            pstmt.setInt(5, p.getIdCategoria());
            pstmt.setInt(6, p.getIdProducto());

            pstmt.executeUpdate();
            System.out.println("Producto " + p.getNombreProducto() + " editado con exito.");

        } catch (SQLException e) {
            System.err.println("Error al editar producto: " + e.getMessage());
            throw e; 
        }
    }
    
    public void eliminarProducto(int id) throws SQLException {
        String SQL = "DELETE FROM PRODUCTOS WHERE idProducto = ?";
        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Producto ID " + id + " eliminado con exito.");

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            throw e; 
        }
    } 
}
