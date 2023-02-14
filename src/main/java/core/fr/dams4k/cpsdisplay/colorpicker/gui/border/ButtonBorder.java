package fr.dams4k.cpsdisplay.colorpicker.gui.border;

import net.minecraft.util.ResourceLocation;

public class ButtonBorder extends Border {
    private int buttonHeight = 20;

    public ButtonBorder(float scale, ButtonMode buttonMode) {
        super(new ResourceLocation("cpsdisplay", "textures/gui/buttons.png"), new int[]{200, 60}, scale, null);
        int offset = buttonMode.ordinal() * buttonHeight; // switch to different button texture
        
        setBorder(BorderType.TOP_LEFT_CORNER, 0, offset, 3, 3);
        setBorder(BorderType.BOTTOM_LEFT_CORNER, 0, 17 + offset, 3, 3);
        setBorder(BorderType.BOTTOM_RIGHT_CORNER, 197, 17 + offset, 3, 3);
        setBorder(BorderType.TOP_RIGHT_CORNER, 197, offset, 3, 3);

        setBorder(BorderType.LEFT_SIDE, 0, 3 + offset, 3, 14);
        setBorder(BorderType.BOTTOM_SIDE, 3, 17 + offset, 194, 3);
        setBorder(BorderType.RIGHT_SIDE, 197, 3 + offset, 3, 14);
        setBorder(BorderType.TOP_SIDE, 3, offset, 194, 3);

        setBorder(BorderType.BACKGROUND, 3, 3+offset, 194, 14);
    }
}
