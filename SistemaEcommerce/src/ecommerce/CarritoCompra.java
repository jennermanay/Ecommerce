package ecommerce;


import java.util.ArrayList;
import java.util.List;

public class CarritoCompra {
    // Almacena productos a comprar
    private List<Producto> articulos; 

    public CarritoCompra() {
        this.articulos = new ArrayList<>();
    }

    public List<Producto> getArticulos() { return articulos; }

    public void agregarProducto(Producto producto) {
        // agregar producto.
        articulos.add(producto); 
        System.out.println("  -> Producto " + producto.getNombreProducto() + " añadido al carrito.");
    }

    public void eliminarProducto(Producto producto) {
        if (articulos.remove(producto)) {
            System.out.println("  -> Producto " + producto.getNombreProducto() + " eliminado del carrito.");
        } else {
            System.out.println("  -> Producto no encontrado en el carrito.");
        }
    } 

    public double calcularTotal() {
        double total = 0;
        for (Producto p : articulos) {
            total += p.getPrecio();
        }
        return total;
    }
}