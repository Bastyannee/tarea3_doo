package logica;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Implementación concreta de una bebida tipo Sprite.
 */
public class Sprite extends Bebida {

    private Image imagen;

    public Sprite(int serie) {
        super(serie);
        try {
            this.imagen = ImageIO.read(new File("imagenes/sprite.png"));
        } catch (IOException e) {
            System.out.println("No se encontró la imagen para Sprite");
        }
    }

    @Override
    public String consumir() {
        return "sprite";
    }

    public void paintComponent(Graphics g) {
        if (this.imagen != null) {
            g.drawImage(this.imagen, super.getX(), super.getY(), 60, 80, null);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(super.getX(), super.getY(), 40, 60);
        }
    }
}