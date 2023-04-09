package fr.dams4k.cpsdisplay.config;

import java.awt.Color;
import java.io.File;

import fr.dams4k.cpsdisplay.ColorConverter;
import fr.dams4k.cpsdisplay.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

public class ModConfig {
	private static Minecraft mc = Minecraft.getMinecraft();

	private static Configuration config;
	
	public static final String CATEGORY_TEXT = "display";
	public static final String CATEGORY_RAINBOW = "rainbow";
	public static final String CATEGORY_BACKGROUND = "background";
    public static final String CATEGORY_UPDATER = "updater";
	
	public static boolean showText = true;
	// Text
	private static double[] positionText = {0.5, 0.5};
	public static double scaleText = 1d;
	public static String hexColorText = "ffffff";
	public static String text = I18n.format("cpsdisplay.display_template.left_right", new Object[0]);
    public static boolean showTextShadow = true;

	// Background
	public static String hexColorBackground = "2a2a2a80";
	public static int marginBackground = 4;
	
	// Rainbow
	public static boolean showRainbow = false;
	public static double speedRainbow = 1d;
	public static float hueRainbow = 0f;
	
    // Updater
    public static boolean majorUpdate = true;
    public static boolean minorUpdate = true;
    public static boolean patchUpdate = true;

	private static Property positionTextProperty;
	private static Property scaleTextProperty;
	private static Property hexColorTextProperty;
	private static Property showTextProperty;
	private static Property textProperty;
    private static Property showTextShadowProperty;

	private static Property hexColorBackgroundProperty;
	private static Property marginBackgroundProperty;
	
	private static Property showRainbowProperty;
	private static Property speedRainbowProperty;

    private static Property majorUpdateProperty;
    private static Property minorUpdateProperty;
    private static Property patchUpdateProperty;

	
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
            showTextShadowProperty = config.get(CATEGORY_TEXT, "shadow", showTextShadow);

			hexColorBackgroundProperty = config.get(CATEGORY_BACKGROUND, "color", hexColorBackground);
			marginBackgroundProperty = config.get(CATEGORY_BACKGROUND, "margin", marginBackground);

			showRainbowProperty = config.get(CATEGORY_RAINBOW, "rainbow", showRainbow);
			speedRainbowProperty = config.get(CATEGORY_RAINBOW, "chroma_speed", speedRainbow);

            majorUpdateProperty = config.get(CATEGORY_UPDATER, "major", majorUpdate);
            minorUpdateProperty = config.get(CATEGORY_UPDATER, "minor", minorUpdate);
            patchUpdateProperty = config.get(CATEGORY_UPDATER, "patch", patchUpdate);

			positionText = positionTextProperty.getDoubleList();
			scaleText = scaleTextProperty.getDouble();
			hexColorText = hexColorTextProperty.getString();
			showText = showTextProperty.getBoolean();
			text = textProperty.getString();
            showTextShadow = showTextShadowProperty.getBoolean();

			hexColorBackground = hexColorBackgroundProperty.getString();
			marginBackground = marginBackgroundProperty.getInt();

			showRainbow = showRainbowProperty.getBoolean();
			speedRainbow = speedRainbowProperty.getDouble();

            majorUpdate = majorUpdateProperty.getBoolean();
            minorUpdate = minorUpdateProperty.getBoolean();
            patchUpdate = patchUpdateProperty.getBoolean();
		} else {
			positionTextProperty.set(positionText);
			scaleTextProperty.set(scaleText);
			hexColorTextProperty.set(hexColorText);
			showTextProperty.set(showText);
			textProperty.set(text);
            showTextShadowProperty.set(showTextShadow);

			hexColorBackgroundProperty.set(hexColorBackground);
			marginBackgroundProperty.set(marginBackground);

			showRainbowProperty.set(showRainbow);
			speedRainbowProperty.set(speedRainbow);

            majorUpdateProperty.set(majorUpdate);
            minorUpdateProperty.set(minorUpdate);
            patchUpdateProperty.set(patchUpdate);
		}
		
		saveConfig();
	}
	
	private static void saveConfig() {
		if (config.hasChanged()) {
			config.save();
		}
	}
	
    public static String getFormattedString(Integer attackClicks, Integer useClicks) {
        return ModConfig.text.replace("{0}", attackClicks.toString()).replace("{1}", useClicks.toString()).replace("&", "§");
    }

	public static Color getChroma() {
		int rgb = Color.HSBtoRGB((float) ((System.currentTimeMillis() * ModConfig.speedRainbow) % 2000l) / 2000f, 0.8f, 0.8f);
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

	public static Color getSelectedTextColor() {
        Color textColor;
        if (!ModConfig.showRainbow) {
            try {
                textColor = ColorConverter.HexToColor(ModConfig.hexColorText, 6);
            } catch (Exception e) {
                textColor = Color.WHITE;
                ModConfig.hexColorText = "ffffff";
                ModConfig.syncConfig(false);
            }
        } else {
            textColor = ModConfig.getChroma();
        }
        return textColor;
	}

    public static Color getTextColor() {
		return ColorConverter.HexToColor(ModConfig.hexColorText, 6);
	}
	public static void setTextColor(Color color) {
		ModConfig.hexColorText = Integer.toHexString(color.getRGB()).substring(2);
	}

	public static Color getBackgroundColor() {
		return ColorConverter.HexToColor(ModConfig.hexColorBackground, 8);
	}
	public static void setBackgroundColor(Color color) {
		String hexString = Integer.toHexString(color.getRGB()); // aarrggbbb
		ModConfig.hexColorBackground = hexString.substring(2) + hexString.subSequence(0, 2); // rrggbbaa
	}
}
