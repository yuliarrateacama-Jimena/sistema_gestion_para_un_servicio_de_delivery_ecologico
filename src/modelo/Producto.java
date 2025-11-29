package modelo;

public class Producto implements Comparable<Producto> {
    private String codigoProducto;
    private String nombre;
    private Categoria categoria;
    private double precio;
    private int stockDisponible;
    private String caracteristicasEcologicas;
    
    public Producto(String codigoProducto, String nombre, Categoria categoria, 
                    double precio, int stockDisponible, String caracteristicasEcologicas) {
        this.codigoProducto = codigoProducto;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stockDisponible = stockDisponible;
        this.caracteristicasEcologicas = caracteristicasEcologicas;
    }
    
    public String getCodigoProducto() {
        return codigoProducto;
    }
    
    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Categoria getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public int getStockDisponible() {
        return stockDisponible;
    }
    
    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }
    
    public String getCaracteristicasEcologicas() {
        return caracteristicasEcologicas;
    }
    
    public void setCaracteristicasEcologicas(String caracteristicasEcologicas) {
        this.caracteristicasEcologicas = caracteristicasEcologicas;
    }
    
    public boolean actualizarStock(int cantidad) {
        if (stockDisponible + cantidad < 0) {
            return false;
        }
        stockDisponible += cantidad;
        return true;
    }
    
    public boolean esStockCritico(int umbral) {
        return stockDisponible < umbral;
    }
    
    public boolean hayStock(int cantidad) {
        return stockDisponible >= cantidad;
    }
    
    @Override
    public int compareTo(Producto otro) {
        return this.codigoProducto.compareTo(otro.codigoProducto);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - S/%.2f (Stock: %d)", 
            codigoProducto, nombre, precio, stockDisponible);
    }
    
    public String toStringDetallado() {
        String stockEstado = stockDisponible < 10 ? "⚠ CRÍTICO" : "✓ Disponible";
        
        return String.format("""
            ╔════════════════════════════════════════════════════╗
            ║           INFORMACIÓN DEL PRODUCTO                 ║
            ╠════════════════════════════════════════════════════╣
            ║ Código:          %-33s║
            ║ Nombre:          %-33s║
            ║ Categoría:       %-33s║
            ║ Precio:          S/ %-30.2f║
            ║ Stock:           %-20d %s║
            ║ Características: %-33s║
            ╚════════════════════════════════════════════════════╝
            """, 
            codigoProducto, 
            nombre, 
            categoria.getNombre(), 
            precio, 
            stockDisponible,
            stockEstado,
            caracteristicasEcologicas);
    }
    
    public String toStringTabla() {
        String stockEstado = stockDisponible < 10 ? "⚠" : " ";
        return String.format("%-10s %-25s %-15s %8.2f %8d %s", 
            codigoProducto, 
            nombre.length() > 25 ? nombre.substring(0, 22) + "..." : nombre,
            categoria.getNombre().length() > 15 ? categoria.getNombre().substring(0, 12) + "..." : categoria.getNombre(),
            precio, 
            stockDisponible,
            stockEstado);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Producto producto = (Producto) obj;
        return codigoProducto.equals(producto.codigoProducto);
    }
    
    @Override
    public int hashCode() {
        return codigoProducto.hashCode();
    }
}