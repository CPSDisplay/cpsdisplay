package fr.dams4k.cpsdisplay.enums;

import net.minecraft.client.resources.I18n;

public enum ToggleEnum {
	ENABLE(true, "cpsdisplay.button.enable"),
	DISABLE(false, "cpsdisplay.button.disable");
    
    private final boolean enable;
	private final String text;

    ToggleEnum(boolean enable, String text) {
		this.enable = enable;
		this.text = text;
	}
	
	public boolean isEnabled() {
		return this.enable;
	}

    public String getText() {
		return I18n.format(this.text, new Object[0]);
	}

    public ToggleEnum toggle() {
        return ToggleEnum.get(!this.enable);
    }

    public static ToggleEnum get(boolean enable) {
        for (ToggleEnum val : ToggleEnum.values()) {
			if (val.isEnabled() == enable) {
				return val;
			}
		}
        return null;
    }

    public static ToggleEnum get(String text) {
        for (ToggleEnum val : ToggleEnum.values()) {
			if (val.getText() == I18n.format(text, new Object[0])) {
				return val;
			}
		}
        return null;
    }
}
