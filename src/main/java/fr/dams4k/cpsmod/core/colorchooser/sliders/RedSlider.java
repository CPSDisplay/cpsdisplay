package fr.dams4k.cpsmod.core.colorchooser.sliders;

import javax.swing.event.ChangeEvent;

public class RedSlider extends BaseSlider {
    public int R = 0;

    public RedSlider(String label_name) {
        super(label_name);
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        super.stateChanged(event);

        R = slider.getValue();

        for (SliderListener listener : listeners) {
            listener.RColorChange(R);
        }
    }
    
}
