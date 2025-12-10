/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

/**
 *
 * @author Jenner Jordy
 */
    

public class Delivery extends Usuario {
    private String direccion;
    private String telefono;
     public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
     public Delivery(int idUsuario, String nombre, String email, String password, String direccion, String telefono) {
        super(idUsuario, nombre, email, password);
     }
    
    @Override
    public String toString() {
        return "ID "+getIdUsuario()+" Delivery: " + getNombre() + " (" + getEmail() + ") Telefono: "+getTelefono();
    }
}