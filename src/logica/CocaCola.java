package logica;

/**
 * Implementación concreta de una bebida tipo CocaCola.
 */
public class CocaCola extends Bebida {
    /**
     * Constructor de una CocaCola.
     * 
     * @param serie El identificador único de esta lata.
     */
    public CocaCola(int serie) {
        super(serie);
    }

    /**
     * Consume la bebida.
     * 
     * @return El string "cocacola".
     */
    @Override
    public String consumir() {
        return "cocacola";
    }
}
