package fr.dams4k.cpsdisplay;

import java.io.File;
import java.util.List;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class CPSTweaker implements ITweaker {
    public CPSTweaker() {
        System.out.println("CPSTweaker successfuly loaded");
        // Load eveythings
        CPSVersionManager cpsVersionManager = CPSVersionManager.instance;

        VersionChecker versionChecker = new VersionChecker(References.MOD_VERSION);
        System.out.println(cpsVersionManager.latestVersion);
        if (versionChecker.compareTo(cpsVersionManager.latestVersion) == VersionChecker.LOWER) {
            System.out.println("Download new version: " + cpsVersionManager.latestReleaseURL);
        } else {
            System.out.println("Mod is up to date");
        }
    }

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {}

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {}

    @Override
    public String getLaunchTarget() {
        return "";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
    
}
