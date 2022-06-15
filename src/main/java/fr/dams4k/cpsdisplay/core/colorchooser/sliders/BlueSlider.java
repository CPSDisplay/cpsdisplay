package fr.dams4k.cpsdisplay.core.colorchooser.sliders;

import javax.swing.event.ChangeEvent;

public class BlueSlider extends BaseSlider {
    public int B = 0;

    public BlueSlider(String label_name) {
        super(label_name);
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        super.stateChanged(event);

        B = slider.getValue();

        for (SliderListener listener : listeners) {
            listener.RColorChange(B);
        }
    }
    
}
