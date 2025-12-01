/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ecommerce;

import ConexionSQL.ProductoCRUD;
import ConexionSQL.UsuarioCRUD;
import ConexionSQL.AdministradorCRUD;
import ecommerce.Cliente;
import ecommerce.Administrador;
import ecommerce.Usuario;
import ecommerce.Pedido;
import ecommerce.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;

/**
 *
 * @author EDSON
 */
public class SistemaEcommerce {
    // Instanciar CRUD para obtener datos
    private static ProductoCRUD productoCRUD = new ProductoCRUD();
    private static UsuarioCRUD usuarioCRUD = new UsuarioCRUD();
    private static AdministradorCRUD adminCRUD = new AdministradorCRUD();
    // Guarda pedidos en lista
    private static List<Pedido> pedidos = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("Iniciando sistema de E-commerce (Consola con Roles)");

        Usuario usuarioActual = null;
        
        do {
            usuarioActual = realizarLogin();
            if (usuarioActual == null) {
                System.out.println("\n Credenciales incorrectas! Intente de nuevo.");
            }
        } while (usuarioActual == null);


        if (usuarioActual instanceof Cliente) {
            System.out.println("\n Inicio de sesion exitoso. Bienvenido, " + usuarioActual.getNombre() + " (Cliente).");
            menuCliente((Cliente) usuarioActual);
            
        } else if (usuarioActual instanceof Administrador) {
            System.out.println("\n Inicio de sesion exitoso. Bienvenido, " + usuarioActual.getNombre() + " (Administrador).");
            menuAdministrador((Administrador) usuarioActual);
        }

        System.out.println("\n Gracias por usar el sistema.");
        scanner.close();
    }
    
    // --- Metodos Login y Utilidad ---
    
    private static Usuario realizarLogin() {
        System.out.println("\n--- INICIO DE SESION ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contrasena: ");
        String password = scanner.nextLine();
        
        return usuarioCRUD.login(email, password);
    }
    
    private static void mostrarInventario() {
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

    // --- MENU DEL CLIENTE 
    
    private static void menuCliente(Cliente cliente) {
        int opcion = -1;
        do {
            System.out.println("\n--- MENU CLIENTE ---");
            System.out.println("1. Ver Productos y Comprar");
            System.out.println("2. Ver Carrito (" + cliente.getCarrito().getArticulos().size() + " items)");
            System.out.println("3. Ver Historial de Pedidos (" + pedidos.size() + ")");
            System.out.println("0. Cerrar Sesion y Salir");
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
        System.out.print("Ingrese el ID del producto a anadir (0 para volver): ");
        if (scanner.hasNextInt()) {
            int id = scanner.nextInt();
            scanner.nextLine();
            if (id == 0) return;
            
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

    // --- MENU ADMINISTRADOR  
    
    private static void menuAdministrador(Administrador admin) {
        int opcion = -1;
        do {
            System.out.println("\n--- MENU ADMINISTRADOR ---");
            System.out.println("1. Agregar Nuevo Producto");
            System.out.println("2. Aumentar Stock de Producto Existente");
            System.out.println("3. Ver Inventario Completo");
            System.out.println("0. Cerrar Sesion y Salir");
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
                    agregarNuevoProducto();
                    break;
                case 2:
                    aumentarStockExistente();
                    break;
                case 3:
                    // Admin tiene permiso de vertodo
                    mostrarInventario(); 
                    break;
                case 0:
                    admin.logout();
                    break;
                default:
                    System.out.println("Opcion no reconocida.");
            }
        } while (opcion != 0);
    }
    
    private static void agregarNuevoProducto() {
        try {
            System.out.println("\n--- AGREGAR NUEVO PRODUCTO ---");
            System.out.print("ID Producto (Nuevo): ");
            int id = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Descripcion: ");
            String descripcion = scanner.nextLine();
            
            System.out.print("Precio: ");
            double precio = scanner.nextDouble();
            
            System.out.print("Stock Inicial: ");
            int stock = scanner.nextInt();
            scanner.nextLine();
            
            Producto nuevoProducto = new Producto(id, nombre, descripcion, precio, stock);
            adminCRUD.agregarProducto(nuevoProducto);
            
        } catch (java.util.InputMismatchException e) {
            System.err.println("Entrada de datos invalida (debe ser numero).");
            scanner.nextLine(); 
        } catch (SQLException e) {
            System.err.println("Fallo de BD. Revise si el ID ya existe o la conexion es correcta.");
        }
    }
    
    private static void aumentarStockExistente() {
        try {
            mostrarInventario(); // Muestra lista productos actuales
            
            System.out.println("\n--- AUMENTAR STOCK ---");
            System.out.print("ID del Producto a modificar: ");
            int id = scanner.nextInt();
            
            System.out.print("Cantidad a ANADIR (ej. 15): ");
            int cantidad = scanner.nextInt();
            scanner.nextLine();
            
            adminCRUD.agregarStock(id, cantidad);
            
        } catch (java.util.InputMismatchException e) {
            System.err.println("Entrada de datos invalida (debe ser numero entero).");
            scanner.nextLine(); 
        } catch (SQLException e) {
            System.err.println("Fallo de BD. No se pudo actualizar el stock.");
        }
    }
    
}
