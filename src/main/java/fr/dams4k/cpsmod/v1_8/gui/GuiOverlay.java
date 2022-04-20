package fr.dams4k.cpsmod.v1_8.gui;

import org.lwjgl.opengl.GL11;

import fr.dams4k.cpsmod.v1_8.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiOverlay extends Gui {
	public GuiOverlay(Minecraft mc, Integer l, Integer r) {
		if (Config.show_text) {
			String text = Config.text.replace("{0}", l.toString()).replace("{1}", r.toString()).replace("&", "§");
			Integer text_color;
			if (!Config.rainbow) {
				try {
					text_color = Integer.parseInt(Config.text_color, 16);
				} catch (Exception e) {
					text_color = Integer.parseInt("ffffff", 16);
					Config.text_color = "ffffff";
					Config.syncConfig(false);
				}
			} else {
				text_color = Config.getChroma();
			}
			
			GL11.glPushMatrix();
			GL11.glScaled(Config.text_scale, Config.text_scale, 1d);

			int x = (int) (Config.text_position[0] / Config.text_scale);
			int y = (int) (Config.text_position[1] / Config.text_scale);

			if (Config.background_color.getAlpha() > 0) {
				drawRect(x, y, x+mc.fontRendererObj.getStringWidth(text), y + mc.fontRendererObj.FONT_HEIGHT, Config.background_color.getRGB());
			}
			
			drawString(mc.fontRendererObj, text, x, y, text_color);

			GL11.glPopMatrix();
		}
	}
}
