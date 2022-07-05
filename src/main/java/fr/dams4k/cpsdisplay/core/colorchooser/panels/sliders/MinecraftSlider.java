package fr.dams4k.cpsdisplay.core.colorchooser.panels.sliders;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JSlider;

import fr.dams4k.cpsdisplay.core.colorchooser.borders.buttonborder.ButtonBorder;
import fr.dams4k.cpsdisplay.core.colorchooser.borders.buttonborder.ButtonMode;

public class MinecraftSlider extends JSlider {
    public MinecraftSlider() {
        // setOpaque(true);
        setBackground(new Color(0, 0, 0, 0));
        setUI(new MinecraftSliderUI(this, new ButtonBorder(4, ButtonMode.BACKGROUND, new Insets(10, 10, 10, 10))));
    }
}
