/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConexionSQL;
import ecommerce.MetodoPago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Jenner Jordy
 */
public class MetodoPagoCRUD {
  
    public List<MetodoPago> obtenerTodos() {
        List<MetodoPago> lista = new ArrayList<>();
        String SQL = "SELECT idMetodoPago, nombreMetodo, numeroCuenta FROM METODOS_PAGO";

        try (Connection conn = SQLConection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                lista.add(new MetodoPago(
                    rs.getInt("idMetodoPago"),
                    rs.getString("nombreMetodo"),
                    rs.getString("numeroCuenta")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener metodos de pago: " + e.getMessage());
        }
        return lista;
    }
    
    public void agregarMetodo(MetodoPago mp) throws SQLException {
        String SQL = "INSERT INTO METODOS_PAGO (nombreMetodo, numeroCuenta) VALUES (?, ?)";
        
        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, mp.getNombreMetodo());
            pstmt.setString(2, mp.getNumeroCuenta());
            
            pstmt.executeUpdate();
            System.out.println("Metodo de pago " + mp.getNombreMetodo() + " agregado.");

        } catch (SQLException e) {
            System.err.println("Error al agregar metodo de pago: " + e.getMessage());
            throw e;
        }
    }
    
    public void editarMetodo(MetodoPago mp) throws SQLException {
        String SQL = "UPDATE METODOS_PAGO SET nombreMetodo = ?, numeroCuenta = ? WHERE idMetodoPago = ?";
        
        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, mp.getNombreMetodo());
            pstmt.setString(2, mp.getNumeroCuenta());
            pstmt.setInt(3, mp.getIdMetodoPago());
            
            pstmt.executeUpdate();
            System.out.println("Metodo de pago " + mp.getIdMetodoPago() + " editado.");

        } catch (SQLException e) {
            System.err.println("Error al editar metodo de pago: " + e.getMessage());
            throw e;
        }
    }
    
        public void eliminarMetodo(Integer mp) throws SQLException {
        String SQL = "DELETE FROM METODOS_PAGO WHERE idMetodoPago = ?";
        
        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, mp);
            
            int filas = pstmt.executeUpdate();
             if (filas > 0) {
                 System.out.println("Categoria ID " + mp + " eliminada con exito.");
            } else {
                 System.out.println("Categoria ID " + mp + " no encontrada.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar Metodo de Pago (Verifique dependencias, ej: Pedidos): " + e.getMessage());
            throw e;
        }
    }
}
