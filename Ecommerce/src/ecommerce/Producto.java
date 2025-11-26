/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

/**
 *
 * @author Jenner Jordy
 */
public class Producto {
    private int idProducto;
    private String nombreProducto;
    private String descripcion;
    private double precio;
    private int stock;

    public Producto(int idProducto, String nombreProducto, String descripcion, double precio, int stock) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    public int getIdProducto() { return idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    @Override
    public String toString() {
        return idProducto + ". " + nombreProducto + " - $" + precio + " (Stock: " + stock + ")";
    }
}
