package fr.dams4k.cpsdisplay.core.colorchooser;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageGenerators {
    public static BufferedImage sbColorSelector(float h) {
        return sbColorSelector(h, 256, 256);
    }

    public static BufferedImage sbColorSelector(float h, int size_x, int size_y) {
        BufferedImage imageOut = new BufferedImage(size_x, size_y, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < size_y; y++) {
            for (int x = 0; x < size_x; x++) {
                int rgb = Color.HSBtoRGB(h, x/((float) size_x-1), 1f-y/((float) size_y-1));
                imageOut.setRGB(x, y, rgb);
            }
        }

        return imageOut;
    }

    public static BufferedImage hColorSelector() {
        return hColorSelector(25, 256);
    }

    public static BufferedImage hColorSelector(int size_x, int size_y) {
        BufferedImage imageOut = new BufferedImage(size_x, size_y, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < size_y; y++) {
            for (int x = 0; x < size_x; x++) {
                int rgb = Color.HSBtoRGB(y/((float) size_y-1), 1f, 1f);
                imageOut.setRGB(x, y, rgb);
            }
        }

        return imageOut;
    }
}
