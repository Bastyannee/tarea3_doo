package logica;/**
 * Representa la máquina expendedora que gestiona el inventario de productos
 * y las transacciones de venta con monedas.
 */
public class Expendedor {
    /** Depósito interno para almacenar latas de CocaCola. */
    private Deposito<Producto> coca;
    /** Depósito interno para almacenar latas de Sprite. */
    private Deposito<Producto> sprite;
    /** Depósito interno para almacenar latas de Fanta. */
    private Deposito<Producto> fanta;
    /** Depósito interno para almacenar barras de Snickers. */
    private Deposito<Producto> snickers;
    /** Depósito interno para almacenar barras de Super8. */
    private Deposito<Producto> super8;
    /** Depósito interno para almacenar las monedas destinadas al vuelto. */
    private Deposito<Moneda> depVuelto;
    /** Depósito interno para almacenar las monedas ingresadas. */
    private Deposito<Moneda> depIngresos;


    /**
     * Constructor del Expendedor.
     * Inicializa los depósitos y los llena "mágicamente" con la cantidad de productos especificada.
     *
     * @param numProductos La cantidad inicial de stock para CADA TIPO de producto.
     */
    public Expendedor(int numProductos) {
        coca = new Deposito<>();
        sprite = new Deposito<>();
        fanta = new Deposito<>();
        snickers = new Deposito<>();
        super8 = new Deposito<>();
        depVuelto = new Deposito<>();
        depIngresos = new Deposito<>();

        for (int i = 0; i < numProductos; i++) {
            coca.add(new CocaCola(100 + i));
            sprite.add(new Sprite(200 + i));
            fanta.add(new Fanta(300 + i));
            snickers.add(new Snickers(400 + i));
            super8.add(new Super8(500 + i));
        }
    }

    /**
     * Procesa la compra de un producto validando la moneda y el stock disponible.
     *
     * @param m La moneda utilizada como medio de pago.
     * @param tipo El tipo de producto solicitado (Enum).
     * @return El Producto extraído si la compra es exitosa.
     * @throws PagoIncorrectoException Si la moneda ingresada es nula.
     * @throws PagoInsuficienteException Si el valor de la moneda es menor al precio del producto.
     * @throws NoHayProductoException Si el depósito del producto solicitado está vacío.
     */
    public Producto comprarProducto(Moneda m, TipoProducto tipo) throws PagoIncorrectoException, PagoInsuficienteException, NoHayProductoException {
        if (m == null) {
            throw new PagoIncorrectoException();
        }

        int precio = tipo.getPrecio();

        if (m.getValor() < precio) {
            depVuelto.add(m);
            throw new PagoInsuficienteException();
        }

        Producto p = null;
        switch (tipo) {
            case COCA_COLA: p = coca.get(); break;
            case SPRITE:    p = sprite.get(); break;
            case FANTA:     p = fanta.get(); break;
            case SNICKERS:  p = snickers.get(); break;
            case SUPER8:    p = super8.get(); break;
        }

        if (p == null) {
            depVuelto.add(m);
            throw new NoHayProductoException();
        }

        depIngresos.add(m);

        int vuelto = m.getValor() - precio;
        while (vuelto > 0) {
            depVuelto.add(new Moneda100());
            vuelto -= 100;
        }

        return p;
    }

    /**
     * Permite retirar el vuelto moneda por moneda tras una transacción.
     *
     * @return Una instancia de Moneda (de $100) desde el depósito de vuelto, o null si está vacío.
     */
    public Moneda getVuelto() {
        return depVuelto.get();
    }
}