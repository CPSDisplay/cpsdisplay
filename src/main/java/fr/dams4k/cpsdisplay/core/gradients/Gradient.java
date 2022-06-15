package fr.dams4k.cpsdisplay.core.gradients;

import java.awt.Color;
import java.util.List;

public class Gradient {
    private List<Color> colors;

    public void getPixel(int x, int width) {
        int gradientWidth = (int) width/(colors.size()-1);
        x = fmod(x, gradientWidth);
    }

    public static int fmod(int a, int b) {
        int result = (int) Math.floor(a / b);
        return a - result * b;
    }
}
