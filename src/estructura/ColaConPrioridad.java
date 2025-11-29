package estructura;

public class ColaConPrioridad<T extends Comparable<T>> {
    private static class NodoPrioridad<T> implements Comparable<NodoPrioridad<T>> {
        T dato;
        int prioridad;
        
        public NodoPrioridad(T dato, int prioridad) {
            this.dato = dato;
            this.prioridad = prioridad;
        }
        
        @Override
        public int compareTo(NodoPrioridad<T> otro) {
            return Integer.compare(this.prioridad, otro.prioridad);
        }
        
        @Override
        public String toString() {
            return dato + " (Prioridad: " + prioridad + ")";
        }
    }
    
    private NodoPrioridad<T>[] heap;
    private int tamanio;
    private static final int CAPACIDAD_INICIAL = 10;
    
    @SuppressWarnings("unchecked")
    public ColaConPrioridad() {
        heap = new NodoPrioridad[CAPACIDAD_INICIAL];
        tamanio = 0;
    }
    
    public void insertar(T dato, int prioridad) {
        if (tamanio == heap.length) {
            expandirCapacidad();
        }
        
        NodoPrioridad<T> nuevo = new NodoPrioridad<>(dato, prioridad);
        heap[tamanio] = nuevo;
        heapifyUp(tamanio);
        tamanio++;
    }
    
    public T extraerMaximo() {
        if (estaVacia()) {
            throw new IllegalStateException("Error: La cola está vacía.");
        }
        
        T dato = heap[0].dato;
        heap[0] = heap[tamanio - 1];
        heap[tamanio - 1] = null;
        tamanio--;
        
        if (tamanio > 0) {
            heapifyDown(0);
        }
        
        return dato;
    }
    
    public T verMaximo() {
        if (estaVacia()) {
            throw new IllegalStateException("Error: La cola está vacía.");
        }
        return heap[0].dato;
    }
    
    public int verPrioridadMaxima() {
        if (estaVacia()) {
            throw new IllegalStateException("Error: La cola está vacía.");
        }
        return heap[0].prioridad;
    }
    
    private void heapifyUp(int indice) {
        int padre = (indice - 1) / 2;
        
        while (indice > 0 && heap[indice].compareTo(heap[padre]) > 0) {
            intercambiar(indice, padre);
            indice = padre;
            padre = (indice - 1) / 2;
        }
    }
    
    private void heapifyDown(int indice) {
        int mayor = indice;
        int hijoIzq = 2 * indice + 1;
        int hijoDer = 2 * indice + 2;
        
        if (hijoIzq < tamanio && heap[hijoIzq].compareTo(heap[mayor]) > 0) {
            mayor = hijoIzq;
        }
        
        if (hijoDer < tamanio && heap[hijoDer].compareTo(heap[mayor]) > 0) {
            mayor = hijoDer;
        }
        
        if (mayor != indice) {
            intercambiar(indice, mayor);
            heapifyDown(mayor);
        }
    }
    
    private void intercambiar(int i, int j) {
        NodoPrioridad<T> temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    
    @SuppressWarnings("unchecked")
    private void expandirCapacidad() {
        int nuevaCapacidad = heap.length * 2;
        NodoPrioridad<T>[] nuevoHeap = new NodoPrioridad[nuevaCapacidad];
        System.arraycopy(heap, 0, nuevoHeap, 0, tamanio);
        heap = nuevoHeap;
    }
    
    public boolean estaVacia() {
        return tamanio == 0;
    }
    
    public int getTamanio() {
        return tamanio;
    }
    
    @SuppressWarnings("unchecked")
    public void limpiar() {
        heap = new NodoPrioridad[CAPACIDAD_INICIAL];
        tamanio = 0;
    }
    
    @SuppressWarnings("unchecked")
    private ColaConPrioridad<T> copiar() {
        ColaConPrioridad<T> copia = new ColaConPrioridad<>();
        copia.heap = new NodoPrioridad[heap.length];
        System.arraycopy(heap, 0, copia.heap, 0, tamanio);
        copia.tamanio = tamanio;
        return copia;
    }
    
    public Object[] toArray() {
        ColaConPrioridad<T> copia = copiar();
        Object[] array = new Object[tamanio];
        int i = 0;
        
        while (!copia.estaVacia()) {
            array[i++] = copia.extraerMaximo();
        }
        
        return array;
    }
}