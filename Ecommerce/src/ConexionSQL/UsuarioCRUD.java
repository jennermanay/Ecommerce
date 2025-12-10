/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConexionSQL;


import ecommerce.Usuario;
import ecommerce.Cliente;
import ecommerce.Delivery;
import ecommerce.Administrador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioCRUD {

    public Usuario login(String email, String password) {
        String SQL = "SELECT idUsuario, nombre, tipoUsuario, direccion, telefono FROM USUARIOS WHERE email = ? AND password = ?";
        Usuario usuario = null;

        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("idUsuario");
                    String nombre = rs.getString("nombre");
                    String tipo = rs.getString("tipoUsuario");
                    String direccion = rs.getString("direccion");
                    String telefono = rs.getString("telefono");

                    if (tipo.equalsIgnoreCase("Cliente")) {
                        // El password ya lo tenemos del input del usuario
                        usuario = new Cliente(id, nombre, email, password, direccion, telefono);
                    } else if (tipo.equalsIgnoreCase("Admin")) {
                        usuario = new Administrador(id, nombre, email, password);
                    } else if (tipo.equalsIgnoreCase("Delivery")) {
                        usuario = new Delivery(id, nombre, email, password, direccion, telefono);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en login: " + e.getMessage());
        }
        return usuario;
    }
    
    public void agregarUsuario(Usuario u) throws SQLException {
        String SQL = "INSERT INTO USUARIOS (nombre, email, password, tipoUsuario, direccion, telefono) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, u.getNombre());
            pstmt.setString(2, u.getEmail());
            pstmt.setString(3, u.getPassword());
            
            String tipo = "Cliente";
            if (u instanceof Administrador) tipo = "Admin";
            else if (u instanceof Delivery) tipo = "Delivery";
            pstmt.setString(4, tipo);
            
            String direccion = null;
            String telefono = null;
            
            if (u instanceof Cliente) {
                 direccion = ((Cliente) u).getDireccion();
                 telefono = ((Cliente) u).getTelefono();
            } else if (u instanceof Delivery) {
            }
            
            pstmt.setString(5, direccion); 
            pstmt.setString(6, telefono); 

            pstmt.executeUpdate();
            System.out.println("Usuario " + u.getNombre() + " agregado con exito.");

        } catch (SQLException e) {
            System.err.println("Error al agregar usuario: " + e.getMessage());
            throw e; 
        }
    }
    
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String SQL = "SELECT idUsuario, nombre, email, password, tipoUsuario, direccion, telefono FROM USUARIOS";
        
        try (Connection conn = SQLConection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            
            while (rs.next()) {
                String tipo = rs.getString("tipoUsuario");
                int id = rs.getInt("idUsuario");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String password = rs.getString("password"); // Usar el pass de la BD, aunque no se debe mostrar
                String direccion = rs.getString("direccion");
                String telefono = rs.getString("telefono");
                
                if (tipo.equalsIgnoreCase("Cliente")) {
                   lista.add(new Cliente(id, nombre, email, password, direccion, telefono));
                } else if (tipo.equalsIgnoreCase("Admin")) {
                   lista.add(new Administrador(id, nombre, email, password));
                } else if (tipo.equalsIgnoreCase("Delivery")) {
                   lista.add(new Delivery(id, nombre, email, password, direccion, telefono));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
        }
        return lista;
    }
    
    public void editarUsuario(Usuario u) throws SQLException {
        String SQL = "UPDATE USUARIOS SET nombre = ?, email = ?, password = ?, direccion = ?, telefono = ? WHERE idUsuario = ?";

        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, u.getNombre());
            pstmt.setString(2, u.getEmail());
            pstmt.setString(3, u.getPassword());
            
            
            String direccion = null;
            String telefono = null;
            
            if (u instanceof Cliente) {
                 direccion = ((Cliente) u).getDireccion();
                 telefono = ((Cliente) u).getTelefono();
            } else if (u instanceof Delivery) {
                 if (u instanceof Cliente) {
                     direccion = ((Cliente) u).getDireccion();
                     telefono = ((Cliente) u).getTelefono();
                 }
            }
            
            pstmt.setString(4, direccion); 
            pstmt.setString(5, telefono); 
            pstmt.setInt(6, u.getIdUsuario());

            pstmt.executeUpdate();
            System.out.println("Usuario " + u.getNombre() + " editado con exito.");

        } catch (SQLException e) {
            System.err.println("Error al editar usuario: " + e.getMessage());
            throw e; 
        }
    }
    
    public void eliminarUsuario(int id) throws SQLException {
        String SQL = "DELETE FROM USUARIOS WHERE idUsuario = ?";
        try (Connection conn = SQLConection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Usuario ID " + id + " eliminado con exito.");

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            throw e; 
        }
    } 
}
