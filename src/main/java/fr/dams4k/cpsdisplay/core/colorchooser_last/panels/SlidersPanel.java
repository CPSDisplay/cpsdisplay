package fr.dams4k.cpsdisplay.core.colorchooser_last.panels;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fr.dams4k.cpsdisplay.core.colorchooser_last.sliders.BlueSlider;
import fr.dams4k.cpsdisplay.core.colorchooser_last.sliders.GreenSlider;
import fr.dams4k.cpsdisplay.core.colorchooser_last.sliders.RedSlider;

public class SlidersPanel extends JPanel {
    public RedSlider redSlider;
    public GreenSlider greenSlider;
    public BlueSlider blueSlider;

    public SlidersPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        redSlider = new RedSlider("red :");
        greenSlider = new GreenSlider("green :");
        blueSlider = new BlueSlider("blue :");

        add(redSlider);
        add(greenSlider);
        add(blueSlider);
    }
}
