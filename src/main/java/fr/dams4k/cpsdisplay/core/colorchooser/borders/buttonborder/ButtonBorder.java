package fr.dams4k.cpsdisplay.core.colorchooser.borders.buttonborder;

import java.awt.Insets;

import fr.dams4k.cpsdisplay.core.colorchooser.borders.BorderBase;
import fr.dams4k.cpsdisplay.core.colorchooser.borders.BordersType;

public class ButtonBorder extends BorderBase {
    private int buttonHeight = 20;

    public ButtonBorder(float scale, ButtonMode buttonMode) {
        this(scale, buttonMode, new Insets(0, 0, 0, 0)); // no insets
    }
    
    public ButtonBorder(float scale, ButtonMode buttonMode, Insets insets) {
        super("assets/minecraft/textures/gui/widgets.png", scale, insets);
        int offset = buttonMode.ordinal() * buttonHeight; // switch to different button texture
        
        setBorder(BordersType.TOP_LEFT_CORNER, 0, 46 + offset,       3, 3);
        setBorder(BordersType.BOTTOM_LEFT_CORNER, 0, 63 + offset,    3, 3);
        setBorder(BordersType.BOTTOM_RIGHT_CORNER, 197, 63 + offset, 3, 3);
        setBorder(BordersType.TOP_RIGHT_CORNER, 197, 46 + offset,    3, 3);

        setBorder(BordersType.LEFT_SIDE, 0, 49 + offset,     3, 14);
        setBorder(BordersType.BOTTOM_SIDE, 3, 63 + offset,   14, 3);
        setBorder(BordersType.RIGHT_SIDE, 197, 49 + offset,  3, 14);
        setBorder(BordersType.TOP_SIDE, 3, 46 + offset,      14, 3);

        setBorder(BordersType.BACKGROUND, 3, 49+offset, 194, 14);
    }
}
