package fr.dams4k.cpstweaker;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.CoreModManager;

public class CPSSetupTweaker implements ITweaker {
    private static ArrayList<URL> newSourceURL = new ArrayList<>();

    public CPSSetupTweaker() throws Exception {
        VersionManagerConfig.preInit();
        VersionManager versionManager = new VersionManager();
        VersionChecker versionChecker = new VersionChecker(References.MOD_VERSION);

        if (versionChecker.compareTo(versionManager.latestVersion) == VersionChecker.LOWER && versionManager.latestReleaseURL != "" && VersionManagerConfig.autoUpdate) {
            Path currentJarPath = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            Path modsFolder = currentJarPath.getParent();
            Path newJarPath = Paths.get(modsFolder.toString(), Paths.get(versionManager.latestReleaseURL).getFileName().toString());

            File oldJarFile = currentJarPath.toFile();
            oldJarFile.delete();

            try {
                BufferedInputStream in = new BufferedInputStream(new URL(versionManager.latestReleaseURL).openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(newJarPath.toString());
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                newSourceURL.add(newJarPath.toUri().toURL());
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Forge forge = Forge.getIfPresent();
        if (forge != null) {
            forge.setupLoad(this);
        }
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

    private static interface Platform {
        String getVersion();

        default void setupLoad(CPSSetupTweaker tweaker) throws Exception {
        }
    }

    private static interface Forge extends Platform {
        static Forge getIfPresent() throws IOException {
            if (Launch.classLoader.getClassBytes("net.minecraftforge.common.ForgeVersion") != null)
                return getUnchecked();
            return null;
        }

        static Forge getUnchecked() {
            return new Impl();
        }

        public static class Impl implements Forge {
            private static final String MIXIN_TWEAKER = "org.spongepowered.asm.launch.MixinTweaker";

            public String getVersion() {
                try {
                    return "forge_" + ForgeVersion.class.getDeclaredField("mcVersion").get((Object) null);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                    return "unknown";
                }
            }

            public void setupLoad(CPSSetupTweaker tweaker) throws Exception {
                List<SourceFile> sourceFiles = getSourceFiles(tweaker.getClass());
                if (sourceFiles.isEmpty()) {
                    System.out.println("Not able to determine current file. Mod will NOT work");
                    return;
                }
                for (SourceFile sourceFile : sourceFiles) {
                    setupSourceFile(sourceFile);
                }
            }

            private void setupSourceFile(SourceFile sourceFile) throws Exception {
                Field ignoredModFile = CoreModManager.class.getDeclaredField("ignoredModFiles");
                ignoredModFile.setAccessible(true);
                ((List<?>) ignoredModFile.get((Object) null)).remove(sourceFile.file.getName());
                CoreModManager.getReparseableCoremods().add(sourceFile.file.getName());
                String coreMod = sourceFile.coreMod;
                if (coreMod != null && !sourceFile.mixin) {
                    Method loadCoreMod = CoreModManager.class.getDeclaredMethod("loadCoreMod",
                            new Class[] { LaunchClassLoader.class, String.class, File.class });
                    loadCoreMod.setAccessible(true);
                    ITweaker tweaker = (ITweaker) loadCoreMod.invoke((Object) null,
                            new Object[] { Launch.classLoader, coreMod, sourceFile.file });
                    ((List<ITweaker>) Launch.blackboard.get("Tweaks")).add(tweaker);
                }
                if (sourceFile.mixin)
                    try {
                        Method addContainer;
                        Object arg;
                        injectMixinTweaker();
                        Class<?> MixinBootstrap = Class.forName("org.spongepowered.asm.launch.MixinBootstrap");
                        Class<?> MixinPlatformManager = Class
                                .forName("org.spongepowered.asm.launch.platform.MixinPlatformManager");
                        Object platformManager = MixinBootstrap.getDeclaredMethod("getPlatform", new Class[0])
                                .invoke((Object) null, new Object[0]);
                        try {
                            addContainer = MixinPlatformManager.getDeclaredMethod("addContainer",
                                    new Class[] { URI.class });
                            arg = sourceFile.file.toURI();
                        } catch (NoSuchMethodException ignored) {
                            Class<?> IContainerHandle = Class
                                    .forName("org.spongepowered.asm.launch.platform.container.IContainerHandle");
                            Class<?> ContainerHandleURI = Class
                                    .forName("org.spongepowered.asm.launch.platform.container.ContainerHandleURI");
                            addContainer = MixinPlatformManager.getDeclaredMethod("addContainer",
                                    new Class[] { IContainerHandle });
                            arg = ContainerHandleURI.getDeclaredConstructor(new Class[] { URI.class })
                                    .newInstance(new Object[] { sourceFile.file.toURI() });
                        }
                        addContainer.invoke(platformManager, new Object[] { arg });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            private List<SourceFile> getSourceFiles(Class<?> tweakerClass) {
                String tweakerClassName = tweakerClass.getName();
                List<SourceFile> sourceFiles = new ArrayList<>();

                List<URL> sourceURLs = new ArrayList<>(newSourceURL);
                sourceURLs.addAll(Launch.classLoader.getSources());

                for (URL url : sourceURLs) {
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
                        
                        if (tweakerClassName.equals(tweakClass)) {
                            sourceFiles.add(new SourceFile(file, coreMod, mixin));   
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return sourceFiles;
            }

            private void injectMixinTweaker()
                    throws ClassNotFoundException, IllegalAccessException, InstantiationException {
                List<String> tweakClasses = (List<String>) Launch.blackboard.get("TweakClasses");
                if (tweakClasses.contains(MIXIN_TWEAKER)) {
                    initMixinTweaker();
                    return;
                }
                if (Launch.blackboard.get("mixin.initialised") != null)
                    return;
                System.out.println("Injecting MixinTweaker from EssentialSetupTweaker");
                List<ITweaker> tweaks = (List<ITweaker>) Launch.blackboard.get("Tweaks");
                tweaks.add(initMixinTweaker());
            }

            private ITweaker initMixinTweaker()
                    throws ClassNotFoundException, IllegalAccessException, InstantiationException {
                Launch.classLoader.addClassLoaderExclusion(MIXIN_TWEAKER.substring(0,
                        MIXIN_TWEAKER.lastIndexOf('.')));
                return (ITweaker) Class
                        .forName(MIXIN_TWEAKER, true, (ClassLoader) Launch.classLoader)
                        .newInstance();
            }

            private static class SourceFile {
                final File file;

                final String coreMod;

                final boolean mixin;

                private SourceFile(File file, String coreMod, boolean mixin) {
                    this.file = file;
                    this.coreMod = coreMod;
                    this.mixin = mixin;
                }
            }
        }
    }
}