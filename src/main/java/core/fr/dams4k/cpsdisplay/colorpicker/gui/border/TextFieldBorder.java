package fr.dams4k.cpsdisplay.colorpicker.gui.border;

public class TextFieldBorder extends Border {

    public TextFieldBorder(float scale) {
        super("assets/cpsdisplay/textures/gui/textfield.png", scale);

        setBorder(BorderType.TOP_LEFT_CORNER, 0, 0, 1, 1);
        setBorder(BorderType.BOTTOM_LEFT_CORNER, 0, 2, 1, 1);
        setBorder(BorderType.BOTTOM_RIGHT_CORNER, 2, 2, 1, 1);
        setBorder(BorderType.TOP_RIGHT_CORNER, 2, 0, 1, 1);
        setBorder(BorderType.LEFT_SIDE, 0, 1, 1, 1);
        setBorder(BorderType.BOTTOM_SIDE, 1, 2, 1, 1);
        setBorder(BorderType.RIGHT_SIDE, 2, 1, 1, 1);
        setBorder(BorderType.TOP_SIDE, 1, 0, 1, 1);

        setBorder(BorderType.BACKGROUND, 1, 1, 1, 1);
    }
}
