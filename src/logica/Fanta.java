package logica;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Implementación concreta de una bebida tipo Fanta.
 */
public class Fanta extends Bebida {

    private Image imagen;

    public Fanta(int serie) {
        super(serie);
        try {
            this.imagen = ImageIO.read(new File("imagenes/fanta.png"));
        } catch (IOException e) {
            System.out.println("No se encontró la imagen para Fanta");
        }
    }

    @Override
    public String consumir() {
        return "fanta";
    }

    public void paintComponent(Graphics g) {
        if (this.imagen != null) {
            g.drawImage(this.imagen, super.getX(), super.getY(), 60, 80, null);
        } else {
            g.setColor(Color.ORANGE);
            g.fillRect(super.getX(), super.getY(), 40, 60);
        }
    }
}