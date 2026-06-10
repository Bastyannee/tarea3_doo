package logica;

/**
 * Clase abstracta que representa la familia de las bebidas.
 * Hereda de la clase base Producto.
 */
public abstract class Bebida extends Producto {
    /**
     * Constructor que traspasa el número de serie a la superclase.
     * 
     * @param serie El identificador único de la bebida.
     */
    public Bebida(int serie) {
        super(serie);
    }

}
