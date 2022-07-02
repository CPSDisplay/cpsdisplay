package fr.dams4k.cpsdisplay.core.colorchooser.borders;

public class ButtonBorder extends BorderBase {

    public ButtonBorder(float scale) {
        super("assets/minecraft/textures/gui/widgets.png", scale);
        setBorder(BordersType.TOP_LEFT_CORNER, 0, 46, 3, 3);
        setBorder(BordersType.BOTTOM_LEFT_CORNER, 0, 63, 3, 3);
        setBorder(BordersType.BOTTOM_RIGHT_CORNER, 197, 63, 3, 3);
        setBorder(BordersType.TOP_RIGHT_CORNER, 197, 46, 3, 3);

        setBorder(BordersType.LEFT_SIDE, 0, 49, 3, 14);
        setBorder(BordersType.BOTTOM_SIDE, 3, 63, 14, 3);
        setBorder(BordersType.RIGHT_SIDE, 197, 49, 3, 14);
        setBorder(BordersType.TOP_SIDE, 3, 46, 14, 3);
    }
    
}
