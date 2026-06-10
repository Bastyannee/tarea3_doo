package view;

import logica.Expendedor;
import logica.Producto;
import logica.Moneda;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;

/**
 * Vista del Expendedor exclusiva para la Tarea 3.
 * Dibuja la imagen de fondo y posiciona los productos en sus casilleros.
 */
public class PanelExpendedor extends JPanel {
    private final Expendedor exp;
    private int x;
    private int y;
    private int ancho;
    private int alto;

    /** Imagen de fondo de la máquina expendedora */
    private Image imagenFondo;

    public PanelExpendedor(int x, int y, int ancho, int alto, Expendedor exp) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.exp = exp;

        // PASO 2: Cargar la imagen de fondo desde la carpeta que creamos
        try {
            this.imagenFondo = ImageIO.read(new File("imagenes/maquinaexpendedora.png"));
        } catch (IOException e) {
            System.out.println("No se pudo cargar la imagen maquina.png");
        }

        // PASO 3: Posicionar los productos en la imagen
        reposicionarElementos();
    }
    /**
     * Evalúa si un punto cartesiano (click) colisiona con el área del expendedor.
     * @param mouseX Coordenada X del click.
     * @param mouseY Coordenada Y del click.
     * @return true si las coordenadas están dentro del área.
     */
    public boolean contieneCoordenadas(int mouseX, int mouseY) {
        return (mouseX >= this.x && mouseX < this.x + this.ancho &&
                mouseY >= this.y && mouseY < this.y + this.alto);
    }

    /**
     * Procesa el click rellenando los depósitos vacíos, tal como pide el enunciado.
     * @param mouseX Coordenada X del click.
     * @param mouseY Coordenada Y del click.
     */
    public void procesarClick(int mouseX, int mouseY) {
        System.out.println("Click en la máquina detectado. Rellenando stock...");
        exp.rellenarDepositosVaciados(5); // Rellena la máquina
        reposicionarElementos();          // Reacomoda los productos visualmente
    }

    /**
     * Calcula las posiciones (x, y) de cada producto para que calcen
     * dentro de los recuadros grises de tu imagen pixel-art.
     */
    public void reposicionarElementos() {
        // --- COORDENADAS PARA LA IMAGEN ---
        // Tendrás que ajustar estos números (sumar o restar píxeles) para que calcen perfecto
        int columna1X = this.x + 80;
        int columna2X = this.x + 220;
        int columna3X = this.x + 360;
        int separacionEntreBotellas = 15; // Distancia entre botellas del mismo depósito

        // FILA DE ARRIBA (Recuadros grises superiores)
        int filaArribaY = this.y + 150;
        asignarCoordenadas(exp.getDepositoCocaCola().getLista(), columna1X, filaArribaY, separacionEntreBotellas);
        asignarCoordenadas(exp.getDepositoSprite().getLista(), columna2X, filaArribaY, separacionEntreBotellas);
        asignarCoordenadas(exp.getDepositoFanta().getLista(), columna3X, filaArribaY, separacionEntreBotellas);

        // FILA DE ABAJO (Recuadros grises inferiores)
        int filaAbajoY = this.y + 350;
        asignarCoordenadas(exp.getDepositoSnickers().getLista(), columna1X, filaAbajoY, separacionEntreBotellas);
        asignarCoordenadas(exp.getDepositoSuper8().getLista(), columna2X, filaAbajoY, separacionEntreBotellas);
        // Te queda un 6to recuadro gris libre, según tu dibujo.

        // DEPÓSITO DE VUELTO (Rectángulo negro inferior derecho)
        ArrayList<Moneda> monedas = exp.getDepositoVuelto().getLista();
        for (int i = 0; i < monedas.size(); i++) {
            monedas.get(i).setXY(this.x + 450 + (i * 5), this.y + 600);
        }

        // DEPÓSITO DE DESPACHO (Rectángulo negro inferior izquierdo)
        Producto pDespacho = exp.getProducto();
        if (pDespacho != null) {
            pDespacho.setXY(this.x + 200, this.y + 600);
        }
    }

    /**
     * Metodo auxiliar para iterar y posicionar una lista de productos en su panel.
     */
    private void asignarCoordenadas(ArrayList<Producto> lista, int inicioX, int inicioY, int separacionX) {
        for (int i = 0; i < lista.size(); i++) {
            lista.get(i).setXY(inicioX + (i * separacionX), inicioY);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Dibujar tu máquina expendedora (Fondo)
        if (this.imagenFondo != null) {
            g.drawImage(this.imagenFondo, this.x, this.y, this.ancho, this.alto, null);
        } else {
            // Fondo de emergencia rojo si la imagen no carga
            g.setColor(Color.RED);
            g.fillRect(this.x, this.y, this.ancho, this.alto);
        }

        // 2. Dibujar los productos ENCIMA del fondo
        dibujarLista(exp.getDepositoCocaCola().getLista(), g);
        dibujarLista(exp.getDepositoSprite().getLista(), g);
        dibujarLista(exp.getDepositoFanta().getLista(), g);
        dibujarLista(exp.getDepositoSnickers().getLista(), g);
        dibujarLista(exp.getDepositoSuper8().getLista(), g);

        // 3. Dibujar las monedas de vuelto
        for (Moneda m : exp.getDepositoVuelto().getLista()) {
            m.paintComponent(g);
        }
    }

    /**
     * Auxiliar para pintar listas de productos.
     * Dibuja desde el último al primero para que el producto de adelante
     * no sea tapado por los de atrás (corrige la superposición).
     */
    private void dibujarLista(ArrayList<Producto> lista, Graphics g) {
        for (int i = lista.size() - 1; i >= 0; i--) {
            lista.get(i).paintComponent(g);
        }
    }
}