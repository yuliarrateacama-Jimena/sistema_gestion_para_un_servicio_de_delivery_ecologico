package servicio;

import estructura.ArbolBinarioBusqueda;
import estructura.ListaEnlazada;
import modelo.Producto;
import modelo.Categoria;

public class GestorInventario {
    private ArbolBinarioBusqueda<Producto> arbolProductos;
    
    public GestorInventario() {
        this.arbolProductos = new ArbolBinarioBusqueda<>();
    }
    
    public boolean agregarProducto(Producto producto) {
        if (buscarProducto(producto.getCodigoProducto()) != null) {
            System.out.println("❌ Error: El producto con código " + producto.getCodigoProducto() + " ya existe.");
            return false;
        }
        
        arbolProductos.insertar(producto);
        System.out.println("✅ Producto agregado exitosamente: " + producto.getNombre());
        return true;
    }
    
    public Producto buscarProducto(String codigo) {
        Producto temp = new Producto(codigo, "", null, 0, 0, "");
        return arbolProductos.buscar(temp);
    }
    
    public boolean actualizarStock(String codigo, int cantidad) {
        Producto producto = buscarProducto(codigo);
        
        if (producto == null) {
            System.out.println("❌ Error: Producto no encontrado.");
            return false;
        }
        
        int stockAnterior = producto.getStockDisponible();
        
        if (!producto.actualizarStock(cantidad)) {
            System.out.println("❌ Error: Stock insuficiente. Stock actual: " + stockAnterior);
            return false;
        }
        
        System.out.println("✅ Stock actualizado: " + stockAnterior + " → " + producto.getStockDisponible());
        return true;
    }
    
    public boolean eliminarProducto(String codigo) {
        Producto producto = buscarProducto(codigo);
        
        if (producto == null) {
            System.out.println("❌ Error: Producto no encontrado.");
            return false;
        }
        
        Producto temp = new Producto(codigo, "", null, 0, 0, "");
        boolean eliminado = arbolProductos.eliminar(temp);
        
        if (eliminado) {
            System.out.println("✅ Producto eliminado: " + producto.getNombre());
        }
        
        return eliminado;
    }
    
    public ListaEnlazada<Producto> listarProductosOrdenados() {
        return arbolProductos.recorridoInorden();
    }
    
    public ListaEnlazada<Producto> listarProductosPorStock() {
        ListaEnlazada<Producto> productos = arbolProductos.recorridoInorden();
        
        Object[] array = productos.toArray();
        int n = array.length;
        
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Producto p1 = (Producto) array[j];
                Producto p2 = (Producto) array[j + 1];
                
                if (p1.getStockDisponible() > p2.getStockDisponible()) {
                    Object temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        
        ListaEnlazada<Producto> listaOrdenada = new ListaEnlazada<>();
        for (Object obj : array) {
            listaOrdenada.insertarAlFinal((Producto) obj);
        }
        
        return listaOrdenada;
    }
    
    public ListaEnlazada<Producto> listarProductosPorPrecio() {
        ListaEnlazada<Producto> productos = arbolProductos.recorridoInorden();
        
        Object[] array = productos.toArray();
        int n = array.length;
        
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Producto p1 = (Producto) array[j];
                Producto p2 = (Producto) array[j + 1];
                
                if (p1.getPrecio() > p2.getPrecio()) {
                    Object temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        
        ListaEnlazada<Producto> listaOrdenada = new ListaEnlazada<>();
        for (Object obj : array) {
            listaOrdenada.insertarAlFinal((Producto) obj);
        }
        
        return listaOrdenada;
    }
    
    public ListaEnlazada<Producto> buscarPorRangoPrecio(double precioMin, double precioMax) {
        ListaEnlazada<Producto> productos = arbolProductos.recorridoInorden();
        ListaEnlazada<Producto> resultado = new ListaEnlazada<>();
        
        for (Object obj : productos.toArray()) {
            Producto p = (Producto) obj;
            if (p.getPrecio() >= precioMin && p.getPrecio() <= precioMax) {
                resultado.insertarAlFinal(p);
            }
        }
        
        return resultado;
    }
    
    public ListaEnlazada<Producto> obtenerProductosStockCritico(int umbral) {
        ListaEnlazada<Producto> productos = arbolProductos.recorridoInorden();
        ListaEnlazada<Producto> criticos = new ListaEnlazada<>();
        
        for (Object obj : productos.toArray()) {
            Producto p = (Producto) obj;
            if (p.esStockCritico(umbral)) {
                criticos.insertarAlFinal(p);
            }
        }
        
        return criticos;
    }
    
    public int getCantidadProductos() {
        return arbolProductos.getTamanio();
    }
    
    public boolean hayProductos() {
        return !arbolProductos.estaVacio();
    }
    
    public ListaEnlazada<Producto> buscarPorCategoria(Categoria categoria) {
        ListaEnlazada<Producto> productos = arbolProductos.recorridoInorden();
        ListaEnlazada<Producto> resultado = new ListaEnlazada<>();
        
        for (Object obj : productos.toArray()) {
            Producto p = (Producto) obj;
            if (p.getCategoria().equals(categoria)) {
                resultado.insertarAlFinal(p);
            }
        }
        
        return resultado;
    }
    
    public ListaEnlazada<Producto> buscarPorNombre(String nombre) {
        ListaEnlazada<Producto> productos = arbolProductos.recorridoInorden();
        ListaEnlazada<Producto> resultado = new ListaEnlazada<>();
        
        String nombreBusqueda = nombre.toLowerCase();
        
        for (Object obj : productos.toArray()) {
            Producto p = (Producto) obj;
            if (p.getNombre().toLowerCase().contains(nombreBusqueda)) {
                resultado.insertarAlFinal(p);
            }
        }
        
        return resultado;
    }
    
    public double calcularValorTotalInventario() {
        ListaEnlazada<Producto> productos = arbolProductos.recorridoInorden();
        double total = 0.0;
        
        for (Object obj : productos.toArray()) {
            Producto p = (Producto) obj;
            total += p.getPrecio() * p.getStockDisponible();
        }
        
        return total;
    }
    
    public Producto obtenerProductoMasCaro() {
        ListaEnlazada<Producto> productos = arbolProductos.recorridoInorden();
        
        if (productos.estaVacia()) {
            return null;
        }
        
        Producto masCaro = (Producto) productos.obtenerEnPosicion(0);
        
        for (Object obj : productos.toArray()) {
            Producto p = (Producto) obj;
            if (p.getPrecio() > masCaro.getPrecio()) {
                masCaro = p;
            }
        }
        
        return masCaro;
    }
    
    public Producto obtenerProductoMasBarato() {
        ListaEnlazada<Producto> productos = arbolProductos.recorridoInorden();
        
        if (productos.estaVacia()) {
            return null;
        }
        
        Producto masBarato = (Producto) productos.obtenerEnPosicion(0);
        
        for (Object obj : productos.toArray()) {
            Producto p = (Producto) obj;
            if (p.getPrecio() < masBarato.getPrecio()) {
                masBarato = p;
            }
        }
        
        return masBarato;
    }
    
    public void mostrarInventario() {
        if (arbolProductos.estaVacio()) {
            System.out.println("\n📦 El inventario está vacío.\n");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                  INVENTARIO DE PRODUCTOS                                  ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║  Código    │ Nombre                    │ Categoría       │  Precio │   Stock │ Estado      ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        ListaEnlazada<Producto> productos = arbolProductos.recorridoInorden();
        
        for (Object obj : productos.toArray()) {
            Producto p = (Producto) obj;
            System.out.println("║ " + p.toStringTabla() + " ║");
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total de productos: " + arbolProductos.getTamanio());
        System.out.println("Valor total del inventario: S/ " + String.format("%.2f", calcularValorTotalInventario()));
        System.out.println();
    }
}