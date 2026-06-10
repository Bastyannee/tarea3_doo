package view;

import logica.Expendedor;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Lienzo central de la simulación y Coordinador (Mediator) de la interfaz gráfica.
 * Instancia el modelo de negocio (Expendedor) y distribuye espacialmente 
 * las vistas de los componentes interactivos. Centraliza la captura de eventos del mouse.
 */
public class PanelPrincipal extends JPanel {
    /** Instancia única del modelo lógico compartida entre las vistas. */
    private final Expendedor expendedor;
    
    /** Vista encapsulada correspondiente al área de la máquina expendedora. */
    private final PanelExpendedor panelExpendedor;
    
    /** Vista encapsulada correspondiente al área de interacción del usuario. */
    private final PanelComprador panelComprador;

    /**
     * Constructor del panel principal.
     * Define las dimensiones base (1024x768), inicializa el modelo lógico
     * y particiona el espacio de renderizado asignando Bounding Boxes a los sub-paneles.
     */
    public PanelPrincipal() {
        this.setPreferredSize(new Dimension(1024, 768));
        this.setBackground(Color.WHITE);

        // 1. Inicializar el Modelo (Lógica Tarea 1) con stock inicial
        this.expendedor = new Expendedor(5);

        // 2. Partición Espacial (Hitboxes)
        // El Comprador toma la franja izquierda (300px de ancho)
        this.panelComprador = new PanelComprador(0, 0, 300, 768, expendedor);
        // El Expendedor toma el resto de la ventana hacia la derecha (724px de ancho)
        this.panelExpendedor = new PanelExpendedor(300, 0, 724, 768, expendedor);

        // 3. Captura centralizada de eventos del mouse (MouseListener)
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                // Delegación de eventos orientada a objetos usando geometría analítica
                if (panelComprador.contieneCoordenadas(x, y)) {
                    panelComprador.procesarClick(x, y);
                } else if (panelExpendedor.contieneCoordenadas(x, y)) {
                    panelExpendedor.procesarClick(x, y);
                }

                // Actualización del árbol de rendering visual posterior al cambio de estado
                repaint();
            }
        });
    }

    /**
     * Metodo central de renderizado de Swing.
     * Limpia el búfer gráfico y propaga la orden de pintado a las vistas hijas en cascada.
     * * @param g Contexto gráfico proporcionado por Swing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Limpia el fondo para evitar el efecto "Ghosting"
        
        // Propagación en cascada del renderizado de los componentes
        panelExpendedor.paintComponent(g);
        panelComprador.paintComponent(g);
    }
}