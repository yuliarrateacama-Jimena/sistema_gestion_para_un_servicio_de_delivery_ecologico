package estructura;

public class Pila<T> {
    private Nodo<T> tope;
    private int tamanio;
    private int capacidadMaxima;
    
    public Pila() {
        this.tope = null;
        this.tamanio = 0;
        this.capacidadMaxima = Integer.MAX_VALUE;
    }
    
    public Pila(int capacidadMaxima) {
        this.tope = null;
        this.tamanio = 0;
        this.capacidadMaxima = capacidadMaxima;
    }
    
    public boolean push(T dato) {
        if (tamanio >= capacidadMaxima) {
            System.out.println("Error: Pila llena. Capacidad máxima alcanzada.");
            return false;
        }
        
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        nuevoNodo.setSiguiente(tope);
        tope = nuevoNodo;
        tamanio++;
        return true;
    }
    
    public T pop() {
        if (estaVacia()) {
            throw new IllegalStateException("Error: No se puede desapilar. La pila está vacía.");
        }
        
        T dato = tope.getDato();
        tope = tope.getSiguiente();
        tamanio--;
        return dato;
    }
    
    public T peek() {
        if (estaVacia()) {
            throw new IllegalStateException("Error: La pila está vacía.");
        }
        return tope.getDato();
    }
    
    public boolean estaVacia() {
        return tope == null;
    }
    
    public boolean estaLlena() {
        return tamanio >= capacidadMaxima;
    }
    
    public int getTamanio() {
        return tamanio;
    }
    
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
    
    public void limpiar() {
        tope = null;
        tamanio = 0;
    }
    
    public int buscar(T dato) {
        Nodo<T> actual = tope;
        int posicion = 0;
        
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return posicion;
            }
            actual = actual.getSiguiente();
            posicion++;
        }
        return -1;
    }
    
    public Object[] toArray() {
        Object[] array = new Object[tamanio];
        Nodo<T> actual = tope;
        int i = 0;
        while (actual != null) {
            array[i++] = actual.getDato();
            actual = actual.getSiguiente();
        }
        return array;
    }
}