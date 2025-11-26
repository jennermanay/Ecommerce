package ecommerce;

import ecommerce.CarritoCompra;
import ecommerce.DetallePedido;
import ecommerce.Pago;
import ecommerce.Pedido;
import ecommerce.Producto;

// Clase Cliente
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

    // Getters y Setters
    public CarritoCompra getCarrito() { return carrito; }

    public void agregarAlCarrito(Producto producto) {
        carrito.agregarProducto(producto);
    }

    public Pedido realizarPedido() {
        if (carrito.getArticulos().isEmpty()) {
            System.out.println(" El carrito esta vacio. No se puede realizar el pedido.");
            return null;
        }

        // Crear el pedido
        Pedido nuevoPedido = new Pedido(
            (int) (Math.random() * 1000), 
            java.time.LocalDate.now(),
            "PENDIENTE"
        );
        
        // Transferir DetallePedido y actualizar stock en BD
        for (Producto p : carrito.getArticulos()) {
            // solo tomo 1 unidad por cada producto añadido al carrito
            DetallePedido detalle = new DetallePedido(1, p.getPrecio()); 
            nuevoPedido.agregarDetalle(detalle, p); 
        }
        
        // Crear pago
        Pago pago = new Pago(
            (int) (Math.random() * 500), 
            nuevoPedido.calcularTotal(), 
            "Tarjeta", 
            java.time.LocalDate.now()
        );
        pago.procesarPago();
        nuevoPedido.setPago(pago);
        
        // Limpiar carrito
        carrito = new CarritoCompra(); 

        System.out.println(" Pedido #" + nuevoPedido.getIdPedido() + " realizado con exito.");
        return nuevoPedido;
    }
}