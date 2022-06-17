package fr.dams4k.cpsdisplay.v1_8.events;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.dams4k.cpsdisplay.core.VersionChecker;
import fr.dams4k.cpsdisplay.v1_8.References;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
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

						// BRACKETS
						ChatStyle bracketStyle = new ChatStyle();
						bracketStyle.setBold(true);
						bracketStyle.setColor(EnumChatFormatting.RED);

						IChatComponent leftBracketMessage = new ChatComponentText("[");
						IChatComponent rightBracketMessage = new ChatComponentText("] ");
						leftBracketMessage.setChatStyle(bracketStyle);
						rightBracketMessage.setChatStyle(bracketStyle);

						// MOD NAME
						ChatStyle modNameStyle = new ChatStyle();
						modNameStyle.setColor(EnumChatFormatting.GRAY);
						IChatComponent modNameMessage = new ChatComponentText("CPS Display");
						modNameMessage.setChatStyle(modNameStyle);

						// INFO
						ChatStyle infoStyle = new ChatStyle();
						infoStyle.setColor(EnumChatFormatting.YELLOW);
						IChatComponent infoMessage = new ChatComponentText("A newer version of this mod exist: ");
						infoMessage.setChatStyle(infoStyle);

						// LINK
						IChatComponent link = new ChatComponentText("download here");
						ChatStyle style = new ChatStyle().setChatClickEvent(new ClickEvent(Action.OPEN_URL, "https://www.curseforge.com/minecraft/mc-mods/cps-mod/files"));
						style.setUnderlined(true);
						style.setBold(true);
						style.setColor(EnumChatFormatting.GREEN);
						link.setChatStyle(style);


						// WHOLE MESSAGE
						IChatComponent message = new ChatComponentText("");
						message.appendSibling(leftBracketMessage);
						message.appendSibling(modNameMessage);
						message.appendSibling(rightBracketMessage);
						message.appendSibling(infoMessage);
						message.appendSibling(link);

						player.addChatMessage(message);
						break;
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			updateMessageSent = true;
		}
	}
}
