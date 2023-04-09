package fr.dams4k.cpsdisplay.gui;

import fr.dams4k.cpsdisplay.config.ModConfig;
import net.minecraft.client.gui.GuiScreen;

public class MoveOverlayGui extends GuiScreen {
    private int diffX = 0;
    private int diffY = 0;

    public MoveOverlayGui(int diffX, int diffY) {
        this.diffX = diffX;
        this.diffY = diffY;
    }

    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int[] newPosition = {diffX+mouseX, diffY+mouseY};
        ModConfig.setTextPosition(newPosition);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        ModConfig.syncConfig(false);
        mc.displayGuiScreen(new GuiConfig());
        super.mouseReleased(mouseX, mouseY, state);
    }
}
