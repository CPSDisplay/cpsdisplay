package fr.dams4k.cpsdisplay.core.colorchooser.panels.sliders;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JSlider;

import fr.dams4k.cpsdisplay.core.colorchooser.borders.BorderBase;
import fr.dams4k.cpsdisplay.core.colorchooser.borders.buttonborder.ButtonBorder;
import fr.dams4k.cpsdisplay.core.colorchooser.borders.buttonborder.ButtonMode;

public class MinecraftSlider extends JSlider {
    public MinecraftSlider() {
        setBackground(new Color(0f, 0f, 0f, 0f));

        BorderBase border = new ButtonBorder(4, ButtonMode.BACKGROUND, new Insets(20, 0, 20, 0));
        BorderBase thumbBorder = new ButtonBorder(4, ButtonMode.NORMAL, new Insets(0, 100, 0, 100));

        setUI(new MinecraftSliderUI(this, border, thumbBorder));
    }
}
