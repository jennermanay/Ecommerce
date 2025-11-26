/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

/**
 *
 * @author Jenner Jordy
 */
import ConexionSQL.ProductoCRUD;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int idPedido;
    private LocalDate fecha;
    private String estado;
    private List<DetallePedido> detalles;
    private Pago pago;
    
    private ProductoCRUD productoCRUD = new ProductoCRUD(); 

    public Pedido(int idPedido, LocalDate fecha, String estado) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.estado = estado;
        this.detalles = new ArrayList<>();
    }

    public int getIdPedido() { return idPedido; }
    public String getEstado() { return estado; }
    public void setPago(Pago pago) { this.pago = pago; }

    public void agregarDetalle(DetallePedido detalle, Producto producto) {
        detalle.setProducto(producto);
        this.detalles.add(detalle);
        
        try {
            productoCRUD.actualizarStock(producto.getIdProducto(), detalle.getCantidad());
            System.out.println("    -> Stock de " + producto.getNombreProducto() + " actualizado en BD.");
        } catch (SQLException e) {
            System.err.println("Error al persistir el stock. El pedido puede tener problemas.");
        }
    }

    public double calcularTotal() {
        double total = 0;
        for (DetallePedido dp : detalles) {
            total += dp.calcularSubTotal();
        }
        return total;
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
        System.out.println("🔔 Estado del pedido #" + idPedido + " cambiado a: " + nuevoEstado);
    }
    
    @Override
    public String toString() {
        String s = "\n--- PEDIDO #" + idPedido + " ---\n";
        s += "Fecha: " + fecha + ", Estado: " + estado + "\n";
        s += "Detalles:\n";
        for (DetallePedido dp : detalles) {
            s += "  - " + dp.getProducto().getNombreProducto() + " x" + dp.getCantidad() + " @$" + dp.getPrecioUnitario() + " = $" + dp.calcularSubTotal() + "\n";
        }
        s += "TOTAL: $" + calcularTotal() + "\n";
        s += (pago != null ? "Pago: " + pago.getMetodoPago() + " (" + pago.getMonto() + ")" : "Pago pendiente") + "\n";
        s += "----------------------";
        return s;
    }
}
