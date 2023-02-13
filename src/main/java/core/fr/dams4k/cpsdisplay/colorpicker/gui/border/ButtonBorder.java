package core.fr.dams4k.cpsdisplay.colorpicker.gui.border;

import java.awt.Insets;

public class ButtonBorder extends Border {
    private int buttonHeight = 20;

    public ButtonBorder(float scale, ButtonMode buttonMode) {
        this(scale, buttonMode, new Insets(0, 0, 0, 0)); // no insets
    }
    
    public ButtonBorder(float scale, ButtonMode buttonMode, Insets insets) {
        super("assets/minecraft/textures/gui/widgets.png", scale, insets);
        int offset = buttonMode.ordinal() * buttonHeight; // switch to different button texture
        
        setBorder(BorderType.TOP_LEFT_CORNER, 0, 46 + offset,       3, 3);
        setBorder(BorderType.BOTTOM_LEFT_CORNER, 0, 63 + offset,    3, 3);
        setBorder(BorderType.BOTTOM_RIGHT_CORNER, 197, 63 + offset, 3, 3);
        setBorder(BorderType.TOP_RIGHT_CORNER, 197, 46 + offset,    3, 3);

        setBorder(BorderType.LEFT_SIDE, 0, 49 + offset,     3, 14);
        setBorder(BorderType.BOTTOM_SIDE, 3, 63 + offset,   14, 3);
        setBorder(BorderType.RIGHT_SIDE, 197, 49 + offset,  3, 14);
        setBorder(BorderType.TOP_SIDE, 3, 46 + offset,      14, 3);

        setBorder(BorderType.BACKGROUND, 3, 49+offset, 194, 14);
    }
}
