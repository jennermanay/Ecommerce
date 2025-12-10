package ecommerce;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarritoCompra {
  
    private Map<Integer, Producto> productosUnicos;
    private Map<Integer, Integer> cantidades;

    public CarritoCompra() {
        this.productosUnicos = new HashMap<>();
        this.cantidades = new HashMap<>();
    }
    
    public List<Producto> getArticulos() {
        return new ArrayList<>(productosUnicos.values());
    }

    public void agregarArticulo(Producto producto) {
        int id = producto.getIdProducto();
        productosUnicos.put(id, producto);
        cantidades.put(id, cantidades.getOrDefault(id, 0) + 1);
    }

    public double calcularTotal() {
        double total = 0;
        for (Producto p : productosUnicos.values()) {
            total += p.getPrecio() * cantidades.get(p.getIdProducto());
        }
        return total;
    }
    
    public List<DetallePedido> getArticulosEnDetalle() {
        List<DetallePedido> detalles = new ArrayList<>();
        int lineId = 1;
        for (Producto p : productosUnicos.values()) {
            int cantidad = cantidades.get(p.getIdProducto());
            detalles.add(new DetallePedido(lineId++, 0, p, p.getPrecio(), cantidad)); 
        }
        return detalles;
    }

    public void vaciarCarrito() {
        productosUnicos.clear();
        cantidades.clear();
    }
}
