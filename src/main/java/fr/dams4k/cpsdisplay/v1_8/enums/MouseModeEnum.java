package fr.dams4k.cpsdisplay.v1_8.enums;

public enum MouseModeEnum {
	LEFT(0, "Left", "{0} CPS"),
	RIGHT(1, "Right", "{1} CPS"),
	LEFT_RIGHT(2, "Left & Right", "[{0} | {1}] CPS"),
	CUSTOM(3, "Custom", "");

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
		return name;
	}
	
	public String getText() {
		return text;
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
	
	public static MouseModeEnum getByText(String str) {
		for (MouseModeEnum val : MouseModeEnum.values()) {
			if (val.getText() == str) {
				return val;
			}
		}
		return MouseModeEnum.CUSTOM;
	}
}
