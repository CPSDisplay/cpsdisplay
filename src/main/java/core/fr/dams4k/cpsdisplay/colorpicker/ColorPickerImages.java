package fr.dams4k.cpsdisplay.colorpicker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class ColorPickerImages {

    public enum ImageDisposition {
        HORIZONTAL,
        VERTICAL;
    }

    public static BufferedImage svColorSelector(float h, int sizeX, int sizeY) {
        BufferedImage imageOut = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                int rgb = Color.HSBtoRGB(h, x/((float) sizeX-1), 1f-y/((float) sizeY-1));
                imageOut.setRGB(x, y, rgb);
            }
        }

        return imageOut;
    }

    public static BufferedImage hColorSelector(int sizeX, int sizeY) {
        return ColorPickerImages.hColorSelector(sizeX, sizeY, ImageDisposition.VERTICAL);
    }

    public static BufferedImage hColorSelector(int sizeX, int sizeY, ImageDisposition disposition) {
        BufferedImage imageOut = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                int rgb = 0;
                switch (disposition) {
                    case HORIZONTAL:
                        rgb = Color.HSBtoRGB(x/((float) sizeX-1), 1f, 1f);
                        break;
                    case VERTICAL:
                        rgb = Color.HSBtoRGB(y/((float) sizeY-1), 1f, 1f);
                        break;   
                }
                imageOut.setRGB(x, y, rgb);
            }
        }

        return imageOut;
    }

    public static BufferedImage createGradient(int sizeX, int sizeY, List<Color> colors) {
        BufferedImage imageOut = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        
        int gradientWidth = (int) (sizeX/(colors.size()-1));

        for (int i = 0; i < colors.size()-1; i++) {
            Color startColor = colors.get(i);
            Color endColor = colors.get(i+1);

            for (int x = 0; x < gradientWidth; x++) {
                float diff = (1f / gradientWidth) * x;
                int rgb = ColorPickerImages.lerp(diff, startColor, endColor).getRGB();
                for (int y = 0; y < sizeY; y++) {
                    imageOut.setRGB(gradientWidth * i + x, y, rgb);
                }
            }
        }

        return imageOut;
    }
    public static BufferedImage createAGradient(int sizeX, int sizeY, List<Color> colors) {
        BufferedImage imageOut = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = imageOut.createGraphics();
        
        int gradientWidth = (int) (sizeX/(colors.size()-1));

        for (int i = 0; i < colors.size()-1; i++) {
            Color startColor = colors.get(i);
            Color endColor = colors.get(i+1);

            for (int x = 0; x < gradientWidth; x++) {
                float diff = (1f / gradientWidth) * x;
                Color color = ColorPickerImages.lerp(diff, startColor, endColor);
                graphics2D.setColor(color);
                graphics2D.drawRect(x, 0, 1, sizeY);
            }
        }
        graphics2D.dispose();

        return imageOut;
    }

    public static Color lerp(float value, Color startColor, Color endColor) {
        float startR = startColor.getRed()/255f;
        float startG = startColor.getGreen()/255f;
        float startB = startColor.getBlue()/255f;
        float startA = startColor.getAlpha()/255f;
        float endR = endColor.getRed()/255f;
        float endG = endColor.getGreen()/255f;
        float endB = endColor.getBlue()/255f;
        float endA = endColor.getAlpha()/255f;

        float r = startR + (endR - startR) * value;
        float g = startG + (endG - startG) * value;
        float b = startB + (endB - startB) * value;
        float a = startA + (endA - startA) * value;

        return new Color(r, g, b, a);
    }
}
