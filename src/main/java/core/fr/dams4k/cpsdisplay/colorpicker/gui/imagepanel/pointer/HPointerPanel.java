package fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.pointer;

import java.util.ArrayList;
import java.util.List;

import fr.dams4k.cpsdisplay.colorpicker.ColorPickerImages;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.ImageType;

public class HPointerPanel extends PointerPanel {
    public List<HPointerListener> listeners = new ArrayList<>();

    protected static int sizeX = 1;
    protected static int sizeY = 360;

    public HPointerPanel() {
        super(ColorPickerImages.hColorSelector(sizeX, sizeY), ImageType.STRETCHING, 1f, false, true);
    }
    
    @Override
    protected void callChangingListeners() {
        for (HPointerListener listener : this.listeners) {
            listener.HColorChanging(this.getPointerY());
        }
    }

    @Override
    protected void callChangedListeners() {
        for (HPointerListener listener : this.listeners) {
            listener.HColorChanged(this.getPointerY());
        }
    }
    public void addListener(HPointerListener listener) {
        this.listeners.add(listener);
    }
}
