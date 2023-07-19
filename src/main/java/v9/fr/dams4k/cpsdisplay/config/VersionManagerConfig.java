package fr.dams4k.cpsdisplay.config;

import java.io.File;
import java.nio.file.Path;

import fr.dams4k.cpsdisplay.References;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class VersionManagerConfig {
    private static Configuration config;

    public static final String CATEGORY_VERSION_MANAGER = "version_manager";
    
    public static boolean majorUpdate = true;
    public static boolean minorUpdate = true;
    public static boolean patchUpdate = true;
    public static boolean autoUpdate = true;

    public static String latestVersion = "0.0.0";
    
    private static Property majorUpdateProperty;
    private static Property minorUpdateProperty;
    private static Property patchUpdateProperty;
    private static Property autoUpdateProperty;

    private static Property latestVersionProperty;

    public static void preInit() {
		if (Launch.minecraftHome == null) {
			Launch.minecraftHome = new File(".");
		}

        Path configFolder = Launch.minecraftHome.toPath().resolve("config").resolve(References.MOD_ID);
		File configFile = new File(configFolder.toString(), "version_manager.cfg");

		config = new Configuration(configFile);
		config.load();
        loadConfig();
	}

    public static void loadConfig() {
        majorUpdateProperty = config.get(CATEGORY_VERSION_MANAGER, "major", majorUpdate);
        minorUpdateProperty = config.get(CATEGORY_VERSION_MANAGER, "minor", minorUpdate);
        patchUpdateProperty = config.get(CATEGORY_VERSION_MANAGER, "patch", patchUpdate);
        autoUpdateProperty = config.get(CATEGORY_VERSION_MANAGER, "auto_update", autoUpdate);
        
        latestVersionProperty = config.get(CATEGORY_VERSION_MANAGER, "latest_version", latestVersion);


        majorUpdate = majorUpdateProperty.getBoolean();
        minorUpdate = minorUpdateProperty.getBoolean();
        patchUpdate = patchUpdateProperty.getBoolean();
        autoUpdate = autoUpdateProperty.getBoolean();

        latestVersion = latestVersionProperty.getString();
    }

    public static void saveConfig() {
        majorUpdateProperty.set(majorUpdate);
        minorUpdateProperty.set(minorUpdate);
        patchUpdateProperty.set(patchUpdate);
        autoUpdateProperty.set(autoUpdate);

        latestVersionProperty.set(latestVersion);

		if (config.hasChanged()) {
			config.save();
		}
    }
}
