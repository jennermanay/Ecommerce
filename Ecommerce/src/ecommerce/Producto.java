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
    private int idCategoria; 

    public Producto(int idProducto, String nombreProducto, String descripcion, double precio, int stock, int idCategoria) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.idCategoria = idCategoria;
    }

    public int getIdProducto() { return idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public String getDescripcion() { return descripcion; }
    public int getIdCategoria() { return idCategoria; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return "ID: " + idProducto + " | " + nombreProducto + " | $" + precio + " | Stock: " + stock + " | Cat ID: " + idCategoria;
    }
}
