package fr.dams4k.cpsmod.v1_8.enums;

import fr.dams4k.cpsmod.v1_8.config.Config;

public enum ColorsEnum {
	WHITE(0, "White", "ffffff"),
	RED(1, "§cRed", "ff0000"),
	GREEN(2, "§aGreen", "00ff00"),
	BLUE(3, "§9Blue", "0000ff"),
	YELLOW(4, "§eYellow", "ffff00"),
	PINK(5, "§dPink", "ff00ff"),
	AQUA(6, "§bAqua", "00ffff"),
	CUSTOM(7, "Custom", ""),
	RAINBOW(8, "§cR§da§9i§bn§ab§eo§6w", "");

	private final int id;
	private final String name;
	private final String hex;
	
	ColorsEnum(int id, String name, String hex_color) {
		this.id = id;
		this.name = name;
		this.hex = hex_color;
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
			if (color.getHex() == hex) {
				return color;
			}
		}
		if (Config.rainbow == true) {
			return ColorsEnum.RAINBOW;
		} else {
			return ColorsEnum.CUSTOM;
		}
	}
}
