package view;

import logica.*;
import logica.exceptions.*;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista/Controlador del Comprador Adaptativo (50% de la pantalla).
 * Coordenadas y tamaños 100% dinámicos basados en proporciones de la ventana.
 *
 * @author Grupo
 * @version 3.0
 */
public class PanelComprador extends JPanel {

    // ── Modelos ───────────────────────────────────────────────────────────────
    private final Expendedor exp;
    private final Comprador  comprador;

    // ── Estado de la transacción ──────────────────────────────────────────────
    private Moneda monedaEnMano = null;
    private int idxMonedaSel = -1;
    private int idxProductoSel = -1;
    private Producto productoListo = null;
    private final List<Moneda> cajVuelto = new ArrayList<>();
    private String estado = "Seleccione una moneda y luego un producto.";

    // ── Catálogo (sincronizado con TipoProducto) ──────────────────────────────
    private static final TipoProducto[] TIPOS  = {
            TipoProducto.COCA_COLA, TipoProducto.SPRITE, TipoProducto.FANTA,
            TipoProducto.SUPER8, TipoProducto.SNICKERS
    };
    private static final String[] NOMBRES = {
            "Coca-Cola", "Sprite", "Fanta", "Super 8", "Snickers"
    };
    private static final int[]    DENOM   = {100, 500, 1000};
    private static final String[] ETIQ_M  = {"$100", "$500", "$1.000"};

    // ── Imágenes de Dinero ───────────────────────────────────────────────────
    private Image imgMoneda100, imgMoneda500, imgMoneda1000;

    // ── Imágenes de Productos ────────────────────────────────────────────────
    private Image imgCoca, imgSprite, imgFanta, imgSnickers;

    // ── Paleta de Colores Moderna y Limpia ───────────────────────────────────
    private static final Color BG_PREMIUM  = new Color(244, 247, 251); // Fondo sutil azulado
    private static final Color CARD        = Color.WHITE;
    private static final Color BORDE       = new Color(205, 215, 230);
    private static final Color AZUL_TEXTO  = new Color(18, 38, 75);
    private static final Color DISPLAY_BG  = new Color(12, 22, 45);
    private static final Color DISPLAY_FG  = new Color(0, 210, 255);
    private static final Color BTN_INS     = new Color(30, 95, 215);
    private static final Color BTN_VUE_SI  = new Color(34, 160, 75);
    private static final Color BTN_VUE_NO  = new Color(185, 195, 185);
    private static final Color BTN_PRD_SI  = new Color(220, 90, 25);
    private static final Color BTN_PRD_NO  = new Color(200, 200, 200);
    private static final Color MSG_OK      = new Color(20, 130, 70);
    private static final Color MSG_ERR     = new Color(190, 35, 35);

    // Fuentes estilizadas y seguras en todas las plataformas
    private static final String FONT_NAME = "Tahoma";

    public PanelComprador(int x, int y, int ancho, int alto,
                          Expendedor exp, Comprador comprador) {
        this.exp       = exp;
        this.comprador = comprador;

        cargarImagenes();

        setBounds(x, y, ancho, alto);
        setOpaque(true);
        setLayout(null);
    }

    private void cargarImagenes() {
        try {
            imgMoneda100  = new ImageIcon(getClass().getResource("/resources/Moneda100.png")).getImage();
            imgMoneda500  = new ImageIcon(getClass().getResource("/resources/Moneda500.png")).getImage();
            imgMoneda1000 = new ImageIcon(getClass().getResource("/resources/Moneda1000.png")).getImage();

            imgCoca       = new ImageIcon(getClass().getResource("/resources/cocacola.png")).getImage();
            imgSprite     = new ImageIcon(getClass().getResource("/resources/sprite.png")).getImage();
            imgFanta      = new ImageIcon(getClass().getResource("/resources/fanta.png")).getImage();
            imgSnickers   = new ImageIcon(getClass().getResource("/resources/LogoSnickers.png")).getImage();
        } catch (Exception e) {
            System.err.println("Aviso al cargar recursos: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // API Pública de Eventos (Cálculos Dinámicos de Click basados en Porcentajes)
    // ══════════════════════════════════════════════════════════════════════════

    public void procesarClick(int lx, int ly) {
        int W = getWidth();
        int H = getHeight();

        int margin = (int)(W * 0.05);
        int cardW  = W - (2 * margin);

        // 1. Detección en sección Monedas / Billetes
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

        // 2. Botón Insertar
        int insY = 220;
        int insH = 40;
        if (ly >= insY && ly < insY + insH && lx >= margin && lx < W - margin) {
            accionInsertar();
            repaint();
            return;
        }

        // 3. Selección de Productos (Grilla Dinámica de 3 columnas)
        int prodCardY = 385;
        int prodW = (int)(cardW * 0.28);
        int gapP = (int)(cardW * 0.04);
        int prodH = 75;

        for (int i = 0; i < 5; i++) {
            int col  = i % 3;
            int fila = i / 3;
            int bx   = margin + gapP + col * (prodW + gapP);
            int by   = prodCardY + 45 + fila * (prodH + 12);

            if (lx >= bx && lx < bx + prodW && ly >= by && ly < by + prodH) {
                idxProductoSel = i;
                estado = NOMBRES[i] + " seleccionado. Precio: $" + TIPOS[i].getPrecio();
                repaint();
                return;
            }
        }

        // 4. Botones inferiores de acción
        int baseY = H - 120;
        int btnH1 = 38;
        int btnH2 = 34;

        if (ly >= baseY && ly < baseY + btnH1 && lx >= margin && lx < W - margin) {
            accionRecogerVuelto();
            repaint();
            return;
        }

        int prdY = baseY + btnH1 + 8;
        if (ly >= prdY && ly < prdY + btnH2 && lx >= margin && lx < W - margin) {
            accionRecogerProducto();
            repaint();
        }
    }

    private void accionInsertar() {
        if (monedaEnMano == null) {
            estado = "Primero selecciona una moneda o billete.";
            return;
        }
        if (idxProductoSel < 0) {
            estado = "Primero selecciona el producto a comprar.";
            return;
        }

        Moneda mReal = comprador.descontarMoneda(DENOM[idxMonedaSel]);
        if (mReal == null) {
            estado = "No tienes fondos suficientes de " + ETIQ_M[idxMonedaSel];
            monedaEnMano = null;
            idxMonedaSel = -1;
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
    }

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

    private void accionRecogerProducto() {
        if (productoListo == null) {
            estado = "No hay productos en la bandeja de retiro.";
            return;
        }
        comprador.recibirProducto(productoListo);
        estado = productoListo.getClass().getSimpleName() + " #" + productoListo.getSerie() + " recogido con éxito.";
        productoListo   = null;
        idxProductoSel  = -1;
    }

    private void vaciarVueltoExpendedor() {
        Moneda v;
        while ((v = exp.getVuelto()) != null) cajVuelto.add(v);
    }

    private long cantMoneda(int valor) {
        return comprador.getMonedas().stream().filter(m -> m.getValor() == valor).count();
    }

    private int totalVuelto() {
        return cajVuelto.stream().mapToInt(Moneda::getValor).sum();
    }

    private Moneda crearMoneda(int valor) {
        switch (valor) {
            case 100:  return new Moneda100();
            case 500:  return new Moneda500();
            default:   return new Moneda1000();
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Renderizado Proporcional y Dinámico
    // ══════════════════════════════════════════════════════════════════════════

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int W = getWidth();
        int H = getHeight();

        // Fondo limpio y moderno
        g2.setColor(BG_PREMIUM);
        g2.fillRect(0, 0, W, H);
        g2.setColor(BORDE);
        g2.drawLine(W - 1, 0, W - 1, H);

        // Variables de layout dinámico para las tarjetas contenedoras
        int margin = (int)(W * 0.05);
        int cardW  = W - (2 * margin);

        pintarSeccionMonedas(g2, margin, cardW);
        pintarBotonInsertar(g2, margin, cardW);
        pintarDisplaySaldo(g2, margin, cardW);
        pintarSeccionProductos(g2, margin, cardW);
        pintarBotonesAccion(g2, margin, cardW, H);
        pintarBarraEstado(g2, W, H);
    }

    private void pintarSeccionMonedas(Graphics2D g2, int margin, int cardW) {
        card(g2, margin, 10, cardW, 195);

        g2.setColor(AZUL_TEXTO);
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 13));
        g2.drawString("MONEDERO", margin + 12, 32);
        g2.setColor(BORDE);
        g2.drawLine(margin + 12, 40, margin + cardW - 12, 40);

        int monW = (int)(cardW * 0.28);
        int gapM = (int)(cardW * 0.04);
        int monY = 65;
        int monH = 125;

        for (int i = 0; i < 3; i++) {
            int mx = margin + gapM + i * (monW + gapM);
            boolean sel  = (idxMonedaSel == i);
            long    cant = cantMoneda(DENOM[i]);
            boolean tiene = cant > 0;

            g2.setColor(sel ? new Color(225, 238, 255) : (tiene ? CARD : new Color(242, 244, 247)));
            g2.fillRoundRect(mx, monY, monW, monH, 10, 10);
            g2.setColor(sel ? BTN_INS : BORDE);
            g2.setStroke(sel ? new BasicStroke(2f) : new BasicStroke(1f));
            g2.drawRoundRect(mx, monY, monW, monH, 10, 10);
            g2.setStroke(new BasicStroke(1f));

            // Proporciones perfectas para evitar estiramiento o aplastamiento
            Image img = (i == 0) ? imgMoneda100 : (i == 1) ? imgMoneda500 : imgMoneda1000;
            if (img != null) {
                if (!tiene) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));

                if (DENOM[i] == 1000) {
                    // Billete de 1000: Mantener aspecto rectangular real (w:h es aprox 1.8:1)
                    int bW = (int)(monW * 0.85);
                    int bH = (int)(bW / 1.75);
                    g2.drawImage(img, mx + (monW - bW) / 2, monY + 18, bW, bH, null);
                } else {
                    // Monedas: Círculos perfectos (Ancho y alto idénticos)
                    int size = (int)(monW * 0.65);
                    g2.drawImage(img, mx + (monW - size) / 2, monY + 10, size, size, null);
                }
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }

            g2.setColor(tiene ? Color.DARK_GRAY : Color.LIGHT_GRAY);
            g2.setFont(new Font(FONT_NAME, Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(ETIQ_M[i], mx + (monW - fm.stringWidth(ETIQ_M[i])) / 2, monY + 88);

            g2.setColor(tiene ? new Color(70, 80, 95) : Color.LIGHT_GRAY);
            g2.fillRoundRect(mx + 10, monY + 98, monW - 20, 18, 6, 6);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font(FONT_NAME, Font.BOLD, 10));
            fm = g2.getFontMetrics();
            String ct = "x" + cant;
            g2.drawString(ct, mx + 10 + ((monW - 20) - fm.stringWidth(ct)) / 2, monY + 111);
        }
    }

    private void pintarBotonInsertar(Graphics2D g2, int margin, int cardW) {
        int insY = 220;
        int insH = 40;
        boolean listo = (monedaEnMano != null && idxProductoSel >= 0);

        g2.setColor(listo ? BTN_INS : new Color(135, 150, 175));
        g2.fillRoundRect(margin, insY, cardW, insH, 8, 8);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 12));
        String txt = listo ? "INGRESAR " + ETIQ_M[idxMonedaSel] + " E INICIAR COMPRA" : "Seleccione Dinero y Artículo";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(txt, margin + (cardW - fm.stringWidth(txt)) / 2, insY + insH / 2 + fm.getAscent() / 2 - 2);
    }

    private void pintarDisplaySaldo(Graphics2D g2, int margin, int cardW) {
        int dY = 275;
        card(g2, margin, dY, cardW, 95);

        g2.setColor(AZUL_TEXTO);
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 12));
        g2.drawString("SALDO", margin + 12, dY + 22);
        g2.setColor(BORDE);
        g2.drawLine(margin + 12, dY + 28, margin + cardW - 12, dY + 28);

        g2.setColor(DISPLAY_BG);
        g2.fillRoundRect(margin + 12, dY + 36, cardW - 24, 46, 6, 6);
        g2.setColor(DISPLAY_FG);
        g2.setFont(new Font("Monospaced", Font.BOLD, 26));
        String txt = "$ " + String.format("%,d", comprador.getSaldo()).replace(",", ".");
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(txt, margin + (cardW - fm.stringWidth(txt)) / 2, dY + 68);
    }

    private void pintarSeccionProductos(Graphics2D g2, int margin, int cardW) {
        int prodCardY = 385;
        card(g2, margin, prodCardY, cardW, 215);

        g2.setColor(AZUL_TEXTO);
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 13));
        g2.drawString("PRODUCTOS DISPONIBLES", margin + 12, prodCardY + 24);
        g2.setColor(BORDE);
        g2.drawLine(margin + 12, prodCardY + 32, margin + cardW - 12, prodCardY + 32);

        Color[] coloresProd = {
                new Color(225, 45, 45), new Color(15, 165, 80), new Color(240, 140, 15),
                new Color(130, 50, 210), new Color(40, 110, 210)
        };

        int prodW = (int)(cardW * 0.28);
        int gapP = (int)(cardW * 0.04);
        int prodH = 75;

        for (int i = 0; i < 5; i++) {
            int col  = i % 3;
            int fila = i / 3;
            int bx   = margin + gapP + col * (prodW + gapP);
            int by   = prodCardY + 45 + fila * (prodH + 12);
            boolean sel = (idxProductoSel == i);

            g2.setColor(sel ? coloresProd[i] : coloresProd[i].darker());
            g2.fillRoundRect(bx, by, prodW, prodH, 8, 8);

            if (sel) {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawRoundRect(bx, by, prodW, prodH, 8, 8);
                g2.setStroke(new BasicStroke(1f));
            }

            Image img = (i == 0) ? imgCoca : (i == 1) ? imgSprite : (i == 2) ? imgFanta : (i == 4) ? imgSnickers : null;

            if (img != null) {
                int imgW = (int)(prodW * 0.32);
                int imgH = (int)(prodH * 0.48);
                g2.drawImage(img, bx + (prodW - imgW) / 2, by + 6, imgW, imgH, null);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font(FONT_NAME, Font.BOLD, 10));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(NOMBRES[i], bx + (prodW - fm.stringWidth(NOMBRES[i])) / 2, by + 52);
            } else {
                // Texto alternativo centrado para el Súper 8 u otros dulces sin asset
                g2.setColor(Color.WHITE);
                g2.setFont(new Font(FONT_NAME, Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(NOMBRES[i], bx + (prodW - fm.stringWidth(NOMBRES[i])) / 2, by + 30);
            }

            // Precio abajo de todo de forma consistente
            g2.setColor(new Color(255, 255, 255, 220));
            g2.setFont(new Font(FONT_NAME, Font.PLAIN, 9));
            FontMetrics fm = g2.getFontMetrics();
            String p = "$" + TIPOS[i].getPrecio();
            g2.drawString(p, bx + (prodW - fm.stringWidth(p)) / 2, by + 66);
        }
    }

    private void pintarBotonesAccion(Graphics2D g2, int margin, int cardW, int H) {
        int baseY = H - 120;
        int btnH1 = 38;
        int btnH2 = 34;
        int tv    = totalVuelto();

        g2.setColor(tv > 0 ? BTN_VUE_SI : BTN_VUE_NO);
        g2.fillRoundRect(margin, baseY, cardW, btnH1, 8, 8);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 12));
        String txtV = tv > 0 ? "RETIRAR VUELTO DE ($" + tv + ")" : "GAVETA DE VUELTO VACÍA";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(txtV, margin + (cardW - fm.stringWidth(txtV)) / 2, baseY + btnH1 / 2 + fm.getAscent() / 2 - 2);

        int prdY = baseY + btnH1 + 8;
        boolean hayProd = productoListo != null;
        g2.setColor(hayProd ? BTN_PRD_SI : BTN_PRD_NO);
        g2.fillRoundRect(margin, prdY, cardW, btnH2, 8, 8);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 11));
        String txtP = hayProd ? "RETIRAR PRODUCTO: " + productoListo.getClass().getSimpleName().toUpperCase() : "BANDEJA DE RETIRO VACÍA";
        fm = g2.getFontMetrics();
        g2.drawString(txtP, margin + (cardW - fm.stringWidth(txtP)) / 2, prdY + btnH2 / 2 + fm.getAscent() / 2 - 2);
    }

    private void pintarBarraEstado(Graphics2D g2, int W, int H) {
        int sy = H - 24;
        g2.setColor(new Color(230, 235, 245));
        g2.fillRect(0, sy, W, 24);
        g2.setColor(BORDE);
        g2.drawLine(0, sy, W, sy);

        boolean err = estado.startsWith("No ") || estado.startsWith("Primero")
                || estado.contains("insuficiente") || estado.contains("Sin stock")
                || estado.contains("Error");
        g2.setColor(err ? MSG_ERR : MSG_OK);
        g2.setFont(new Font(FONT_NAME, Font.PLAIN, 11));
        g2.drawString("ℹ️ " + estado, 12, sy + 16);
    }

    private void card(Graphics2D g2, int cx, int cy, int cw, int ch) {
        g2.setColor(new Color(0, 0, 0, 12));
        g2.fillRoundRect(cx + 1, cy + 3, cw, ch, 12, 12);
        g2.setColor(CARD);
        g2.fillRoundRect(cx, cy, cw, ch, 12, 12);
        g2.setColor(BORDE);
        g2.drawRoundRect(cx, cy, cw, ch, 12, 12);
    }
}