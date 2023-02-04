package fr.dams4k.cpsdisplay.v1_8.config;

import java.awt.Color;
import java.io.File;

import fr.dams4k.cpsdisplay.core.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

public class ModConfig {
	private static Minecraft mc = Minecraft.getMinecraft();

	private static Configuration config;
	
	public static final String CATEGORY_NAME_TEXT = "display";
	public static final String CATEGORY_NAME_RAINBOW = "rainbow";
	public static final String CATEGORY_NAME_BACKGROUND = "background";
	
	public static boolean showText = true;
	// Text
	private static double[] textPosition = {0, 0};
	public static double textScale = 1d;
	public static String textHexColor = "ffffff";
	public static String text = "{0} | {1} CPS";

	// Background
	public static String backgroundHexColor = "2a2a2a80";
	
	// Rainbow
	public static boolean rainbow = false;
	public static double rainbowSpeed = 1d;
	public static double rainbowPrecision = 0.1;
	public static float rainbowHue = 0f;
	public static boolean playRainbow;
	
	private static Property textPositionProperty;
	private static Property textScaleProperty;
	private static Property textHexColorProperty;
	private static Property showTextProperty;
	private static Property textProperty;

	private static Property backgroundHexColorProperty;
	
	private static Property rainbowProperty;
	private static Property rainbowSpeedProperty;

	
	public static void preInit() {
		File configFile = new File(Loader.instance().getConfigDir(), References.MOD_ID + ".cfg");
		config = new Configuration(configFile);
		config.load();
		syncConfig(true);
	}
	
	public static void syncConfig(boolean load) {
		if (load) {
			showTextProperty = config.get(CATEGORY_NAME_TEXT, "enable", showText);
			
			textPositionProperty = config.get(CATEGORY_NAME_TEXT, "position", textPosition);
			textScaleProperty = config.get(CATEGORY_NAME_TEXT, "scale", textScale);
			textHexColorProperty = config.get(CATEGORY_NAME_TEXT, "color", textHexColor);
			textProperty = config.get(CATEGORY_NAME_TEXT, "text", text);

			backgroundHexColorProperty = config.get(CATEGORY_NAME_BACKGROUND, "color", backgroundHexColor);

			rainbowProperty = config.get(CATEGORY_NAME_RAINBOW, "rainbow", rainbow);
			rainbowSpeedProperty = config.get(CATEGORY_NAME_RAINBOW, "chroma_speed", rainbowSpeed);

			textPosition = textPositionProperty.getDoubleList();
			textScale = textScaleProperty.getDouble();
			textHexColor = textHexColorProperty.getString();
			showText = showTextProperty.getBoolean();
			text = textProperty.getString();

			backgroundHexColor = backgroundHexColorProperty.getString();

			rainbow = rainbowProperty.getBoolean();
			rainbowSpeed = rainbowSpeedProperty.getDouble();
		} else {
			textPositionProperty.set(textPosition);
			textScaleProperty.set(textScale);
			textHexColorProperty.set(textHexColor);
			showTextProperty.set(showText);
			textProperty.set(text);

			backgroundHexColorProperty.set(backgroundHexColor);

			rainbowProperty.set(rainbow);
			rainbowSpeedProperty.set(rainbowSpeed);
		}
		
		saveConfig();
	}
	
	private static void saveConfig() {
		if (config.hasChanged()) {
			config.save();
		}
	}
	
	public static Color getChroma() {
		double precision = ModConfig.rainbowPrecision * 20000;
		
		int rgb = Color.HSBtoRGB((float) ((System.currentTimeMillis() * ModConfig.rainbowSpeed) % ((long) precision)) / ((float) precision), 0.8f, 0.8f);
		return new Color(rgb);
	}

	public static void setTextPosition(int[] textPosition) {
		ScaledResolution scaledResolution = new ScaledResolution(mc);
		int scaleFactor = scaledResolution.getScaleFactor();

		double[] textPositionPercentage = {(double) textPosition[0] / (mc.displayWidth / scaleFactor), (double) textPosition[1] / (mc.displayHeight / scaleFactor)};

		ModConfig.textPosition = textPositionPercentage;
	}

	public static int[] getTextPosition() {
		ScaledResolution scaledResolution = new ScaledResolution(mc);
		int scaleFactor = scaledResolution.getScaleFactor();

		int[] realPosition = {(int) (ModConfig.textPosition[0] * (mc.displayWidth / scaleFactor)), (int) (ModConfig.textPosition[1] * (mc.displayHeight / scaleFactor))};
		return realPosition;
	}

	public static Color getTextColor() {
		return ModConfig.HexToColor(ModConfig.textHexColor);
	}

	public static Color getBackgroundColor() {
		return ModConfig.HexToColor(ModConfig.backgroundHexColor);
	}

	public static Color HexToColor(String hex) {
		hex = hex.replace("#", "");
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
