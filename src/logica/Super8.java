package logica;

/**
 * Implementación concreta de un dulce tipo Super8.
 */
public class Super8 extends Dulce {
    /**
     * Constructor de un Super8.
     * 
     * @param serie El identificador único de este dulce.
     */
    public Super8(int serie) {
        super(serie);
    }

    /**
     * Consume el dulce.
     * 
     * @return El string "super8".
     */
    @Override
    public String consumir() {
        return "super8";
    }
}
