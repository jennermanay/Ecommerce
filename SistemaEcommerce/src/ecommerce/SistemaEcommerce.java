package ecommerce;

import ConexionSQL.*;
import ecommerce.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;

/**
 *
 * @author EDSON
 */
public class SistemaEcommerce {
    private static ProductoCRUD productoCRUD = new ProductoCRUD();
    private static UsuarioCRUD usuarioCRUD = new UsuarioCRUD();
    private static PedidoCRUD pedidoCRUD = new PedidoCRUD();
    private static MetodoPagoCRUD metodoPagoCRUD = new MetodoPagoCRUD();
    private static CategoriaCRUD categoriaCRUD = new CategoriaCRUD();
    
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Iniciando sistema de E-commerce (Consola con Roles)");

        Usuario usuarioActual = null;
        
        do {
            usuarioActual = realizarLogin();
            if (usuarioActual == null) {
                System.out.println("Error! Credenciales incorrectas. Intente de nuevo.");
            }
        } while (usuarioActual == null);


        if (usuarioActual instanceof Cliente) {
            System.out.println("Inicio de sesion exitoso. Bienvenido, " + usuarioActual.getNombre() + " (Cliente).");
            menuCliente((Cliente) usuarioActual);
            
        } else if (usuarioActual instanceof Administrador) {
            System.out.println("Inicio de sesion exitoso. Bienvenido, " + usuarioActual.getNombre() + " (Administrador).");
            menuAdministrador((Administrador) usuarioActual);
            
        } else if (usuarioActual instanceof Delivery) {
            System.out.println("Inicio de sesion exitoso. Bienvenido, " + usuarioActual.getNombre() + " (Delivery).");
            menuDelivery((Delivery) usuarioActual);
        }

        System.out.println("Gracias por usar el sistema.");
        scanner.close();
    }
    
     
    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero entero.");
                System.out.print(mensaje);
            }
        }
    }
    
    private static double leerDoble(String mensaje) {
        System.out.print(mensaje);
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero decimal.");
                System.out.print(mensaje);
            }
        }
    }

    private static Usuario realizarLogin() {
        System.out.println("\n--- INICIO DE SESION ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contrasena: ");
        String password = scanner.nextLine();
        
        return usuarioCRUD.login(email, password);
    }
    
    private static void mostrarInventario(List<Producto> productos) {
        if (productos.isEmpty()) {
            System.out.println("El inventario esta vacio.");
            return;
        }
        System.out.println("\n--- INVENTARIO DISPONIBLE ---");
        System.out.printf("%-5s | %-25s | %-10s | %-5s | %s\n", "ID", "NOMBRE", "PRECIO", "STOCK", "CAT_ID");
        System.out.println("-----------------------------------------------------------------");
        for (Producto p : productos) {
            System.out.printf("%-5d | %-25s | $%-9.2f | %-5d | %d\n", 
                p.getIdProducto(), p.getNombreProducto(), p.getPrecio(), p.getStock(), p.getIdCategoria());
        }
    }

  
    private static void menuCliente(Cliente cliente) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MENU CLIENTE ---");
            System.out.println("1. Ver Productos y Comprar");
            System.out.println("2. Ver Carrito");
            System.out.println("3. Ver Estado de Mis Pedidos");
            System.out.println("0. Cerrar Sesion y Salir");
            opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    comprar(cliente);
                    break;
                case 2:
                    verCarrito(cliente);
                    break;
                case 3:
                    verEstadoPedidosCliente(cliente.getIdUsuario()); 
                    break;
                case 0:
                    cliente.logout();
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }
    
    private static void comprar(Cliente cliente) {
        List<Producto> productos = productoCRUD.obtenerTodos();
        mostrarInventario(productos);
        
        if (productos.isEmpty()) return;
        
        int idProducto = leerEntero("Ingrese el ID del producto a anadir (0 para volver): ");
        if (idProducto == 0) return;
        
        Producto p = productoCRUD.obtenerPorId(idProducto); 
        if (p != null) {
            cliente.agregarAlCarrito(p);
        } else {
            System.out.println("Producto no encontrado.");
        }
    }
    
    private static void verCarrito(Cliente cliente) {
        int nextPedidoID=pedidoCRUD.obtenerUltimoIdPedido() + 1;
        if (cliente.getCarrito().getArticulos().isEmpty()) {
            System.out.println("El carrito esta vacio.");
            return;
        }
        
        System.out.println("\n--- CARRITO DE COMPRAS ---");
        
         cliente.getCarrito().getArticulosEnDetalle().forEach(dp -> {
            System.out.printf("%s x %d - $%.2f\n", 
                              dp.getProducto().getNombreProducto(), 
                              dp.getCantidad(), 
                              dp.getPrecioUnitario() * dp.getCantidad());
        });
        
        System.out.println("Total: $" + cliente.getCarrito().calcularTotal());

        System.out.print("Desea realizar el pedido ahora? (s/n): ");
        String respuesta = scanner.nextLine().toLowerCase();

        if (respuesta.equals("s")) {
            MetodoPago mp = seleccionarMetodoPago();
            if (mp == null) {
                System.out.println("Operacion cancelada.");
                return;
            }
            
            Pedido nuevoPedido = cliente.generarNuevoPedido(mp.getIdMetodoPago());

            if (nuevoPedido != null) {
                nuevoPedido.setIdPedido(nextPedidoID); 

                if (pedidoCRUD.guardarPedido(nuevoPedido)) {
                    System.out.println("Pedido guardado exitosamente con ID: " + nuevoPedido.getIdPedido());
                } else {
                    System.out.println("Fallo al guardar el pedido. Revisar stock o base de datos.");
                }
            }
        }
    }
    
    private static MetodoPago seleccionarMetodoPago() {
        List<MetodoPago> metodos = metodoPagoCRUD.obtenerTodos();
        if (metodos.isEmpty()) {
            System.out.println("No hay metodos de pago registrados. Contacte al administrador.");
            return null;
        }
        
        System.out.println("\n--- SELECCION DE METODO DE PAGO ---");
        for (MetodoPago mp : metodos) {
            System.out.println(mp.getIdMetodoPago() + ". " + mp.getNombreMetodo() + " (" + mp.getNumeroCuenta() + ")");
        }
        
        int idSeleccionado = leerEntero("Ingrese el ID del metodo de pago: ");

        for (MetodoPago mp : metodos) {
            if (mp.getIdMetodoPago() == idSeleccionado) {
                return mp;
            }
        }
        System.out.println("ID de metodo de pago invalido.");
        return null;
    }
    
    private static void verEstadoPedidosCliente(int idCliente) {
        List<String> estados = pedidoCRUD.obtenerPedidosCliente(idCliente);
        System.out.println("\n--- ESTADO DE SUS PEDIDOS ---");
        if (estados.isEmpty()) {
            System.out.println("No ha realizado pedidos.");
        } else {
            estados.forEach(System.out::println);
        }
    }

    private static void menuAdministrador(Administrador admin) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MENU ADMINISTRADOR ---");
            System.out.println("1. Gestion de Productos (CRUD)");
            System.out.println("2. Gestion de Usuarios (CRUD)");
            System.out.println("3. Gestion de Metodos de Pago (CRUD)");
            System.out.println("4. Gestion de Categorias");
            System.out.println("5. Ver Estado de Ordenes (Todos)");
            System.out.println("0. Cerrar Sesion y Salir");
            opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    menuGestionProductos();
                    break;
                case 2:
                    menuGestionUsuarios();
                    break;
                case 3:
                    menuGestionMetodosPago();
                    break;
                case 4:
                    menuGestionCategorias();
                    break;
                case 5:
                    verTodasOrdenes();
                    break;
                case 0:
                    admin.logout();
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }
    
    private static void menuGestionProductos() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- GESTION DE PRODUCTOS ---");
            System.out.println("1. Ver Todos los Productos");
            System.out.println("2. Agregar Nuevo Producto");
            System.out.println("3. Aumentar Stock Existente");
            System.out.println("4. Editar Producto");
            System.out.println("5. Eliminar Producto");
            System.out.println("0. Volver al Menu Principal");
            opcion = leerEntero("Seleccione una opcion: ");
            try {
            switch (opcion) {
                case 1:
                    mostrarInventario(productoCRUD.obtenerTodos());
                    break;
                case 2:
                    agregarProducto();
                    break;
                case 3:
                    aumentarStock();
                    break;
                case 4:
                    editarProducto();
                    break;
                case 5:
                    eliminarProducto();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }catch (SQLException e) {
                System.out.println("Error en la operacion de Base de Datos: " + e.getMessage());
            }
      }
    }
    
    private static void agregarProducto() {
        System.out.println("\n--- AGREGAR NUEVO PRODUCTO ---");
        String nombre = "";
        while (nombre.trim().isEmpty()) {
            System.out.print("Nombre: ");
            nombre = scanner.nextLine();
        }
        System.out.print("Descripcion: ");
        String descripcion = scanner.nextLine();
        double precio = leerDoble("Precio: ");
        int stock = leerEntero("Stock Inicial: ");
        int idCategoria = 0;
        
        List<Categoria> categorias = categoriaCRUD.obtenerTodas();
        if (!categorias.isEmpty()) {
            System.out.println("--- CATEGORIAS DISPONIBLES ---");
            categorias.forEach(System.out::println);
            idCategoria = leerEntero("ID de Categoria: ");
        } else {
             System.out.println("Advertencia: No hay categorias. Use ID 1 por defecto.");
             idCategoria = 1;
        }

        Producto nuevoProducto = new Producto(0, nombre, descripcion, precio, stock, idCategoria);
        try {
            productoCRUD.agregarProducto(nuevoProducto);
        } catch (SQLException e) {
            System.out.println("Error de BD: No se pudo agregar el producto. " + e.getMessage());
        }
    }

    private static void aumentarStock() {
        mostrarInventario(productoCRUD.obtenerTodos());
        int idProducto = leerEntero("Ingrese ID del producto a aumentar stock: ");
        int cantidad = leerEntero("Cantidad a agregar: ");
        
        try {
            productoCRUD.aumentarStock(idProducto, cantidad);
        } catch (SQLException e) {
            System.out.println("Error de BD: No se pudo actualizar el stock. " + e.getMessage());
        }
    }
    
    private static void eliminarProducto() throws SQLException {
        mostrarInventario(productoCRUD.obtenerTodos());
        int id = leerEntero("Ingrese el ID del Producto a eliminar: ");
        if (id>0) {
            productoCRUD.eliminarProducto(id);
        } else {
            System.out.println("El nombre no puede ser 0 ó negativo.");
        }
    } 
   
    private static void editarProducto() throws SQLException {
        mostrarInventario(productoCRUD.obtenerTodos());
        int id = leerEntero("Ingrese el ID del Producto a editar: ");
        System.out.print("Ingrese el NUEVO nombre: ");
        String nuevoNombre = scanner.nextLine();
        System.out.print("Ingrese la NUEVA descripción: ");
        String nuevaDescripcion = scanner.nextLine();
        System.out.print("Ingrese el NUEVO precio: ");
        Double nuevoPrecio = scanner.nextDouble();
        System.out.print("Ingrese el NUEVO stock: ");
        Integer nuevoStock = scanner.nextInt();
        categoriaCRUD.obtenerTodas().forEach(System.out::println);
        System.out.print("Ingrese la NUEVA categoria: ");
        Integer nuevaCategoria = scanner.nextInt();
     

        if (!nuevoNombre.trim().isEmpty()) {
            Producto productoEditada = new Producto(id, nuevoNombre, nuevaDescripcion, nuevoPrecio, nuevoStock, nuevaCategoria);
            productoCRUD.editarProducto(productoEditada);
        } else {
            System.out.println("El nombre no puede estar vacio. Operacion cancelada.");
        }
    } 

    private static void menuGestionUsuarios() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- GESTION DE USUARIOS ---");
            System.out.println("1. Ver Todos los Usuarios");
            System.out.println("2. Agregar Nuevo Usuario");
            System.out.println("3. Editar Usuario");  
            System.out.println("4. Eliminar usuario");
            System.out.println("0. Volver al Menu Principal");
            opcion = leerEntero("Seleccione una opcion: ");
            try{
            switch (opcion) {
                case 1:
                    usuarioCRUD.obtenerTodos().forEach(System.out::println);
                    break;
                case 2:
                    agregarUsuario();
                    break;
                case 3:
                    editarUsuario();
                    break;
                case 4:
                    eliminarUsuario();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } catch (SQLException e) {
                System.out.println("Error en la operacion de Base de Datos: " + e.getMessage());
            }
    }
    }
   
     private static void eliminarUsuario() throws SQLException {
        usuarioCRUD.obtenerTodos().forEach(System.out::println);
        int id = leerEntero("Ingrese el ID del Usuario a eliminar: ");
        if (id>0) {
            usuarioCRUD.eliminarUsuario(id);
        } else {
            System.out.println("El nombre no puede ser 0 ó negativo.");
        }
    } 
   
    private static void editarUsuario() throws SQLException {
        usuarioCRUD.obtenerTodos().forEach(System.out::println);
        int id = leerEntero("Ingrese el ID del Usuario a editar: ");
        System.out.print("Ingrese el NUEVO nombre: ");
        String nuevoNombre = scanner.nextLine();
        System.out.print("Ingrese el NUEVO email: ");
        String nuevoEmail = scanner.nextLine();
        System.out.print("Ingrese la NUEVA contraseña: ");
        String nuevoPassword = scanner.nextLine();
     

        if (!nuevoNombre.trim().isEmpty()) {
            Usuario usuarioEditada = new Usuario(id, nuevoNombre, nuevoEmail, nuevoPassword);
            usuarioCRUD.editarUsuario(usuarioEditada);
        } else {
            System.out.println("El nombre no puede estar vacio. Operacion cancelada.");
        }
    } 
    
    private static void agregarUsuario() {
        System.out.println("\n--- AGREGAR NUEVO USUARIO ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contrasena: ");
        String password = scanner.nextLine();
        System.out.print("Tipo (Cliente, Admin, Delivery): ");
        String tipo = scanner.nextLine();

        Usuario nuevoUsuario = null;

        try {
            if (tipo.equalsIgnoreCase("Cliente") || tipo.equalsIgnoreCase("Delivery")) {
                System.out.print("Direccion: ");
                String direccion = scanner.nextLine();
                System.out.print("Telefono: ");
                String telefono = scanner.nextLine();
                
                if (tipo.equalsIgnoreCase("Cliente")) {
                    nuevoUsuario = new Cliente(0, nombre, email, password, direccion, telefono);
                } else {
                    nuevoUsuario = new Delivery(0, nombre, email, password, direccion, telefono);
                }
            } else if (tipo.equalsIgnoreCase("Admin")) {
                nuevoUsuario = new Administrador(0, nombre, email, password);
            } else {
                System.out.println("Tipo de usuario invalido.");
                return;
            }

            usuarioCRUD.agregarUsuario(nuevoUsuario);

        } catch (SQLException e) {
            System.out.println("Error de BD al agregar usuario: " + e.getMessage());
        }
    }
    
    private static void menuGestionMetodosPago() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- GESTION METODO DE PAGOS ---");
            System.out.println("1. Listar Metodos de Pago");
            System.out.println("2. Agregar Metodo de Pago (CRUD)");
            System.out.println("3. Editar Metodo de Pago");
            System.out.println("4. Eliminar Metodo de pago");
            System.out.println("0. Cerrar Sesion y Salir");
            opcion = leerEntero("Seleccione una opcion: ");

            try {
            switch (opcion) {
                case 1:
                    metodoPagoCRUD.obtenerTodos().forEach(System.out::println);
                    break;
                case 2:
                    agregarMetodoPago();
                    break;
                case 3:
                    editarMetodoPago();
                    break;
                case 4:
                    eliminarMetodoPago();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
          } catch (SQLException e) {
                System.out.println("Error en la operacion de Base de Datos: " + e.getMessage());
            }
        } 
    }
    
    private static void agregarMetodoPago() throws SQLException {
        System.out.println("\n--- AGREGAR METODO DE PAGO ---");
        System.out.print("Nombre del nuevo método: ");
        String nombre = scanner.nextLine();
        System.out.print("Nombre numero de cuenta: ");
        String numero = scanner.nextLine();
        
        if (!nombre.trim().isEmpty() || !numero.trim().isEmpty()) {
            MetodoPago nuevoMetodo = new MetodoPago(0,nombre, numero);
            
            metodoPagoCRUD.agregarMetodo(nuevoMetodo);
        } else {
            System.out.println("El nombre no puede estar vacio.");
        }
    }
    
        private static void editarMetodoPago() throws SQLException {
        metodoPagoCRUD.obtenerTodos().forEach(System.out::println);
        int id = leerEntero("Ingrese el ID del Metodo de pago a editar: ");
        System.out.print("Ingrese el NUEVO nombre: ");
        String nuevoNombre = scanner.nextLine();
        System.out.print("Ingrese el NUEVO numero de cuenta: ");
        String nuevoNumero = scanner.nextLine();

        if (!nuevoNombre.trim().isEmpty() || !nuevoNumero.trim().isEmpty()) {
            MetodoPago metodoEditada = new MetodoPago(id, nuevoNombre, nuevoNumero);
            metodoPagoCRUD.editarMetodo(metodoEditada);
        } else {
            System.out.println("El nombre no puede estar vacio. Operacion cancelada.");
        }
    }
        private static void eliminarMetodoPago() throws SQLException {
        metodoPagoCRUD.obtenerTodos().forEach(System.out::println);
        int id = leerEntero("Ingrese el ID del meotodo de Pago a eliminar: ");
        if (id>0) {
            metodoPagoCRUD.eliminarMetodo(id);
        } else {
            System.out.println("El nombre no puede ser 0 ó negativo.");
        }
    } 
        
        
    

// Logica para ver, agregar o editar metodos de pago
        
    
    private static void menuGestionCategorias() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- GESTION DE CATEGORIAS ---");
            System.out.println("1. Ver Todas las Categorias");
            System.out.println("2. Agregar Nueva Categoria");
            System.out.println("3. Editar Categoria Existente");   
            System.out.println("4. Eliminar Categoria Existente");   
            System.out.println("0. Volver al Menu Principal");
            opcion = leerEntero("Seleccione una opcion: ");

            try {
                switch (opcion) {
                    case 1:
                        categoriaCRUD.obtenerTodas().forEach(System.out::println);
                        break;
                    case 2:
                        agregarCategoria();
                        break;
                    case 3:
                        editarCategoria();
                        break;
                    case 4:
                        eliminarCategoria();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                }
            } catch (SQLException e) {
                System.out.println("Error en la operacion de Base de Datos: " + e.getMessage());
            }
        }
    }


    private static void verTodasOrdenes() {
        List<String> ordenes = pedidoCRUD.obtenerTodasOrdenes();
        System.out.println("\n--- ESTADO DE TODAS LAS ORDENES ---");
        if (ordenes.isEmpty()) {
            System.out.println("No hay ordenes registradas.");
        } else {
            ordenes.forEach(System.out::println);
        }
    }
    
    private static void menuDelivery(Delivery delivery) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MENU DELIVERY ---");
            System.out.println("1. Entregar Pedido (Ver Pendientes)");
            System.out.println("0. Cerrar Sesion y Salir");
            opcion = leerEntero("Seleccione una opcion: ");
            
            switch (opcion) {
                case 1:
                    gestionarEntrega(delivery.getIdUsuario());
                    break;
                case 0:
                    delivery.logout();
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }
    
    private static void gestionarEntrega(int idDelivery) {
        List<String> pedidosPendientes = pedidoCRUD.obtenerPedidosPendientes();
        if (pedidosPendientes.isEmpty()) {
            System.out.println("No hay pedidos pendientes para entregar.");
            return;
        }
        
        System.out.println("\n--- PEDIDOS PENDIENTES ---");
        pedidosPendientes.forEach(System.out::println);
        
        int idPedido = leerEntero("Ingrese el ID del pedido a confirmar entrega (0 para volver): ");
        if (idPedido == 0) return;
        
        try {
            if (pedidoCRUD.confirmarEntrega(idPedido, idDelivery)) {
                System.out.println("Entrega del pedido ID " + idPedido + " confirmada con exito.");
            } else {
                System.out.println("Fallo al confirmar la entrega. El pedido no existe o ya fue entregado.");
            }
        } catch (SQLException e) {
             System.out.println("Error de base de datos al confirmar entrega: " + e.getMessage());
        }
    }
      private static void agregarCategoria() throws SQLException {
        System.out.println("\n--- AGREGAR CATEGORIA ---");
        System.out.print("Nombre de la Nueva Categoria: ");
        String nombre = scanner.nextLine();
        
        if (!nombre.trim().isEmpty()) {
            Categoria nuevaCat = new Categoria(0, nombre);
            categoriaCRUD.agregarCategoria(nuevaCat);
        } else {
            System.out.println("El nombre no puede estar vacio.");
        }
    }
    
    private static void editarCategoria() throws SQLException {
        categoriaCRUD.obtenerTodas().forEach(System.out::println);
        int id = leerEntero("Ingrese el ID de la Categoria a editar: ");
        System.out.print("Ingrese el NUEVO nombre: ");
        String nuevoNombre = scanner.nextLine();

        if (!nuevoNombre.trim().isEmpty()) {
            Categoria catEditada = new Categoria(id, nuevoNombre);
            categoriaCRUD.editarCategoria(catEditada);
        } else {
            System.out.println("El nombre no puede estar vacio. Operacion cancelada.");
        }
    }
    
     private static void eliminarCategoria() throws SQLException {
        categoriaCRUD.obtenerTodas().forEach(System.out::println);
        int id = leerEntero("Ingrese el ID de la Categoria a eliminar: ");
        if (id>0) {
            categoriaCRUD.eliminarCategoria(id);
        } else {
            System.out.println("El nombre no puede ser 0 ó negativo.");
        }
    } 
    
}
