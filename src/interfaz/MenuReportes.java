// interfaz/MenuReportes.java
package interfaz;

import servicio.GestorInventario;
import servicio.GestorPedidos;
import servicio.GestorEntregas;
import servicio.GestorCategorias;
import servicio.GestorHistorial;
import modelo.Producto;
import java.util.Scanner;
import modelo.Pedido;

public class MenuReportes {
    private Scanner scanner;
    private GestorInventario gestorInventario;
    private GestorPedidos gestorPedidos;
    private GestorEntregas gestorEntregas;
    private GestorCategorias gestorCategorias;
    private GestorHistorial gestorHistorial;
    
    public MenuReportes(Scanner scanner, GestorInventario gestorInventario, GestorPedidos gestorPedidos,
                        GestorEntregas gestorEntregas, GestorCategorias gestorCategorias, GestorHistorial gestorHistorial) {
        this.scanner = scanner;
        this.gestorInventario = gestorInventario;
        this.gestorPedidos = gestorPedidos;
        this.gestorEntregas = gestorEntregas;
        this.gestorCategorias = gestorCategorias;
        this.gestorHistorial = gestorHistorial;
    }
    
    public void mostrar() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenu();
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1: reporteInventario(); break;
                case 2: reportePedidos(); break;
                case 3: reporteEntregas(); break;
                case 4: reporteCategorias(); break;
                case 5: reporteHistorial(); break;
                case 0: continuar = false; break;
                default: System.out.println("❌ Opción inválida."); break;
            }
            
            if (continuar && opcion != 0) pausar();
        }
    }
    
    private void mostrarMenu() {
        limpiarPantalla();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        📊 MENÚ DE REPORTES                                     ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   1. 📦 Reporte de Inventario                                                 ║");
        System.out.println("║   2. 🛒 Reporte de Pedidos                                                    ║");
        System.out.println("║   3. 🚚 Reporte de Entregas                                                   ║");
        System.out.println("║   4. 📂 Reporte de Categorías                                                 ║");
        System.out.println("║   5. 📜 Reporte de Historial                                                  ║");
        System.out.println("║   0. ⬅️ Volver al menú principal                                             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.print("\n🔹 Seleccione una opción: ");
    }
    
    private void reporteInventario() {
        System.out.println("\n📦 REPORTE DE INVENTARIO");
        gestorInventario.mostrarInventario();
        Producto masCaro = gestorInventario.obtenerProductoMasCaro();
        Producto masBarato = gestorInventario.obtenerProductoMasBarato();
        if (masCaro != null) System.out.println("• Producto más caro: " + masCaro);
        if (masBarato != null) System.out.println("• Producto más barato: " + masBarato);
    }
    
    private void reportePedidos() {
        System.out.println("\n🛒 REPORTE DE PEDIDOS");
        gestorPedidos.mostrarTodosLosPedidos();
        gestorPedidos.mostrarEstadisticasPorTipoCliente();
        Pedido mayor = gestorPedidos.obtenerPedidoMayorValor();
        if (mayor != null) System.out.println("• Pedido de mayor valor: S/ " + String.format("%.2f", mayor.getTotal()));
        System.out.println("• Promedio de productos por pedido: " + String.format("%.1f", gestorPedidos.calcularPromedioProductosPorPedido()));
    }
    
    private void reporteEntregas() {
        System.out.println("\n🚚 REPORTE DE ENTREGAS");
        gestorEntregas.mostrarEntregasCompletadas();
        System.out.println("• Tiempo promedio de entrega: " + String.format("%.1f", gestorEntregas.calcularTiempoPromedioEntrega()) + " minutos");
        gestorEntregas.mostrarEntregasPorDistrito();
        gestorEntregas.mostrarEficienciaPorRepartidor();
    }
    
    private void reporteCategorias() {
        System.out.println("\n📂 REPORTE DE CATEGORÍAS");
        gestorCategorias.listarCategorias();
        gestorCategorias.mostrarEstadisticasProductosPorCategoria(gestorInventario);
    }
    
    private void reporteHistorial() {
        System.out.println("\n📜 REPORTE DE HISTORIAL");
        gestorHistorial.mostrarHistorialCompleto();
    }
    
    private int leerOpcion() {
        try {
            String input = scanner.nextLine().trim();
            return input.isEmpty() ? -1 : Integer.parseInt(input);
        } catch (Exception e) { return -1; }
    }
    
    private void pausar() {
        System.out.print("\n📌 Presione ENTER para continuar...");
        scanner.nextLine();
    }
    
    private void limpiarPantalla() {
        for (int i = 0; i < 50; i++) System.out.println();
    }
}