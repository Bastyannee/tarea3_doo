package logica;

/**
 * Entidad que simula a un cliente interactuando con la máquina expendedora.
 */

public class Comprador {
    private String producto;
    private int vuelto = 0;

    /**
     * El comprador intenta adquirir un producto desde el expendedor, ingresando una moneda y seleccionando un tipo.
     * Si la compra falla, la excepción correspondiente delegará el error al Main.
     *
     * @param mon La moneda utilizada para el pago.
     * @param tipo El tipo de producto deseado (Enum TipoProducto).
     * @param exp La referencia a la máquina expendedora.
     * @throws PagoIncorrectoException Si la moneda es nula.
     * @throws PagoInsuficienteException Si el valor de la moneda no cubre el precio.
     * @throws NoHayProductoException Si no queda stock en el depósito correspondiente.
     */

    public Comprador(Moneda mon, TipoProducto tipo, Expendedor exp) throws PagoIncorrectoException, PagoInsuficienteException, NoHayProductoException {
        // Si arroja excepción, el flujo se corta aquí y se delega al caller.
        Producto prod = exp.comprarProducto(mon, tipo);

        if (prod != null) {
            this.producto = prod.consumir();
        }
        
        // Solo llegamos a este punto si la compra fue un éxito.
        Moneda m;
        while ((m = exp.getVuelto()) != null) {
            vuelto = vuelto + m.getValor();
        }
    }

    /**
     * Retorna el total de dinero recibido como vuelto tras la transacción.
     *
     * @return El vuelto acumulado en formato entero.
     */
    public int cuantoVuelto() {
        return vuelto;
    }

    /**
     * Retorna el identificador del producto que fue consumido.
     *
     * @return Un string con el nombre del producto, o null si la compra falló.
     */
    public String queConsumiste(){
        return producto;
    }
}
