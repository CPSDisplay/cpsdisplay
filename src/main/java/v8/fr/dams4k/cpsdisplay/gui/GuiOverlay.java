package fr.dams4k.cpsdisplay.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import fr.dams4k.cpsdisplay.config.ModConfig;
import fr.dams4k.cpsdisplay.renderer.ModFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class GuiOverlay extends Gui {
	private ModFontRenderer modFontRenderer;

	public GuiOverlay(Minecraft mc, Integer l, Integer r, Color color) {
		modFontRenderer = new ModFontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, mc.isUnicode());
		modFontRenderer.onResourceManagerReload(null);

		if (ModConfig.showText) {
			String text = ModConfig.text.replace("{0}", l.toString()).replace("{1}", r.toString()).replace("&", "§");
			Color textColor;
			if (!ModConfig.showRainbow) {
				try {
					textColor = ModConfig.getTextColor();
				} catch (Exception e) {
					textColor = Color.WHITE;
					ModConfig.hexColorText = "ffffff";
					ModConfig.syncConfig(false);
				}
			} else {
				textColor = ModConfig.getChroma();
			}
			
			GL11.glPushMatrix();
			GL11.glScaled(ModConfig.scaleText, ModConfig.scaleText, 1d);

			ArrayList<Integer> positions = GuiOverlay.getBackgroundPositions(l, r, false);

			int x = positions.get(0);
			int y = positions.get(1);

			if (color.getAlpha() > 0) {
				int margin = ModConfig.marginBackground;
				drawRect(x-margin, y-margin, positions.get(2)+margin, positions.get(3)+margin, color.getRGB());
			}
			
			// modFontRenderer.drawString(text, x, y, textColor.getRGB(), true);
			List<Color> colors = new ArrayList<>();
			colors.add(Color.RED);
			colors.add(Color.BLUE);
			colors.add(Color.GREEN);
			colors.add(Color.GRAY);
			colors.add(Color.MAGENTA);
			colors.add(Color.ORANGE);
			modFontRenderer.drawGradientString(text, x, y, colors, false, true);

			GL11.glPopMatrix();
		}
	}

	public GuiOverlay(Minecraft mc, Integer l, Integer r) {
		this(mc, l, r, ModConfig.getBackgroundColor());
	}

	public static ArrayList<Integer> getBackgroundPositions(Integer l, Integer r, boolean scaled) {
		Minecraft mc = Minecraft.getMinecraft();

		ArrayList<Integer> list = new ArrayList<Integer>();
		String text = ModConfig.text.replace("{0}", l.toString()).replace("{1}", r.toString()).replace("&", "§");
		
		int[] textPosition = ModConfig.getTextPosition();

		list.add((int) (textPosition[0] / ModConfig.scaleText));
		list.add((int) (textPosition[1] / ModConfig.scaleText));
		list.add(list.get(0)+mc.fontRendererObj.getStringWidth(text));
		list.add(list.get(1)+mc.fontRendererObj.FONT_HEIGHT-1);

		if (scaled) {
			for (int i = 0; i < list.size(); i++) {
				list.set(i, (int) Math.round(list.get(i) * ModConfig.scaleText));
			}
		}

		return list;
	}

	public static boolean positionInOverlay(int x, int y) {
		ArrayList<Integer> positions = GuiOverlay.getBackgroundPositions(0, 0, true);
		return positions.get(0) <= x && x <= positions.get(2) && positions.get(1) <= y && y <= positions.get(3);
	}
}
