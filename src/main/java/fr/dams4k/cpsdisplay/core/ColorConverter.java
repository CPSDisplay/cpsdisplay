package fr.dams4k.cpsdisplay.core;

import java.awt.Color;
import java.util.Collections;

public class ColorConverter {
    public static Color HexToColor(String hex, int size) {
		hex = hex.replace("#", "");;
		hex += String.join("", Collections.nCopies(Math.max(0, size-hex.length()), "0"));
		switch (hex.length()) {
			case 6:
				return new Color(
					Integer.valueOf(hex.substring(0, 2), 16),
					Integer.valueOf(hex.substring(2, 4), 16),
					Integer.valueOf(hex.substring(4, 6), 16)
				);
			case 8:
				return new Color(
					Integer.valueOf(hex.substring(0, 2), 16),
					Integer.valueOf(hex.substring(2, 4), 16),
					Integer.valueOf(hex.substring(4, 6), 16),
					Integer.valueOf(hex.substring(6, 8), 16)
				);
		}
		return null;
	}
}
