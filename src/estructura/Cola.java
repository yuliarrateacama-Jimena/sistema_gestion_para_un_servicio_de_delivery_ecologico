package estructura;

public class Cola<T> {
    private Nodo<T> frente;
    private Nodo<T> finalCola;
    private int tamanio;
    
    public Cola() {
        this.frente = null;
        this.finalCola = null;
        this.tamanio = 0;
    }
    
    public void encolar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        
        if (estaVacia()) {
            frente = nuevoNodo;
            finalCola = nuevoNodo;
        } else {
            finalCola.setSiguiente(nuevoNodo);
            finalCola = nuevoNodo;
        }
        tamanio++;
    }
    
    public T desencolar() {
        if (estaVacia()) {
            throw new IllegalStateException("Error: No se puede desencolar. La cola está vacía.");
        }
        
        T dato = frente.getDato();
        frente = frente.getSiguiente();
        
        if (frente == null) {
            finalCola = null;
        }
        
        tamanio--;
        return dato;
    }
    
    public T verFrente() {
        if (estaVacia()) {
            throw new IllegalStateException("Error: La cola está vacía.");
        }
        return frente.getDato();
    }
    
    public boolean estaVacia() {
        return frente == null;
    }
    
    public int getTamanio() {
        return tamanio;
    }
    
    public void limpiar() {
        frente = null;
        finalCola = null;
        tamanio = 0;
    }
    
    public boolean contiene(T dato) {
        Nodo<T> actual = frente;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }
    
    public Object[] toArray() {
        Object[] array = new Object[tamanio];
        Nodo<T> actual = frente;
        int i = 0;
        while (actual != null) {
            array[i++] = actual.getDato();
            actual = actual.getSiguiente();
        }
        return array;
    }
}