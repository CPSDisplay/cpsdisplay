package fr.dams4k.cpsmod.core.colorchooser.sliders;

import javax.swing.event.ChangeEvent;

public class GreenSlider extends BaseSlider {
    public int G = 0;

    public GreenSlider(String label_name) {
        super(label_name);
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        super.stateChanged(event);

        G = slider.getValue();

        for (SliderListener listener : listeners) {
            listener.RColorChange(G);
        }
    }
    
}
