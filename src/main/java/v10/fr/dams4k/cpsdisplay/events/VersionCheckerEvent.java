package fr.dams4k.cpsdisplay.events;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.dams4k.cpsdisplay.References;
import fr.dams4k.cpsdisplay.VersionChecker;
import fr.dams4k.cpsdisplay.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class VersionCheckerEvent {
    private boolean updateMessageSent = false;

    @SubscribeEvent
    public void onClientJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayerSP && !updateMessageSent) {
            try {
				URL githubTagsURL = new URL(References.MOD_GITHUB_LASTEST_RELEASE);
				
				Scanner scanner = new Scanner(githubTagsURL.openStream());
				String response = scanner.useDelimiter("\\Z").next();
				JsonParser parser = new JsonParser();
                JsonObject jsonObject = (JsonObject) parser.parse(response);
                JsonArray assets = (JsonArray) jsonObject.getAsJsonArray("assets");

                String[] splitedLastestVersion = jsonObject.get("tag_name").getAsString().split("\\.");

                List<String> clearedLatestVersion = new ArrayList<>();
                clearedLatestVersion.add(ModConfig.majorUpdate ? splitedLastestVersion[0] : "0");
                clearedLatestVersion.add(ModConfig.minorUpdate ? splitedLastestVersion[1] : "0");
                clearedLatestVersion.add(ModConfig.patchUpdate ? splitedLastestVersion[2] : "0");

                String latestVersion = String.join(".", clearedLatestVersion);
				VersionChecker modVersionChecker = new VersionChecker(References.MOD_VERSION);
                if (modVersionChecker.compareTo(latestVersion) == VersionChecker.LOWER) {
                    String mcVersion = "mc" + Minecraft.getMinecraft().getVersion();

                    for (int i = 0; i < assets.size(); i++) {
                        JsonObject object = (JsonObject) assets.get(i);
                        String assetName = object.get("name").getAsString();
                        if (!assetName.contains(mcVersion) || assetName.contains("sources")) continue;
                        
                        EntityPlayerSP player = (EntityPlayerSP) event.getEntity();
                        
                        // MOD NAME
						ITextComponent modNameMessage = new TextComponentString(I18n.format("cpsdisplay.version.mod_name", new Object[0]));

						// DESCRIPTION
						ITextComponent description = new TextComponentString(I18n.format("cpsdisplay.version.description", new Object[0]));

						// LINK
						ITextComponent link = new TextComponentString(I18n.format("cpsdisplay.version.url", new Object[0]));
                        Style style = new Style().setClickEvent(new ClickEvent(Action.OPEN_URL, References.MOD_DOWNLOAD_URL));
						link.setStyle(style);

						// WHOLE MESSAGE
						ITextComponent message = new TextComponentString("");

						message.appendSibling(modNameMessage);
						message.appendText(" ");
						message.appendSibling(description);
						message.appendText(" ");
						message.appendSibling(link);

						player.addChatMessage(message);
						break;
                    }
                }
            } catch (MalformedURLException e) {
			} catch (IOException e) {
			} finally {
                MinecraftForge.EVENT_BUS.unregister(this);
            }

            updateMessageSent = true;
        }
    }
}
