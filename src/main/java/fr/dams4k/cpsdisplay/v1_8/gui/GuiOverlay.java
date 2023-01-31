package fr.dams4k.cpsdisplay.v1_8.gui;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import fr.dams4k.cpsdisplay.v1_8.config.ModConfig;
import fr.dams4k.cpsdisplay.v1_8.renderer.ModFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class GuiOverlay extends Gui {
	private ModFontRenderer modFontRenderer;

	public GuiOverlay(Minecraft mc, Integer l, Integer r, Color color) {
		modFontRenderer = new ModFontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, mc.isUnicode());
		modFontRenderer.onResourceManagerReload(null);

		if (ModConfig.show_text) {
			String text = ModConfig.text.replace("{0}", l.toString()).replace("{1}", r.toString()).replace("&", "§");
			Integer text_color;
			if (!ModConfig.rainbow) {
				try {
					text_color = Integer.parseInt(ModConfig.text_color, 16);
				} catch (Exception e) {
					text_color = Integer.parseInt("ffffff", 16);
					ModConfig.text_color = "ffffff";
					ModConfig.syncConfig(false);
				}
			} else {
				text_color = ModConfig.getChroma();
			}
			
			GL11.glPushMatrix();
			GL11.glScaled(ModConfig.text_scale, ModConfig.text_scale, 1d);

			ArrayList<Integer> positions = GuiOverlay.getBackgroundPositions(mc, l, r, false);

			int x = positions.get(0);
			int y = positions.get(1);

			if (color.getAlpha() > 0) {
				drawRect(x, y, positions.get(2), positions.get(3), color.getRGB());
			}
			
			// modFontRenderer.drawGradientString(text, x, y, 0x00ffff, 0x0000ff, true, true);
			drawString(mc.fontRendererObj, text, x, y, text_color);

			GL11.glPopMatrix();
		}
	}

	public GuiOverlay(Minecraft mc, Integer l, Integer r) {
		this(mc, l, r, ModConfig.background_color);
	}

	public static ArrayList<Integer> getBackgroundPositions(Minecraft mc, Integer l, Integer r, boolean scaled) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		String text = ModConfig.text.replace("{0}", l.toString()).replace("{1}", r.toString()).replace("&", "§");
		
		int[] text_position = ModConfig.getText_position();

		list.add((int) (text_position[0] / ModConfig.text_scale));
		list.add((int) (text_position[1] / ModConfig.text_scale));
		list.add(list.get(0)+mc.fontRendererObj.getStringWidth(text));
		list.add(list.get(1)+mc.fontRendererObj.FONT_HEIGHT);

		if (scaled) {
			for (int i = 0; i < list.size(); i++) {
				list.set(i, (int) Math.round(list.get(i) * ModConfig.text_scale));
			}
		}

		return list;
	}
}
