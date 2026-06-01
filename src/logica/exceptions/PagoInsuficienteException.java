package logica.exceptions;
/**
 * Ocurre cuando el comprador intenta adquirir un producto con un pago inferior a su valor.
 */
public class PagoInsuficienteException extends Exception {
    public PagoInsuficienteException() {
        super("El monto ingresado es menor al valor del producto. Ingrese una moneda de mayor valor.");
    }
}
