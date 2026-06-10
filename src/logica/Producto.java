package logica;
import java.awt.Graphics;
import java.awt.Color;

/**
 * Clase abstracta base que representa cualquier ítem que pueda ser vendido en el Expendedor.
 * Define la estructura fundamental de almacenamiento de serie y consumo.
 */
public abstract class Producto {
    private int x;
    private int y;
    /** Número de serie único para identificar la instancia en memoria. */
    private int serie;

    /**
     * Constructor base para inicializar el producto.
     * 
     * @param serie El identificador único del producto.
     */
    public Producto(int serie) {
        this.serie = serie;
    }
    /**
     * Establece las coordenadas espaciales del elemento en la interfaz gráfica.
     * Esto permite reposicionar el objeto dentro de los depósitos[cite: 75].
     * * @param x La nueva coordenada X relativa al panel contenedor.
     * @param y La nueva coordenada Y relativa al panel contenedor.
     */
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Obtiene la coordenada X actual del producto.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Obtiene la coordenada Y actual del producto.
     */
    public int getY() {
        return this.y;
    }
    /**
     * Dibuja la representación gráfica del objeto en la ventana
     * * @param g Contexto gráfico proporcionado por Swing.
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.GREEN); // Puedes cambiar el color según el producto
        g.fillRect(this.x, this.y, 40, 60);
    }
    /**
     * Simula el consumo del producto.
     * 
     * @return Un String que describe el sabor o la acción de consumo.
     */
    public abstract String consumir();

    /**
     * Obtiene el número de serie del producto.
     * 
     * @return El número de serie entero.
     */
    public int getSerie() {
        return serie;
    }
}
