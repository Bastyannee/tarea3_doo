package view;

import logica.Expendedor;
import javax.swing.JPanel;
import java.awt.Graphics;

/**
 * Stub de la vista del Comprador.
 * Gestiona el área interactiva izquierda de la pantalla, controlando las selecciones
 * del usuario y su monedero.
 */
public class PanelComprador extends JPanel {
    private final Expendedor exp;
    private int x;
    private int y;
    private int ancho;
    private int alto;

    /**
     * Constructor del panel del comprador.
     * * @param x Coordenada X de origen en el lienzo principal.
     * @param y Coordenada Y de origen en el lienzo principal.
     * @param ancho Ancho total del área interactiva.
     * @param alto Alto total del área interactiva.
     * @param exp Referencia compartida al modelo lógico.
     */
    public PanelComprador(int x, int y, int ancho, int alto, Expendedor exp) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.exp = exp;
    }

    /**
     * Evalúa si un punto cartesiano (click) colisiona con el Bounding Box de este panel.
     * * @param mouseX Coordenada X del click.
     * @param mouseY Coordenada Y del click.
     * @return true si las coordenadas están dentro del área; false en caso contrario.
     */
    public boolean contieneCoordenadas(int mouseX, int mouseY) {
        return (mouseX >= this.x && mouseX < this.x + this.ancho &&
                mouseY >= this.y && mouseY < this.y + this.alto);
    }

    /**
     * Procesa la lógica de negocio asociada a un click válido dentro del panel.
     * * @param mouseX Coordenada X del click.
     * @param mouseY Coordenada Y del click.
     */
    public void procesarClick(int mouseX, int mouseY) {
        // Implementación pendiente por Integrante 3
    }

    /**
     * Dibuja los componentes visuales correspondientes al comprador.
     * * @param g Contexto gráfico proporcionado por Swing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Implementación pendiente por Integrante 3
    }
}