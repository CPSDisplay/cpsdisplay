package fr.dams4k.cpsdisplay.core.colorpicker.gui.imagepanel.pointer;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import fr.dams4k.cpsdisplay.core.colorpicker.ColorPickerImages;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.imagepanel.ImageType;

public class SVPointerPanel extends PointerPanel {
    protected List<SVPointerListener> listeners = new ArrayList<>();

    public static int sizeX = 280;
    public static int sizeY = 280;

    public SVPointerPanel() {
        super(ColorPickerImages.svColorSelector(0f, sizeX, sizeY), ImageType.NORMAL, 1f, true, true);
    }

    private void callListeners() {
        for (SVPointerListener listener : this.listeners) {
            listener.SColorChanged(this.getPointerX());
            listener.VColorChanged(this.getPointerY());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        this.callListeners();
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        this.callListeners();
    }

    public void addListener(SVPointerListener listener) {
        this.listeners.add(listener);
    }
}
