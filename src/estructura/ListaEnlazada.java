package estructura;

public class ListaEnlazada<T> {
    private Nodo<T> cabeza;
    private int tamanio;
    
    public ListaEnlazada() {
        this.cabeza = null;
        this.tamanio = 0;
    }
    
    public void insertarAlInicio(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        nuevoNodo.setSiguiente(cabeza);
        cabeza = nuevoNodo;
        tamanio++;
    }
    
    public void insertarAlFinal(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
        tamanio++;
    }
    
    public T buscar(T dato) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }
    
    public boolean eliminar(T dato) {
        if (cabeza == null) {
            return false;
        }
        
        if (cabeza.getDato().equals(dato)) {
            cabeza = cabeza.getSiguiente();
            tamanio--;
            return true;
        }
        
        Nodo<T> actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().equals(dato)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tamanio--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }
    
    public T obtenerEnPosicion(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }
    
    public boolean estaVacia() {
        return cabeza == null;
    }
    
    public int getTamanio() {
        return tamanio;
    }
    
    public void limpiar() {
        cabeza = null;
        tamanio = 0;
    }
    
    public void mostrar() {
        if (cabeza == null) {
            System.out.println("Lista vacía");
            return;
        }
        
        Nodo<T> actual = cabeza;
        System.out.print("[");
        while (actual != null) {
            System.out.print(actual.getDato());
            if (actual.getSiguiente() != null) {
                System.out.print(", ");
            }
            actual = actual.getSiguiente();
        }
        System.out.println("]");
    }
    
    public Object[] toArray() {
        Object[] array = new Object[tamanio];
        Nodo<T> actual = cabeza;
        int i = 0;
        while (actual != null) {
            array[i++] = actual.getDato();
            actual = actual.getSiguiente();
        }
        return array;
    }
    
    @Override
    public String toString() {
        if (cabeza == null) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        Nodo<T> actual = cabeza;
        while (actual != null) {
            sb.append(actual.getDato());
            if (actual.getSiguiente() != null) {
                sb.append(", ");
            }
            actual = actual.getSiguiente();
        }
        sb.append("]");
        return sb.toString();
    }
}