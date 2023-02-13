package fr.dams4k.cpsdisplay.colorpicker.gui;

import java.awt.Color;
import java.awt.Graphics;

import fr.dams4k.cpsdisplay.colorpicker.gui.border.InventoryBorder;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.ImagePanel;

public class ColorPreview extends ImagePanel {
    private Color color = Color.WHITE;
    public ColorPreview(Color color, float textureScale) {
        this.color = color;
        this.setImageBorder(new InventoryBorder(textureScale));
        this.setOpaque(false);
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(this.color);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        super.paintComponent(g);
    }
}
