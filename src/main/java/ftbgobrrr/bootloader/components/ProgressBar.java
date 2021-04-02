package ftbgobrrr.bootloader.components;

import javax.swing.*;
import java.awt.*;

public class ProgressBar extends JPanel {

    public double value;

    public ProgressBar()
    {
        this.setLayout(null);
        this.setOpaque(false);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double v) {
        value = v;
    }

    protected void paintComponent(Graphics g) {
        double barRectWidth = (this.getWidth() * this.getValue()) / 100;

        g.setColor(getBackground());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        int end = (int) barRectWidth;
        g.setColor(getForeground());
        g.fillRect(0, 0, end, this.getHeight());

        repaint();
    }
}
