package interfaz;

import servicio.GestorInventario;
import servicio.GestorCategorias;
import servicio.GestorHistorial;
import modelo.Producto;
import modelo.Categoria;
import estructura.ListaEnlazada;
import java.util.Scanner;

public class MenuInventario {
    private Scanner scanner;
    private GestorInventario gestorInventario;
    private GestorCategorias gestorCategorias;
    private GestorHistorial gestorHistorial;
    
    public MenuInventario(Scanner scanner, GestorInventario gestorInventario, 
                          GestorCategorias gestorCategorias, GestorHistorial gestorHistorial) {
        this.scanner = scanner;
        this.gestorInventario = gestorInventario;
        this.gestorCategorias = gestorCategorias;
        this.gestorHistorial = gestorHistorial;
    }
    
    public void mostrar() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenu();
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1:
                    agregarProducto();
                    break;
                case 2:
                    buscarProducto();
                    break;
                case 3:
                    actualizarStock();
                    break;
                case 4:
                    eliminarProducto();
                    break;
                case 5:
                    listarProductos();
                    break;
                case 6:
                    buscarPorRangoPrecio();
                    break;
                case 7:
                    verProductosStockCritico();
                    break;
                case 8:
                    gestorInventario.mostrarInventario();
                    break;
                case 0:
                    continuar = false;
                    break;
                default:
                    System.out.println("❌ Opción inválida.");
            }
            
            if (continuar && opcion != 0) {
                pausar();
            }
        }
    }
    
    private void mostrarMenu() {
        limpiarPantalla();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        📦 GESTIÓN DE INVENTARIO                                ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   1. ➕ Agregar producto                                                       ║");
        System.out.println("║   2. 🔍 Buscar producto por código                                             ║");
        System.out.println("║   3. ✏️  Actualizar stock                                                      ║");
        System.out.println("║   4. 🗑️  Eliminar producto                                                     ║");
        System.out.println("║   5. 📋 Listar productos (ordenados)                                           ║");
        System.out.println("║   6. 💰 Buscar por rango de precio                                             ║");
        System.out.println("║   7. ⚠️  Ver productos con stock crítico                                       ║");
        System.out.println("║   8. 📊 Mostrar inventario completo                                            ║");
        System.out.println("║   0. ← Volver                                                                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total de productos: " + gestorInventario.getCantidadProductos());
        System.out.print("\n🔹 Seleccione una opción: ");
    }
    
    private void agregarProducto() {
        System.out.println("\n═══ AGREGAR NUEVO PRODUCTO ═══\n");
        
        System.out.print("Código del producto: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        
        if (codigo.isEmpty()) {
            System.out.println("❌ Código inválido.");
            return;
        }
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        
        gestorCategorias.mostrarMenuCategorias();
        System.out.print("\nSeleccione número de categoría: ");
        int numCategoria = leerOpcion();
        
        ListaEnlazada<Categoria> categorias = gestorCategorias.obtenerTodasLasCategorias();
        if (numCategoria < 1 || numCategoria > categorias.getTamanio()) {
            System.out.println("❌ Categoría inválida.");
            return;
        }
        
        Categoria categoria = (Categoria) categorias.obtenerEnPosicion(numCategoria - 1);
        
        System.out.print("Precio (S/): ");
        double precio = leerDouble();
        
        System.out.print("Stock inicial: ");
        int stock = leerOpcion();
        
        System.out.print("Características ecológicas: ");
        String caracteristicas = scanner.nextLine().trim();
        
        Producto nuevoProducto = new Producto(codigo, nombre, categoria, precio, stock, caracteristicas);
        
        if (gestorInventario.agregarProducto(nuevoProducto)) {
            gestorHistorial.registrarAgregarProducto(nuevoProducto);
        }
    }
    
    private void buscarProducto() {
        System.out.print("\nCódigo del producto a buscar: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        
        Producto producto = gestorInventario.buscarProducto(codigo);
        
        if (producto != null) {
            System.out.println("\n✅ Producto encontrado:");
            System.out.println(producto.toStringDetallado());
        } else {
            System.out.println("❌ Producto no encontrado.");
        }
    }
    
    private void actualizarStock() {
        System.out.print("\nCódigo del producto: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        
        Producto producto = gestorInventario.buscarProducto(codigo);
        if (producto == null) {
            System.out.println("❌ Producto no encontrado.");
            return;
        }
        
        System.out.println("Producto: " + producto.getNombre());
        System.out.println("Stock actual: " + producto.getStockDisponible());
        System.out.print("Cantidad a agregar/quitar (usar - para quitar): ");
        int cantidad = leerOpcion();
        
        gestorInventario.actualizarStock(codigo, cantidad);
    }
    
    private void eliminarProducto() {
        System.out.print("\nCódigo del producto a eliminar: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        
        Producto producto = gestorInventario.buscarProducto(codigo);
        if (producto == null) {
            System.out.println("❌ Producto no encontrado.");
            return;
        }
        
        System.out.println("\nProducto a eliminar:");
        System.out.println(producto.toString());
        
        if (confirmar("¿Está seguro de eliminar este producto?")) {
            if (gestorInventario.eliminarProducto(codigo)) {
                gestorHistorial.registrarEliminarProducto(producto);
            }
        }
    }
    
    private void listarProductos() {
        System.out.println("\n═══ OPCIONES DE LISTADO ═══");
        System.out.println("1. Ordenar por código");
        System.out.println("2. Ordenar por stock (menor a mayor)");
        System.out.println("3. Ordenar por precio (menor a mayor)");
        System.out.print("\nSeleccione: ");
        
        int opcion = leerOpcion();
        ListaEnlazada<Producto> productos;
        
        switch (opcion) {
            case 1:
                productos = gestorInventario.listarProductosOrdenados();
                break;
            case 2:
                productos = gestorInventario.listarProductosPorStock();
                break;
            case 3:
                productos = gestorInventario.listarProductosPorPrecio();
                break;
            default:
                System.out.println("❌ Opción inválida.");
                return;
        }
        
        if (productos.estaVacia()) {
            System.out.println("\n📦 No hay productos en el inventario.\n");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                            LISTADO DE PRODUCTOS                                ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        
        for (Object obj : productos.toArray()) {
            Producto p = (Producto) obj;
            System.out.println("║ " + p.toString());
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
    }
    
    private void buscarPorRangoPrecio() {
        System.out.print("\nPrecio mínimo (S/): ");
        double precioMin = leerDouble();
        
        System.out.print("Precio máximo (S/): ");
        double precioMax = leerDouble();
        
        ListaEnlazada<Producto> productos = gestorInventario.buscarPorRangoPrecio(precioMin, precioMax);
        
        if (productos.estaVacia()) {
            System.out.println("\n❌ No se encontraron productos en ese rango de precio.\n");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    PRODUCTOS EN RANGO S/" + precioMin + " - S/" + precioMax + "                          ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        
        for (Object obj : productos.toArray()) {
            Producto p = (Producto) obj;
            System.out.println("║ " + p.toString());
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total encontrados: " + productos.getTamanio());
    }
    
    private void verProductosStockCritico() {
        System.out.print("\nUmbral de stock crítico (unidades): ");
        int umbral = leerOpcion();
        
        ListaEnlazada<Producto> criticos = gestorInventario.obtenerProductosStockCritico(umbral);
        
        if (criticos.estaVacia()) {
            System.out.println("\n✅ No hay productos con stock crítico.\n");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        ⚠️  PRODUCTOS CON STOCK CRÍTICO                         ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        
        for (Object obj : criticos.toArray()) {
            Producto p = (Producto) obj;
            System.out.println("║ " + p.toString());
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("⚠️  Total con stock crítico: " + criticos.getTamanio());
    }
    
    private int leerOpcion() {
        try {
            String input = scanner.nextLine().trim();
            return input.isEmpty() ? -1 : Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private double leerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    private boolean confirmar(String mensaje) {
        System.out.print(mensaje + " (S/N): ");
        String respuesta = scanner.nextLine().trim().toUpperCase();
        return respuesta.equals("S") || respuesta.equals("SI") || respuesta.equals("SÍ");
    }
    
    private void pausar() {
        System.out.print("\n📌 Presione ENTER para continuar...");
        scanner.nextLine();
    }
    
    private void limpiarPantalla() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}