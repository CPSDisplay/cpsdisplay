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
	public static final String CATEGORY_NAME_RAINBOW = "text";
	public static final String CATEGORY_NAME_BACKGROUND = "background";
	
	public static boolean showText = true;
	// Text
	private static double[] textPosition = {0, 0};
	public static double textScale = 1d;
	public static String textColor = "ffffff";
	public static String text = "{0} | {1} CPS";
	public static String backgroundColor = "2b2a2a80";
	
	// Rainbow
	public static boolean rainbow = false;
	public static double rainbowSpeed = 1d;
	public static double rainbowPrecision = 0.1;
	public static float rainbowHue = 0f;
	public static boolean playRainbow;
	
	private static Property textPositionProperty;
	private static Property textScaleProperty;
	private static Property textColorProperty;
	private static Property showTextProperty;
	private static Property textProperty;
	private static Property rainbowProperty;
	private static Property rainbowSpeedProperty;
	
	// private static Property backgroundColorRedProperty;
	// private static Property backgroundColorGreenProperty;
	// private static Property backgroundColorBlueProperty;
	// private static Property backgroundColorAlphaProperty;

	public static Color background_color = new Color(Integer.parseInt(ModConfig.backgroundColor, 16)); // à sauvegarder séparement, var R var G var B var A
	
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
			textColorProperty = config.get(CATEGORY_NAME_TEXT, "color", textColor);
			textProperty = config.get(CATEGORY_NAME_TEXT, "text", text);
			rainbowProperty = config.get(CATEGORY_NAME_RAINBOW, "rainbow", rainbow);
			rainbowSpeedProperty = config.get(CATEGORY_NAME_RAINBOW, "chroma_speed", rainbowSpeed);

			// backgroundColorRedProperty = config.get(CATEGORY_NAME_BACKGROUND, "color_r", bg_color_r);
			// backgroundColorGreenProperty = config.get(CATEGORY_NAME_BACKGROUND, "color_g", bg_color_g);
			// backgroundColorBlueProperty = config.get(CATEGORY_NAME_BACKGROUND, "color_b", bg_color_b);
			// backgroundColorAlphaProperty = config.get(CATEGORY_NAME_BACKGROUND, "color_a", bg_color_a);

			textPosition = textPositionProperty.getDoubleList();
			textScale = textScaleProperty.getDouble();
			textColor = textColorProperty.getString();
			showText = showTextProperty.getBoolean();
			text = textProperty.getString();
			rainbow = rainbowProperty.getBoolean();
			rainbowSpeed = rainbowSpeedProperty.getDouble();

			// bg_color_r = backgroundColorRedProperty.getInt();
			// bg_color_g = backgroundColorGreenProperty.getInt();
			// bg_color_b = backgroundColorBlueProperty.getInt();
			// bg_color_a = backgroundColorAlphaProperty.getInt();
			// background_color = new Color(bg_color_r, bg_color_g, bg_color_b, bg_color_a);

		} else {
			textPositionProperty.set(textPosition);
			textScaleProperty.set(textScale);
			textColorProperty.set(textColor);
			showTextProperty.set(showText);
			textProperty.set(text);
			rainbowProperty.set(rainbow);
			rainbowSpeedProperty.set(rainbowSpeed);

			// backgroundColorRedProperty.set(bg_color_r);
			// backgroundColorGreenProperty.set(bg_color_g);
			// backgroundColorBlueProperty.set(bg_color_b);
			// backgroundColorAlphaProperty.set(bg_color_a);
		}
		
		saveConfig();
	}
	
	private static void saveConfig() {
		if (config.hasChanged()) {
			config.save();
		}
	}
	
	public static int getChroma() {
		double precision = ModConfig.rainbowPrecision * 20000;
		
		int rgb = Color.HSBtoRGB((float) ((System.currentTimeMillis() * ModConfig.rainbowSpeed) % ((long) precision)) / ((float) precision), 0.8f, 0.8f);
		return rgb;
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
}
