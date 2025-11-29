package servicio;

import estructura.ListaEnlazada;
import modelo.Categoria;
import modelo.Producto;

public class GestorCategorias {
    private ListaEnlazada<Categoria> listaCategorias;
    private int contadorCategorias;
    
    public GestorCategorias() {
        this.listaCategorias = new ListaEnlazada<>();
        this.contadorCategorias = 1;
        inicializarCategoriasDefault();
    }
    
    private void inicializarCategoriasDefault() {
        crearCategoria("Recipientes", "Recipientes reutilizables para alimentos y bebidas", 
                      "Libres de BPA, aptos para microondas");
        crearCategoria("Bolsas Reutilizables", "Bolsas ecológicas para compras", 
                      "Material reciclado, lavables");
        crearCategoria("Utensilios", "Cubiertos y utensilios biodegradables", 
                      "Bambú, madera sostenible");
        crearCategoria("Contenedores", "Contenedores de almacenamiento ecológicos", 
                      "Vidrio, acero inoxidable");
        crearCategoria("Productos de Limpieza Eco", "Productos de limpieza biodegradables", 
                      "Sin químicos tóxicos, veganos");
        crearCategoria("Textiles Sostenibles", "Productos textiles ecológicos", 
                      "Algodón orgánico, fibras recicladas");
    }
    
    public Categoria crearCategoria(String nombre, String descripcion, String caracteristicas) {
        if (buscarCategoriaPorNombre(nombre) != null) {
            System.out.println("❌ Error: Ya existe una categoría con el nombre '" + nombre + "'");
            return null;
        }
        
        Categoria nuevaCategoria = new Categoria(contadorCategorias++, nombre, descripcion, caracteristicas);
        listaCategorias.insertarAlFinal(nuevaCategoria);
        
        System.out.println("✅ Categoría creada: " + nombre);
        return nuevaCategoria;
    }
    
    public void listarCategorias() {
        if (listaCategorias.estaVacia()) {
            System.out.println("\n📑 No hay categorías registradas.\n");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                  LISTA DE CATEGORÍAS                                       ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        for (Object obj : listaCategorias.toArray()) {
            Categoria c = (Categoria) obj;
            System.out.println("║ " + c.toString());
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total de categorías: " + listaCategorias.getTamanio());
        System.out.println();
    }
    
    public Categoria buscarCategoriaPorNombre(String nombre) {
        for (Object obj : listaCategorias.toArray()) {
            Categoria c = (Categoria) obj;
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                return c;
            }
        }
        return null;
    }
    
    public Categoria buscarCategoriaPorID(int id) {
        for (Object obj : listaCategorias.toArray()) {
            Categoria c = (Categoria) obj;
            if (c.getIdCategoria() == id) {
                return c;
            }
        }
        return null;
    }
    
    public boolean modificarCategoria(int id, String nuevoNombre, String nuevaDescripcion, String nuevasCaracteristicas) {
        Categoria categoria = buscarCategoriaPorID(id);
        
        if (categoria == null) {
            System.out.println("❌ Error: Categoría no encontrada.");
            return false;
        }
        
        if (!categoria.getNombre().equals(nuevoNombre)) {
            if (buscarCategoriaPorNombre(nuevoNombre) != null) {
                System.out.println("❌ Error: Ya existe otra categoría con el nombre '" + nuevoNombre + "'");
                return false;
            }
        }
        
        categoria.setNombre(nuevoNombre);
        categoria.setDescripcion(nuevaDescripcion);
        categoria.setCaracteristicas(nuevasCaracteristicas);
        
        System.out.println("✅ Categoría modificada exitosamente.");
        return true;
    }
    
    public boolean eliminarCategoria(int id, GestorInventario gestorInventario) {
        Categoria categoria = buscarCategoriaPorID(id);
        
        if (categoria == null) {
            System.out.println("❌ Error: Categoría no encontrada.");
            return false;
        }
        
        ListaEnlazada<Producto> productosCategoria = gestorInventario.buscarPorCategoria(categoria);
        
        if (!productosCategoria.estaVacia()) {
            System.out.println("❌ Error: No se puede eliminar la categoría porque tiene " + 
                             productosCategoria.getTamanio() + " productos asociados.");
            System.out.println("   Elimine o reasigne los productos primero.");
            return false;
        }
        
        boolean eliminada = listaCategorias.eliminar(categoria);
        
        if (eliminada) {
            System.out.println("✅ Categoría eliminada: " + categoria.getNombre());
        }
        
        return eliminada;
    }
    
    public void listarProductosPorCategoria(Categoria categoria, GestorInventario gestorInventario) {
        ListaEnlazada<Producto> productos = gestorInventario.buscarPorCategoria(categoria);
        
        if (productos.estaVacia()) {
            System.out.println("\n📦 No hay productos en la categoría: " + categoria.getNombre() + "\n");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     PRODUCTOS DE: " + categoria.getNombre() + "                                         ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        for (Object obj : productos.toArray()) {
            Producto p = (Producto) obj;
            System.out.println("║ " + p.toString());
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total de productos: " + productos.getTamanio());
        System.out.println();
    }
    
    public void mostrarEstadisticasProductosPorCategoria(GestorInventario gestorInventario) {
        if (listaCategorias.estaVacia()) {
            System.out.println("\n📊 No hay categorías registradas.\n");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║          PRODUCTOS POR CATEGORÍA                           ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        
        for (Object obj : listaCategorias.toArray()) {
            Categoria c = (Categoria) obj;
            ListaEnlazada<Producto> productos = gestorInventario.buscarPorCategoria(c);
            int cantidad = productos.getTamanio();
            
            System.out.println(String.format("║ %-40s │ %5d productos ║", 
                c.getNombre(), cantidad));
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
    
    public Categoria obtenerCategoriaConMasProductos(GestorInventario gestorInventario) {
        if (listaCategorias.estaVacia()) {
            return null;
        }
        
        Categoria categoriaMax = null;
        int maxProductos = 0;
        
        for (Object obj : listaCategorias.toArray()) {
            Categoria c = (Categoria) obj;
            ListaEnlazada<Producto> productos = gestorInventario.buscarPorCategoria(c);
            int cantidad = productos.getTamanio();
            
            if (cantidad > maxProductos) {
                maxProductos = cantidad;
                categoriaMax = c;
            }
        }
        
        return categoriaMax;
    }
    
    public Categoria obtenerCategoriaConMenorStock(GestorInventario gestorInventario) {
        if (listaCategorias.estaVacia()) {
            return null;
        }
        
        Categoria categoriaMin = null;
        int minStock = Integer.MAX_VALUE;
        
        for (Object obj : listaCategorias.toArray()) {
            Categoria c = (Categoria) obj;
            ListaEnlazada<Producto> productos = gestorInventario.buscarPorCategoria(c);
            
            int stockTotal = 0;
            for (Object objProd : productos.toArray()) {
                Producto p = (Producto) objProd;
                stockTotal += p.getStockDisponible();
            }
            
            if (stockTotal < minStock) {
                minStock = stockTotal;
                categoriaMin = c;
            }
        }
        
        return categoriaMin;
    }
    
    public int getCantidadCategorias() {
        return listaCategorias.getTamanio();
    }
    
    public boolean hayCategorias() {
        return !listaCategorias.estaVacia();
    }
    
    public ListaEnlazada<Categoria> obtenerTodasLasCategorias() {
        return listaCategorias;
    }
    
    public void mostrarMenuCategorias() {
        if (listaCategorias.estaVacia()) {
            System.out.println("No hay categorías disponibles.");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                  CATEGORÍAS DISPONIBLES                    ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        
        int contador = 1;
        for (Object obj : listaCategorias.toArray()) {
            Categoria c = (Categoria) obj;
            System.out.println(String.format("║ %2d. %-53s║", contador++, c.getNombre()));
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}
                    