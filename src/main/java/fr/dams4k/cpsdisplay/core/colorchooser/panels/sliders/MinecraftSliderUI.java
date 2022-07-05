package fr.dams4k.cpsdisplay.core.colorchooser.panels.sliders;

import java.awt.Graphics;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

import fr.dams4k.cpsdisplay.core.colorchooser.borders.BorderBase;

public class MinecraftSliderUI extends BasicSliderUI {
    private BorderBase border;

    public MinecraftSliderUI(JSlider slider, BorderBase border) {
        super(slider);
        this.border = border;
    }
    
    @Override
    public void paintTrack(Graphics graphics) {
        super.paintTrack(graphics);
        border.drawBorder(graphics, slider, true);
        // Graphics2D graphics2d = (Graphics2D) graphics;

        
    }
}
