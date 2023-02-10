package fr.dams4k.cpsdisplay.core.colorpicker.gui.imagepanel.pointer;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import fr.dams4k.cpsdisplay.core.colorpicker.ColorPickerImages;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.imagepanel.ImageType;

public class HPointerPanel extends PointerPanel {
    public List<HPointerListener> listeners = new ArrayList<>();

    protected static int sizeX = 1;
    protected static int sizeY = 360;

    public HPointerPanel() {
        super(ColorPickerImages.hColorSelector(sizeX, sizeY), ImageType.STRETCHING, 1f, false, true);
    }
    
    

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        callListeners();
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        callListeners();
    }
    
    private void callListeners() {
        for (HPointerListener listener : this.listeners) {
            listener.HColorChanged(this.getPointerY());
        }
    }
    public void addListener(HPointerListener listener) {
        this.listeners.add(listener);
    }
}
