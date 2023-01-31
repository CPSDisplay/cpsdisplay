package fr.dams4k.cpsdisplay.core.colorpicker;

import java.awt.Color;

import fr.dams4k.cpsdisplay.core.colorpicker.border.InventoryBorder;
import fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.ImagePanel;

public class ColorPreview extends ImagePanel {
    private Color color = Color.WHITE;
    public ColorPreview(Color color) {
        this.color = color;
        this.setImageBorder(new InventoryBorder(4f));
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
        this.setBackground(this.color);
    }
}
