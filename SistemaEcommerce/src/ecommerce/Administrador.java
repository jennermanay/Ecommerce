package ecommerce;
import java.util.List;

public class Administrador extends Usuario {
    public Administrador(int idUsuario, String nombre, String email, String password) {
        super(idUsuario, nombre, email, password);
    }

    // Métodos de administración de productos
    public void agregarProducto(Producto producto, List<Producto> inventario) {
        inventario.add(producto);
        System.out.println(" Producto " + producto.getNombreProducto() + " agregado.");
    }

    public void modificarProducto(Producto productoExistente, double nuevoPrecio) {
        productoExistente.setStock(productoExistente.getStock() + 10); 
        // buscar productoID y actualizar atributos
        System.out.println(" Producto " + productoExistente.getNombreProducto() + " modificado. Nuevo stock: " + productoExistente.getStock());
    }

    public void eliminarProducto(Producto producto, List<Producto> inventario) {
        inventario.remove(producto);
        System.out.println("️ Producto " + producto.getNombreProducto() + " eliminado.");
    }
}