package sk.drakkar.oar.gui.swing;

import sk.drakkar.oar.ColorGenerator;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

public class ArticleColorIcon implements Icon {
    public static final int DEFAULT_DIMENSION = 16;

    private ColorGenerator.Color color;

    private int width = DEFAULT_DIMENSION;

    private int height = DEFAULT_DIMENSION;

    public ArticleColorIcon(ColorGenerator.Color color) {
        this.color = color;
    }

    public ArticleColorIcon(ColorGenerator.Color color, int width, int height) {
        this.color = color;
        this.width = width;
        this.height = height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(Color.decode(color.getHexColor()));
        g.fillRect(x, y, getIconWidth(), getIconHeight());
    }

    @Override
    public int getIconWidth() {
        return this.width;
    }

    @Override
    public int getIconHeight() {
        return this.height;
    }
}
