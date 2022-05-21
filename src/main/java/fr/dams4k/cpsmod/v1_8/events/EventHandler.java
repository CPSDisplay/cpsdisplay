package fr.dams4k.cpsmod.v1_8.events;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.lwjgl.input.Mouse;

import fr.dams4k.cpsmod.core.VersionChecker;
import fr.dams4k.cpsmod.v1_8.gui.GuiConfig;
import fr.dams4k.cpsmod.v1_8.gui.GuiOverlay;
import fr.dams4k.cpsmod.v1_8.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.config.GuiConfigEntries.ChatColorEntry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;


public class EventHandler {
	private boolean updateMessageSent = false;

	private List<Long> leftClicks = new ArrayList<Long>();
	private List<Long> rightClicks = new ArrayList<Long>();

	private GameSettings gs = Minecraft.getMinecraft().gameSettings;
	
	@SubscribeEvent
	public void onClientJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayerSP && !updateMessageSent) {
			try {
				URL githubTagsURL = new URL("https://api.github.com/repos/Dams4K/Minecraft-CPSMod/tags");
				
				Scanner scanner = new Scanner(githubTagsURL.openStream());
				String response = scanner.useDelimiter("\\Z").next();
				JsonParser parser = new JsonParser();
				JsonArray json = (JsonArray) parser.parse(response);

				VersionChecker modVersion = new VersionChecker("1.2.0"); // TODO: get real mod version with mcmod.info file

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

	@SubscribeEvent
	public void onRenderGui(RenderGameOverlayEvent.Post game_overlay_event) {
		if (game_overlay_event.type == ElementType.HOTBAR && !(Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu)) {
			new GuiOverlay(Minecraft.getMinecraft(), this.getLeftCPS(), this.getRightCPS());
		}
	}
	
	@SubscribeEvent
	public void onNewTick(ClientTickEvent event) {
		if (ClientProxy.CPS_OVERLAY_CONFIG.isKeyDown()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiConfig());
		}
	}

	@SubscribeEvent
	public void onKeyPress(InputEvent.MouseInputEvent event) {
		if (Mouse.getEventButtonState()) {
			if (gs.keyBindAttack.isKeyDown() && Mouse.getEventButton() == gs.keyBindAttack.getKeyCode()+100)
				leftClicks.add(System.currentTimeMillis());
			
			if (gs.keyBindUseItem.isKeyDown() && Mouse.getEventButton() == gs.keyBindUseItem.getKeyCode()+100)
				rightClicks.add(System.currentTimeMillis());
		}
	}

	public int getLeftCPS() {
		long current_time = System.currentTimeMillis();
		this.leftClicks.removeIf(e -> (e.longValue() + 1000l < current_time));
		return leftClicks.size();
	}

	public int getRightCPS() {
		long current_time = System.currentTimeMillis();
		this.rightClicks.removeIf(e -> (e.longValue() + 1000l < current_time));
		return rightClicks.size();
	}
}
