package modelo;

public class Cliente {
    private String nombre;
    private String tipoCliente;
    private String telefono;
    private String email;
    private String direccion;
    
    public Cliente(String nombre, String tipoCliente, String telefono, String email, String direccion) {
        this.nombre = nombre;
        this.tipoCliente = tipoCliente;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }
    
    public Cliente(String nombre, String tipoCliente) {
        this.nombre = nombre;
        this.tipoCliente = tipoCliente;
        this.telefono = "";
        this.email = "";
        this.direccion = "";
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getTipoCliente() {
        return tipoCliente;
    }
    
    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public boolean esPremium() {
        return tipoCliente.equalsIgnoreCase("Premium");
    }
    
    public double obtenerDescuento() {
        return esPremium() ? 0.10 : 0.0;
    }
    
    @Override
    public String toString() {
        String badge = esPremium() ? "⭐" : "👤";
        return String.format("%s %s (%s)", badge, nombre, tipoCliente);
    }
    
    public String toStringDetallado() {
        String badge = esPremium() ? "⭐ PREMIUM" : "👤 REGULAR";
        
        return String.format("""
            ╔════════════════════════════════════════════════════╗
            ║           INFORMACIÓN DEL CLIENTE                  ║
            ╠════════════════════════════════════════════════════╣
            ║ Nombre:          %-33s║
            ║ Tipo:            %-33s║
            ║ Teléfono:        %-33s║
            ║ Email:           %-33s║
            ║ Dirección:       %-33s║
            ║ Descuento:       %-32.0f%%║
            ╚════════════════════════════════════════════════════╝
            """, 
            nombre, 
            badge,
            telefono.isEmpty() ? "No registrado" : telefono,
            email.isEmpty() ? "No registrado" : email,
            direccion.isEmpty() ? "No registrada" : direccion,
            obtenerDescuento() * 100);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cliente cliente = (Cliente) obj;
        return nombre.equals(cliente.nombre);
    }
    
    @Override
    public int hashCode() {
        return nombre.hashCode();
    }
}