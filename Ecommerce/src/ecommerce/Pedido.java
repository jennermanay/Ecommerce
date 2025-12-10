/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

/**
 *
 * @author Jenner Jordy
 */

import java.util.List;
import java.time.LocalDateTime;
import java.util.Date;


public class Pedido {
    private int idPedido;
    private int idCliente;
    private Date fechaPedido;
    private double total;
    
    private int idMetodoPago; 
    private String estado;
    private String direccion;
    private Integer idDelivery; // Integer para permitir NULL en SQL Server
    private LocalDateTime horaEntrega;
    
    private List<DetallePedido> detalles; 

    public Pedido(int idPedido, int idCliente, Date fechaPedido, String estado, double total, List<DetallePedido> detalles, String direccion) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.fechaPedido = fechaPedido;
        this.total = total;
        this.estado = estado;
        this.detalles = detalles;
        this.direccion=direccion;
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public int getIdCliente() { return idCliente; }
    public double getTotal() { return total; }
    public List<DetallePedido> getDetalles() { return detalles; }    
    public int getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(int idMetodoPago) { this.idMetodoPago = idMetodoPago; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Integer getIdDelivery() { return idDelivery; }
    public void setIdDelivery(Integer idDelivery) { this.idDelivery = idDelivery; }
    public LocalDateTime getHoraEntrega() { return horaEntrega; }
    public void setHoraEntrega(LocalDateTime horaEntrega) { this.horaEntrega = horaEntrega; }

    @Override
    public String toString() {
        return "Pedido #" + idPedido + " | Total: $" + total + " | Estado: " + estado;
    }
}
