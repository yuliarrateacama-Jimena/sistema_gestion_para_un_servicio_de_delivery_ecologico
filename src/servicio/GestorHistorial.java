package servicio;

import estructura.Pila;
import modelo.Operacion;
import modelo.Producto;
import modelo.Pedido;

public class GestorHistorial {
    private Pila<Operacion> pilaOperaciones;
    private static final int CAPACIDAD_MAXIMA = 50;
    
    public GestorHistorial() {
        this.pilaOperaciones = new Pila<>(CAPACIDAD_MAXIMA);
    }
    
    public void registrarOperacion(String tipoOperacion, String descripcion, 
                                    Object datosAnteriores, String modulo) {
        Operacion operacion = new Operacion(tipoOperacion, descripcion, datosAnteriores, modulo);
        
        if (pilaOperaciones.estaLlena()) {
            System.out.println("⚠️ Historial lleno. La operación más antigua será eliminada.");
        }
        
        pilaOperaciones.push(operacion);
    }
    
    public void registrarAgregarProducto(Producto producto) {
        String descripcion = "Producto agregado: " + producto.getNombre() + " (Código: " + producto.getCodigoProducto() + ")";
        registrarOperacion("AGREGAR", descripcion, producto, "INVENTARIO");
    }
    
    public void registrarModificarProducto(Producto productoAnterior, Producto productoNuevo) {
        String descripcion = "Producto modificado: " + productoNuevo.getCodigoProducto();
        registrarOperacion("MODIFICAR", descripcion, productoAnterior, "INVENTARIO");
    }
    
    public void registrarEliminarProducto(Producto producto) {
        String descripcion = "Producto eliminado: " + producto.getNombre() + " (Código: " + producto.getCodigoProducto() + ")";
        registrarOperacion("ELIMINAR", descripcion, producto, "INVENTARIO");
    }
    
    public void registrarProcesarPedido(Pedido pedido) {
        String descripcion = "Pedido procesado: " + pedido.getIdPedido() + " - Cliente: " + pedido.getCliente().getNombre();
        registrarOperacion("PROCESAR", descripcion, pedido, "PEDIDOS");
    }
    
    public void registrarCambiarEstadoPedido(Pedido pedido, String estadoAnterior) {
        String descripcion = "Estado de pedido " + pedido.getIdPedido() + " cambió de " + estadoAnterior + " a " + pedido.getEstado();
        registrarOperacion("MODIFICAR", descripcion, estadoAnterior, "PEDIDOS");
    }
    
    public boolean deshacerUltimaOperacion(GestorInventario gestorInventario, GestorPedidos gestorPedidos) {
        if (pilaOperaciones.estaVacia()) {
            System.out.println("❌ No hay operaciones para deshacer.");
            return false;
        }
        
        Operacion operacion = pilaOperaciones.pop();
        
        if (!operacion.esDeshacible()) {
            System.out.println("❌ Esta operación no puede deshacerse.");
            return false;
        }
        
        System.out.println("🔄 Deshaciendo operación: " + operacion.getDescripcion());
        
        boolean exito = false;
        
        switch (operacion.getTipoOperacion()) {
            case "AGREGAR":
                if (operacion.getModulo().equals("INVENTARIO")) {
                    Producto producto = (Producto) operacion.getDatosAnteriores();
                    exito = gestorInventario.eliminarProducto(producto.getCodigoProducto());
                }
                break;
                
            case "ELIMINAR":
                if (operacion.getModulo().equals("INVENTARIO")) {
                    Producto producto = (Producto) operacion.getDatosAnteriores();
                    exito = gestorInventario.agregarProducto(producto);
                }
                break;
                
            case "MODIFICAR":
                if (operacion.getModulo().equals("INVENTARIO")) {
                    Producto productoAnterior = (Producto) operacion.getDatosAnteriores();
                    System.out.println("⚠️ Restauración de modificaciones no implementada completamente.");
                    exito = true;
                }
                break;
                
            case "PROCESAR":
                if (operacion.getModulo().equals("PEDIDOS")) {
                    Pedido pedido = (Pedido) operacion.getDatosAnteriores();
                    for (Object obj : pedido.getListaProductos().toArray()) {
                        Producto p = (Producto) obj;
                        gestorInventario.actualizarStock(p.getCodigoProducto(), 1);
                    }
                    pedido.setEstado("Pendiente");
                    exito = true;
                }
                break;
                
            default:
                System.out.println("❌ Tipo de operación no reconocida.");
                break;
        }
        
        if (exito) {
            System.out.println("✅ Operación deshecha exitosamente.");
        } else {
            System.out.println("❌ No se pudo deshacer la operación.");
        }
        
        return exito;
    }
    
    public void mostrarHistorialCompleto() {
        if (pilaOperaciones.estaVacia()) {
            System.out.println("\n📜 El historial está vacío.\n");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              HISTORIAL DE OPERACIONES                                     ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   Fecha/Hora    │ Módulo          │ Tipo         │ Descripción                            ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        Object[] operaciones = pilaOperaciones.toArray();
        
        for (Object obj : operaciones) {
            Operacion op = (Operacion) obj;
            System.out.println("║ " + op.toStringTabla() + " ║");
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total de operaciones: " + pilaOperaciones.getTamanio() + "/" + CAPACIDAD_MAXIMA);
        System.out.println();
    }
    
    public void mostrarUltimasOperaciones(int cantidad) {
        if (pilaOperaciones.estaVacia()) {
            System.out.println("\n📜 El historial está vacío.\n");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                          ÚLTIMAS " + cantidad + " OPERACIONES                                           ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        Object[] operaciones = pilaOperaciones.toArray();
        int limite = Math.min(cantidad, operaciones.length);
        
        for (int i = 0; i < limite; i++) {
            Operacion op = (Operacion) operaciones[i];
            System.out.println("║ " + (i + 1) + ". " + op.toString());
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════╝\n");
    }
    
    public void limpiarHistorial() {
        pilaOperaciones.limpiar();
        System.out.println("✅ Historial limpiado.");
    }
    
    public void buscarPorTipo(String tipo) {
        Object[] operaciones = pilaOperaciones.toArray();
        int encontradas = 0;
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     OPERACIONES DE TIPO: " + tipo + "                                         ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        for (Object obj : operaciones) {
            Operacion op = (Operacion) obj;
            if (op.getTipoOperacion().equalsIgnoreCase(tipo)) {
                System.out.println("║ " + op.toString());
                encontradas++;
            }
        }
        
        if (encontradas == 0) {
            System.out.println("║ No se encontraron operaciones de este tipo.                                               ║");
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Encontradas: " + encontradas);
        System.out.println();
    }
    
    public void buscarPorModulo(String modulo) {
        Object[] operaciones = pilaOperaciones.toArray();
        int encontradas = 0;
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     OPERACIONES DEL MÓDULO: " + modulo + "                                    ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        for (Object obj : operaciones) {
            Operacion op = (Operacion) obj;
            if (op.getModulo().equalsIgnoreCase(modulo)) {
                System.out.println("║ " + op.toString());
                encontradas++;
            }
        }
        
        if (encontradas == 0) {
            System.out.println("║ No se encontraron operaciones de este módulo.                                             ║");
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Encontradas: " + encontradas);
        System.out.println();
    }
    
    public int getCantidadOperaciones() {
        return pilaOperaciones.getTamanio();
    }
    
    public boolean hayOperaciones() {
        return !pilaOperaciones.estaVacia();
    }
    
    public Operacion verUltimaOperacion() {
        if (pilaOperaciones.estaVacia()) {
            return null;
        }
        return pilaOperaciones.peek();
    }
}