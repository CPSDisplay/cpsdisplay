package fr.dams4k.cpsdisplay.core.colorpicker;

import java.awt.Color;

public interface ColorPickerListener {
    void closed();
    void newColor(Color color);
}
