package fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.pointer;

import java.awt.Graphics;

import fr.dams4k.cpsdisplay.core.colorpicker.Label;
import fr.dams4k.cpsdisplay.core.colorpicker.border.Border;
import fr.dams4k.cpsdisplay.core.colorpicker.border.ButtonBorder;
import fr.dams4k.cpsdisplay.core.colorpicker.border.ButtonMode;

public class Button extends Label {
    private Border imageBorder;

    public Button(String string, float textureSize) {
        super(string);
        this.imageBorder = new ButtonBorder(textureSize, ButtonMode.NORMAL);
    }

    @Override
    protected void paintComponent(Graphics g) {
        imageBorder.drawBorder(g, this, true);
        super.paintComponent(g);
    }
}
