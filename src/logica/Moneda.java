package logica;
import java.awt.Graphics;
import java.awt.Color;

/**
 * Clase abstracta que representa un medio de pago genérico.
 * Gestiona el identificador único basado en la dirección de memoria.
 */
public abstract class Moneda implements Comparable<Moneda> {
    /** Coordenada X para la representación gráfica en la ventana. */
    private int x;

    /** Coordenada Y para la representación gráfica en la ventana. */
    private int y;
    /**
     * Constructor por defecto.
     */
    public Moneda() {
    }

    /**
     * Obtiene el número de serie de la moneda.
     * Se utiliza la dirección de memoria (hashCode) como número de serie único.
     * 
     * @return El código hash de la instancia.
     */
    public int getSerie() {
        return this.hashCode();
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
     * Dibuja la representación gráfica del objeto en la ventana[cite: 14].
     * * @param g Contexto gráfico proporcionado por Swing.
     */
    public void paintComponent(Graphics g) {
        if (this.getValor() == 100) g.setColor(new Color(205, 127, 50)); 
        else if (this.getValor() == 500) g.setColor(new Color(192, 192, 192)); 
        else if (this.getValor() == 1000) g.setColor(new Color(255, 215, 0)); 
        else g.setColor(Color.GREEN);

        g.fillOval(this.x, this.y, 30, 30);
        g.setColor(Color.BLACK);
        g.drawOval(this.x, this.y, 30, 30);
        
        g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 9));
        String serie = String.valueOf(Math.abs(this.getSerie()));
        g.drawString(serie.substring(0, Math.min(4, serie.length())), this.x + 3, this.y + 18);
    }

    /**
     * Obtiene el valor monetario de la moneda.
     * 
     * @return El valor entero representativo.
     */
    public abstract int getValor();
    /**
     * Compara esta moneda con otra basándose en su valor monetario.
     *para que Collections.sort() funcione en los Mains.
     *
     * @param otra La moneda con la que se desea comparar.
     * @return Un entero negativo, cero o positivo según la comparación.
     */
    @Override
    public int compareTo(Moneda otra) {
        // Compara los valores de forma ascendente
        return Integer.compare(this.getValor(), otra.getValor());
    }

    /**
     * Representación textual de la moneda mostrando su valor y número de serie.
     *
     * @return Un String formateado.
     */
    @Override
    public String toString() {
        return "Moneda de $" + getValor() + " (Serie: " + getSerie() + ")";
    }
}