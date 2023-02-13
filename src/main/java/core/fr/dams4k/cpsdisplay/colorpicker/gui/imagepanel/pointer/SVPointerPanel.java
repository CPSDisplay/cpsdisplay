package fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.pointer;

import java.util.ArrayList;
import java.util.List;

import fr.dams4k.cpsdisplay.colorpicker.ColorPickerImages;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.ImageType;

public class SVPointerPanel extends PointerPanel {
    protected List<SVPointerListener> listeners = new ArrayList<>();

    public static int sizeX = 280;
    public static int sizeY = 280;

    public SVPointerPanel() {
        super(ColorPickerImages.svColorSelector(0f, sizeX, sizeY), ImageType.NORMAL, 1f, true, true);
    }


    @Override
    protected void callChangingListeners() {
        for (SVPointerListener listener : this.listeners) {
            listener.SColorChanging(this.getPointerX());
            listener.VColorChanging(this.getPointerY());
        }
    }
    @Override
    protected void callChangedListeners() {
        for (SVPointerListener listener : this.listeners) {
            listener.SColorChanged(this.getPointerX());
            listener.VColorChanged(this.getPointerY());
        }
    }

    public void addListener(SVPointerListener listener) {
        this.listeners.add(listener);
    }
}
