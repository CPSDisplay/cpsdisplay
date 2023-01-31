package fr.dams4k.cpsdisplay.core_last.colorchooser.panels.sliders;

import java.awt.Graphics;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

import fr.dams4k.cpsdisplay.core_last.colorchooser.borders.BorderBase;

public class MinecraftSliderUI extends BasicSliderUI {
    private BorderBase border;
    private BorderBase thumbBorder;

    public MinecraftSliderUI(JSlider slider, BorderBase border, BorderBase thumbBorder) {
        super(slider);
        this.border = border;
        this.thumbBorder = thumbBorder;
    }
    
    @Override
    public void paintTrack(Graphics g) {
        border.drawBorder(g, slider, true);
    }

    @Override
    public void paintThumb(Graphics g) {
        thumbBorder.drawBorder(g, slider, true);
    }
}
