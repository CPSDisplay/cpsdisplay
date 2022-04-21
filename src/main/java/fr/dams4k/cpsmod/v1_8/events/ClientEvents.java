package fr.dams4k.cpsmod.v1_8.events;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import fr.dams4k.cpsmod.v1_8.gui.GuiConfig;
import fr.dams4k.cpsmod.v1_8.gui.GuiOverlay;
import fr.dams4k.cpsmod.v1_8.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ClientEvents {
	private List<Long> leftClicks = new ArrayList<>();
	private List<Long> rightClicks = new ArrayList<>();

	private GameSettings gs = Minecraft.getMinecraft().gameSettings;
	
	
	@SubscribeEvent
	public void onRenderGui(RenderGameOverlayEvent.Post game_overlay_event) {
		if (game_overlay_event.type == ElementType.HOTBAR && !(Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu)) {
			new GuiOverlay(Minecraft.getMinecraft(), this.getLeftCPS(), this.getRightCPS());
		}
	}
	
	@SubscribeEvent
	public void onKeyPress(InputEvent.MouseInputEvent event) {
		if (Mouse.getEventButtonState()) {
			if (gs.keyBindAttack.isKeyDown() && Mouse.getEventButton() == gs.keyBindAttack.getKeyCode()+100) leftClicks.add(System.currentTimeMillis());
			if (gs.keyBindUseItem.isKeyDown() && Mouse.getEventButton() == gs.keyBindUseItem.getKeyCode()+100) rightClicks.add(System.currentTimeMillis());
		}
	}

	public int getLeftCPS() {
		long current_time = System.currentTimeMillis();
		this.leftClicks.removeIf(e -> (e.longValue() + 1000L < current_time));
		return leftClicks.size();
	}

	public int getRightCPS() {
		long current_time = System.currentTimeMillis();
		this.rightClicks.removeIf(e -> (e.longValue() + 1000L < current_time));
		return rightClicks.size();
	}
	
	@SubscribeEvent
	public void onUpdate(ClientTickEvent event) {
		if (ClientProxy.CPS_OVERLAY_CONFIG.isKeyDown()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiConfig());
		}
	}
}
