package fr.dams4k.cpsdisplay;

import java.util.regex.Pattern;

public class VersionChecker {
    public static int SAME = 0;
    public static int LOWER = 1;
    public static int UPPER = 2;

    public enum VersionDiff {
        PATCH,
        MINOR,
        MAJOR
    }

    private String[] XYZ;
    private String separator = "";

    public VersionChecker(String version) {
        this(version, ".");
    }

    public VersionChecker(String version, String separator) {
        this.separator = separator;
        this.XYZ = split(version);
    }

    private String[] split(String str) {
        return str.split(Pattern.quote(separator));
    }

    public int compareTo(String otherVersion) {
        String[] otherXYZ = split(otherVersion);

        for (int i = 0; i < XYZ.length; i++) {
            int n1 = Integer.valueOf(XYZ[i]);
            int n2 = Integer.valueOf(otherXYZ[i]);

            if (n1 < n2) return VersionChecker.LOWER;
            if (n1 > n2) return VersionChecker.UPPER;
        }

        return VersionChecker.SAME;
    }

    public VersionDiff getVersionDifference(String otherVersion) {
        String[] otherXYZ = split(otherVersion);

        if (!otherXYZ[0].equals(XYZ[0])) return VersionDiff.MAJOR;
        if (!otherXYZ[1].equals(XYZ[1])) return VersionDiff.MINOR;
        if (!otherXYZ[2].equals(XYZ[2])) return VersionDiff.PATCH;

        return null;
    }
}
