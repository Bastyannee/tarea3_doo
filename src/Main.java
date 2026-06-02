import view.Ventana;

/**
 * Clase principal que inicializa el sistema.
 * Restringe la instanciación exclusiva del contenedor gráfico principal (Ventana)
 * asegurando su ejecución segura dentro del Event Dispatch Thread de Swing.
 */
public class Main {
    /**
     * Punto de entrada de la aplicación.
     * * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Ejecución segura en el Event Dispatch Thread (EDT) de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            new Ventana();
        });
    }
}