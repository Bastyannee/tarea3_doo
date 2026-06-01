import view.Ventana;

public class Main {
    public static void main(String[] args) {
        // Ejecución segura en el Event Dispatch Thread (EDT) de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            new Ventana();
        });
    }
}