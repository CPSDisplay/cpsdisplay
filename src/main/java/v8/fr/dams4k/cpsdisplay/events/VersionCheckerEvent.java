package fr.dams4k.cpsdisplay.events;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.dams4k.cpsdisplay.References;
import fr.dams4k.cpsdisplay.VersionChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class VersionCheckerEvent {
	private boolean updateMessageSent = false;
    
    @SubscribeEvent
	public void onClientJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayerSP && !updateMessageSent) {
			try {
				URL githubTagsURL = new URL(References.MOD_GITHUB_LASTEST_RELEASE);
				
				Scanner scanner = new Scanner(githubTagsURL.openStream());
				String response = scanner.useDelimiter("\\Z").next();
				JsonParser parser = new JsonParser();
                JsonObject jsonObject = (JsonObject) parser.parse(response);
                JsonArray assets = (JsonArray) jsonObject.getAsJsonArray("assets");

				VersionChecker modVersion = new VersionChecker(References.MOD_VERSION);
                String lastAssetVersion = jsonObject.get("tag_name").getAsString();
                if (modVersion.compareTo(lastAssetVersion) == VersionChecker.LOWER) {
                    String mcVersion = "" + Minecraft.getMinecraft().getVersion();
                    
                    for (int i = 0; i < assets.size(); i++) {
                        JsonObject object = (JsonObject) assets.get(i);
                        String assetName = object.get("name").getAsString();
                        if (!assetName.contains(mcVersion) || assetName.contains("sources")) continue;
                        
                        EntityPlayerSP player = (EntityPlayerSP) event.entity;

                        // MOD NAME
                        IChatComponent modNameMessage = new ChatComponentText(I18n.format("cpsdisplay.version.mod_name", new Object[0]));

                        // DESCRIPTION
                        IChatComponent description = new ChatComponentText(I18n.format("cpsdisplay.version.description", new Object[0]));

                        // LINK
                        IChatComponent link = new ChatComponentText(I18n.format("cpsdisplay.version.url", new Object[0]));
                        ChatStyle style = new ChatStyle().setChatClickEvent(new ClickEvent(Action.OPEN_URL, References.MOD_DOWNLOAD_URL));
                        link.setChatStyle(style);

                        // WHOLE MESSAGE
                        IChatComponent message = new ChatComponentText("");

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
