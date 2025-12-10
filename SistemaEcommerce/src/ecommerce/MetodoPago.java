/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

/**
 *
 * @author Jenner Jordy
 */
public class MetodoPago {
  private int idMetodoPago;
    private String nombreMetodo;
    private String numeroCuenta;

    public MetodoPago(int idMetodoPago, String nombreMetodo, String numeroCuenta) {
        this.idMetodoPago = idMetodoPago;
        this.nombreMetodo = nombreMetodo;
        this.numeroCuenta = numeroCuenta;
    }

    public int getIdMetodoPago() { return idMetodoPago; }
    public String getNombreMetodo() { return nombreMetodo; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNombreMetodo(String nombreMetodo) { this.nombreMetodo = nombreMetodo; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    
    @Override
    public String toString() {
        return idMetodoPago + " - " + nombreMetodo + " (" + numeroCuenta + ")";
    }
}