package logica;

/**
 * Implementación concreta de una bebida tipo Sprite.
 */
public class Sprite extends Bebida {
    /**
     * Constructor de una Sprite.
     * 
     * @param serie El identificador único de esta lata.
     */
    public Sprite(int serie) {
        super(serie);
    }

    /**
     * Consume la bebida.
     * 
     * @return El string "sprite".
     */
    @Override
    public String consumir() {
        return "sprite";
    }
}
