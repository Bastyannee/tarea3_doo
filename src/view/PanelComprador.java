package view;

import logica.Expendedor;
import javax.swing.JPanel;
import java.awt.Graphics;

public class PanelComprador extends JPanel {
    private Expendedor exp;

    // Constructor temporal (Stub)
    public PanelComprador(int x, int y, Expendedor exp) {
        this.exp = exp;
    }

    public boolean contieneCoordenadas(int x, int y) {
        return false; // Lógica temporal
    }

    public void procesarClick(int x, int y) {
        // Vacío por ahora
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Aquí tu compañero dibujará la interfaz del comprador
    }
}