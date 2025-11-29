package interfaz;

import servicio.*;
import java.util.Scanner;

public class MenuPrincipal {
    private Scanner scanner;
    private GestorInventario gestorInventario;
    private GestorPedidos gestorPedidos;
    private GestorHistorial gestorHistorial;
    private GestorCategorias gestorCategorias;
    private GestorEntregas gestorEntregas;
    
    private MenuInventario menuInventario;
    private MenuPedidos menuPedidos;
    private MenuCategorias menuCategorias;
    private MenuEntregas menuEntregas;
    private MenuReportes menuReportes;
    
    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        
        this.gestorInventario = new GestorInventario();
        this.gestorPedidos = new GestorPedidos();
        this.gestorHistorial = new GestorHistorial();
        this.gestorCategorias = new GestorCategorias();
        this.gestorEntregas = new GestorEntregas();
        
        this.menuInventario = new MenuInventario(scanner, gestorInventario, gestorCategorias, gestorHistorial);
        this.menuPedidos = new MenuPedidos(scanner, gestorPedidos, gestorInventario, gestorHistorial);
        this.menuCategorias = new MenuCategorias(scanner, gestorCategorias, gestorInventario, gestorHistorial);
        this.menuEntregas = new MenuEntregas(scanner, gestorEntregas, gestorPedidos, gestorHistorial);
        this.menuReportes = new MenuReportes(scanner, gestorInventario, gestorPedidos, gestorEntregas, gestorCategorias, gestorHistorial);
    }
    
    public void iniciar() {
        mostrarBienvenida();
        
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenu();
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1:
                    menuInventario.mostrar();
                    break;
                case 2:
                    menuPedidos.mostrar();
                    break;
                case 3:
                    menuHistorial();
                    break;
                case 4:
                    menuCategorias.mostrar();
                    break;
                case 5:
                    menuEntregas.mostrar();
                    break;
                case 6:
                    menuReportes.mostrar();
                    break;
                case 0:
                    continuar = confirmarSalida();
                    break;
                default:
                    System.out.println("❌ Opción inválida. Intente nuevamente.");
            }
            
            if (continuar && opcion != 0) {
                pausar();
            }
        }
        
        mostrarDespedida();
        scanner.close();
    }
    
    private void mostrarBienvenida() {
        limpiarPantalla();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                                ║");
        System.out.println("║                         🌿 SISTEMA ECODELIVERY 🌿                              ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                  Gestión de Productos Ecológicos y Entregas                    ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                              Versión 1.0                                       ║");
        System.out.println("║                                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        pausar();
    }
    
    private void mostrarMenu() {
        limpiarPantalla();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                          🌿 SISTEMA ECODELIVERY 🌿                             ║");
        System.out.println("║                      Gestión de Productos Ecológicos                           ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                                ║");
        System.out.println("║   1. 📦 Gestión de Inventario                                                  ║");
        System.out.println("║   2. 🛒 Gestión de Pedidos                                                     ║");
        System.out.println("║   3. 🔄 Historial de Operaciones                                               ║");
        System.out.println("║   4. 📑 Categorías de Productos                                                ║");
        System.out.println("║   5. 🚚 Sistema de Rutas de Entrega                                            ║");
        System.out.println("║   6. 📊 Reportes y Estadísticas                                                ║");
        System.out.println("║                                                                                ║");
        System.out.println("║   0. ❌ Salir del Sistema                                                      ║");
        System.out.println("║                                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.print("\n🔹 Seleccione una opción: ");
    }
    
    private void menuHistorial() {
        limpiarPantalla();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        🔄 HISTORIAL DE OPERACIONES                             ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   1. Ver historial completo                                                    ║");
        System.out.println("║   2. Ver últimas N operaciones                                                 ║");
        System.out.println("║   3. Deshacer última operación                                                 ║");
        System.out.println("║   4. Buscar operaciones por tipo                                               ║");
        System.out.println("║   5. Buscar operaciones por módulo                                             ║");
        System.out.println("║   6. Limpiar historial                                                         ║");
        System.out.println("║   0. ← Volver al menú principal                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.print("\n🔹 Seleccione una opción: ");
        
        int opcion = leerOpcion();
        
        switch (opcion) {
            case 1:
                gestorHistorial.mostrarHistorialCompleto();
                break;
            case 2:
                System.out.print("¿Cuántas operaciones desea ver? ");
                int cantidad = leerOpcion();
                gestorHistorial.mostrarUltimasOperaciones(cantidad);
                break;
            case 3:
                if (confirmar("¿Está seguro de deshacer la última operación?")) {
                    gestorHistorial.deshacerUltimaOperacion(gestorInventario, gestorPedidos);
                }
                break;
            case 4:
                System.out.print("Tipo de operación (AGREGAR/MODIFICAR/ELIMINAR/PROCESAR): ");
                String tipo = scanner.nextLine().toUpperCase();
                gestorHistorial.buscarPorTipo(tipo);
                break;
            case 5:
                System.out.print("Módulo (INVENTARIO/PEDIDOS/CATEGORIAS/ENTREGAS): ");
                String modulo = scanner.nextLine().toUpperCase();
                gestorHistorial.buscarPorModulo(modulo);
                break;
            case 6:
                if (confirmar("¿Está seguro de limpiar todo el historial?")) {
                    gestorHistorial.limpiarHistorial();
                }
                break;
            case 0:
                return;
            default:
                System.out.println("❌ Opción inválida.");
        }
    }
    
    private boolean confirmarSalida() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                          ⚠️  CONFIRMAR SALIDA                                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        return confirmar("¿Está seguro de que desea salir del sistema?");
    }
    
    private void mostrarDespedida() {
        limpiarPantalla();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                                ║");
        System.out.println("║                  ✅ Gracias por usar Sistema EcoDelivery                       ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                    🌿 Cuidemos nuestro planeta juntos 🌿                        ║");
        System.out.println("║                                                                                ║");
        System.out.println("║                            ¡Hasta pronto!                                      ║");
        System.out.println("║                                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
    }
    
    private int leerOpcion() {
        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return -1;
            }
            return Integer.parseInt(input);
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