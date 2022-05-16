package fr.dams4k.cpsmod.core.colorchooser.sliders;

import java.awt.CardLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SlidersContainer extends JPanel {
    public SlidersContainer() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        BaseSlider redSlider = new BaseSlider("red :");
        BaseSlider greenSlider = new BaseSlider("green :");
        BaseSlider blueSlider = new BaseSlider("blue :");

        add(redSlider);
        add(greenSlider);
        add(blueSlider);
    }
}
