package view;

import logica.Comprador;
import logica.Expendedor;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelPrincipal extends JPanel {
    private final Expendedor expendedor;
    private final Comprador comprador;
    private final PanelExpendedor panelExpendedor;
    private final PanelComprador panelComprador;

    public PanelPrincipal() {
        this.setPreferredSize(new Dimension(1024, 768));
        this.setBackground(Color.WHITE);

        this.expendedor = new Expendedor(5);
        this.comprador = new Comprador();

        this.panelComprador = new PanelComprador(0, 0, 300, 768, this.expendedor, this.comprador);
        this.panelExpendedor = new PanelExpendedor(300, 0, 724, 768, this.expendedor);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                // Delegación espacial manual estricta
                if (x >= 0 && x < 300) {
                    panelComprador.procesarClick(x, y);
                } else if (x >= 300 && x <= 1024) {
                    panelExpendedor.procesarClick(x - 300, y);
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        panelComprador.paintComponent(g);
        
        Graphics gExp = g.create(300, 0, 724, 768);
        panelExpendedor.paintComponent(gExp);
        gExp.dispose();
    }
}