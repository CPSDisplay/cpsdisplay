package fr.dams4k.cpsdisplay.colorpicker.gui.border;

import net.minecraft.util.ResourceLocation;

public class InventoryBorder extends Border {
    public InventoryBorder(float scale) {
        super(new ResourceLocation("cpsdisplay", "textures/gui/inventory.png"), new int[]{22, 22}, scale, null);

        setBorder(BorderType.TOP_LEFT_CORNER, 0, 0, 3, 3);
        setBorder(BorderType.BOTTOM_LEFT_CORNER, 0, 19, 3, 3);
        setBorder(BorderType.BOTTOM_RIGHT_CORNER, 19, 19, 3, 3);
        setBorder(BorderType.TOP_RIGHT_CORNER, 19, 0, 3, 3);

        setBorder(BorderType.LEFT_SIDE, 0, 3, 3, 16);
        setBorder(BorderType.BOTTOM_SIDE, 3, 19, 16, 3);
        setBorder(BorderType.RIGHT_SIDE, 19, 3, 3, 16);
        setBorder(BorderType.TOP_SIDE, 3, 0, 16, 3);
    }
}
