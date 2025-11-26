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
    private int cantidad;
    private double precioUnitario;
    private Producto producto; // Objeto asociado

    public DetallePedido(int cantidad, double precioUnitario) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

     public double getPrecioUnitario() { return precioUnitario; }
    public int getCantidad() { return cantidad; }
    public Producto getProducto() { return producto; }
    
    public void setProducto(Producto producto) { this.producto = producto; }

    public double calcularSubTotal() {
        return cantidad * precioUnitario;
    }
}