package view;
import logica.Expendedor;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelPrincipal extends JPanel {
    // Instancia única del modelo lógico compartida
    private final Expendedor expendedor;
    
    // Sub-paneles visuales (Vistas)
    private final PanelExpendedor panelExpendedor;
    private final PanelComprador panelComprador;

    public PanelPrincipal() {
        this.setPreferredSize(new Dimension(1024, 768));
        this.setBackground(Color.WHITE);

        // 1. Inicializar el Modelo (Lógica Tarea 1) con stock inicial
        this.expendedor = new Expendedor(5);

        // 2. Inicializar las Vistas pasando las referencias lógicas y coordenadas (x, y) relativas
        this.panelExpendedor = new PanelExpendedor(50, 50, expendedor);
        this.panelComprador = new PanelComprador(600, 50, expendedor);

        // 3. Captura centralizada de eventos del mouse (MouseListener)
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                // Delegación de eventos orientada a objetos
                if (panelExpendedor.contieneCoordenadas(x, y)) {
                    panelExpendedor.procesarClick(x, y);
                } else if (panelComprador.contieneCoordenadas(x, y)) {
                    panelComprador.procesarClick(x, y);
                }

                // Actualización del árbol de rendering visual posterior al cambio de estado
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Propagación en cascada del renderizado de los componentes
        panelExpendedor.paintComponent(g);
        panelComprador.paintComponent(g);
    }
}