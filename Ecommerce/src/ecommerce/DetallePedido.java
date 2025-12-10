/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

/**
 *
 * @author Jenner Jordy
 */
public class DetallePedido {
private int idDetalle;
    private int idPedido;
    private Producto producto;
    private double precioUnitario;
    private int cantidad;

    public DetallePedido(int idDetalle, int idPedido, Producto producto, double precioUnitario, int cantidad) {
        this.idDetalle = idDetalle;
        this.idPedido = idPedido;
        this.producto = producto;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
}
