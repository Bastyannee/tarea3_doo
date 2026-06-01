package logica;


/**
 * Clase abstracta que representa un medio de pago genérico.
 * Gestiona el identificador único basado en la dirección de memoria.
 */
public abstract class Moneda implements Comparable<Moneda> {
    
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
