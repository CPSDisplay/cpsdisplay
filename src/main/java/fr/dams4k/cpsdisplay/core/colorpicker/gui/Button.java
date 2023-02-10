package fr.dams4k.cpsdisplay.core.colorpicker.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.MouseInputListener;

import fr.dams4k.cpsdisplay.core.colorpicker.gui.border.Border;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.border.ButtonBorder;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.border.ButtonMode;
import fr.dams4k.cpsdisplay.v1_8.config.ModConfig;

public class Button extends Label implements MouseInputListener {
    private List<ButtonListener> buttonListeners = new ArrayList<>();

    private float borderTextureScale;
    private Border imageBorder;

    private boolean hovered = false;

    public Button(String text, float textureScale) {
        super(text);
        this.borderTextureScale = textureScale;
        this.imageBorder = new ButtonBorder(this.borderTextureScale, ButtonMode.NORMAL);

        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        imageBorder.drawBorder(g, this, true);
        
        Color color = this.hovered ? ModConfig.HexToColor("FFFFA0", 6) : ModConfig.HexToColor("E0E0E0", 6);
        Color shadowColor = this.hovered ? ModConfig.HexToColor("3F3F28", 6) : ModConfig.HexToColor("383838", 6);

        this.paintCenteredString(g, this.text, this.getWidth()/2 + (int) this.fontSize, this.getHeight()/2 + (int) this.fontSize, shadowColor);
        this.paintCenteredString(g, this.text, this.getWidth()/2, this.getHeight()/2, color);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (ButtonListener listener : this.buttonListeners) {
            listener.buttonClicked();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.hovered = true;
        this.imageBorder = new ButtonBorder(this.borderTextureScale, ButtonMode.HOVERED);
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.hovered = false;
        this.imageBorder = new ButtonBorder(this.borderTextureScale, ButtonMode.NORMAL);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    public void addButtonListener(ButtonListener listener) {
        this.buttonListeners.add(listener);
    }
}
