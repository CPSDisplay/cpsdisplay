package fr.dams4k.cpsmod.v1_8.config;

import java.awt.Color;
import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

public class Config {
	private static Configuration config;
	
	public static final String CATEGORY_NAME_TEXT = "display";
	public static final String CATEGORY_NAME_RAINBOW = "text";
	public static final String CATEGORY_NAME_BACKGROUND = "background";
	
	public static boolean show_text = true;
	// Text
	public static int[] text_position = {0, 0};
	public static double text_scale = 1d;
	public static String text_color = "ffffff";
	public static String text = "{0} | {1} CPS";
	
	// Rainbow
	public static boolean rainbow = false;
	public static int rainbow_speed = 100;
	public static double rainbow_precision = 0.1;
	public static float rainbow_hue = 0f;
	public static boolean play_rainbow;
	
	private static Property textPositionProperty;
	private static Property textScaleProperty;
	private static Property textColorProperty;
	private static Property showTextProperty;
	private static Property textProperty;
	private static Property rainbowProperty;
	private static Property rainbowSpeedProperty;
	
	private static Property backgroundColorRedProperty;
	private static Property backgroundColorGreenProperty;
	private static Property backgroundColorBlueProperty;
	private static Property backgroundColorAlphaProperty;

	// Background
	public static int bg_color_r = 43;
	public static int bg_color_g = 43;
	public static int bg_color_b = 43;
	public static int bg_color_a = 127;

	public static Color background_color = new Color(bg_color_r, bg_color_g, bg_color_b, bg_color_a); // à sauvegarder séparement, var R var G var B var A
	
	public static void preInit() {
		File configFile = new File(Loader.instance().getConfigDir(), "cpsmod.cfg");
		config = new Configuration(configFile);
		config.load();
		syncConfig(true);
	}
	
	public static void syncConfig(boolean load) {
		if (load) {
			showTextProperty = config.get(CATEGORY_NAME_TEXT, "enable", show_text);
			
			textPositionProperty = config.get(CATEGORY_NAME_TEXT, "position", text_position);
			textScaleProperty = config.get(CATEGORY_NAME_TEXT, "scale", text_scale);
			textColorProperty = config.get(CATEGORY_NAME_TEXT, "color", text_color);
			textProperty = config.get(CATEGORY_NAME_TEXT, "text", text);
			rainbowProperty = config.get(CATEGORY_NAME_RAINBOW, "rainbow", rainbow);
			rainbowSpeedProperty = config.get(CATEGORY_NAME_RAINBOW, "chroma_speed", rainbow_speed);

			backgroundColorRedProperty = config.get(CATEGORY_NAME_BACKGROUND, "color_r", bg_color_r);
			backgroundColorGreenProperty = config.get(CATEGORY_NAME_BACKGROUND, "color_g", bg_color_g);
			backgroundColorBlueProperty = config.get(CATEGORY_NAME_BACKGROUND, "color_b", bg_color_b);
			backgroundColorAlphaProperty = config.get(CATEGORY_NAME_BACKGROUND, "color_a", bg_color_a);

			text_position = textPositionProperty.getIntList();
			
			text_scale = textScaleProperty.getDouble();
			text_color = textColorProperty.getString();
			show_text = showTextProperty.getBoolean();
			text = textProperty.getString();
			rainbow = rainbowProperty.getBoolean();
			rainbow_speed = rainbowSpeedProperty.getInt();

			bg_color_r = backgroundColorRedProperty.getInt();
			bg_color_g = backgroundColorGreenProperty.getInt();
			bg_color_b = backgroundColorBlueProperty.getInt();
			bg_color_a = backgroundColorAlphaProperty.getInt();
			background_color = new Color(bg_color_r, bg_color_g, bg_color_b, bg_color_a);

		} else {
			textPositionProperty.set(text_position);
			textScaleProperty.set(text_scale);
			textColorProperty.set(text_color);
			showTextProperty.set(show_text);
			textProperty.set(text);
			rainbowProperty.set(rainbow);
			rainbowSpeedProperty.set(rainbow_speed);

			backgroundColorRedProperty.set(bg_color_r);
			backgroundColorGreenProperty.set(bg_color_g);
			backgroundColorBlueProperty.set(bg_color_b);
			backgroundColorAlphaProperty.set(bg_color_a);
		}
		
		saveConfig();
	}
	
	private static void saveConfig() {
		if (config.hasChanged()) {
			config.save();
		}
	}
	
	public static int getChroma() {
		double precision = Config.rainbow_precision * 20000;

		int rgb = Color.HSBtoRGB((float) ((System.currentTimeMillis() * rainbow_speed) % ((long) precision)) / ((float) precision), 0.8F, 0.8F);
		return rgb;
	}
}
