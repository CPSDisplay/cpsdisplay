package fr.dams4k.cpsmod.core.colorchooser.sliders;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class BaseSlider extends JPanel {
    public JLabel label;
    public JSlider slider;

    public BaseSlider(String label_name) {
        this.label = new JLabel(label_name);
        this.slider = new JSlider(0, 255, 0);

        add(label);
        add(slider);
    }
}
