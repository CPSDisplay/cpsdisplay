package fr.dams4k.cpsdisplay.v1_8.events;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.dams4k.cpsdisplay.core.References;
import fr.dams4k.cpsdisplay.core.VersionChecker;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class VersionCheckerEvent {
	private boolean updateMessageSent = false;
    
    @SubscribeEvent
	public void onClientJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayerSP && !updateMessageSent) {
			try {
				URL githubTagsURL = new URL("https://api.github.com/repos/Dams4K/minecraft-cpsdisplay/tags");
				
				Scanner scanner = new Scanner(githubTagsURL.openStream());
				String response = scanner.useDelimiter("\\Z").next();
				JsonParser parser = new JsonParser();
				JsonArray json = (JsonArray) parser.parse(response);

				VersionChecker modVersion = new VersionChecker(References.MOD_VERSION);

				for (int i = 0; i < json.size(); i++) {
					JsonObject object = (JsonObject) json.get(i);

					String objectVersion = object.get("name").getAsString();
					if (modVersion.compareTo(objectVersion) == VersionChecker.LOWER) {
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
			}

			updateMessageSent = true;
		}
	}
}
