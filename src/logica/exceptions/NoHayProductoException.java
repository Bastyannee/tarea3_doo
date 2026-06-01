package logica.exceptions;

/**
 * Ocurre cuando el comprador intenta adquirir un producto que no tiene stock disponible en el expendedor.
 */
public class NoHayProductoException extends Exception {
    public NoHayProductoException() {
        super("No queda stock del producto solicitado.");
    }
}
