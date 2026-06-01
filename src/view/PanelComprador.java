package view;

import logica.Expendedor;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class PanelComprador extends JPanel {
    private final Expendedor exp;
    private int x, y, ancho, alto;

    // Constructor corregido con los 5 parámetros requeridos
    public PanelComprador(int x, int y, int ancho, int alto, Expendedor exp) {
    this.x = x; this.y = y; this.ancho = ancho; this.alto = alto; this.exp = exp;
}

    // Método matemático para verificar si el click cayó dentro del área del Comprador
    public boolean contieneCoordenadas(int mouseX, int mouseY) {
    return (mouseX >= this.x && mouseX < this.x + this.ancho &&
            mouseY >= this.y && mouseY < this.y + this.alto);
    }

    public void procesarClick(int mouseX, int mouseY) {
        // Aquí interactuará tu compañero (Integrante 3) con sus zonas rectangulares
        System.out.println("Click capturado en el área del Comprador: (" + mouseX + ", " + mouseY + ")");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dibujo del contenedor base para pruebas visuales
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, ancho, alto);
        
        g.setColor(Color.BLACK);
        g.drawRect(x, y, ancho, alto);
        g.drawString("Área Comprador", x + 20, y + 30);
    }
}