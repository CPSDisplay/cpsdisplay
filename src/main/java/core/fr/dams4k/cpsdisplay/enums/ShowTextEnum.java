package fr.dams4k.cpsdisplay.enums;

import net.minecraft.client.resources.I18n;

public enum ShowTextEnum {
	ENABLE(true, "cpsdisplay.button.enable"),
	DISABLE(false, "cpsdisplay.button.disable");

	private final boolean enable;
	private final String text;
	
	ShowTextEnum(boolean enable, String text) {
		this.enable = enable;
		this.text = text;
	}
	
	public boolean getBool() {
		return enable;
	}
	
	public String getText() {
		return I18n.format(text, new Object[0]);
	}
	
	public static ShowTextEnum getByBool(boolean bool) {
		for (ShowTextEnum val : ShowTextEnum.values()) {
			if (val.getBool() == bool) {
				return val;
			}
		}
		return ShowTextEnum.ENABLE;
	}
	
	public static ShowTextEnum getByText(String name) {
		for (ShowTextEnum val : ShowTextEnum.values()) {
			if (val.getText() == name) {
				return val;
			}
		}
		return ShowTextEnum.ENABLE;
	}
}
