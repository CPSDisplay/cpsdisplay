package fr.dams4k.cpsdisplay.colorpicker;

import java.awt.Color;

public interface ColorPickerListener {
    void closed();
    void newColor(Color color);
}
