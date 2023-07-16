package fr.dams4k.cpstweaker;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import fr.dams4k.cpsdisplay.References;
import fr.dams4k.cpsdisplay.VersionChecker;
import fr.dams4k.cpsdisplay.VersionManager;
import fr.dams4k.cpsdisplay.config.VersionManagerConfig;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.CoreModManager;
import scala.reflect.internal.util.SourceFile;

public class CPSTweaker implements ITweaker {
    public CPSTweaker() {
        System.out.println("CPSTweaker successfuly loaded");
        getSourceFiles(this.getClass());
        // try {
        //     Method loadCoreMod = CoreModManager.class.getDeclaredMethod("loadCoreMod",
        //             new Class[] { LaunchClassLoader.class, String.class, File.class });
        //     loadCoreMod.setAccessible(true);

        // } catch (NoSuchMethodException e) {
        //     e.printStackTrace();
        // } catch (SecurityException e) {
        //     e.printStackTrace();
        // }
        System.out.println(CPSDelayedTweaker.isRequired());
        // Load eveythings
        VersionManagerConfig.preInit();
        VersionManager cpsVersionManager = new VersionManager();

        VersionChecker versionChecker = new VersionChecker(References.MOD_VERSION);
        System.out.println(cpsVersionManager.latestVersion);
        if (versionChecker.compareTo(cpsVersionManager.latestVersion) == VersionChecker.LOWER) {
            System.out.println("Download new version: " + cpsVersionManager.latestReleaseURL);
        } else {
            System.out.println("Mod is up to date");
        }
    }

    private List<SourceFile> getSourceFiles(Class<?> tweakerClass) {
        String tweakerClassName = tweakerClass.getName();
        List<SourceFile> sourceFiles = new ArrayList<>();
        for (URL url : Launch.classLoader.getSources()) {
            try {
                URI uri = url.toURI();
                if (!"file".equals(uri.getScheme()))
                    continue;
                File file = new File(uri);
                if (!file.exists() || !file.isFile())
                    continue;
                String tweakClass = null;
                String coreMod = null;
                boolean mixin = false;
                try (JarFile jar = new JarFile(file)) {
                    if (jar.getManifest() != null) {
                        Attributes attributes = jar.getManifest().getMainAttributes();
                        tweakClass = attributes.getValue("TweakClass");
                        coreMod = attributes.getValue("FMLCorePlugin");
                        mixin = (attributes.getValue("MixinConfigs") != null);
                    }
                }
                System.out.println(file.getName());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sourceFiles;
    }

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
    }

    @Override
    public String getLaunchTarget() {
        return "";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }

}
