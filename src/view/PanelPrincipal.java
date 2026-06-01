package view;

import logica.Expendedor;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelPrincipal extends JPanel {
    private final Expendedor expendedor;
    private final PanelExpendedor panelExpendedor;
    private final PanelComprador panelComprador;

    public PanelPrincipal() {
        this.setPreferredSize(new Dimension(1024, 768));
        this.setBackground(Color.WHITE);

        this.expendedor = new Expendedor(5);

        // 1. Refactorización: Pasamos (x, y, ancho, alto, modelo lógico)
        // El Comprador toma la franja izquierda (300px de ancho)
        this.panelComprador = new PanelComprador(0, 0, 300, 768, expendedor);

        // El Expendedor toma el resto de la ventana hacia la derecha
        this.panelExpendedor = new PanelExpendedor(300, 0, 724, 768, expendedor);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                // 2. Delegación espacial estricta
                if (panelComprador.contieneCoordenadas(mouseX, mouseY)) {
                    panelComprador.procesarClick(mouseX, mouseY);
                } else if (panelExpendedor.contieneCoordenadas(mouseX, mouseY)) {
                    panelExpendedor.procesarClick(mouseX, mouseY);
                }

                repaint(); // Ordena a Swing que repinte la pantalla completa
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Cascading render
        panelComprador.paintComponent(g);
        panelExpendedor.paintComponent(g);
    }
}