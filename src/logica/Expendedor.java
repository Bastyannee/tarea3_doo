package logica;
import logica.exceptions.*;

/**
 * Representa la máquina expendedora que gestiona el inventario de productos
 * y las transacciones de venta con monedas.
 */
public class Expendedor {
    private Deposito<Producto> coca;
    private Deposito<Producto> sprite;
    private Deposito<Producto> fanta;
    private Deposito<Producto> snickers;
    private Deposito<Producto> super8;
    private Deposito<Moneda> depVuelto;
    private Deposito<Moneda> depIngresos;

    /** Depósito especial de un solo producto donde cae la compra exitosa. */
    private Producto depositoDespacho;

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
        depositoDespacho = null;

        rellenarDepositosVaciados(numProductos);
    }

    /**
     * Rellena los depósitos que se encuentren vacíos, tal como exige la Tarea 3.
     * @param cantidad La cantidad de productos a rellenar por depósito vacío.
     */
    public void rellenarDepositosVaciados(int cantidad) {
        if (coca.getLista().isEmpty()) for (int i = 0; i < cantidad; i++) coca.add(new CocaCola(1000 + i));
        if (sprite.getLista().isEmpty()) for (int i = 0; i < cantidad; i++) sprite.add(new Sprite(2000 + i));
        if (fanta.getLista().isEmpty()) for (int i = 0; i < cantidad; i++) fanta.add(new Fanta(3000 + i));
        if (snickers.getLista().isEmpty()) for (int i = 0; i < cantidad; i++) snickers.add(new Snickers(4000 + i));
        if (super8.getLista().isEmpty()) for (int i = 0; i < cantidad; i++) super8.add(new Super8(5000 + i));
    }

    /**
     * Procesa la compra de un producto. Su tipo de retorno es void y el producto
     * se deja en el depósito de despacho.
     *
     * @param m La moneda utilizada como medio de pago.
     * @param tipo El tipo de producto solicitado (Enum).
     * @throws PagoIncorrectoException Si la moneda ingresada es nula.
     * @throws PagoInsuficienteException Si el valor de la moneda es menor al precio del producto.
     * @throws NoHayProductoException Si el depósito del producto solicitado está vacío.
     */
    public void comprarProducto(Moneda m, TipoProducto tipo) throws PagoIncorrectoException, PagoInsuficienteException, NoHayProductoException {
        if (m == null) throw new PagoIncorrectoException();

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

        // El producto se deja en el depósito especial
        this.depositoDespacho = p;
    }

    /**
     * Retira el producto comprado desde el depósito especial.
     * @return El Producto comprado, o null si está vacío.
     */
    public Producto getProducto() {
        Producto p = this.depositoDespacho;
        this.depositoDespacho = null;
        return p;
    }

    /**
     * Permite retirar el vuelto moneda por moneda tras una transacción.
     * @return Una instancia de Moneda desde el depósito de vuelto, o null si está vacío.
     */
    public Moneda getVuelto() { return depVuelto.get(); }

    // --- GETTERS PARA LAS VISTAS ---
    public Deposito<Producto> getDepositoCocaCola() { return this.coca; }
    public Deposito<Producto> getDepositoSprite() { return this.sprite; }
    public Deposito<Producto> getDepositoFanta() { return this.fanta; }
    public Deposito<Producto> getDepositoSnickers() { return this.snickers; }
    public Deposito<Producto> getDepositoSuper8() { return this.super8; }
    public Deposito<Moneda> getDepositoVuelto() { return this.depVuelto; }
}