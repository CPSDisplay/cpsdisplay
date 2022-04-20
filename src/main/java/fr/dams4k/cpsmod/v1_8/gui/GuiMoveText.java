package fr.dams4k.cpsmod.v1_8.gui;

import java.io.IOException;

import fr.dams4k.cpsmod.v1_8.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiMoveText extends GuiScreen {
	@Override
	public void initGui() {
	}
	
	@Override
	public void updateScreen() {
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int[] mouse_position = {mouseX, mouseY};
		Config.text_position = mouse_position;
		Config.syncConfig(false);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		Minecraft.getMinecraft().displayGuiScreen(new GuiConfig());
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		Minecraft.getMinecraft().displayGuiScreen(new GuiConfig());
		super.keyTyped(typedChar, keyCode);
	}
}
