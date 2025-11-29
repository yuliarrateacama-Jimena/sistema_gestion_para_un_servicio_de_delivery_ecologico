package servicio;

import estructura.ColaConPrioridad;
import estructura.ListaEnlazada;
import modelo.Pedido;
import modelo.Producto;
import modelo.Cliente;

public class GestorPedidos {
    private ColaConPrioridad<Pedido> colaPedidos;
    private ListaEnlazada<Pedido> pedidosProcesados;
    private ListaEnlazada<Pedido> todosLosPedidos;
    private int contadorPedidos;
    
    public GestorPedidos() {
        this.colaPedidos = new ColaConPrioridad<>();
        this.pedidosProcesados = new ListaEnlazada<>();
        this.todosLosPedidos = new ListaEnlazada<>();
        this.contadorPedidos = 1000;
    }
    
    public Pedido registrarPedido(Cliente cliente, String tipoEntrega, String direccion) {
        String idPedido = "PED-" + (contadorPedidos++);
        Pedido nuevoPedido = new Pedido(idPedido, cliente, tipoEntrega, direccion);
        
        colaPedidos.insertar(nuevoPedido, nuevoPedido.getPrioridad());
        todosLosPedidos.insertarAlFinal(nuevoPedido);
        
        System.out.println("✅ Pedido registrado: " + idPedido);
        System.out.println("   Cliente: " + cliente.getNombre() + " (" + cliente.getTipoCliente() + ")");
        System.out.println("   Tipo: " + tipoEntrega);
        System.out.println("   Prioridad asignada: " + nuevoPedido.getPrioridad());
        
        return nuevoPedido;
    }
    
    public boolean agregarProductoAPedido(Pedido pedido, Producto producto) {
        if (pedido.getEstado().equals("En Proceso") || pedido.getEstado().equals("Entregado")) {
            System.out.println("❌ No se pueden agregar productos a un pedido " + pedido.getEstado());
            return false;
        }
        
        pedido.agregarProducto(producto);
        System.out.println("✅ Producto agregado al pedido: " + producto.getNombre());
        return true;
    }
    
    public Pedido procesarSiguientePedido(GestorInventario gestorInventario) {
        if (colaPedidos.estaVacia()) {
            System.out.println("❌ No hay pedidos pendientes para procesar.");
            return null;
        }
        
        Pedido pedido = colaPedidos.extraerMaximo();
        
        System.out.println("\n🔄 Procesando pedido: " + pedido.getIdPedido());
        System.out.println("   Prioridad: " + pedido.getPrioridad());
        
        boolean stockDisponible = true;
        ListaEnlazada<Producto> productosDelPedido = pedido.getListaProductos();
        
        for (Object obj : productosDelPedido.toArray()) {
            Producto p = (Producto) obj;
            Producto productoInventario = gestorInventario.buscarProducto(p.getCodigoProducto());
            
            if (productoInventario == null || !productoInventario.hayStock(1)) {
                System.out.println("❌ Stock insuficiente para: " + p.getNombre());
                stockDisponible = false;
                break;
            }
        }
        
        if (!stockDisponible) {
            System.out.println("❌ Pedido no puede procesarse por falta de stock.");
            colaPedidos.insertar(pedido, Math.max(1, pedido.getPrioridad() - 2));
            return null;
        }
        
        for (Object obj : productosDelPedido.toArray()) {
            Producto p = (Producto) obj;
            gestorInventario.actualizarStock(p.getCodigoProducto(), -1);
        }
        
        pedido.setEstado("En Proceso");
        pedidosProcesados.insertarAlFinal(pedido);
        
        System.out.println("✅ Pedido procesado exitosamente");
        System.out.println("   Total: S/ " + String.format("%.2f", pedido.getTotal()));
        
        return pedido;
    }
    
    public void mostrarPedidosPendientes() {
        if (colaPedidos.estaVacia()) {
            System.out.println("\n📋 No hay pedidos pendientes.\n");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              PEDIDOS PENDIENTES (POR PRIORIDAD)                           ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        Object[] pedidos = colaPedidos.toArray();
        
        for (int i = 0; i < pedidos.length; i++) {
            Pedido p = (Pedido) pedidos[i];
            System.out.println("║ " + (i + 1) + ". " + p.toString());
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total pendientes: " + colaPedidos.getTamanio());
        System.out.println();
    }
    
    public Pedido buscarPedidoPorID(String idPedido) {
        for (Object obj : todosLosPedidos.toArray()) {
            Pedido p = (Pedido) obj;
            if (p.getIdPedido().equals(idPedido)) {
                return p;
            }
        }
        return null;
    }
    
    public double calcularTotalPedido(Pedido pedido) {
        return pedido.getTotal();
    }
    
    public void mostrarEstadisticasPorTipoCliente() {
        int premium = 0;
        int regular = 0;
        double totalPremium = 0.0;
        double totalRegular = 0.0;
        
        for (Object obj : todosLosPedidos.toArray()) {
            Pedido p = (Pedido) obj;
            if (p.getCliente().esPremium()) {
                premium++;
                totalPremium += p.getTotal();
            } else {
                regular++;
                totalRegular += p.getTotal();
            }
        }
        
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║     ESTADÍSTICAS POR TIPO DE CLIENTE           ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║ Premium:  " + String.format("%-3d pedidos | S/ %12.2f", premium, totalPremium) + " ║");
        System.out.println("║ Regular:  " + String.format("%-3d pedidos | S/ %12.2f", regular, totalRegular) + " ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║ TOTAL:    " + String.format("%-3d pedidos | S/ %12.2f", (premium + regular), (totalPremium + totalRegular)) + " ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
    }
    
    public Pedido obtenerPedidoMayorValor() {
        if (todosLosPedidos.estaVacia()) {
            return null;
        }
        
        Pedido mayor = (Pedido) todosLosPedidos.obtenerEnPosicion(0);
        
        for (Object obj : todosLosPedidos.toArray()) {
            Pedido p = (Pedido) obj;
            if (p.getTotal() > mayor.getTotal()) {
                mayor = p;
            }
        }
        
        return mayor;
    }
    
    public double calcularPromedioProductosPorPedido() {
        if (todosLosPedidos.estaVacia()) {
            return 0.0;
        }
        
        int totalProductos = 0;
        
        for (Object obj : todosLosPedidos.toArray()) {
            Pedido p = (Pedido) obj;
            totalProductos += p.getCantidadProductos();
        }
        
        return (double) totalProductos / todosLosPedidos.getTamanio();
    }
    
    public int getCantidadPedidosPendientes() {
        return colaPedidos.getTamanio();
    }
    
    public int getCantidadPedidosProcesados() {
        return pedidosProcesados.getTamanio();
    }
    
    public int getCantidadTotalPedidos() {
        return todosLosPedidos.getTamanio();
    }
    
    public ListaEnlazada<Pedido> obtenerTodosLosPedidos() {
        return todosLosPedidos;
    }
    
    public ListaEnlazada<Pedido> obtenerPedidosProcesados() {
        return pedidosProcesados;
    }
    
    public ListaEnlazada<Pedido> buscarPorEstado(String estado) {
        ListaEnlazada<Pedido> resultado = new ListaEnlazada<>();
        
        for (Object obj : todosLosPedidos.toArray()) {
            Pedido p = (Pedido) obj;
            if (p.getEstado().equalsIgnoreCase(estado)) {
                resultado.insertarAlFinal(p);
            }
        }
        
        return resultado;
    }
    
    public ListaEnlazada<Pedido> buscarPorCliente(String nombreCliente) {
        ListaEnlazada<Pedido> resultado = new ListaEnlazada<>();
        
        for (Object obj : todosLosPedidos.toArray()) {
            Pedido p = (Pedido) obj;
            if (p.getCliente().getNombre().toLowerCase().contains(nombreCliente.toLowerCase())) {
                resultado.insertarAlFinal(p);
            }
        }
        
        return resultado;
    }
    
    public void mostrarTodosLosPedidos() {
        if (todosLosPedidos.estaVacia()) {
            System.out.println("\n📋 No hay pedidos registrados.\n");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                  TODOS LOS PEDIDOS                                         ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        for (Object obj : todosLosPedidos.toArray()) {
            Pedido p = (Pedido) obj;
            System.out.println("║ " + p.toString());
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total de pedidos: " + todosLosPedidos.getTamanio());
        System.out.println();
    }
}