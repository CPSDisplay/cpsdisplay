package fr.dams4k.cpsdisplay.core.colorpicker.gui;

import java.awt.Graphics;

import fr.dams4k.cpsdisplay.core.colorpicker.gui.border.Border;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.border.TextFieldBorder;

public class TextField extends Label {
    private Border imageBorder;

    public TextField(float textureScale) {
        super("#ffffff");
        this.imageBorder = new TextFieldBorder(textureScale);
    }

    protected void paintComponent(Graphics g) {
        this.imageBorder.drawBorder(g, this, true);
        super.paintComponent(g);
    }
}
