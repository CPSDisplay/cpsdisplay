package fr.dams4k.cpsdisplay.enums;

import net.minecraft.client.resources.I18n;

public enum MouseModeEnum {
	LEFT(0, "cpsdisplay.button.display_left", "cpsdisplay.display_template.left"),
	RIGHT(1, "cpsdisplay.button.display_right", "cpsdisplay.display_template.right"),
	LEFT_RIGHT(2, "cpsdisplay.button.display_left_right", "cpsdisplay.display_template.left_right"),
	CUSTOM(3, "cpsdisplay.button.display_custom", "cpsdisplay.display_template.custom");

	private final int id;
	private final String name;
	private final String text;
	
	MouseModeEnum(int id, String name, String text) {
		this.id = id;
		this.name = name;
		this.text = text;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return I18n.format(name, new Object[0]);
	}
	
	public String getText() {
		return I18n.format(text, new Object[0]);
	}
	
	public static MouseModeEnum getById(int id) {
		for (MouseModeEnum val : MouseModeEnum.values()) {
			if (val.getId() == id) {
				return val;
			}
		}
		return getById(0);
	}
	
	public static MouseModeEnum getByName(String str) {
		for (MouseModeEnum val : MouseModeEnum.values()) {
			if (val.getName() == str) {
				return val;
			}
		}
		return getById(0);
	}
	
	public static MouseModeEnum getByText(String text) {
		for (MouseModeEnum val : MouseModeEnum.values()) {
			if (val.getText().equals(I18n.format(text, new Object[0]))) {
				return val;
			}
		}
		return MouseModeEnum.CUSTOM;
	}
}
