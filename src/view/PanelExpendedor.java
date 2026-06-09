package view;

import logica.Expendedor;
import logica.Producto;
import logica.Moneda;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Vista del Expendedor.
 * Gestiona el área gráfica derecha de la pantalla, dibujando la estructura
 * de la máquina, los depósitos, y el control de stock.
 */
public class PanelExpendedor extends JPanel {
    private final Expendedor exp;
    private int x;
    private int y;
    private int ancho;
    private int alto;

    /**
     * Constructor del panel del expendedor.
     * @param x Coordenada X de origen.
     * @param y Coordenada Y de origen.
     * @param ancho Ancho total de la máquina.
     * @param alto Alto total de la máquina.
     * @param exp Referencia al modelo lógico.
     */
    public PanelExpendedor(int x, int y, int ancho, int alto, Expendedor exp) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.exp = exp;

        reposicionarElementos();
    }

    /**
     * Evalúa si un punto cartesiano (click) colisiona con el Expendedor.
     * @param mouseX Coordenada X del click.
     * @param mouseY Coordenada Y del click.
     * @return true si las coordenadas están dentro del área.
     */
    public boolean contieneCoordenadas(int mouseX, int mouseY) {
        return (mouseX >= this.x && mouseX < this.x + this.ancho &&
                mouseY >= this.y && mouseY < this.y + this.alto);
    }

    /**
     * Procesa la lógica al hacer click. Rellena los depósitos vacíos.
     */
    public void procesarClick(int mouseX, int mouseY) {
        exp.rellenarDepositosVaciados(5); // Rellena con 5 productos mágicamente
        reposicionarElementos();          // Reacomoda las vistas
    }

    /**
     * Asigna coordenadas físicas a los elementos dentro de los depósitos.
     */
    public void reposicionarElementos() {
        int margenX = this.x + 30;
        int margenY = this.y + 40;
        int separacionX = 45; // Espacio horizontal entre productos
        int separacionY = 80; // Espacio vertical entre filas

        // Fila 1: CocaCola
        asignarCoordenadas(exp.getDepositoCocaCola().getLista(), margenX, margenY, separacionX);
        // Fila 2: Sprite
        asignarCoordenadas(exp.getDepositoSprite().getLista(), margenX, margenY + separacionY, separacionX);
        // Fila 3: Fanta
        asignarCoordenadas(exp.getDepositoFanta().getLista(), margenX, margenY + (separacionY * 2), separacionX);
        // Fila 4: Snickers
        asignarCoordenadas(exp.getDepositoSnickers().getLista(), margenX, margenY + (separacionY * 3), separacionX);
        // Fila 5: Super8
        asignarCoordenadas(exp.getDepositoSuper8().getLista(), margenX, margenY + (separacionY * 4), separacionX);

        // Reposicionar depósito de vuelto (amontonados en la esquina inferior izquierda)
        ArrayList<Moneda> monedas = exp.getDepositoVuelto().getLista();
        for (int i = 0; i < monedas.size(); i++) {
            monedas.get(i).setXY(this.x + 30 + (i * 10), this.y + this.alto - 60);
        }

        // Reposicionar el producto del depósito de despacho (esquina inferior derecha)
        Producto pDespacho = exp.getProducto();
        if (pDespacho != null) {
            pDespacho.setXY(this.x + this.ancho - 80, this.y + this.alto - 100);
            // Lo devolvemos al modelo (ya que getProducto lo remueve temporalmente)
            // Esto es un parche visual simple para que se dibuje
            // En la versión final, esto requiere que el comprador haga click para sacarlo.
        }
    }

    /**
     * Metodo auxiliar para iterar y posicionar una lista de productos en una fila.
     */
    private void asignarCoordenadas(ArrayList<Producto> lista, int inicioX, int inicioY, int separacionX) {
        for (int i = 0; i < lista.size(); i++) {
            lista.get(i).setXY(inicioX + (i * separacionX), inicioY);
        }
    }

    /**
     * Dibuja la máquina y solicita a los productos que se dibujen.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar el chasis de la máquina
        g.setColor(new Color(50, 50, 50));
        g.fillRect(this.x, this.y, this.ancho, this.alto);

        // Dibujar el "vidrio" principal
        g.setColor(new Color(200, 230, 255, 50));
        g.fillRect(this.x + 10, this.y + 10, this.ancho - 20, this.alto - 120);

        // Dibujar el hueco de despacho
        g.setColor(Color.BLACK);
        g.fillRect(this.x + this.ancho - 100, this.y + this.alto - 110, 80, 80);

        // Dibujar los productos en stock
        dibujarLista(exp.getDepositoCocaCola().getLista(), g);
        dibujarLista(exp.getDepositoSprite().getLista(), g);
        dibujarLista(exp.getDepositoFanta().getLista(), g);
        dibujarLista(exp.getDepositoSnickers().getLista(), g);
        dibujarLista(exp.getDepositoSuper8().getLista(), g);

        // Dibujar monedas de vuelto
        for (Moneda m : exp.getDepositoVuelto().getLista()) m.paintComponent(g);
    }

    /**
     * Auxiliar para pintar listas de productos.
     */
    private void dibujarLista(ArrayList<Producto> lista, Graphics g) {
        for (Producto p : lista) p.paintComponent(g);
    }
}