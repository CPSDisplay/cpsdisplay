package fr.dams4k.cpsdisplay.core_last.colorchooser.borders;

public class InventoryBorder extends BorderBase {

    public InventoryBorder(float scale) {
        super("assets/minecraft/textures/gui/widgets.png", scale);
        
        setBorder(BordersType.TOP_LEFT_CORNER, 0, 0, 3, 3);
        setBorder(BordersType.BOTTOM_LEFT_CORNER, 0, 19, 3, 3);
        setBorder(BordersType.BOTTOM_RIGHT_CORNER, 179, 19, 3, 3);
        setBorder(BordersType.TOP_RIGHT_CORNER, 179, 0, 3, 3);
        setBorder(BordersType.LEFT_SIDE, 0, 3, 3, 16);
        setBorder(BordersType.BOTTOM_SIDE, 4, 19, 16, 3);
        setBorder(BordersType.RIGHT_SIDE, 179, 3, 3, 16);
        setBorder(BordersType.TOP_SIDE, 4, 0, 16, 3);
    }
}
