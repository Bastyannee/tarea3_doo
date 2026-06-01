package logica;

/**
 * Implementación concreta de una bebida tipo Fanta.
 */
public class Fanta extends Bebida {
    /**
     * Constructor de una Fanta.
     * 
     * @param serie El identificador único de esta lata.
     */
    public Fanta(int serie) {
        super(serie);
    }

    /**
     * Consume la bebida.
     * 
     * @return El string "fanta".
     */
    @Override
    public String consumir() {
        return "fanta";
    }
}
