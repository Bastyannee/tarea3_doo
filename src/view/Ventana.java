package view;

import javax.swing.JFrame;

public class Ventana extends JFrame {
    public Ventana() {
        super("Simulación Máquina Expendedora 2D");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Agregamos el panel principal al centro del JFrame
        this.add(new PanelPrincipal());
        
        this.pack(); // Ajusta el tamaño de la ventana según los componentes internos
        this.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        this.setVisible(true);
    }
}