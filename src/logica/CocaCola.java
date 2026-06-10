package logica;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Implementación concreta de una bebida tipo CocaCola.
 */
public class CocaCola extends Bebida {

    private Image imagen;

    /**
     * Constructor de una CocaCola.
     * * @param serie El identificador único de esta lata.
     */
    public CocaCola(int serie) {
        super(serie);

        // Carga la imagen específica de este producto
        try {
            this.imagen = ImageIO.read(new File("imagenes/coca.png"));
        } catch (IOException e) {
            System.out.println("No se encontró la imagen para CocaCola");
        }
    }

    /**
     * Consume la bebida.
     * * @return El string "coca".
     */
    @Override
    public String consumir() {
        return "coca";
    }

    /**
     * Dibuja la representación gráfica del producto.
     * @param g Contexto gráfico proporcionado por Swing.
     */
    public void paintComponent(Graphics g) {
        if (this.imagen != null) {
            // Dibuja la imagen usando las coordenadas de la clase padre
            g.drawImage(this.imagen, super.getX(), super.getY(), 60, 80, null);
        } else {
            // Fallback si la imagen no carga
            g.setColor(Color.GREEN);
            g.fillRect(super.getX(), super.getY(), 40, 60);
        }
    }
}