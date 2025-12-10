
package ecommerce;

import java.util.Date;
import java.util.List;

public class Cliente extends Usuario {
    private String direccion;
    private String telefono;
    private CarritoCompra carrito;

    public Cliente(int idUsuario, String nombre, String email, String password, String direccion, String telefono) {
        super(idUsuario, nombre, email, password);
        this.direccion = direccion;
        this.telefono = telefono;
        this.carrito = new CarritoCompra();
    }

    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public CarritoCompra getCarrito() { return carrito; }

    public void agregarAlCarrito(Producto producto) {
        carrito.agregarArticulo(producto);
        System.out.println("Producto " + producto.getNombreProducto() + " anadido al carrito.");
    }
    
    public Pedido generarNuevoPedido(int idMetodoPago) {
        if (carrito.getArticulos().isEmpty()) {
            System.out.println("El carrito esta vacio. No se puede realizar el pedido.");
            return null;
        }
        
        Pedido nuevoPedido = new Pedido(
            (int)(new Date().getTime() % 10000), 
            this.idUsuario, 
            new Date(), 
            "POR_ENTREGAR", 
            carrito.calcularTotal(), 
            carrito.getArticulosEnDetalle(),
            this.direccion);

        nuevoPedido.setIdMetodoPago(idMetodoPago);
        
        carrito.vaciarCarrito();
        System.out.println("Pedido generado localmente. Total: " + nuevoPedido.getTotal());
        return nuevoPedido;
    }

    @Override
    public String toString() {
        return "ID "+getIdUsuario()+" Cliente: " + getNombre() + " (" + getEmail() + ") Direccion: " + getDireccion()+ " Telefono: "+getTelefono();
    }
}
