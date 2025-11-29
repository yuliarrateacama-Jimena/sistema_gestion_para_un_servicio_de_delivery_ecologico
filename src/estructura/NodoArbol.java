package estructura;

public class NodoArbol<T extends Comparable<T>> {
    private T dato;
    private NodoArbol<T> izquierdo;
    private NodoArbol<T> derecho;
    private int altura;
    
    public NodoArbol(T dato) {
        this.dato = dato;
        this.izquierdo = null;
        this.derecho = null;
        this.altura = 1;
    }
    
    public T getDato() {
        return dato;
    }
    
    public void setDato(T dato) {
        this.dato = dato;
    }
    
    public NodoArbol<T> getIzquierdo() {
        return izquierdo;
    }
    
    public void setIzquierdo(NodoArbol<T> izquierdo) {
        this.izquierdo = izquierdo;
    }
    
    public NodoArbol<T> getDerecho() {
        return derecho;
    }
    
    public void setDerecho(NodoArbol<T> derecho) {
        this.derecho = derecho;
    }
    
    public int getAltura() {
        return altura;
    }
    
    public void setAltura(int altura) {
        this.altura = altura;
    }
    
    public boolean esHoja() {
        return izquierdo == null && derecho == null;
    }
    
    public boolean tieneAmbosHijos() {
        return izquierdo != null && derecho != null;
    }
    
    @Override
    public String toString() {
        return dato.toString();
    }
}