package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Operacion {
    private String tipoOperacion;
    private LocalDateTime fecha;
    private String descripcion;
    private Object datosAnteriores;
    private String usuario;
    private String modulo;
    
    public Operacion(String tipoOperacion, String descripcion, Object datosAnteriores, String usuario, String modulo) {
        this.tipoOperacion = tipoOperacion;
        this.fecha = LocalDateTime.now();
        this.descripcion = descripcion;
        this.datosAnteriores = datosAnteriores;
        this.usuario = usuario;
        this.modulo = modulo;
    }
    
    public Operacion(String tipoOperacion, String descripcion, Object datosAnteriores, String modulo) {
        this(tipoOperacion, descripcion, datosAnteriores, "Sistema", modulo);
    }
    
    public Operacion(String tipoOperacion, String descripcion, String modulo) {
        this(tipoOperacion, descripcion, null, "Sistema", modulo);
    }
    
    public String getTipoOperacion() {
        return tipoOperacion;
    }
    
    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Object getDatosAnteriores() {
        return datosAnteriores;
    }
    
    public void setDatosAnteriores(Object datosAnteriores) {
        this.datosAnteriores = datosAnteriores;
    }
    
    public String getUsuario() {
        return usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public String getModulo() {
        return modulo;
    }
    
    public void setModulo(String modulo) {
        this.modulo = modulo;
    }
    
    public boolean esDeshacible() {
        return datosAnteriores != null;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = fecha.format(formatter);
        
        String icono = switch (tipoOperacion) {
            case "AGREGAR" -> "➕";
            case "MODIFICAR" -> "✏️";
            case "ELIMINAR" -> "🗑️";
            case "PROCESAR" -> "⚙️";
            default -> "📝";
        };
        
        return String.format("%s [%s] %s - %s | %s", 
            icono, fechaFormateada, modulo, tipoOperacion, descripcion);
    }
    
    public String toStringDetallado() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = fecha.format(formatter);
        
        String deshacible = esDeshacible() ? "✓ Sí" : "✗ No";
        
        return String.format("""
            ╔════════════════════════════════════════════════════════════╗
            ║              DETALLE DE LA OPERACIÓN                       ║
            ╠════════════════════════════════════════════════════════════╣
            ║ Tipo:            %-41s║
            ║ Módulo:          %-41s║
            ║ Fecha:           %-41s║
            ║ Usuario:         %-41s║
            ║ Descripción:     %-41s║
            ║ Deshacible:      %-41s║
            ╚════════════════════════════════════════════════════════════╝
            """, 
            tipoOperacion, 
            modulo,
            fechaFormateada, 
            usuario, 
            descripcion,
            deshacible);
    }
    
    public String toStringTabla() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
        String fechaFormateada = fecha.format(formatter);
        
        String icono = switch (tipoOperacion) {
            case "AGREGAR" -> "➕";
            case "MODIFICAR" -> "✏️";
            case "ELIMINAR" -> "🗑️";
            case "PROCESAR" -> "⚙️";
            default -> "📝";
        };
        
        return String.format("%s %-12s %-15s %-12s %-35s", 
            icono,
            fechaFormateada,
            modulo.length() > 15 ? modulo.substring(0, 12) + "..." : modulo,
            tipoOperacion.length() > 12 ? tipoOperacion.substring(0, 9) + "..." : tipoOperacion,
            descripcion.length() > 35 ? descripcion.substring(0, 32) + "..." : descripcion);
    }
}