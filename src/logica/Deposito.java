package logica;

import java.util.ArrayList;

/**
 * Clase genérica que representa un depósito de almacenamiento dentro del Expendedor.
 * Utiliza un parámetro de tipo genérico (T) para ser agnóstica al contenido,
 * permitiendo almacenar tanto instancias de Producto como de Moneda.
 *
 * @param <T> El tipo de objeto que almacenará este depósito.
 */
public class Deposito<T> {
    
    /** Lista interna para almacenar los elementos del depósito. */
    private ArrayList<T> items;

    /**
     * Constructor por defecto.
     * Inicializa la estructura de datos interna (ArrayList) vacía.
     */
    public Deposito() {
        this.items = new ArrayList<>();
    }

    /**
     * Agrega un nuevo elemento al final del depósito.
     *
     * @param item El elemento de tipo T que se desea almacenar.
     */
    public void add(T item) {
        this.items.add(item);
    }

    /**
     * Retorna y remueve un elemento del depósito.
     * Simula la extracción física de un objeto de la máquina.
     *
     * @return El primer elemento de tipo T en el depósito, o null si está vacío.
     */
    public T get() {
        if (this.items.isEmpty()) {
            return null;
        }
        // Se extrae el elemento en el índice 0 simulando una cola (FIFO).
        return this.items.remove(0);
    }
    /**
     * Obtiene la lista interna de elementos almacenados en el depósito.
     */
    public ArrayList<T> getLista() {
        return this.items;
    }
}
