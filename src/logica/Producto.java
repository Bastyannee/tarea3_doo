package logica;

/**
 * Clase abstracta base que representa cualquier ítem que pueda ser vendido en el Expendedor.
 * Define la estructura fundamental de almacenamiento de serie y consumo.
 */
public abstract class Producto {
    /** Número de serie único para identificar la instancia en memoria. */
    private int serie;

    /**
     * Constructor base para inicializar el producto.
     * 
     * @param serie El identificador único del producto.
     */
    public Producto(int serie) {
        this.serie = serie;
    }

    /**
     * Simula el consumo del producto.
     * 
     * @return Un String que describe el sabor o la acción de consumo.
     */
    public abstract String consumir();

    /**
     * Obtiene el número de serie del producto.
     * 
     * @return El número de serie entero.
     */
    public int getSerie() {
        return serie;
    }
}
