package logica.exceptions;
/**
* Ocurre cuando el comprador intenta adquirir un producto sin haber ingresado una moneda válida.
 */
public class PagoIncorrectoException extends Exception {
    public PagoIncorrectoException() {
        super("Pago incorrecto. No se ha ingresado una moneda válida para la compra.");
    }
}
