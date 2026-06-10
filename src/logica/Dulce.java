package logica;

/**
 * Clase abstracta que representa la familia de los dulces.
 * Hereda de la clase base Producto.
 */
public abstract class Dulce extends Producto {
    /**
     * Constructor que traspasa el número de serie a la superclase.
     * 
     * @param serie El identificador único del dulce.
     */
    public Dulce(int serie) {
        super(serie);
    }

}
