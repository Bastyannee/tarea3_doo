package logica;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Implementación concreta de un dulce tipo Super8.
 */
public class Super8 extends Dulce {

    private Image imagen;

    public Super8(int serie) {
        super(serie);
        try {
            this.imagen = ImageIO.read(new File("imagenes/super8.png"));
        } catch (IOException e) {
            System.out.println("No se encontró la imagen para Super8");
        }
    }

    @Override
    public String consumir() {
        return "super8";
    }

    public void paintComponent(Graphics g) {
        if (this.imagen != null) {
            // Ajusté el tamaño a 50x30 igual que el Snickers
            g.drawImage(this.imagen, super.getX(), super.getY(), 60, 80, null);
        } else {
            g.setColor(Color.YELLOW);
            g.fillRect(super.getX(), super.getY(), 50, 30);
        }
    }
}