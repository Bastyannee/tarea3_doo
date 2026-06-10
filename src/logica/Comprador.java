package logica;
import logica.exceptions.*;

import java.util.ArrayList;
import java.util.List;
/**
 * Entidad que simula a un cliente interactuando con la máquina expendedora.
 */

public class Comprador {
    private int saldo;
    private List<Moneda> monedas;
    private List<Producto> productos;
    private static final int saldo_inicial = 3300;

    /**
     * Construye un comprador con saldo inicial y monedero con monedas de ejemplo.
     */
    public Comprador() {
        this.saldo = saldo_inicial;
        this.monedas = new ArrayList<>();
        this.productos = new ArrayList<>();

        monedas.add(new Moneda1000());
        monedas.add(new Moneda1000());
        monedas.add(new Moneda500());
        monedas.add(new Moneda500());
        monedas.add(new Moneda100());
        monedas.add(new Moneda100());
        monedas.add(new Moneda100());
    }

    /**
     * Descuenta una moneda del monedero según el valor especificado. Retorna la moneda descontada o null si no hay fondos suficientes.
     *
     * @param valor valor de la moneda a descontar.
     * @return Moneda descontada, o null si no se encontró.
     */
    public Moneda descontarMoneda(int valor) {
        for (int i = 0; i < monedas.size(); i++) {
            if (monedas.get(i).getValor() == valor) {
                saldo -= valor;
                return monedas.remove(i);
            }
        }
        return null;
    }

    /**
     * Agrega una moneda recibida (vuelto) al monedero del comprador
     * y actualiza el saldo.
     *
     * @param m Moneda recibida como vuelto.
     */
    public void recibirMoneda(Moneda m) {
        if (m != null) {
            monedas.add(m);
            saldo += m.getValor();
        }
    }

    /**
     * Agrega un producto recogido a la colección del comprador.
     *
     * @param p Producto recogido del expendedor.
     */
    public void recibirProducto(Producto p) {
        if (p != null) {
            productos.add(p);
        }
    }

    /**
     * @return saldo disponible actual en pesos
     */
    public int getSaldo() {
        return saldo;
    }

    /**
     * @return lista de monedas en el monedero
     */
    public List<Moneda> getMonedas() {
        return monedas;
    }

    /**
     * @return lista de productos recogidos
     */
    public List<Producto> getProductos() {
        return productos;
    }
}