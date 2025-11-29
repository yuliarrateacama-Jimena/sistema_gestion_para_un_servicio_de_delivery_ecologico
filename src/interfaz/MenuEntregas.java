// interfaz/MenuEntregas.java
package interfaz;

import servicio.GestorEntregas;
import servicio.GestorPedidos;
import servicio.GestorHistorial;
import modelo.Entrega;
import modelo.Pedido;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MenuEntregas {
    private Scanner scanner;
    private GestorEntregas gestorEntregas;
    private GestorPedidos gestorPedidos;
    private GestorHistorial gestorHistorial;
    
    public MenuEntregas(Scanner scanner, GestorEntregas gestorEntregas, 
                        GestorPedidos gestorPedidos, GestorHistorial gestorHistorial) {
        this.scanner = scanner;
        this.gestorEntregas = gestorEntregas;
        this.gestorPedidos = gestorPedidos;
        this.gestorHistorial = gestorHistorial;
    }
    
    public void mostrar() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenu();
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1:
                    agregarEntrega();
                    break;
                case 2:
                    procesarSiguienteEntrega();
                    break;
                case 3:
                    marcarEntregado();
                    break;
                case 4:
                    gestorEntregas.mostrarRutaDelDia();
                    break;
                case 5:
                    gestorEntregas.mostrarEntregasCompletadas();
                    break;
                case 6:
                    verEstadisticas();
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
        System.out.println("║                       🚚 GESTIÓN DE ENTREGAS                                   ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   1. ➕ Agregar entrega                                                        ║");
        System.out.println("║   2. 🔄 Procesar siguiente entrega                                             ║");
        System.out.println("║   3. ✅ Marcar como entregado                                                  ║");
        System.out.println("║   4. 📋 Ver ruta del día                                                       ║");
        System.out.println("║   5. 📋 Ver entregas completadas                                               ║");
        System.out.println("║   6. 📊 Ver estadísticas                                                       ║");
        System.out.println("║   0. ⬅️  Volver al menú principal                                             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
    }
    
    private void agregarEntrega() {
        System.out.print("\nID de pedido: ");
        String id = scanner.nextLine().trim();
        Pedido pedido = gestorPedidos.buscarPedidoPorID(id);
        
        if (pedido == null) {
            System.out.println("❌ Pedido no encontrado.");
            return;
        }
        
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine().trim();
        System.out.print("Distrito: ");
        String distrito = scanner.nextLine().trim();
        System.out.print("Repartidor: ");
        String repartidor = scanner.nextLine().trim();
        System.out.print("Hora estimada (yyyy-MM-dd HH:mm): ");
        String horaStr = scanner.nextLine().trim();
        
        LocalDateTime horaEstimada;
        try {
            horaEstimada = LocalDateTime.parse(horaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            System.out.println("❌ Formato de hora inválido.");
            return;
        }
        
        Entrega nueva = gestorEntregas.agregarEntrega(pedido, direccion, distrito, repartidor, horaEstimada);
        if (nueva != null) {
            gestorHistorial.registrarOperacion("AGREGAR", "Entrega agregada para pedido " + id, null, "ENTREGAS");
        }
    }
    
    private void procesarSiguienteEntrega() {
        Entrega entregada = gestorEntregas.procesarSiguienteEntrega();
        if (entregada != null) {
            gestorHistorial.registrarOperacion("PROCESAR", "Entrega procesada para pedido " + entregada.getPedido().getIdPedido(), null, "ENTREGAS");
        }
    }
    
    private void marcarEntregado() {
        System.out.print("\nID de pedido: ");
        String id = scanner.nextLine().trim();
        Pedido pedido = gestorPedidos.buscarPedidoPorID(id);
        
        if (pedido == null) {
            System.out.println("❌ Pedido no encontrado.");
            return;
        }
        
        Entrega entrega = gestorEntregas.buscarEntregaPorPedido(pedido);
        
        if (entrega == null) {
            System.out.println("❌ No hay entrega asociada a este pedido.");
            return;
        }
        
        if (confirmar("¿Marcar como entregado?")) {
            entrega.marcarEntregado();
            gestorHistorial.registrarOperacion("MODIFICAR", "Entrega marcada como entregada para pedido " + id, entrega.getEstado(), "ENTREGAS");
            System.out.println("✅ Entrega marcada como entregada.");
        }
    }
    
    private void verEstadisticas() {
        gestorEntregas.mostrarEntregasPorDistrito();
        gestorEntregas.mostrarEficienciaPorRepartidor();
    }
    
    private int leerOpcion() {
        try {
            String input = scanner.nextLine().trim();
            return input.isEmpty() ? -1 : Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
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