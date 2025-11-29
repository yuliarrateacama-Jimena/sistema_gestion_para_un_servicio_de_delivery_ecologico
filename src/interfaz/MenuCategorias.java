// interfaz/MenuCategorias.java
package interfaz;

import servicio.GestorCategorias;
import servicio.GestorInventario;
import servicio.GestorHistorial;
import modelo.Categoria;
import estructura.ListaEnlazada;
import java.util.Scanner;

public class MenuCategorias {
    private Scanner scanner;
    private GestorCategorias gestorCategorias;
    private GestorInventario gestorInventario;
    private GestorHistorial gestorHistorial;
    
    public MenuCategorias(Scanner scanner, GestorCategorias gestorCategorias, 
                          GestorInventario gestorInventario, GestorHistorial gestorHistorial) {
        this.scanner = scanner;
        this.gestorCategorias = gestorCategorias;
        this.gestorInventario = gestorInventario;
        this.gestorHistorial = gestorHistorial;
    }
    
    public void mostrar() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenu();
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1: agregarCategoria(); break;
                case 2: buscarCategoria(); break;
                case 3: actualizarCategoria(); break;
                case 4: eliminarCategoria(); break;
                case 5: gestorCategorias.listarCategorias(); break;
                case 6: verEstadisticas(); break;
                case 0: continuar = false; break;
                default: System.out.println("❌ Opción inválida."); break;
            }
            
            if (continuar && opcion != 0) pausar();
        }
    }
    
    private void mostrarMenu() {
        limpiarPantalla();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        📂 GESTIÓN DE CATEGORÍAS                                ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   1. ➕ Agregar categoría                                                      ║");
        System.out.println("║   2. 🔍 Buscar categoría                                                       ║");
        System.out.println("║   3. ✏️  Actualizar categoría                                                  ║");
        System.out.println("║   4. 🗑️  Eliminar categoría                                                   ║");
        System.out.println("║   5. 📋 Listar todas las categorías                                            ║");
        System.out.println("║   6. 📊 Estadísticas de categorías                                             ║");
        System.out.println("║   0. ← Volver al menú principal                                               ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total de categorías: " + gestorCategorias.getCantidadCategorias());
        System.out.print("\n🔹 Seleccione una opción: ");
    }
    
    private void agregarCategoria() {
        System.out.println("\n═══ AGREGAR NUEVA CATEGORÍA ═══\n");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine().trim();
        System.out.print("Características ecológicas: ");
        String caracteristicas = scanner.nextLine().trim();
        
        Categoria nueva = gestorCategorias.crearCategoria(nombre, descripcion, caracteristicas);
        if (nueva != null) {
            // Si tienes método en GestorHistorial: gestorHistorial.registrarAgregarCategoria(nueva);
            System.out.println("✅ Categoría agregada con éxito.");
        }
    }
    
    private void buscarCategoria() {
        System.out.print("\nID de categoría a buscar: ");
        int id = leerOpcion();
        Categoria cat = gestorCategorias.buscarCategoriaPorID(id);
        if (cat != null) {
            System.out.println("\n✅ Categoría encontrada:");
            System.out.println(cat.toStringDetallado());
            gestorCategorias.listarProductosPorCategoria(cat, gestorInventario);
        } else {
            System.out.println("❌ Categoría no encontrada.");
        }
    }
    
    private void actualizarCategoria() {
        System.out.print("\nID de categoría a actualizar: ");
        int id = leerOpcion();
        Categoria cat = gestorCategorias.buscarCategoriaPorID(id);
        if (cat == null) {
            System.out.println("❌ Categoría no encontrada.");
            return;
        }
        System.out.println("Datos actuales:");
        System.out.println(cat.toStringDetallado());
        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Nueva descripción (Enter para mantener): ");
        String desc = scanner.nextLine().trim();
        System.out.print("Nuevas características (Enter para mantener): ");
        String caract = scanner.nextLine().trim();
        
        if (gestorCategorias.modificarCategoria(id, 
            nombre.isEmpty() ? cat.getNombre() : nombre,
            desc.isEmpty() ? cat.getDescripcion() : desc,
            caract.isEmpty() ? cat.getCaracteristicas() : caract)) {
            // registrarModificarCategoria si existe
            System.out.println("✅ Categoría actualizada.");
        }
    }
    
    private void eliminarCategoria() {
        System.out.print("\nID de categoría a eliminar: ");
        int id = leerOpcion();
        if (gestorCategorias.eliminarCategoria(id, gestorInventario)) {
            // registrarEliminarCategoria si existe
        }
    }
    
    private void verEstadisticas() {
        System.out.println("\n📊 ESTADÍSTICAS DE CATEGORÍAS");
        Categoria mas = gestorCategorias.obtenerCategoriaConMasProductos(gestorInventario);
        Categoria menos = gestorCategorias.obtenerCategoriaConMenorStock(gestorInventario);
        if (mas != null) System.out.println("• Categoría con más productos: " + mas.getNombre());
        if (menos != null) System.out.println("• Categoría con menor stock total: " + menos.getNombre());
        gestorCategorias.mostrarEstadisticasProductosPorCategoria(gestorInventario);
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