package fr.dams4k.cpsdisplay.v1_8.enums;

public enum ShowTextEnum {
	ENABLE(true, "§aEnable"),
	DISABLE(false, "§cDisable");

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
		return text;
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
