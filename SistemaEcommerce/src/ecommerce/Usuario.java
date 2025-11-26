package ecommerce;


public class Usuario {
    protected int idUsuario;
    protected String nombre;
    protected String email;
    protected String password;

    public Usuario(int idUsuario, String nombre, String email, String password) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }
    
    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNombre() {
        return nombre;
    }

    // Metodos
    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }
    public void logout() {
        System.out.println("Sesion de " + nombre + " cerrada.");
    }
    public void operation() { 
        
    }
}