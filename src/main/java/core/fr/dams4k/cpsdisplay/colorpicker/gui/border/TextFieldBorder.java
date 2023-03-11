package fr.dams4k.cpsdisplay.colorpicker.gui.border;

import net.minecraft.util.ResourceLocation;

public class TextFieldBorder extends Border {

    public TextFieldBorder(float scale) {
        super(new ResourceLocation("cpsdisplay", "textures/gui/textfield.png"), new int[]{32, 32}, scale, null);

        setBorder(BorderType.TOP_LEFT_CORNER, 0, 0, 1, 1);
        setBorder(BorderType.BOTTOM_LEFT_CORNER, 0, 31, 1, 1);
        setBorder(BorderType.BOTTOM_RIGHT_CORNER, 31, 31, 1, 1);
        setBorder(BorderType.TOP_RIGHT_CORNER, 31, 0, 1, 1);
        setBorder(BorderType.LEFT_SIDE, 0, 1, 1, 30);
        setBorder(BorderType.BOTTOM_SIDE, 1, 31, 30, 1);
        setBorder(BorderType.RIGHT_SIDE, 31, 1, 1, 30);
        setBorder(BorderType.TOP_SIDE, 1, 0, 30, 1);

        setBorder(BorderType.BACKGROUND, 1, 1, 1, 1);
    }
}
