package fr.dams4k.cpsdisplay.core.colorchooser_last.sliders;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BaseSlider extends JPanel implements ChangeListener {
    public List<SliderListener> listeners = new ArrayList<SliderListener>();

    public JLabel label;
    public JSlider slider;

    public BaseSlider(String label_name) {
        label = new JLabel(label_name);
        slider = new JSlider(0, 255, 0);
        
        slider.addChangeListener(this);

        add(label);
        add(slider);
    }

    public void addListener(SliderListener newListener) {
        listeners.add(newListener);
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        System.out.println(slider.getValue());
    }
}
