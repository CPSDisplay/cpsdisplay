package fr.dams4k.cpsdisplay.gui;

import java.awt.Color;
import java.util.ArrayList;

import fr.dams4k.cpsdisplay.config.ModConfig;
import net.minecraft.client.Minecraft;
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

        ArrayList<Integer> positions = GuiOverlay.getBackgroundPositions(0, 0, true);
        
        new GuiOverlay(Minecraft.getMinecraft(), 0, 0, ModConfig.getBackgroundColor());
        
        drawVerticalLine(positions.get(0), positions.get(1), positions.get(3), Color.RED.getRGB());
        drawVerticalLine(positions.get(2), positions.get(1), positions.get(3), Color.RED.getRGB());
        drawHorizontalLine(positions.get(0), positions.get(2), positions.get(1), Color.RED.getRGB());
        drawHorizontalLine(positions.get(0), positions.get(2), positions.get(3), Color.RED.getRGB());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        ModConfig.syncConfig(false);
        mc.displayGuiScreen(new GuiConfig());
        super.mouseReleased(mouseX, mouseY, state);
    }
}
