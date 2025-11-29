package interfaz;

import servicio.GestorPedidos;
import servicio.GestorInventario;
import servicio.GestorHistorial;
import modelo.Pedido;
import modelo.Cliente;
import modelo.Producto;
import java.util.Scanner;

public class MenuPedidos {
    private Scanner scanner;
    private GestorPedidos gestorPedidos;
    private GestorInventario gestorInventario;
    private GestorHistorial gestorHistorial;
    
    public MenuPedidos(Scanner scanner, GestorPedidos gestorPedidos, 
                       GestorInventario gestorInventario, GestorHistorial gestorHistorial) {
        this.scanner = scanner;
        this.gestorPedidos = gestorPedidos;
        this.gestorInventario = gestorInventario;
        this.gestorHistorial = gestorHistorial;
    }
    
    public void mostrar() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenu();
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1:
                    registrarPedido();
                    break;
                case 2:
                    procesarSiguientePedido();
                    break;
                case 3:
                    gestorPedidos.mostrarPedidosPendientes();
                    break;
                case 4:
                    buscarPedido();
                    break;
                case 5:
                    gestorPedidos.mostrarEstadisticasPorTipoCliente();
                    break;
                case 6:
                    mostrarPedidoMayorValor();
                    break;
                case 7:
                    gestorPedidos.mostrarTodosLosPedidos();
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
        System.out.println("║                         🛒 GESTIÓN DE PEDIDOS                                  ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   1. ➕ Registrar nuevo pedido                                                 ║");
        System.out.println("║   2. ⚙️  Procesar siguiente pedido                                             ║");
        System.out.println("║   3. 📋 Ver pedidos pendientes                                                 ║");
        System.out.println("║   4. 🔍 Buscar pedido por ID                                                   ║");
        System.out.println("║   5. 📊 Estadísticas por tipo de cliente                                       ║");
        System.out.println("║   6. 💎 Ver pedido de mayor valor                                              ║");
        System.out.println("║   7. 📑 Ver todos los pedidos                                                  ║");
        System.out.println("║   0. ← Volver                                                                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Pendientes: " + gestorPedidos.getCantidadPedidosPendientes() + 
                         " | Procesados: " + gestorPedidos.getCantidadPedidosProcesados());
        System.out.print("\n🔹 Seleccione una opción: ");
    }
    
    private void registrarPedido() {
        System.out.println("\n═══ REGISTRAR NUEVO PEDIDO ═══\n");
        
        System.out.print("Nombre del cliente: ");
        String nombreCliente = scanner.nextLine().trim();
        
        System.out.print("Tipo de cliente (1=Premium, 2=Regular): ");
        int tipoNum = leerOpcion();
        String tipoCliente = (tipoNum == 1) ? "Premium" : "Regular";
        
        Cliente cliente = new Cliente(nombreCliente, tipoCliente);
        
        System.out.print("Tipo de entrega (1=Express, 2=Normal): ");
        int entregaNum = leerOpcion();
        String tipoEntrega = (entregaNum == 1) ? "Express" : "Normal";
        
        System.out.print("Dirección de entrega: ");
        String direccion = scanner.nextLine().trim();
        
        Pedido pedido = gestorPedidos.registrarPedido(cliente, tipoEntrega, direccion);
        
        boolean agregarMas = true;
        while (agregarMas) {
            System.out.print("\n¿Agregar producto al pedido? (S/N): ");
            String resp = scanner.nextLine().trim().toUpperCase();
            
            if (!resp.equals("S")) {
                break;
            }
            
            System.out.print("Código del producto: ");
            String codigo = scanner.nextLine().trim().toUpperCase();
            
            Producto producto = gestorInventario.buscarProducto(codigo);
            
            if (producto == null) {
                System.out.println("❌ Producto no encontrado.");
                continue;
            }
            
            if (!producto.hayStock(1)) {
                System.out.println("❌ Sin stock disponible.");
                continue;
            }
            
            gestorPedidos.agregarProductoAPedido(pedido, producto);
        }
        
        if (pedido.getCantidadProductos() > 0) {
            System.out.println("\n✅ Pedido registrado exitosamente.");
            System.out.println("   ID: " + pedido.getIdPedido());
            System.out.println("   Productos: " + pedido.getCantidadProductos());
            System.out.println("   Total: S/ " + String.format("%.2f", pedido.getTotal()));
            System.out.println("   Prioridad: " + pedido.getPrioridad());
        } else {
            System.out.println("⚠️  Pedido creado pero sin productos.");
        }
    }
    
    private void procesarSiguientePedido() {
        System.out.println("\n═══ PROCESAR SIGUIENTE PEDIDO ═══\n");
        
        if (gestorPedidos.getCantidadPedidosPendientes() == 0) {
            System.out.println("❌ No hay pedidos pendientes.");
            return;
        }
        
        Pedido pedido = gestorPedidos.procesarSiguientePedido(gestorInventario);
        
        if (pedido != null) {
            gestorHistorial.registrarProcesarPedido(pedido);
        }
    }
    
    private void buscarPedido() {
        System.out.print("\nID del pedido: ");
        String id = scanner.nextLine().trim().toUpperCase();
        
        Pedido pedido = gestorPedidos.buscarPedidoPorID(id);
        
        if (pedido != null) {
            System.out.println("\n✅ Pedido encontrado:");
            System.out.println(pedido.toStringDetallado());
        } else {
            System.out.println("❌ Pedido no encontrado.");
        }
    }
    
    private void mostrarPedidoMayorValor() {
        Pedido mayor = gestorPedidos.obtenerPedidoMayorValor();
        
        if (mayor != null) {
            System.out.println("\n💎 PEDIDO DE MAYOR VALOR:");
            System.out.println(mayor.toStringDetallado());
        } else {
            System.out.println("\n❌ No hay pedidos registrados.");
        }
    }
    
    private int leerOpcion() {
        try {
            String input = scanner.nextLine().trim();
            return input.isEmpty() ? -1 : Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
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