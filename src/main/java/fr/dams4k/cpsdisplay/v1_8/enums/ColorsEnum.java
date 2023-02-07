package fr.dams4k.cpsdisplay.v1_8.enums;

import fr.dams4k.cpsdisplay.v1_8.config.ModConfig;

public enum ColorsEnum {
	WHITE(	0, "White",	 "ffffff"),
	BLACK(	1, "Black",	 "000000"),
	RED(	2, "§cRed",	 "ff0000"),
	GREEN(	3, "§aGreen",	 "00ff00"),
	BLUE(	4, "§9Blue",	 "0000ff"),
	YELLOW(	5, "§eYellow", "ffff00"),
	PINK(	6, "§dPink",	 "ff00ff"),
	AQUA(	7, "§bAqua",	 "00ffff"),
	CUSTOM(	8, "Custom",	 ""),
	RAINBOW(9, "§cR§da§9i§bn§ab§eo§6w", "");

	private final int id;
	private final String name;
	private final String hex;
	
	ColorsEnum(int id, String name, String hexColor) {
		this.id = id;
		this.name = name;
		this.hex = hexColor;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getHex() {
		return hex;
	}
	
	public static ColorsEnum getById(int id) {
		for (ColorsEnum color : ColorsEnum.values()) {
			if (color.getId() == id) {
				return color;
			}
		}
		return ColorsEnum.WHITE;
	}
	
	public static ColorsEnum getByName(String name) {
		for (ColorsEnum color : ColorsEnum.values()) {
			if (color.getName() == name) {
				return color;
			}
		}
		return ColorsEnum.WHITE;
	}
	
	public static ColorsEnum getByHex(String hex) {
		for (ColorsEnum color : ColorsEnum.values()) {
			if (color.getHex().equals(hex)) {
				return color;
			}
		}
		if (ModConfig.showRainbow == true) {
			return ColorsEnum.RAINBOW;
		} else {
			return ColorsEnum.CUSTOM;
		}
	}
}
