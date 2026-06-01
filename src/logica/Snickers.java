package logica;


/**
 * Implementación concreta de un dulce tipo Snickers.
 */
public class Snickers extends Dulce {
    /**
     * Constructor de un Snickers.
     * 
     * @param serie El identificador único de este dulce.
     */
    public Snickers(int serie) {
        super(serie);
    }

    /**
     * Consume el dulce.
     * 
     * @return El string "snickers".
     */
    @Override
    public String consumir() {
        return "snickers";
    }
}
