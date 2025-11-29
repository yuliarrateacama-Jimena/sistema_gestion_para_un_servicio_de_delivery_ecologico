package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

public class Entrega {
    private Pedido pedido;
    private String direccion;
    private String distrito;
    private String repartidor;
    private LocalDateTime horaEstimada;
    private LocalDateTime horaReal;
    private String estado;
    
    public Entrega(Pedido pedido, String direccion, String distrito, String repartidor, LocalDateTime horaEstimada) {
        this.pedido = pedido;
        this.direccion = direccion;
        this.distrito = distrito;
        this.repartidor = repartidor;
        this.horaEstimada = horaEstimada;
        this.horaReal = null;
        this.estado = "Pendiente";
    }
    
    public Pedido getPedido() {
        return pedido;
    }
    
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getDistrito() {
        return distrito;
    }
    
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
    
    public String getRepartidor() {
        return repartidor;
    }
    
    public void setRepartidor(String repartidor) {
        this.repartidor = repartidor;
    }
    
    public LocalDateTime getHoraEstimada() {
        return horaEstimada;
    }
    
    public void setHoraEstimada(LocalDateTime horaEstimada) {
        this.horaEstimada = horaEstimada;
    }
    
    public LocalDateTime getHoraReal() {
        return horaReal;
    }
    
    public void setHoraReal(LocalDateTime horaReal) {
        this.horaReal = horaReal;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public void marcarEntregado() {
        this.estado = "Entregado";
        this.horaReal = LocalDateTime.now();
        this.pedido.setEstado("Entregado");
    }
    
    public void marcarEnCamino() {
        this.estado = "En Camino";
    }
    
    public long calcularTiempoEntrega() {
        if (horaReal == null) {
            return -1;
        }
        
        Duration duracion = Duration.between(horaEstimada, horaReal);
        return duracion.toMinutes();
    }
    
    public boolean llegoATiempo() {
        if (horaReal == null) {
            return false;
        }
        return !horaReal.isAfter(horaEstimada);
    }
    
    public boolean tieneRetraso() {
        if (horaReal == null && estado.equals("Pendiente")) {
            return LocalDateTime.now().isAfter(horaEstimada);
        }
        return !llegoATiempo();
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String horaEstimadaStr = horaEstimada.format(formatter);
        
        String estadoIcon = switch (estado) {
            case "Pendiente" -> "⏳";
            case "En Camino" -> "🚚";
            case "Entregado" -> "✅";
            default -> "❓";
        };
        
        return String.format("%s [%s] %s - %s | Est: %s | Repartidor: %s", 
            estadoIcon, pedido.getIdPedido(), distrito, estado, horaEstimadaStr, repartidor);
    }
    
    public String toStringDetallado() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String horaEstimadaStr = horaEstimada.format(formatter);
        String horaRealStr = horaReal != null ? horaReal.format(formatter) : "Pendiente";
        
        String tiempoEntrega = "N/A";
        if (horaReal != null) {
            long minutos = calcularTiempoEntrega();
            tiempoEntrega = minutos + " minutos";
            if (minutos < 0) {
                tiempoEntrega += " (ADELANTADO)";
            } else if (minutos > 0) {
                tiempoEntrega += " (RETRASO)";
            } else {
                tiempoEntrega += " (A TIEMPO)";
            }
        }
        
        return String.format("""
            ╔════════════════════════════════════════════════════════════╗
            ║              DETALLE DE LA ENTREGA                         ║
            ╠════════════════════════════════════════════════════════════╣
            ║ ID Pedido:       %-41s║
            ║ Cliente:         %-41s║
            ║ Dirección:       %-41s║
            ║ Distrito:        %-41s║
            ║ Repartidor:      %-41s║
            ║ Estado:          %-41s║
            ║ Hora Estimada:   %-41s║
            ║ Hora Real:       %-41s║
            ║ Tiempo Entrega:  %-41s║
            ╚════════════════════════════════════════════════════════════╝
            """, 
            pedido.getIdPedido(),
            pedido.getCliente().getNombre(),
            direccion,
            distrito,
            repartidor,
            estado,
            horaEstimadaStr,
            horaRealStr,
            tiempoEntrega);
    }
    
    public String toStringTabla() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String horaEstimadaStr = horaEstimada.format(formatter);
        
        String estadoIcon = switch (estado) {
            case "Pendiente" -> "⏳";
            case "En Camino" -> "🚚";
            case "Entregado" -> "✅";
            default -> "❓";
        };
        
        String retrasoIcon = tieneRetraso() ? "⚠" : " ";
        
        return String.format("%s %-12s %-20s %-15s %-8s %-20s %s", 
            estadoIcon,
            pedido.getIdPedido(),
            pedido.getCliente().getNombre().length() > 20 ? 
                pedido.getCliente().getNombre().substring(0, 17) + "..." : 
                pedido.getCliente().getNombre(),
            distrito.length() > 15 ? distrito.substring(0, 12) + "..." : distrito,
            horaEstimadaStr,
            repartidor.length() > 20 ? repartidor.substring(0, 17) + "..." : repartidor,
            retrasoIcon);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Entrega entrega = (Entrega) obj;
        return pedido.equals(entrega.pedido);
    }
    
    @Override
    public int hashCode() {
        return pedido.hashCode();
    }
}