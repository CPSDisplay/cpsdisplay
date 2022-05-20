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
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;


public class EventHandler {
	private boolean upToDateMessageSent = false;

	private List<Long> leftClicks = new ArrayList<Long>();
	private List<Long> rightClicks = new ArrayList<Long>();

	private GameSettings gs = Minecraft.getMinecraft().gameSettings;
	
	@SubscribeEvent
	public void onClientJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayerSP && !upToDateMessageSent) {
			try {
				URL githubTagsURL = new URL("https://api.github.com/repos/Dams4K/Minecraft-CPSMod/tags");
				
				Scanner scanner = new Scanner(githubTagsURL.openStream());
				String response = scanner.useDelimiter("\\Z").next();
				JsonParser parser = new JsonParser();
				JsonArray json = (JsonArray) parser.parse(response);

				VersionChecker modVersion = new VersionChecker("1.2.0");

				for (int i = 0; i < json.size(); i++) {
					JsonObject object = (JsonObject) json.get(i);

					String objectVersion = object.get("name").getAsString();
					if (modVersion.compareTo(objectVersion) == VersionChecker.LOWER) {
						EntityPlayerSP player = (EntityPlayerSP) event.entity;
						player.addChatMessage(new ChatComponentText("msg"));
					}
				}

				// System.out.println(json);

				// VersionChecker version = new VersionChecker("1.10.2");
				// VersionChecker version2 = new VersionChecker("1-10-2", "-");
				
				// System.out.println(version.compareTo("1.10.2"));
				// System.out.println(version.compareTo("1.10.3"));
				// System.out.println(version.compareTo("1.11.2"));
				// System.out.println(version.compareTo("1.9.5"));
				// System.out.println(version2.compareTo("1-9-5"));

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			upToDateMessageSent = true;
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
