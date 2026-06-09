package view;

import logica.Expendedor;
// Importa aquí tus clases de la Tarea 1 (como Deposito, Producto, Moneda) si es necesario
// import logica.Deposito;

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

    // --- ENUNCIADO: Vistas de los depósitos internos ---
    // Necesitas depósitos de productos (stocks), de vuelto y el especial de despacho
    private PanelDeposito depositoProductos;
    private PanelDeposito depositoVuelto;
    private PanelDeposito depositoDespacho; // Almacena el producto comprado (capacidad 1)

    /**
     * Constructor del panel del expendedor.
     * @param x Coordenada X de origen en el lienzo principal.
     * @param y Coordenada Y de origen en el lienzo principal.
     * @param ancho Ancho total del área interactiva.
     * @param alto Alto total del área interactiva.
     * @param exp Referencia compartida al modelo lógico.
     */
    public PanelExpendedor(int x, int y, int ancho, int alto, Expendedor exp) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.exp = exp;

        // ENUNCIADO: Inicializar los depósitos con posiciones RELATIVAS al expendedor
        // Ejemplo: supongamos que el depósito de productos ocupa la parte superior
        this.depositoProductos = new PanelDeposito(this.x + 20, this.y + 20, this.ancho - 40, this.alto / 2);

        // El depósito de despacho (donde cae el producto comprado) abajo a la derecha
        this.depositoDespacho = new PanelDeposito(this.x + 20, this.y + (alto - 100), 80, 60);

        // El depósito de vuelto abajo a la izquierda
        this.depositoVuelto = new PanelDeposito(this.x + (ancho - 100), this.y + (alto - 100), 80, 60);

        // Al inicio, debes calcular u ordenar las posiciones de los productos dentro
        // de los depósitos lógicos usando setXY.
        reposicionarElementos();
    }

    /**
     * Evalúa si un punto cartesiano (click) colisiona con el Bounding Box de este panel.
     */
    public boolean contieneCoordenadas(int mouseX, int mouseY) {
        return (mouseX >= this.x && mouseX < this.x + this.ancho &&
                mouseY >= this.y && mouseY < this.y + this.alto);
    }

    /**
     * Procesa la lógica de negocio asociada a un click válido dentro del panel.
     */
    public void procesarClick(int mouseX, int mouseY) {
        //Si se hace click dentro del panel del expendedor, se debe rellenar los depósitos vacíos
        // Puedes verificar si el click cae en alguna zona específica o en general de la máquina
        System.out.println("Click en Expendedor detectado. Rellenando stock si es necesario...");

        // Aquí llamas al metodo
        // exp.rellenarDepositos();

        // Tras modificar el modelo (rellenar), reacomodamos visualmente y repintamos
        reposicionarElementos();
    }

    /**
     * Metodo auxiliar para ordenar y asignar coordenadas (setXY) a las vistas de
     * Productos y Monedas que estén almacenados en los depósitos lógicos.
     */
    private void reposicionarElementos() {
        // ENUNCIADO: Cada vez que se saque o agregue un producto o moneda,
        // se deberá llamar a reposicionar mediante setXY todas las vistas.

        // Aquí recorrerás los ArrayList de tu modelo lógico (exp.getDeposito...())
        // y calcularás las coordenadas X e Y de cada botella/moneda basándote en su índice
        // y en la posición del PanelDeposito correspondiente.
    }

    /**
     * Dibuja los componentes visuales correspondientes al expendedor.
     * @param g Contexto gráfico proporcionado por Swing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Dibujar la estructura externa de la máquina (Mínimo un rectángulo)
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(this.x, this.y, this.ancho, this.alto);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(this.x, this.y, this.ancho, this.alto);

        // Simular el "vidrio" frontal
        g.setColor(new Color(200, 230, 255, 100)); // Color celeste con transparencia (Alpha)
        g.fillRect(this.x + 10, this.y + 10, this.ancho - 20, this.alto / 2 + 20);

        // 2. ENUNCIADO: Llamar en cascada a los paintComponent de los depósitos (vistas)
        depositoProductos.paintComponent(g);
        depositoVuelto.paintComponent(g);
        depositoDespacho.paintComponent(g);

        // 3. ENUNCIADO: Los paneles de depósitos llamarán a los paintComponent de los productos/monedas.
        // Como las posiciones ya estarán actualizadas por reposicionarElementos(),
        // puedes hacer que este metodo dibuje los objetos que saca directamente del modelo lógico.
    }

    // CLASE INTERNA AUXILIAR PARA REPRESENTAR LAS VISTAS DE LOS DEPÓSITOS
    private class PanelDeposito {
        private int depX, depY, depAncho, depAlto;

        public PanelDeposito(int x, int y, int ancho, int alto) {
            this.depX = x;
            this.depY = y;
            this.depAncho = ancho;
            this.depAlto = alto;
        }

        public void paintComponent(Graphics g) {
            // Dibujar el recuadro del depósito
            g.setColor(Color.GRAY);
            g.drawRect(this.depX, this.depY, this.depAncho, this.depAlto);

            // Aquí dentro se puede iterar sobre los productos/monedas de este depósito específico
            // y llamar al paintComponent() de cada producto o moneda.
        }
    }
}