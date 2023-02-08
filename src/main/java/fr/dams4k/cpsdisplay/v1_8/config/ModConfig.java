package fr.dams4k.cpsdisplay.v1_8.config;

import java.awt.Color;
import java.io.File;
import java.util.Collections;

import fr.dams4k.cpsdisplay.core.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

public class ModConfig {
	private static Minecraft mc = Minecraft.getMinecraft();

	private static Configuration config;
	
	public static final String CATEGORY_TEXT = "display";
	public static final String CATEGORY_RAINBOW = "rainbow";
	public static final String CATEGORY_BACKGROUND = "background";
	
	public static boolean showText = true;
	// Text
	private static double[] positionText = {0, 0};
	public static double scaleText = 1d;
	public static String hexColorText = "ffffff";
	public static String text = "{0} | {1} CPS";

	// Background
	public static String hexColorBackground = "2a2a2a80";
	public static int marginBackground = 4;
	
	// Rainbow
	public static boolean showRainbow = false;
	public static double speedRainbow = 1d;
	public static double precisionRainbow = 0.1;
	public static float hueRainbow = 0f;
	public static boolean playRainbow;
	
	private static Property positionTextProperty;
	private static Property scaleTextProperty;
	private static Property hexColorTextProperty;
	private static Property showTextProperty;
	private static Property textProperty;

	private static Property hexColorBackgroundProperty;
	private static Property marginBackgroundProperty;
	
	private static Property showRainbowProperty;
	private static Property speedRainbowProperty;

	
	public static void preInit() {
		File configFile = new File(Loader.instance().getConfigDir(), References.MOD_ID + ".cfg");
		config = new Configuration(configFile);
		config.load();
		syncConfig(true);
	}
	
	public static void syncConfig(boolean load) {
		if (load) {
			showTextProperty = config.get(CATEGORY_TEXT, "enable", showText);
			
			positionTextProperty = config.get(CATEGORY_TEXT, "position", positionText);
			scaleTextProperty = config.get(CATEGORY_TEXT, "scale", scaleText);
			hexColorTextProperty = config.get(CATEGORY_TEXT, "color", hexColorText);
			textProperty = config.get(CATEGORY_TEXT, "text", text);

			hexColorBackgroundProperty = config.get(CATEGORY_BACKGROUND, "color", hexColorBackground);
			marginBackgroundProperty = config.get(CATEGORY_BACKGROUND, "margin", marginBackground);

			showRainbowProperty = config.get(CATEGORY_RAINBOW, "rainbow", showRainbow);
			speedRainbowProperty = config.get(CATEGORY_RAINBOW, "chroma_speed", speedRainbow);

			positionText = positionTextProperty.getDoubleList();
			scaleText = scaleTextProperty.getDouble();
			hexColorText = hexColorTextProperty.getString();
			showText = showTextProperty.getBoolean();
			text = textProperty.getString();

			hexColorBackground = hexColorBackgroundProperty.getString();
			marginBackground = marginBackgroundProperty.getInt();

			showRainbow = showRainbowProperty.getBoolean();
			speedRainbow = speedRainbowProperty.getDouble();
		} else {
			positionTextProperty.set(positionText);
			scaleTextProperty.set(scaleText);
			hexColorTextProperty.set(hexColorText);
			showTextProperty.set(showText);
			textProperty.set(text);

			hexColorBackgroundProperty.set(hexColorBackground);
			marginBackgroundProperty.set(marginBackground);

			showRainbowProperty.set(showRainbow);
			speedRainbowProperty.set(speedRainbow);
		}
		
		saveConfig();
	}
	
	private static void saveConfig() {
		if (config.hasChanged()) {
			config.save();
		}
	}
	
	public static Color getChroma() {
		double precision = ModConfig.precisionRainbow * 20000;
		
		int rgb = Color.HSBtoRGB((float) ((System.currentTimeMillis() * ModConfig.speedRainbow) % ((long) precision)) / ((float) precision), 0.8f, 0.8f);
		return new Color(rgb);
	}

	public static void setTextPosition(int[] positionText) {
		ScaledResolution scaledResolution = new ScaledResolution(mc);
		int scaleFactor = scaledResolution.getScaleFactor();

		double[] positionTextPercentage = {(double) positionText[0] / (mc.displayWidth / scaleFactor), (double) positionText[1] / (mc.displayHeight / scaleFactor)};

		ModConfig.positionText = positionTextPercentage;
	}

	public static int[] getTextPosition() {
		ScaledResolution scaledResolution = new ScaledResolution(mc);
		int scaleFactor = scaledResolution.getScaleFactor();

		int[] realPosition = {(int) (ModConfig.positionText[0] * (mc.displayWidth / scaleFactor)), (int) (ModConfig.positionText[1] * (mc.displayHeight / scaleFactor))};
		return realPosition;
	}

	public static Color getTextColor() {
		return ModConfig.HexToColor(ModConfig.hexColorText, 6);
	}
	public static void setTextColor(Color color) {
		ModConfig.hexColorText = Integer.toHexString(color.getRGB()).substring(2);
	}

	public static Color getBackgroundColor() {
		return ModConfig.HexToColor(ModConfig.hexColorBackground, 8);
	}
	public static void setBackgroundColor(Color color) {
		String hexString = Integer.toHexString(color.getRGB()); // aarrggbbb
		ModConfig.hexColorBackground = hexString.substring(2) + hexString.subSequence(0, 2); // rrggbbaa
	}

	public static Color HexToColor(String hex, int size) {
		hex = hex.replace("#", "");;
		hex += String.join("", Collections.nCopies(Math.max(0, size-hex.length()), "0"));
		switch (hex.length()) {
			case 6:
				return new Color(
					Integer.valueOf(hex.substring(0, 2), 16),
					Integer.valueOf(hex.substring(2, 4), 16),
					Integer.valueOf(hex.substring(4, 6), 16)
				);
			case 8:
				return new Color(
					Integer.valueOf(hex.substring(0, 2), 16),
					Integer.valueOf(hex.substring(2, 4), 16),
					Integer.valueOf(hex.substring(4, 6), 16),
					Integer.valueOf(hex.substring(6, 8), 16)
				);
		}
		return null;
	}
}
