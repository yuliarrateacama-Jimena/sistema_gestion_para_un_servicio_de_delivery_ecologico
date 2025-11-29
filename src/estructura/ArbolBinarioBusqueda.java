package estructura;

public class ArbolBinarioBusqueda<T extends Comparable<T>> {
    private NodoArbol<T> raiz;
    private int tamanio;
    
    public ArbolBinarioBusqueda() {
        this.raiz = null;
        this.tamanio = 0;
    }
    
    public void insertar(T dato) {
        raiz = insertarRecursivo(raiz, dato);
        tamanio++;
    }
    
    private NodoArbol<T> insertarRecursivo(NodoArbol<T> nodo, T dato) {
        if (nodo == null) {
            return new NodoArbol<>(dato);
        }
        
        int comparacion = dato.compareTo(nodo.getDato());
        
        if (comparacion < 0) {
            nodo.setIzquierdo(insertarRecursivo(nodo.getIzquierdo(), dato));
        } else if (comparacion > 0) {
            nodo.setDerecho(insertarRecursivo(nodo.getDerecho(), dato));
        } else {
            tamanio--;
            return nodo;
        }
        
        actualizarAltura(nodo);
        return balancear(nodo);
    }
    
    public T buscar(T dato) {
        return buscarRecursivo(raiz, dato);
    }
    
    private T buscarRecursivo(NodoArbol<T> nodo, T dato) {
        if (nodo == null) {
            return null;
        }
        
        int comparacion = dato.compareTo(nodo.getDato());
        
        if (comparacion < 0) {
            return buscarRecursivo(nodo.getIzquierdo(), dato);
        } else if (comparacion > 0) {
            return buscarRecursivo(nodo.getDerecho(), dato);
        } else {
            return nodo.getDato();
        }
    }
    
    public boolean eliminar(T dato) {
        if (buscar(dato) == null) {
            return false;
        }
        raiz = eliminarRecursivo(raiz, dato);
        tamanio--;
        return true;
    }
    
    private NodoArbol<T> eliminarRecursivo(NodoArbol<T> nodo, T dato) {
        if (nodo == null) {
            return null;
        }
        
        int comparacion = dato.compareTo(nodo.getDato());
        
        if (comparacion < 0) {
            nodo.setIzquierdo(eliminarRecursivo(nodo.getIzquierdo(), dato));
        } else if (comparacion > 0) {
            nodo.setDerecho(eliminarRecursivo(nodo.getDerecho(), dato));
        } else {
            if (nodo.esHoja()) {
                return null;
            }
            
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();
            }
            if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();
            }
            
            T sucesor = encontrarMinimo(nodo.getDerecho());
            nodo.setDato(sucesor);
            nodo.setDerecho(eliminarRecursivo(nodo.getDerecho(), sucesor));
        }
        
        actualizarAltura(nodo);
        return balancear(nodo);
    }
    
    private T encontrarMinimo(NodoArbol<T> nodo) {
        while (nodo.getIzquierdo() != null) {
            nodo = nodo.getIzquierdo();
        }
        return nodo.getDato();
    }
    
    public ListaEnlazada<T> recorridoInorden() {
        ListaEnlazada<T> lista = new ListaEnlazada<>();
        inordenRecursivo(raiz, lista);
        return lista;
    }
    
    private void inordenRecursivo(NodoArbol<T> nodo, ListaEnlazada<T> lista) {
        if (nodo != null) {
            inordenRecursivo(nodo.getIzquierdo(), lista);
            lista.insertarAlFinal(nodo.getDato());
            inordenRecursivo(nodo.getDerecho(), lista);
        }
    }
    
    public ListaEnlazada<T> recorridoPreorden() {
        ListaEnlazada<T> lista = new ListaEnlazada<>();
        preordenRecursivo(raiz, lista);
        return lista;
    }
    
    private void preordenRecursivo(NodoArbol<T> nodo, ListaEnlazada<T> lista) {
        if (nodo != null) {
            lista.insertarAlFinal(nodo.getDato());
            preordenRecursivo(nodo.getIzquierdo(), lista);
            preordenRecursivo(nodo.getDerecho(), lista);
        }
    }
    
    public ListaEnlazada<T> recorridoPostorden() {
        ListaEnlazada<T> lista = new ListaEnlazada<>();
        postordenRecursivo(raiz, lista);
        return lista;
    }
    
    private void postordenRecursivo(NodoArbol<T> nodo, ListaEnlazada<T> lista) {
        if (nodo != null) {
            postordenRecursivo(nodo.getIzquierdo(), lista);
            postordenRecursivo(nodo.getDerecho(), lista);
            lista.insertarAlFinal(nodo.getDato());
        }
    }
    
    private int obtenerAltura(NodoArbol<T> nodo) {
        return nodo == null ? 0 : nodo.getAltura();
    }
    
    private void actualizarAltura(NodoArbol<T> nodo) {
        int alturaIzq = obtenerAltura(nodo.getIzquierdo());
        int alturaDer = obtenerAltura(nodo.getDerecho());
        nodo.setAltura(Math.max(alturaIzq, alturaDer) + 1);
    }
    
    private int obtenerBalance(NodoArbol<T> nodo) {
        return nodo == null ? 0 : obtenerAltura(nodo.getIzquierdo()) - obtenerAltura(nodo.getDerecho());
    }
    
    private NodoArbol<T> balancear(NodoArbol<T> nodo) {
        int balance = obtenerBalance(nodo);
        
        if (balance > 1 && obtenerBalance(nodo.getIzquierdo()) >= 0) {
            return rotarDerecha(nodo);
        }
        
        if (balance > 1 && obtenerBalance(nodo.getIzquierdo()) < 0) {
            nodo.setIzquierdo(rotarIzquierda(nodo.getIzquierdo()));
            return rotarDerecha(nodo);
        }
        
        if (balance < -1 && obtenerBalance(nodo.getDerecho()) <= 0) {
            return rotarIzquierda(nodo);
        }
        
        if (balance < -1 && obtenerBalance(nodo.getDerecho()) > 0) {
            nodo.setDerecho(rotarDerecha(nodo.getDerecho()));
            return rotarIzquierda(nodo);
        }
        
        return nodo;
    }
    
    private NodoArbol<T> rotarDerecha(NodoArbol<T> y) {
        NodoArbol<T> x = y.getIzquierdo();
        NodoArbol<T> T2 = x.getDerecho();
        
        x.setDerecho(y);
        y.setIzquierdo(T2);
        
        actualizarAltura(y);
        actualizarAltura(x);
        
        return x;
    }
    
    private NodoArbol<T> rotarIzquierda(NodoArbol<T> x) {
        NodoArbol<T> y = x.getDerecho();
        NodoArbol<T> T2 = y.getIzquierdo();
        
        y.setIzquierdo(x);
        x.setDerecho(T2);
        
        actualizarAltura(x);
        actualizarAltura(y);
        
        return y;
    }
    
    public boolean estaVacio() {
        return raiz == null;
    }
    
    public int getTamanio() {
        return tamanio;
    }
    
    public int getAltura() {
        return obtenerAltura(raiz);
    }
    
    public void limpiar() {
        raiz = null;
        tamanio = 0;
    }
}