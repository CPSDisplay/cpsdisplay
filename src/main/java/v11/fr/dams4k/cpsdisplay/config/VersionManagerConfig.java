package fr.dams4k.cpsdisplay.config;

import java.io.File;

import fr.dams4k.cpsdisplay.References;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

public class VersionManagerConfig {
    private static Configuration config;

    public static final String CATEGORY_VERSION_MANAGER = "version_manager";
    
    public static boolean majorUpdate = true;
    public static boolean minorUpdate = true;
    public static boolean patchUpdate = true;
    public static boolean autoUpdate = true;

    private static Property majorUpdateProperty;
    private static Property minorUpdateProperty;
    private static Property patchUpdateProperty;
    private static Property autoUpdateProperty;

    public static void preInit() {
		File configFile = new File(Loader.instance().getConfigDir(), References.MOD_ID + "_vm.cfg");
		config = new Configuration(configFile);
		config.load();
        loadConfig();
	}

    public static void loadConfig() {
        majorUpdateProperty = config.get(CATEGORY_VERSION_MANAGER, "major", majorUpdate);
        minorUpdateProperty = config.get(CATEGORY_VERSION_MANAGER, "minor", minorUpdate);
        patchUpdateProperty = config.get(CATEGORY_VERSION_MANAGER, "patch", patchUpdate);
        autoUpdateProperty = config.get(CATEGORY_VERSION_MANAGER, "auto_update", autoUpdate);

        majorUpdate = majorUpdateProperty.getBoolean();
        minorUpdate = minorUpdateProperty.getBoolean();
        patchUpdate = patchUpdateProperty.getBoolean();
        autoUpdate = autoUpdateProperty.getBoolean();
    }

    public static void saveConfig() {
        majorUpdateProperty.set(majorUpdate);
        minorUpdateProperty.set(minorUpdate);
        patchUpdateProperty.set(patchUpdate);
        autoUpdateProperty.set(autoUpdate);

		if (config.hasChanged()) {
			config.save();
		}
    }
}
