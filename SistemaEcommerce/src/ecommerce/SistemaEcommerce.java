/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ecommerce;

import ConexionSQL.ProductoCRUD;
import ecommerce.Cliente;
import ecommerce.Pedido;
import ecommerce.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author EDSON
 */
public class SistemaEcommerce {
    // Instanciar CRUD para obtener datos
    private static ProductoCRUD productoCRUD = new ProductoCRUD();
    // Guarda pedidos en lista
    private static List<Pedido> pedidos = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("Iniciando sistema de E-commerce (Consola con JDBC)");

        // Login de xliente
        Cliente clienteActual = new Cliente(1, "Edson Choque", "echoque@mail.com", "pass123", "Calle 123", "987654321");
        
        System.out.println("\n Bienvenido, " + clienteActual.getNombre() + ".");

        menuPrincipal(clienteActual);

        System.out.println("\n Gracias por usar el sistema. ¡Vuelva pronto!");
        scanner.close();
    }

    private static void mostrarInventario() {
        // Obtener productos con stock > 0 desde la Base de Datos
        List<Producto> inventario = productoCRUD.obtenerTodos(); 
        
        if (inventario.isEmpty()) {
            System.out.println(" No hay productos disponibles en este momento.");
            return;
        }
        
        System.out.println("\n--- INVENTARIO DISPONIBLE (Desde BD) ---");
        for (Producto p : inventario) {
             System.out.println(p);
        }
    }

    private static void menuPrincipal(Cliente cliente) {
        int opcion = -1;
        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Ver Productos y Comprar");
            System.out.println("2. Ver Carrito (" + cliente.getCarrito().getArticulos().size() + " items)");
            System.out.println("3. Ver Historial de Pedidos (" + pedidos.size() + ")");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Opcion no valida. Intente de nuevo.");
                scanner.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    comprar(cliente);
                    break;
                case 2:
                    verCarrito(cliente);
                    break;
                case 3:
                    verHistorialPedidos();
                    break;
                case 0:
                    cliente.logout();
                    break;
                default:
                    System.out.println("Opcion no reconocida.");
            }
        } while (opcion != 0);
    }
    
    private static void comprar(Cliente cliente) {
        mostrarInventario();
        System.out.print("Ingrese el ID del producto a añadir (0 para volver): ");
        if (scanner.hasNextInt()) {
            int id = scanner.nextInt();
            scanner.nextLine();
            if (id == 0) return;
            
            // Buscar el producto en la BD
            Producto productoSeleccionado = productoCRUD.obtenerPorId(id);

            if (productoSeleccionado != null && productoSeleccionado.getStock() > 0) {
                cliente.agregarAlCarrito(productoSeleccionado);
            } else {
                System.out.println(" Producto no encontrado, sin stock o ID invalido.");
            }
        } else {
            System.out.println("Entrada invalida.");
            scanner.nextLine();
        }
    }
    
    private static void verCarrito(Cliente cliente) {
        if (cliente.getCarrito().getArticulos().isEmpty()) {
            System.out.println(" El carrito esta vacio.");
            return;
        }

        System.out.println("\n--- SU CARRITO ---");
        for (Producto p : cliente.getCarrito().getArticulos()) {
            System.out.println("- " + p.getNombreProducto() + " ($" + p.getPrecio() + ")");
        }
        System.out.println("Total Estimado: $" + cliente.getCarrito().calcularTotal());

        System.out.print("\n Desea realizar el pedido ahora? (s/n): ");
        String respuesta = scanner.nextLine().toLowerCase();

        if (respuesta.equals("s")) {
            Pedido nuevoPedido = cliente.realizarPedido();
            if (nuevoPedido != null) {
                pedidos.add(nuevoPedido);
            }
        }
    }
    
    private static void verHistorialPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("\n Aun no hay pedidos realizados en esta sesion.");
            return;
        }
        System.out.println("\n--- HISTORIAL DE PEDIDOS ---");
        for (Pedido p : pedidos) {
            System.out.println(p);
        }
    }
    
}
