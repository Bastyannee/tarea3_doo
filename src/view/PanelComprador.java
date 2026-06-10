package view;

import logica.*;
import logica.exceptions.*;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente de interfaz gráfica que representa el panel de control interactivo del comprador dentro de la simulación de la máquina expendedora.
 * Hereda de JPanel. Administra visualmente el dinero del monedero, el catálogo de opciones y las gavetas de retiro. Además, procesa las coordenadas de clics para delegar eventos de compra a las clases de lógica subyacentes.
 */
public class PanelComprador extends JPanel {

    /** Instancia del expendedor al cual se conectan las transacciones del panel. */
    private final Expendedor exp;

    /** Instancia del comprador dueño del dinero y destinatario de los productos. */
    private final Comprador  comprador;

    /** Moneda virtual actualmente retenida en memoria tras ser seleccionada para el pago. */
    private Moneda monedaEnMano = null;

    /** Índice numérico que mapea la moneda seleccionada con los arreglos de datos constantes. */
    private int idxMonedaSel = -1;

    /** Índice numérico que mapea el producto seleccionado con el catálogo disponible. */
    private int idxProductoSel = -1;

    /** Almacena la referencia del artículo comprado mientras permanece en la bandeja física de retiro. */
    private Producto productoListo = null;

    /** Colección local de monedas que emula la gaveta física donde se deposita el vuelto. */
    private final List<Moneda> cajVuelto = new ArrayList<>();

    /** Cadena de texto dinámica proyectada en tiempo real en la barra informativa inferior. */
    private String estado = "Seleccione una moneda y luego un producto.";

    /** Bandera de control que habilita un sistema de confirmación de compra mediante doble pulsación. */
    private boolean esperandoConfirmacion = false;

    /** Colección estática con el orden de los enumeradores de tipos de productos ofrecidos. */
    private static final TipoProducto[] TIPOS  = {
            TipoProducto.COCA_COLA, TipoProducto.SPRITE, TipoProducto.FANTA,
            TipoProducto.SUPER8, TipoProducto.SNICKERS
    };

    /** Nombres comerciales textuales vinculados estrictamente con el arreglo de tipos. */
    private static final String[] NOMBRES = {
            "Coca-Cola", "Sprite", "Fanta", "Super 8", "Snickers"
    };

    /** Valores enteros asociados con las monedas admitidas en el monedero. */
    private static final int[]    DENOM   = {100, 500, 1000};

    /** Etiquetas de formato monetario para proyectar en pantalla. */
    private static final String[] ETIQ_M  = {"$100", "$500", "$1.000"};

    /**
     * Recursos Gráficos del Componente
     */
    private Image imgMoneda100, imgMoneda500, imgMoneda1000;
    private Image imgCoca, imgSprite, imgFanta, imgSnickers;

    /**
     * Definición de la Paleta de Colores de la UI
     */
    private static final Color METAL_LIGHT = new Color(228, 232, 235);
    private static final Color METAL_DARK = new Color(165, 174, 182);
    private static final Color BORDE = new Color(105, 112, 118);
    private static final Color DISPLAY_BG = new Color(16, 32, 42);
    private static final Color DISPLAY_FG = new Color(0, 240, 255);
    private static final Color MSG_OK = new Color(255, 255, 255);
    private static final Color MSG_ERR = new Color(255, 80, 80);

    /** Nombre tipográfico unificado para las leyendas del panel. */
    private static final String FONT_NAME = "SansSerif";

    /**
     * Construye e inicializa un nuevo panel contenedor para la experiencia del comprador.
     * @param x Coordenada de origen horizontal respecto al contenedor padre.
     * @param y Coordenada de origen vertical respecto al contenedor padre.
     * @param ancho Ancho absoluto asignado en píxeles.
     * @param alto Alto absoluto asignado en píxeles.
     * @param exp Manejador principal de la lógica del expendedor.
     * @param comprador Instancia de entidad que opera los fondos monetarios.
     */
    public PanelComprador(int x, int y, int ancho, int alto,
                          Expendedor exp, Comprador comprador) {
        this.exp = exp;
        this.comprador = comprador;

        cargarImagenes();

        setBounds(x, y, ancho, alto);
        setOpaque(true);
        setLayout(null);
    }

    /**
     * Recupera de forma segura los mapas de bits requeridos desde la carpeta interna de recursos.
     */
    private void cargarImagenes() {
        try {
            imgMoneda100 = new ImageIcon(getClass().getResource("/resources/Moneda100.png")).getImage();
            imgMoneda500 = new ImageIcon(getClass().getResource("/resources/Moneda500.png")).getImage();
            imgMoneda1000 = new ImageIcon(getClass().getResource("/resources/Moneda1000.png")).getImage();

            imgCoca = new ImageIcon(getClass().getResource("/resources/cocacola.png")).getImage();
            imgSprite = new ImageIcon(getClass().getResource("/resources/sprite.png")).getImage();
            imgFanta = new ImageIcon(getClass().getResource("/resources/fanta.png")).getImage();
            imgSnickers = new ImageIcon(getClass().getResource("/resources/LogoSnickers.png")).getImage();
        } catch (Exception e) {
            System.err.println("Aviso al cargar recursos: " + e.getMessage());
        }
    }

    /**
     * Determina el elemento colisionado por el puntero del ratón mediante coordenadas relativas.
     * Evalúa las áreas reactivas del monedero, catálogo de botones de acción y gavetas físicas.
     * Modifica las variables de estado y solicita el redibujo completo del panel en pantalla.
     * @param lx Coordenada del eje horizontal del clic.
     * @param ly Coordenada del eje vertical del clic.
     */
    public void procesarClick(int lx, int ly) {
        int W = getWidth();
        int H = getHeight();

        int margin = (int)(W * 0.05);
        int cardW = W - (2 * margin);

        /**
         * Si estaba esperando confirmación y el usuario hace clic en otro lado, se cancela
         */
        if (esperandoConfirmacion && !(ly >= 220 && ly < 260 && lx >= margin && lx < W - margin)) {
            esperandoConfirmacion = false;
            estado = "Operación cancelada. Elige un producto.";
            repaint();
        }

        int monW = (int)(cardW * 0.28);
        int gapM = (int)(cardW * 0.04);
        int monY = 65;
        int monH = 135;

        for (int i = 0; i < 3; i++) {
            int mx = margin + gapM + i * (monW + gapM);
            if (lx >= mx && lx < mx + monW && ly >= monY && ly < monY + monH) {
                long cant = cantMoneda(DENOM[i]);
                if (cant > 0) {
                    idxMonedaSel = i;
                    monedaEnMano = crearMoneda(DENOM[i]);
                    estado = ETIQ_M[i] + " en mano. Elige un producto.";
                } else {
                    estado = "No tienes " + ETIQ_M[i] + " en tu monedero.";
                }
                repaint();
                return;
            }
        }

        int insY = 220;
        int insH = 40;
        if (ly >= insY && ly < insY + insH && lx >= margin && lx < W - margin) {
            // Manejamos el flujo del botón mediante un método dedicado
            gestionarBotonCompra();
            repaint();
            return;
        }

        int prodCardY = 385;
        int prodW = (int)(cardW * 0.28);
        int gapP = (int)(cardW * 0.04);
        int prodH = 82;

        for (int i = 0; i < 5; i++) {
            int col = i % 3;
            int fila = i / 3;
            int bx = margin + gapP + col * (prodW + gapP);
            int by = prodCardY + 45 + fila * (prodH + 12);

            if (lx >= bx && lx < bx + prodW && ly >= by && ly < by + prodH) {
                idxProductoSel = i;
                estado = NOMBRES[i] + " seleccionado. Precio: $" + TIPOS[i].getPrecio();
                repaint();
                return;
            }
        }

        int baseY = H - 145;
        int btnH1 = 38;
        int btnH2 = 38;

        if (ly >= baseY && ly < baseY + btnH1 && lx >= margin && lx < W - margin) {
            accionRecogerVuelto();
            repaint();
            return;
        }

        int prdY = baseY + btnH1 + 10;
        if (ly >= prdY && ly < prdY + btnH2 && lx >= margin && lx < W - margin) {
            accionRecogerProducto();
            repaint();
        }
    }

    /**
     * Gestiona las fases condicionales del botón de compra.
     * Valida que se cumplan las precondiciones esenciales de pago y artículo. En el primer toque activa el estado transitorio de confirmación. Si se pulsa inmediatamente después, confirma la orden disparando la inserción monetaria real.
     */
    private void gestionarBotonCompra() {
        if (monedaEnMano == null) {
            estado = "Primero selecciona una moneda o billete.";
            return;
        }
        if (idxProductoSel < 0) {
            estado = "Primero selecciona el producto a comprar.";
            return;
        }

        if (!esperandoConfirmacion) {
            esperandoConfirmacion = true;
            estado = "¿Estás seguro de comprar " + NOMBRES[idxProductoSel] + " por " + ETIQ_M[idxMonedaSel] + "?";
        } else {
            accionInsertar();
        }
    }

    /**
     * Ejecuta el proceso definitivo de compra acoplando la interfaz con las reglas de negocio.
     * Se encarga de debitar el dinero físico del comprador e ingresarlo al expendedor.
     * Implementa bloques de captura de errores para procesar excepciones lógicas como fallas de stock, monedas inválidas o saldo insuficiente, asegurando la devolución pertinente del vuelto.
     */
    private void accionInsertar() {
        Moneda mReal = comprador.descontarMoneda(DENOM[idxMonedaSel]);
        if (mReal == null) {
            estado = "No tienes fondos suficientes de " + ETIQ_M[idxMonedaSel];
            monedaEnMano = null;
            idxMonedaSel = -1;
            esperandoConfirmacion = false;
            return;
        }

        try {
            Producto p = exp.comprarProducto(mReal, TIPOS[idxProductoSel]);
            productoListo = p;
            vaciarVueltoExpendedor();
            int tv = totalVuelto();
            estado = "¡Compra OK! " + p.consumir() + (tv > 0 ? "  Vuelto: $" + tv : "") + "  Retira tu item.";
        } catch (PagoInsuficienteException e) {
            vaciarVueltoExpendedor();
            estado = "Fondos insuficientes. Retira la moneda devuelta.";
        } catch (NoHayProductoException e) {
            vaciarVueltoExpendedor();
            estado = "Sin existencias de " + NOMBRES[idxProductoSel] + ". Moneda devuelta.";
        } catch (PagoIncorrectoException e) {
            comprador.recibirMoneda(mReal);
            estado = "Error inesperado en el pago.";
        }

        monedaEnMano = null;
        idxMonedaSel = -1;
        esperandoConfirmacion = false; // Finaliza el flujo
    }

    /**
     * Retira la totalidad de las monedas depositadas en la gaveta del vuelto local y las reincorpora de manera permanente al saldo disponible del monedero del comprador.
     */
    private void accionRecogerVuelto() {
        if (cajVuelto.isEmpty()) {
            estado = "La gaveta de vuelto está vacía.";
            return;
        }
        int total = totalVuelto();
        for (Moneda m : cajVuelto) comprador.recibirMoneda(m);
        cajVuelto.clear();
        estado = "Vuelto retirado: $" + total + ".  Saldo actual: $" + comprador.getSaldo();
    }

    /**
     * Extrae el producto disponible en la bandeja intermedia de entrega física, transfiriéndolo con éxito al inventario del comprador.
     */
    private void accionRecogerProducto() {
        if (productoListo == null) {
            estado = "No hay productos en la bandeja de retiro.";
            return;
        }
        exp.getProducto();

        comprador.recibirProducto(productoListo);
        estado = productoListo.getClass().getSimpleName() + " #" + productoListo.getSerie() + " recogido con éxito.";
        productoListo = null;
        idxProductoSel = -1;
    }

    /**
     * Vacía el depósito de vuelto lógico interno de la máquina expendedora, traspasando las monedas una a una al almacenamiento temporal visible de este panel.
     */
    private void vaciarVueltoExpendedor() {
        Moneda v;
        while ((v = exp.getVuelto()) != null) cajVuelto.add(v);
    }

    /**
     * Examina el monedero del comprador filtrando y contando la cantidad de objetos moneda que igualan un valor específico.
     * @param valor Denominación entera a contabilizar.
     * @return Monto total de ocurrencias encontradas.
     */
    private long cantMoneda(int valor) {
        return comprador.getMonedas().stream().filter(m -> m.getValor() == valor).count();
    }

    /**
     * Suma acumulativa de las denominaciones numéricas de las monedas presentes en la gaveta.
     * @return Suma neta del capital total de vuelto disponible.
     */
    private int totalVuelto() {
        return cajVuelto.stream().mapToInt(Moneda::getValor).sum();
    }

    /**
     * Factoría de generación instantánea de tipos concretos de moneda basados en un entero.
     * @param valor Denominación objetivo.
     * @return Instancia concreta derivada de Moneda.
     */
    private Moneda crearMoneda(int valor) {
        switch (valor) {
            case 100: return new Moneda100();
            case 500: return new Moneda500();
            default: return new Moneda1000();
        }
    }

    /**
     * Intercepta el ciclo estándar de pintado de Swing para componer la UI a través del motor gráfico.
     * @param g Lienzo gráfico primitivo inyectado por el sistema de ventanas.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int W = getWidth();
        int H = getHeight();

        GradientPaint brushedMetal = new GradientPaint(0, 0, METAL_LIGHT, W, 0, METAL_DARK);
        g2.setPaint(brushedMetal);
        g2.fillRect(0, 0, W, H);

        g2.setColor(new Color(255, 255, 255, 35));
        for (int i = 0; i < W; i += 5) {
            g2.drawLine(i, 0, i, H);
        }

        int margin = (int)(W * 0.05);
        int cardW  = W - (2 * margin);

        pintarSeccionMonedas(g2, margin, cardW);
        pintarBotonInsertar(g2, margin, cardW);
        pintarDisplaySaldo(g2, margin, cardW);
        pintarSeccionProductos(g2, margin, cardW);
        pintarBotonesAccion(g2, margin, cardW, H);
        pintarBarraEstado(g2, W, H);
    }

    /**
     * Métodos de renderizado internos y auxiliares de dibujo
     * @param g2 El objeto de Graphics2D utilizado para pintar.
     * @param margin Margen de seguridad lateral izquierdo medido en píxeles para alinear el bloque general.
     * @param cardW Ancho total utilizable medido en píxeles asignado a la tarjeta contenedora del panel.
     */
    private void pintarSeccionMonedas(Graphics2D g2, int margin, int cardW) {
        card(g2, margin, 12, cardW, 190);

        g2.setColor(new Color(35, 42, 48));
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 12));
        g2.drawString("⬜ EFECTIVO EN MONEDERO", margin + 14, 34);

        int monW = (int)(cardW * 0.28);
        int gapM = (int)(cardW * 0.04);
        int monY = 65;

        for (int i = 0; i < 3; i++) {
            int mx = margin + gapM + i * (monW + gapM);
            boolean sel = (idxMonedaSel == i);
            long cant = cantMoneda(DENOM[i]);

            g2.setColor(new Color(36, 40, 44));
            g2.fillRoundRect(mx, monY, monW, 125, 8, 8);
            g2.setColor(new Color(18, 20, 22));
            g2.drawRoundRect(mx, monY, monW, 125, 8, 8);

            if (sel) {
                g2.setColor(DISPLAY_FG);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(mx - 1, monY - 1, monW + 2, 127, 9, 9);
                g2.setStroke(new BasicStroke(1f));
            }

            Image img = (i == 0) ? imgMoneda100 : (i == 1) ? imgMoneda500 : imgMoneda1000;
            if (img != null) {
                if (cant == 0) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                }
                if (DENOM[i] == 1000) {
                    int bW = (int)(monW * 0.82);
                    int bH = (int)(bW / 1.75);
                    g2.drawImage(img, mx + (monW - bW) / 2, monY + 22, bW, bH, null);
                } else {
                    int size = (int)(monW * 0.65);
                    g2.drawImage(img, mx + (monW - size) / 2, monY + 12, size, size, null);
                }
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }

            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font(FONT_NAME, Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(ETIQ_M[i], mx + (monW - fm.stringWidth(ETIQ_M[i])) / 2, monY + 92);

            g2.setColor(new Color(70, 78, 86));
            g2.fillRoundRect(mx + 8, monY + 102, monW - 16, 16, 4, 4);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font(FONT_NAME, Font.BOLD, 10));
            String ct = "x" + cant;
            g2.drawString(ct, mx + 8 + ((monW - 16) - g2.getFontMetrics().stringWidth(ct)) / 2, monY + 114);
        }
    }

    private void pintarBotonInsertar(Graphics2D g2, int margin, int cardW) {
        int bY = 215;
        int bH = 40;
        boolean listo = (monedaEnMano != null && idxProductoSel >= 0);

        g2.setColor(new Color(85, 92, 98));
        g2.drawRoundRect(margin - 1, bY - 1, cardW + 2, bH + 2, 8, 8);

        GradientPaint alum;
        if (esperandoConfirmacion) {
            alum = new GradientPaint(margin, bY, new Color(16, 54, 45), margin, bY + bH, new Color(34, 94, 75));
        } else if (listo) {
            alum = new GradientPaint(margin, bY, new Color(10, 26, 46), margin, bY + bH, new Color(24, 58, 96));
        } else {
            alum = new GradientPaint(margin, bY, new Color(212, 216, 220), margin, bY + bH, new Color(148, 155, 162));
        }
        g2.setPaint(alum);
        g2.fillRoundRect(margin, bY, cardW, bH, 7, 7);

        g2.setColor(new Color(255, 255, 255, 120));
        g2.drawLine(margin + 4, bY + 1, margin + cardW - 4, bY + 1);

        g2.setColor(listo ? Color.WHITE : Color.DARK_GRAY);
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 12));

        String txt;
        if (esperandoConfirmacion) {
            txt = "¿ESTÁS SEGURO DE COMPRAR " + NOMBRES[idxProductoSel].toUpperCase() + " POR " + ETIQ_M[idxMonedaSel] + "?";
        } else if (listo) {
            txt = "VAS A COMPRAR CON " + ETIQ_M[idxMonedaSel];
        } else {
            txt = "Seleccione Dinero y Artículo";
        }

        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(txt, margin + (cardW - fm.stringWidth(txt)) / 2, bY + bH / 2 + fm.getAscent() / 2 - 2);
    }

    private void pintarDisplaySaldo(Graphics2D g2, int margin, int cardW) {
        int dY = 268;
        card(g2, margin, dY, cardW, 100);

        g2.setColor(new Color(35, 42, 48));
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 12));
        g2.drawString("⬜ SALDO INTRODUCIDO", margin + 14, dY + 24);

        g2.setColor(DISPLAY_BG);
        g2.fillRoundRect(margin + 12, dY + 38, cardW - 24, 48, 6, 6);

        g2.setColor(new Color(255, 255, 255, 10));
        int[] rx = { margin + 12, margin + (int)(cardW * 0.45), margin + (int)(cardW * 0.25), margin + 12 };
        int[] ry = { dY + 38, dY + 38, dY + 86, dY + 86 };
        g2.fillPolygon(rx, ry, 4);

        g2.setColor(DISPLAY_FG);
        g2.setFont(new Font("Monospaced", Font.BOLD, 28));
        String txt = "$ " + String.format("%,d", comprador.getSaldo()).replace(",", ".");
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(txt, margin + (cardW - fm.stringWidth(txt)) / 2, dY + 72);
    }

    private void pintarSeccionProductos(Graphics2D g2, int margin, int cardW) {
        int cY = 380;
        card(g2, margin, cY, cardW, 225);

        g2.setColor(new Color(35, 42, 48));
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 12));
        g2.drawString("⬜ CATÁLOGO DE EXPENDEDOR", margin + 14, cY + 24);

        Color[] colP = {
                new Color(215, 30, 40), new Color(15, 155, 75), new Color(245, 125, 15),
                new Color(115, 40, 185), new Color(25, 95, 195)
        };

        int prodW = (int)(cardW * 0.28);
        int gapP = (int)(cardW * 0.04);
        int prodH = 82;

        for (int i = 0; i < 5; i++) {
            int col = i % 3;
            int fila = i / 3;
            int bx = margin + gapP + col * (prodW + gapP);
            int by = cY + 45 + fila * (prodH + 12);
            boolean sel = (idxProductoSel == i);

            g2.setColor(colP[i]);
            g2.fillRoundRect(bx, by, prodW, prodH, 8, 8);

            g2.setColor(new Color(255, 255, 255, 70));
            g2.drawLine(bx + 2, by + 1, bx + prodW - 2, by + 1);

            if (sel) {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawRoundRect(bx, by, prodW, prodH, 8, 8);
                g2.setStroke(new BasicStroke(1f));
            }

            Image img = (i == 0) ? imgCoca : (i == 1) ? imgSprite : (i == 2) ? imgFanta : (i == 4) ? imgSnickers : null;
            if (img != null) {
                int iw = (int)(prodW * 0.35);
                int ih = (int)(prodH * 0.45);
                g2.drawImage(img, bx + (prodW - iw) / 2, by + 8, iw, ih, null);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font(FONT_NAME, Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(NOMBRES[i], bx + (prodW - fm.stringWidth(NOMBRES[i])) / 2, by + 56);
            } else {
                g2.setColor(Color.WHITE);
                g2.setFont(new Font(FONT_NAME, Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(NOMBRES[i], bx + (prodW - fm.stringWidth(NOMBRES[i])) / 2, by + 40);
            }

            g2.setColor(new Color(255, 255, 255, 200));
            g2.setFont(new Font(FONT_NAME, Font.PLAIN, 10));
            FontMetrics fm = g2.getFontMetrics();
            String pr = "$" + TIPOS[i].getPrecio();
            g2.drawString(pr, bx + (prodW - fm.stringWidth(pr)) / 2, by + 72);
        }
    }

    private void pintarBotonesAccion(Graphics2D g2, int margin, int cardW, int H) {
        int baseY = H - 145;
        int bH = 38;

        pintarRanuraMecanica(g2, margin, baseY, cardW, bH,
                cajVuelto.isEmpty() ? "GAVETA DE VUELTO VACÍA" : "RETIRAR VUELTO ($" + totalVuelto() + ")", !cajVuelto.isEmpty());

        pintarRanuraMecanica(g2, margin, baseY + 46, cardW, bH,
                productoListo == null ? "BANDEJA DE RETIRO VACÍA" : "RETIRAR PRODUCTO: " + productoListo.getClass().getSimpleName().toUpperCase(), productoListo != null);
    }

    private void pintarRanuraMecanica(Graphics2D g2, int mx, int my, int mw, int mh, String text, boolean activo) {
        GradientPaint shadowDrop;
        if (activo) {
            shadowDrop = new GradientPaint(mx, my, new Color(15, 18, 22), mx, my + mh, new Color(42, 48, 54));
        } else {
            shadowDrop = new GradientPaint(mx, my, new Color(105, 110, 115), mx, my + mh, new Color(185, 190, 195));
        }
        g2.setPaint(shadowDrop);
        g2.fillRoundRect(mx, my, mw, mh, 6, 6);

        g2.setColor(activo ? new Color(5, 8, 12) : new Color(55, 60, 65));
        g2.drawRoundRect(mx, my, mw, mh, 6, 6);

        if (activo) {
            g2.setColor(DISPLAY_FG);
            g2.fillRect(mx + (mw - 40) / 2, my + mh - 4, 40, 2);
        }

        g2.setColor(activo ? Color.WHITE : new Color(55, 65, 70));
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, mx + (mw - fm.stringWidth(text)) / 2, my + mh / 2 + fm.getAscent() / 2 - 1);
    }

    private void pintarBarraEstado(Graphics2D g2, int W, int H) {
        int sY = H - 26;
        g2.setColor(new Color(12, 70, 100));
        g2.fillRect(0, sY, W, 26);
        g2.setColor(new Color(6, 45, 70));
        g2.drawLine(0, sY, W, sY);

        boolean err = estado.startsWith("No ") || estado.startsWith("Primero")
                || estado.contains("insuficiente") || estado.contains("Sin stock")
                || estado.contains("Error") || estado.contains("cancelada");
        g2.setColor(err ? MSG_ERR : MSG_OK);
        g2.setFont(new Font(FONT_NAME, Font.PLAIN, 11));
        g2.drawString("ℹ️ " + estado, 14, sY + 17);
    }

    private void card(Graphics2D g2, int cx, int cy, int cw, int ch) {
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillRoundRect(cx + 1, cy + 2, cw, ch, 10, 10);

        g2.setColor(new Color(244, 246, 247, 225));
        g2.fillRoundRect(cx, cy, cw, ch, 10, 10);

        g2.setColor(BORDE);
        g2.drawRoundRect(cx, cy, cw, ch, 10, 10);

        g2.setColor(new Color(195, 202, 208));
        g2.drawLine(cx + 14, cy + 32, cx + cw - 14, cy + 32);
    }
}