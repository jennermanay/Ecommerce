/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

/**
 *
 * @author Jenner Jordy
 */
public class Categoria {
 private int idCategoria;
    private String nombreCategoria;

    public Categoria(int idCategoria, String nombreCategoria) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }

    public int getIdCategoria() { return idCategoria; }
    public String getNombreCategoria() { return nombreCategoria; }
    
    @Override
    public String toString() {
        return idCategoria + ": " + nombreCategoria;
    }
}