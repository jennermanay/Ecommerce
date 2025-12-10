/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConexionSQL;
import ecommerce.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Jenner Jordy
 */
public class CategoriaCRUD {
    public List<Categoria> obtenerTodas() {
        List<Categoria> lista = new ArrayList<>();
        String SQL = "SELECT idCategoria, nombreCategoria FROM CATEGORIAS order by idCategoria";

        try (Connection conn = SQLConection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                lista.add(new Categoria(
                    rs.getInt("idCategoria"),
                    rs.getString("nombreCategoria")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener categorias: " + e.getMessage());
        }
        return lista;
    }
    
    public void agregarCategoria(Categoria c) throws SQLException {
        String SQL = "INSERT INTO CATEGORIAS (nombreCategoria) VALUES (?)";
        
        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, c.getNombreCategoria());
            
            pstmt.executeUpdate();
            System.out.println("Categoria " + c.getNombreCategoria() + " agregada con exito.");

        } catch (SQLException e) {
            System.err.println("Error al agregar categoria: " + e.getMessage());
            throw e;
        }
    }
    
    public void editarCategoria(Categoria c) throws SQLException {
        String SQL = "UPDATE CATEGORIAS SET nombreCategoria = ? WHERE idCategoria = ?";
        
        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, c.getNombreCategoria());
            pstmt.setInt(2, c.getIdCategoria());
            
            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                 System.out.println("Categoria ID " + c.getIdCategoria() + " actualizada con exito.");
            } else {
                 System.out.println("Categoria ID " + c.getIdCategoria() + " no encontrada para actualizar.");
            }

        } catch (SQLException e) {
            System.err.println("Error al editar categoria: " + e.getMessage());
            throw e;
        }
    }
    
    public void eliminarCategoria(int idCategoria) throws SQLException {
        String SQL = "DELETE FROM CATEGORIAS WHERE idCategoria = ?";
        
        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, idCategoria);
            
            int filas = pstmt.executeUpdate();
             if (filas > 0) {
                 System.out.println("Categoria ID " + idCategoria + " eliminada con exito.");
            } else {
                 System.out.println("Categoria ID " + idCategoria + " no encontrada.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar categoria (Verifique dependencias, ej: Productos): " + e.getMessage());
            throw e;
        }
    }
}
