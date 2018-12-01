package auleria.bootloader.components;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Image img;
    private int sizeX;
    private int sizeY;

    public ImagePanel(String img, int sizeX, int sizeY) {
        this(new ImageIcon(ImagePanel.class.getResource(img)).getImage(), sizeX, sizeY);
    }

    public ImagePanel(Image img, int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
        setBackground(new Color(255, 255, 255, 0));
    }

    public void paintComponent(Graphics g) {

        g.drawImage(this.img, 0, 0, this.sizeX, this.sizeY, this);
        super.paintComponent(g);
    }
}

