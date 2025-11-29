package modelo;

import estructura.ListaEnlazada;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pedido implements Comparable<Pedido> {
    private String idPedido;
    private Cliente cliente;
    private ListaEnlazada<Producto> listaProductos;
    private String tipoEntrega;
    private String direccion;
    private String estado;
    private int prioridad;
    private LocalDateTime fechaPedido;
    private double total;
    
    public Pedido(String idPedido, Cliente cliente, String tipoEntrega, String direccion) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.listaProductos = new ListaEnlazada<>();
        this.tipoEntrega = tipoEntrega;
        this.direccion = direccion;
        this.estado = "Pendiente";
        this.fechaPedido = LocalDateTime.now();
        this.prioridad = calcularPrioridad();
        this.total = 0.0;
    }
    
    private int calcularPrioridad() {
        boolean esPremium = cliente.esPremium();
        boolean esExpress = tipoEntrega.equalsIgnoreCase("Express");
        
        if (esPremium && esExpress) return 10;
        if (esPremium && !esExpress) return 8;
        if (!esPremium && esExpress) return 7;
        return 5;
    }
    
    public void agregarProducto(Producto producto) {
        listaProductos.insertarAlFinal(producto);
        recalcularTotal();
    }
    
    public boolean eliminarProducto(Producto producto) {
        boolean eliminado = listaProductos.eliminar(producto);
        if (eliminado) {
            recalcularTotal();
        }
        return eliminado;
    }
    
    public void recalcularTotal() {
        total = 0.0;
        
        for (Object obj : listaProductos.toArray()) {
            Producto producto = (Producto) obj;
            total += producto.getPrecio();
        }
        
        double descuento = cliente.obtenerDescuento();
        total = total * (1 - descuento);
    }
    
    public String getIdPedido() {
        return idPedido;
    }
    
    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.prioridad = calcularPrioridad();
    }
    
    public ListaEnlazada<Producto> getListaProductos() {
        return listaProductos;
    }
    
    public String getTipoEntrega() {
        return tipoEntrega;
    }
    
    public void setTipoEntrega(String tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
        this.prioridad = calcularPrioridad();
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public int getPrioridad() {
        return prioridad;
    }
    
    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }
    
    public double getTotal() {
        return total;
    }
    
    public int getCantidadProductos() {
        return listaProductos.getTamanio();
    }
    
    @Override
    public int compareTo(Pedido otro) {
        return Integer.compare(this.prioridad, otro.prioridad);
    }
    
    @Override
    public String toString() {
        String estadoIcon = switch (estado) {
            case "Pendiente" -> "⏳";
            case "En Proceso" -> "🔄";
            case "Entregado" -> "✅";
            default -> "❓";
        };
        
        return String.format("%s [%s] %s - %s | %d productos | S/%.2f | Prioridad: %d", 
            estadoIcon, idPedido, cliente.getNombre(), estado, 
            listaProductos.getTamanio(), total, prioridad);
    }
    
    public String toStringDetallado() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaFormateada = fechaPedido.format(formatter);
        
        StringBuilder sb = new StringBuilder();
        sb.append("╔════════════════════════════════════════════════════════════╗\n");
        sb.append("║              DETALLE DEL PEDIDO                            ║\n");
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ ID Pedido:       %-41s║\n", idPedido));
        sb.append(String.format("║ Cliente:         %-41s║\n", cliente.toString()));
        sb.append(String.format("║ Tipo Entrega:    %-41s║\n", tipoEntrega));
        sb.append(String.format("║ Dirección:       %-41s║\n", direccion));
        sb.append(String.format("║ Estado:          %-41s║\n", estado));
        sb.append(String.format("║ Prioridad:       %-41d║\n", prioridad));
        sb.append(String.format("║ Fecha:           %-41s║\n", fechaFormateada));
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        sb.append("║ PRODUCTOS:                                                 ║\n");
        
        if (listaProductos.estaVacia()) {
            sb.append("║   (Sin productos)                                          ║\n");
        } else {
            int contador = 1;
            for (Object obj : listaProductos.toArray()) {
                Producto p = (Producto) obj;
                String linea = String.format("║ %d. %-50s S/%6.2f ║", 
                    contador++, 
                    p.getNombre().length() > 50 ? p.getNombre().substring(0, 47) + "..." : p.getNombre(), 
                    p.getPrecio());
                sb.append(linea).append("\n");
            }
        }
        
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        
        if (cliente.esPremium()) {
            double subtotal = total / (1 - cliente.obtenerDescuento());
            sb.append(String.format("║ Subtotal:        %42.2f║\n", subtotal));
            sb.append(String.format("║ Descuento (10%%): %42.2f║\n", subtotal - total));
        }
        
        sb.append(String.format("║ TOTAL:           %42.2f║\n", total));
        sb.append("╚════════════════════════════════════════════════════════════╝");
        
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pedido pedido = (Pedido) obj;
        return idPedido.equals(pedido.idPedido);
    }
    
    @Override
    public int hashCode() {
        return idPedido.hashCode();
    }
}