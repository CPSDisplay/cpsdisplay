package fr.dams4k.cpsdisplay.gui;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import fr.dams4k.cpsdisplay.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class CPSOverlay extends Gui {
	public final Minecraft mc = Minecraft.getMinecraft();

    public void renderOverlay(Integer attackClicks, Integer useClicks) {
		if (ModConfig.showText) {
			String text = ModConfig.getFormattedString(attackClicks, useClicks);
			Color textColor = ModConfig.getSelectedTextColor();
            Color backgroundColor = ModConfig.getBackgroundColor();
			
			GL11.glPushMatrix();
			GL11.glScaled(ModConfig.scaleText, ModConfig.scaleText, 1d);

			ArrayList<Integer> positions = CPSOverlay.getBackgroundPositions(attackClicks, useClicks, false);

			int x = positions.get(0);
			int y = positions.get(1);

			if (backgroundColor.getAlpha() > 0) {
				int margin = ModConfig.marginBackground;
				drawRect(x-margin, y-margin, positions.get(2)+margin, positions.get(3)+margin, backgroundColor.getRGB());
			}
			
			// Styles aren't reset after drawing the shadow, so we force minecraft to reset them by adding §r at the beginning of the text
			mc.fontRendererObj.drawString("§r" + text, x, y, textColor.getRGB(), ModConfig.showTextShadow);
            
			GL11.glPopMatrix();
		}
	}

	public static ArrayList<Integer> getBackgroundPositions(Integer attackClicks, Integer useClicks, boolean scaled) {
		Minecraft mc = Minecraft.getMinecraft();

		ArrayList<Float> list = new ArrayList<>();
		String text = ModConfig.getFormattedString(attackClicks, useClicks);
		
		int[] textPosition = ModConfig.getTextPosition();

        float k = ModConfig.showTextShadow ? 0f : mc.fontRendererObj.getUnicodeFlag() ? 0.5f : 1f;
        int j = mc.fontRendererObj.getUnicodeFlag() ? 1 : 0;

		list.add((float) (textPosition[0] / ModConfig.scaleText));
		list.add((float) (textPosition[1] / ModConfig.scaleText) + j);
		list.add(list.get(0)+mc.fontRendererObj.getStringWidth(text) - k);
		list.add(list.get(1)+mc.fontRendererObj.FONT_HEIGHT - 1 - k + j);

        ArrayList<Integer> finalList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (scaled) {
                finalList.add((int) (Math.round(list.get(i) * ModConfig.scaleText)));
            } else {
                finalList.add(Math.round(list.get(i)));
            }
        }
		return finalList;
	}

	public static boolean positionInOverlay(int x, int y) {
		ArrayList<Integer> positions = CPSOverlay.getBackgroundPositions(0, 0, true);
		return positions.get(0) <= x && x <= positions.get(2) && positions.get(1) <= y && y <= positions.get(3);
	}
}
