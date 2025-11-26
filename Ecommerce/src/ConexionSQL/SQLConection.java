/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConexionSQL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Jenner Jordy
 */

public class SQLConection {
    // Puerto estándar de SQL Server es 1433.
    // Reemplaza 'localhost' con el nombre/IP de tu servidor si no es local.
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=ecommerce;encrypt=false;trustServerCertificate=true;";
    
    // Si usas autenticación de Windows:
    // private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=ecommerce_db;integratedSecurity=true";
    
    private static final String USER = "sa";       // <-- Usuario de SQL Server
    private static final String PASS = "Sistemas10$";   // <-- Contraseña de SQL Server
    
    public static Connection getConnection() throws SQLException {
        // Carga explícita del driver (Buena práctica, aunque a veces innecesaria)
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver JDBC de SQL Server no encontrado. Asegúrate de añadir el .jar.");
            throw new SQLException(e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
