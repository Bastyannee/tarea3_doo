package logica;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Implementación concreta de un dulce tipo Snickers.
 */
public class Snickers extends Dulce {

    private Image imagen;

    public Snickers(int serie) {
        super(serie);
        try {
            this.imagen = ImageIO.read(new File("imagenes/snickers.png"));
        } catch (IOException e) {
            System.out.println("No se encontró la imagen para Snickers");
        }
    }

    @Override
    public String consumir() {
        return "snickers";
    }

    public void paintComponent(Graphics g) {
        if (this.imagen != null) {
            // Los dulces suelen ser más anchos y bajitos, ajusté el tamaño a 50x30
            g.drawImage(this.imagen, super.getX(), super.getY(), 60, 80, null);
        } else {
            g.setColor(new Color(139, 69, 19)); // Color café
            g.fillRect(super.getX(), super.getY(), 50, 30);
        }
    }
}