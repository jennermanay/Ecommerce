/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

/**
 *
 * @author Jenner Jordy
 */
import java.time.LocalDate;

public class Pago {
    private int idPago;
    private double monto;
    private String metodoPago;
    private LocalDate fecha;

    public Pago(int idPago, double monto, String metodoPago, LocalDate fecha) {
        this.idPago = idPago;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fecha = fecha;
    }

    public String getMetodoPago() { return metodoPago; }
    public double getMonto() { return monto; }

    public boolean procesarPago() {
        System.out.println(" Procesando pago de $" + monto + " con " + metodoPago + "...");
        System.out.println(" Pago procesado correctamente.");
        return true;
    }
}