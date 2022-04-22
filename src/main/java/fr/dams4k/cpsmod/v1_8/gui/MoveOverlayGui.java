package fr.dams4k.cpsmod.v1_8.gui;

import java.awt.Color;
import java.util.ArrayList;

import fr.dams4k.cpsmod.v1_8.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class MoveOverlayGui extends GuiScreen {
    int diff_x = 0;
    int diff_y = 0;

    public MoveOverlayGui(int diff_x, int diff_y) {
        this.diff_x = diff_x;
        this.diff_y = diff_y;
    }

    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int[] new_pos = {diff_x+mouseX, diff_y+mouseY};
        ModConfig.setText_position(new_pos);

        ArrayList<Integer> positions = GuiOverlay.getBackgroundPositions(mc, 0, 0, true);
        
        Color color = new Color(ModConfig.bg_color_r, ModConfig.bg_color_g, ModConfig.bg_color_b, (int) Math.round(ModConfig.bg_color_a * 0.5));
        new GuiOverlay(Minecraft.getMinecraft(), 0, 0, color);
        
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
