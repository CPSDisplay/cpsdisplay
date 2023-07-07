package fr.dams4k.cpsdisplay;

import java.awt.Color;
import java.util.Collections;

public class ColorConverter {
	private static final String HEX_CHARS = "0123456789abcdef";

    public static Color HexToColor(String givenHex, int size) {
		givenHex = givenHex.replace("#", "");
		givenHex = givenHex.toLowerCase();
		String hex = "";

		// Make sure each characters are an hex value
		for (char c : givenHex.toCharArray()) {
			String sC = String.valueOf(c);
			if (HEX_CHARS.contains(sC)) {
				hex += sC;
			} else {
				hex += "0";
			}
		}
		// Fill in the missing characters
		hex += String.join("", Collections.nCopies(Math.max(0, size-hex.length()), "0"));
		switch ((hex.substring(0, size)).length()) { // Use the desired size
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
		return Color.WHITE; // Default is white
	}

	public static String ColorToHex(Color color) {
		// Returned string will be rrggbbaa
		int[] rgba = {color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()};
		String hexValue = "";

		for (int i = 0; i < rgba.length; i ++) {
			int intChannelValue = rgba[i];
			String hexChannelValue = Integer.toHexString(intChannelValue);

			// Make sure channel string is 2" long
			while (hexChannelValue.length() < 2) {
				hexChannelValue += "0";
			}
			hexValue += hexChannelValue;
		}

		// Make sure final string is 8" long
		while (hexValue.length() < 8) {
			hexValue += "0";
		}
		
		return hexValue.substring(0, 8);
	}
}
