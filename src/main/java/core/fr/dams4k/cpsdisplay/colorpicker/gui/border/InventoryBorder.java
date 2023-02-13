package fr.dams4k.cpsdisplay.colorpicker.gui.border;

public class InventoryBorder extends Border {

    public InventoryBorder(float scale) {
        super("assets/minecraft/textures/gui/widgets.png", scale);
        
        setBorder(BorderType.TOP_LEFT_CORNER, 0, 0, 3, 3);
        setBorder(BorderType.BOTTOM_LEFT_CORNER, 0, 19, 3, 3);
        setBorder(BorderType.BOTTOM_RIGHT_CORNER, 179, 19, 3, 3);
        setBorder(BorderType.TOP_RIGHT_CORNER, 179, 0, 3, 3);
        setBorder(BorderType.LEFT_SIDE, 0, 3, 3, 16);
        setBorder(BorderType.BOTTOM_SIDE, 4, 19, 16, 3);
        setBorder(BorderType.RIGHT_SIDE, 179, 3, 3, 16);
        setBorder(BorderType.TOP_SIDE, 4, 0, 16, 3);
    }
}
