package fr.dams4k.cpsdisplay.core.utils;

// i name all my class like that when it's for random functions
public class Utils {
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
