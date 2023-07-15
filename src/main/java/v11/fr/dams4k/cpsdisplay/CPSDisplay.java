package fr.dams4k.cpsdisplay;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.dams4k.cpsdisplay.config.ModConfig;
import fr.dams4k.cpsdisplay.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = References.MOD_ID, name = References.MOD_NAME, clientSideOnly = true, acceptableRemoteVersions = "*", version = References.MOD_VERSION)
public class CPSDisplay {
    @SidedProxy(clientSide = "fr.dams4k.cpsdisplay.proxy.ClientProxy")
    public static ClientProxy proxy;

    public static String latestVersion = "0.0.0";
    public static String latestReleaseURL = "";

    public static void loadLatestVersion() throws IOException {
        String mcVersion = "mc" + Minecraft.getMinecraft().getVersion();

        URL githubTagsURL = new URL(References.MOD_GITHUB_LASTEST_RELEASE);
        
        Scanner scanner = new Scanner(githubTagsURL.openStream());
        String response = scanner.useDelimiter("\\Z").next();
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(response);

        JsonArray assets = (JsonArray) jsonObject.getAsJsonArray("assets");
        for (int i = 0; i < assets.size(); i++) {
            JsonObject object = (JsonObject) assets.get(i);
            String assetName = object.get("name").getAsString();
            if (!assetName.contains(mcVersion) || assetName.contains("sources")) continue; // Check if the current minecraft version has the update
            
            String[] splitedLastestVersion = jsonObject.get("tag_name").getAsString().split("\\.");

            List<String> clearedLatestVersion = new ArrayList<>();
            clearedLatestVersion.add(ModConfig.majorUpdate ? splitedLastestVersion[0] : "0");
            clearedLatestVersion.add(ModConfig.minorUpdate ? splitedLastestVersion[1] : "0");
            clearedLatestVersion.add(ModConfig.patchUpdate ? splitedLastestVersion[2] : "0");

            CPSDisplay.latestVersion = String.join(".", clearedLatestVersion);
            CPSDisplay.latestReleaseURL = object.get("browser_download_url").getAsString();   
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
        try {
            loadLatestVersion();
        } catch (IOException e) {
            // Nothing lol
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
