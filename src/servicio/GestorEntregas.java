package servicio;

import estructura.Cola;
import estructura.ListaEnlazada;
import modelo.Entrega;
import modelo.Pedido;
import java.time.LocalDateTime;

public class GestorEntregas {
    private Cola<Entrega> colaEntregas;
    private ListaEnlazada<Entrega> entregasCompletadas;
    private ListaEnlazada<Entrega> todasLasEntregas;
    
    public GestorEntregas() {
        this.colaEntregas = new Cola<>();
        this.entregasCompletadas = new ListaEnlazada<>();
        this.todasLasEntregas = new ListaEnlazada<>();
    }
    
    public Entrega agregarEntrega(Pedido pedido, String direccion, String distrito, 
                                   String repartidor, LocalDateTime horaEstimada) {
        
        if (!pedido.getEstado().equals("En Proceso")) {
            System.out.println("❌ Error: Solo se pueden agregar entregas de pedidos en proceso.");
            return null;
        }
        
        Entrega nuevaEntrega = new Entrega(pedido, direccion, distrito, repartidor, horaEstimada);
        colaEntregas.encolar(nuevaEntrega);
        todasLasEntregas.insertarAlFinal(nuevaEntrega);
        
        System.out.println("✅ Entrega agregada a la ruta:");
        System.out.println("   Pedido: " + pedido.getIdPedido());
        System.out.println("   Distrito: " + distrito);
        System.out.println("   Repartidor: " + repartidor);
        System.out.println("   Hora estimada: " + horaEstimada.toString());
        
        return nuevaEntrega;
    }
    
    public Entrega procesarSiguienteEntrega() {
        if (colaEntregas.estaVacia()) {
            System.out.println("❌ No hay entregas pendientes en la cola.");
            return null;
        }
        
        Entrega entrega = colaEntregas.desencolar();
        
        System.out.println("\n🚚 Procesando entrega:");
        System.out.println("   Pedido: " + entrega.getPedido().getIdPedido());
        System.out.println("   Cliente: " + entrega.getPedido().getCliente().getNombre());
        System.out.println("   Dirección: " + entrega.getDireccion());
        System.out.println("   Distrito: " + entrega.getDistrito());
        
        entrega.marcarEntregado();
        entregasCompletadas.insertarAlFinal(entrega);
        
        long tiempoEntrega = entrega.calcularTiempoEntrega();
        String resultado = tiempoEntrega <= 0 ? "A TIEMPO ✅" : "CON RETRASO ⚠️";
        
        System.out.println("   Estado: Entregado");
        System.out.println("   Tiempo de entrega: " + Math.abs(tiempoEntrega) + " minutos");
        System.out.println("   Resultado: " + resultado);
        
        return entrega;
    }
    
    public void mostrarRutaDelDia() {
        if (colaEntregas.estaVacia()) {
            System.out.println("\n🚚 No hay entregas pendientes para hoy.\n");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                  RUTA DEL DÍA (FIFO)                                      ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   Pedido     │ Cliente              │ Distrito        │ Hora Est │ Repartidor             ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        Object[] entregas = colaEntregas.toArray();
        
        for (int i = 0; i < entregas.length; i++) {
            Entrega e = (Entrega) entregas[i];
            System.out.println("║ " + (i + 1) + ". " + e.toStringTabla() + " ║");
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total de entregas pendientes: " + colaEntregas.getTamanio());
        System.out.println();
    }
    
    public boolean asignarRepartidor(Pedido pedido, String nuevoRepartidor) {
        for (Object obj : colaEntregas.toArray()) {
            Entrega e = (Entrega) obj;
            if (e.getPedido().equals(pedido)) {
                e.setRepartidor(nuevoRepartidor);
                System.out.println("✅ Repartidor asignado: " + nuevoRepartidor + " → Pedido " + pedido.getIdPedido());
                return true;
            }
        }
        
        System.out.println("❌ Entrega no encontrada en la cola.");
        return false;
    }
    
    public boolean reagendarEntrega(Pedido pedido) {
        Cola<Entrega> colaTemp = new Cola<>();
        Entrega entregaReagendar = null;
        
        while (!colaEntregas.estaVacia()) {
            Entrega e = colaEntregas.desencolar();
            if (e.getPedido().equals(pedido)) {
                entregaReagendar = e;
            } else {
                colaTemp.encolar(e);
            }
        }
        
        while (!colaTemp.estaVacia()) {
            colaEntregas.encolar(colaTemp.desencolar());
        }
        
        if (entregaReagendar != null) {
            colaEntregas.encolar(entregaReagendar);
            System.out.println("✅ Entrega reagendada al final de la ruta: " + pedido.getIdPedido());
            return true;
        }
        
        System.out.println("❌ Entrega no encontrada en la cola.");
        return false;
    }
    
    public int obtenerTotalEntregasDelDia() {
        return entregasCompletadas.getTamanio();
    }
    
    public double calcularTiempoPromedioEntrega() {
        if (entregasCompletadas.estaVacia()) {
            return 0.0;
        }
        
        long tiempoTotal = 0;
        int contador = 0;
        
        for (Object obj : entregasCompletadas.toArray()) {
            Entrega e = (Entrega) obj;
            long tiempo = e.calcularTiempoEntrega();
            if (tiempo >= 0) {
                tiempoTotal += Math.abs(tiempo);
                contador++;
            }
        }
        
        return contador > 0 ? (double) tiempoTotal / contador : 0.0;
    }
    
    public void mostrarEntregasPorDistrito() {
        if (todasLasEntregas.estaVacia()) {
            System.out.println("\n📊 No hay datos de entregas.\n");
            return;
        }
        
        String[] distritos = new String[100];
        int[] contadores = new int[100];
        int cantidadDistritos = 0;
        
        for (Object obj : todasLasEntregas.toArray()) {
            Entrega e = (Entrega) obj;
            String distrito = e.getDistrito();
            
            int indice = -1;
            for (int i = 0; i < cantidadDistritos; i++) {
                if (distritos[i].equals(distrito)) {
                    indice = i;
                    break;
                }
            }
            
            if (indice == -1) {
                distritos[cantidadDistritos] = distrito;
                contadores[cantidadDistritos] = 1;
                cantidadDistritos++;
            } else {
                contadores[indice]++;
            }
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              ENTREGAS POR DISTRITO                         ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        
        for (int i = 0; i < cantidadDistritos; i++) {
            System.out.println(String.format("║ %-40s │ %8d entregas ║", distritos[i], contadores[i]));
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
    
    public void mostrarEficienciaPorRepartidor() {
        if (entregasCompletadas.estaVacia()) {
            System.out.println("\n📊 No hay entregas completadas.\n");
            return;
        }
        
        String[] repartidores = new String[50];
        int[] entregasRealizadas = new int[50];
        long[] tiemposTotales = new long[50];
        int cantidadRepartidores = 0;
        
        for (Object obj : entregasCompletadas.toArray()) {
            Entrega e = (Entrega) obj;
            String repartidor = e.getRepartidor();
            long tiempo = Math.abs(e.calcularTiempoEntrega());
            
            int indice = -1;
            for (int i = 0; i < cantidadRepartidores; i++) {
                if (repartidores[i].equals(repartidor)) {
                    indice = i;
                    break;
                }
            }
            
            if (indice == -1) {
                repartidores[cantidadRepartidores] = repartidor;
                entregasRealizadas[cantidadRepartidores] = 1;
                tiemposTotales[cantidadRepartidores] = tiempo;
                cantidadRepartidores++;
            } else {
                entregasRealizadas[indice]++;
                tiemposTotales[indice] += tiempo;
            }
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        EFICIENCIA POR REPARTIDOR                               ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Repartidor                      │ Entregas │ Tiempo Promedio                  ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        
        for (int i = 0; i < cantidadRepartidores; i++) {
            double promedio = (double) tiemposTotales[i] / entregasRealizadas[i];
            System.out.println(String.format("║ %-31s │ %8d │ %6.1f minutos               ║", 
                repartidores[i], entregasRealizadas[i], promedio));
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝\n");
    }
    
    public int getCantidadEntregasPendientes() {
        return colaEntregas.getTamanio();
    }
    
    public int getCantidadEntregasCompletadas() {
        return entregasCompletadas.getTamanio();
    }
    
    public boolean hayEntregasPendientes() {
        return !colaEntregas.estaVacia();
    }
    
    public Entrega buscarEntregaPorPedido(Pedido pedido) {
        for (Object obj : todasLasEntregas.toArray()) {
            Entrega e = (Entrega) obj;
            if (e.getPedido().equals(pedido)) {
                return e;
            }
        }
        return null;
    }
    
    public void mostrarEntregasCompletadas() {
        if (entregasCompletadas.estaVacia()) {
            System.out.println("\n✅ No hay entregas completadas aún.\n");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              ENTREGAS COMPLETADAS                                          ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        for (Object obj : entregasCompletadas.toArray()) {
            Entrega e = (Entrega) obj;
            System.out.println("║ " + e.toString());
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total completadas: " + entregasCompletadas.getTamanio());
        System.out.println("Tiempo promedio: " + String.format("%.1f", calcularTiempoPromedioEntrega()) + " minutos");
        System.out.println();
    }
}