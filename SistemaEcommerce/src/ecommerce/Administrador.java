package ecommerce;
import java.util.List;

public class Administrador extends Usuario {
    public Administrador(int idUsuario, String nombre, String email, String password) {
        super(idUsuario, nombre, email, password);
    }
    
    @Override
    public String toString() {
        return "ID "+getIdUsuario()+" Administrador: " + getNombre();
    }
}
