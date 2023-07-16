package fr.dams4k.cpsdisplay;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.dams4k.cpsdisplay.config.VersionManagerConfig;
import net.minecraftforge.common.MinecraftForge;

public class VersionManager {
    public String latestVersion = "0.0.0";
    public String latestReleaseURL = "";

    public VersionManager() {
        this.loadLatestVersion();
    }
    
    public void loadLatestVersion() {
        String mcVersion = "mc" + MinecraftForge.MC_VERSION;
        
        try {
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
                clearedLatestVersion.add(VersionManagerConfig.majorUpdate ? splitedLastestVersion[0] : "0");
                clearedLatestVersion.add(VersionManagerConfig.minorUpdate ? splitedLastestVersion[1] : "0");
                clearedLatestVersion.add(VersionManagerConfig.patchUpdate ? splitedLastestVersion[2] : "0");

                latestVersion = String.join(".", clearedLatestVersion);
                latestReleaseURL = object.get("browser_download_url").getAsString();
            }
        } catch (IOException e) {
            // Nothing lol
        }
    }
}
