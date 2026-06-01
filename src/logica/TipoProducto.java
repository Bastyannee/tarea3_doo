package logica;


/**
 * Enumeración que define los tipos de productos disponibles en el expendedor.
 * Centraliza la información del catálogo y asocia cada producto con su precio estandarizado.
 */
public enum TipoProducto {
    COCA_COLA(1000),
    SPRITE(1000),
    FANTA(1000),
    SNICKERS(800),
    SUPER8(500);

    /** Valor monetario del producto. */
    private final int precio;

    /**
     * Constructor de la constante del catálogo.
     * 
     * @param precio El precio del producto en formato entero.
     */
    TipoProducto(int precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el precio asociado al tipo de producto.
     * 
     * @return El precio entero del producto.
     */
    public int getPrecio() {
        return precio;
    }
}
