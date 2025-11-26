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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoCRUD {
    
     public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
         String SQL = "SELECT idProducto, nombreProducto, descripcion, precio, stock FROM PRODUCTOS WHERE stock > 0 ORDER BY idProducto"; 
        
         try (Connection con = SQLConection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Producto p = new Producto(
                    rs.getInt("idProducto"),
                    rs.getString("nombreProducto"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                );
                productos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos de la BD: " + e.getMessage());
        }
        return productos;
    }
    
     public Producto obtenerPorId(int id) {
        Producto producto = null;
        String SQL = "SELECT idProducto, nombreProducto, descripcion, precio, stock FROM PRODUCTOS WHERE idProducto = ?";
        
        try (Connection con = SQLConection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(SQL)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    producto = new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("nombreProducto"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener producto por ID: " + e.getMessage());
        }
        return producto;
    }

     public void actualizarStock(int idProducto, int cantidadVendida) throws SQLException {
        String SQL_STOCK = "UPDATE PRODUCTOS SET stock = stock - ? WHERE idProducto = ?";
        
        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_STOCK)) {
            
            pstmt.setInt(1, cantidadVendida);
            pstmt.setInt(2, idProducto);
            
            if (pstmt.executeUpdate() == 0) {
                 throw new SQLException("Fallo al actualizar stock, no se encontró el producto.");
            }
            
        } catch (SQLException e) {
             System.err.println("Error FATAL al actualizar el stock del producto " + idProducto + ": " + e.getMessage());
             throw e; // Lanza la excepción para que la capa superior maneje el fallo de persistencia
        }
    }
}