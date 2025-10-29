

package Simulation;

import Shapes.*;
import javax.swing.*;
import java.awt.*;
import java.awt.GraphicsEnvironment;

/**
 * ProfitBar: barra de progreso visual que muestra permanentemente
 * la ganancia acumulada del día.
 *
 * Cumple con el requisito:
 *  - La ganancia se muestra como una barra de progreso
 *  - El máximo representa la ganancia máxima posible
 *  - No muestra números ni texto adicional
 */
public class ProfitBar {
    private final JProgressBar bar;
    private final JFrame frame;
    private int maxProfit;
    private int currentProfit;
    private final boolean uiEnabled;

    public ProfitBar(int maxProfit) {
        this.maxProfit = maxProfit;
        this.currentProfit = 0;
        this.uiEnabled = !GraphicsEnvironment.isHeadless();

        bar = new JProgressBar(0, maxProfit);
        bar.setValue(0);
        bar.setStringPainted(false); // sin texto

        if (uiEnabled) {
            bar.setForeground(new Color(40, 180, 40));
            bar.setBackground(new Color(230, 230, 230));
            bar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));

            frame = new JFrame("Ganancias del día");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setUndecorated(true);
            frame.setAlwaysOnTop(true);
            frame.setSize(400, 20);
            frame.setLayout(new BorderLayout());
            frame.add(bar, BorderLayout.CENTER);

            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setLocation(screen.width - 420, screen.height - 80);
            frame.setVisible(true);
        } else {
            frame = null;
        }
    }

    /** Actualiza la ganancia mostrada */
    public void updateProfit(int profit) {
        this.currentProfit = Math.min(profit, maxProfit);
        bar.setValue(this.currentProfit);
    }

    /** Cambia el máximo de ganancias posibles */
    public void setMaxProfit(int maxProfit) {
        this.maxProfit = maxProfit;
        bar.setMaximum(maxProfit);
        updateProfit(currentProfit);
    }

    /** Resetea la barra de progreso a cero */
    public void reset() {
        this.currentProfit = 0;
        bar.setValue(0);
    }

    /** Cierra la barra si ya no es necesaria */
    public void close() {
        if (uiEnabled && frame != null) frame.dispose();
    }
}
