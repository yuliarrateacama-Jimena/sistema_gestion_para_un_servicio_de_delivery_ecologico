// utilidades/DatosPrueba.java
package utilidades;

import servicio.*;
import modelo.*;
import java.time.LocalDateTime;

public class DatosPrueba {
    public static void cargarDatos(GestorInventario gi, GestorCategorias gc, GestorPedidos gp, GestorEntregas ge) {
        // Categorías ya inicializadas en GestorCategorias

        // Productos
        Categoria cat1 = gc.buscarCategoriaPorNombre("Recipientes");
        Categoria cat2 = gc.buscarCategoriaPorNombre("Bolsas Reutilizables");
        
        Producto p1 = new Producto("P001", "Botella Reutilizable", cat1, 15.0, 50, "Acero inoxidable, libre de BPA");
        Producto p2 = new Producto("P002", "Bolsa de Tela", cat2, 5.0, 100, "Algodón orgánico, lavable");
        gi.agregarProducto(p1);
        gi.agregarProducto(p2);
        
        // Clientes
        Cliente c1 = new Cliente("Juan Perez", "Premium", "999123456", "juan@email.com", "Av. Principal 123");
        Cliente c2 = new Cliente("Maria Lopez", "Regular", "999654321", "maria@email.com", "Calle Secundaria 456");
        
        // Pedidos
        Pedido ped1 = gp.registrarPedido(c1, "Express", "Av. Principal 123");
        gp.agregarProductoAPedido(ped1, p1);
        Pedido ped2 = gp.registrarPedido(c2, "Normal", "Calle Secundaria 456");
        gp.agregarProductoAPedido(ped2, p2);
        
        // Procesar uno
        gp.procesarSiguientePedido(gi);
        
        // Entregas
        ge.agregarEntrega(ped1, "Av. Principal 123", "Miraflores", "Repartidor1", LocalDateTime.now().plusHours(1));
        ge.agregarEntrega(ped2, "Calle Secundaria 456", "San Isidro", "Repartidor2", LocalDateTime.now().plusHours(2));
    }
}
