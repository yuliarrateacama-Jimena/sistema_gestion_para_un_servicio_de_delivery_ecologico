package modelo;

public class Categoria {
    private int idCategoria;
    private String nombre;
    private String descripcion;
    private String caracteristicas;
    
    public Categoria(int idCategoria, String nombre, String descripcion, String caracteristicas) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.caracteristicas = caracteristicas;
    }
    
    public Categoria(String nombre, String descripcion, String caracteristicas) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.caracteristicas = caracteristicas;
    }
    
    public int getIdCategoria() {
        return idCategoria;
    }
    
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getCaracteristicas() {
        return caracteristicas;
    }
    
    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d | %s - %s", idCategoria, nombre, descripcion);
    }
    
    public String toStringDetallado() {
        return String.format("""
            ╔════════════════════════════════════════╗
            ║        INFORMACIÓN DE CATEGORÍA        ║
            ╠════════════════════════════════════════╣
            ║ ID:              %-21d║
            ║ Nombre:          %-21s║
            ║ Descripción:     %-21s║
            ║ Características: %-21s║
            ╚════════════════════════════════════════╝
            """, idCategoria, nombre, descripcion, caracteristicas);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Categoria categoria = (Categoria) obj;
        return idCategoria == categoria.idCategoria;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idCategoria);
    }
}