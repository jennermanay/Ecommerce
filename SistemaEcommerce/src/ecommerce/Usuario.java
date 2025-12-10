
package ecommerce;

public class Usuario {
    protected int idUsuario;
    protected String nombre;
    protected String email;
    protected String password;
    protected String direccion;
    protected int telefono;
    
    public Usuario(int idUsuario, String nombre, String email, String password) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }
    
    public void Usuario2(int idUsuario, String nombre, String email, String password, String direccion, Integer telefono) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.direccion=direccion;
        this.telefono=telefono;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
    
    public void logout() {
        System.out.println(nombre + " ha cerrado sesion.");
    }
}
