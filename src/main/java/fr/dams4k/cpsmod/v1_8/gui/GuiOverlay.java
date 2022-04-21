package fr.dams4k.cpsmod.v1_8.gui;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import fr.dams4k.cpsmod.v1_8.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiOverlay extends Gui {
	public GuiOverlay(Minecraft mc, Integer l, Integer r, Color color) {
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

			ArrayList<Integer> positions = GuiOverlay.getBackgroundPositions(mc, l, r, false);

			int x = positions.get(0);
			int y = positions.get(1);

			if (color.getAlpha() > 0) {
				drawRect(x, y, positions.get(2), positions.get(3), color.getRGB());
			}
			
			drawString(mc.fontRendererObj, text, x, y, text_color);

			GL11.glPopMatrix();
		}
	}

	public GuiOverlay(Minecraft mc, Integer l, Integer r) {
		this(mc, l, r, Config.background_color);
	}

	public static ArrayList<Integer> getBackgroundPositions(Minecraft mc, Integer l, Integer r, boolean scaled) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		String text = Config.text.replace("{0}", l.toString()).replace("{1}", r.toString()).replace("&", "§");
		
		list.add((int) (Config.text_position[0] / Config.text_scale));
		list.add((int) (Config.text_position[1] / Config.text_scale));
		list.add(list.get(0)+mc.fontRendererObj.getStringWidth(text));
		list.add(list.get(1)+mc.fontRendererObj.FONT_HEIGHT);

		if (scaled) {
			for (int i = 0; i < list.size(); i++) {
				list.set(i, (int) Math.round(list.get(i) * Config.text_scale));
			}
		}

		return list;
	}
}
