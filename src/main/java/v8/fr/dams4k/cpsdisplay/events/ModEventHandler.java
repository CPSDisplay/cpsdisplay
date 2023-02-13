package v8.fr.dams4k.cpsdisplay.events;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import v8.fr.dams4k.cpsdisplay.gui.GuiConfig;
import v8.fr.dams4k.cpsdisplay.gui.GuiOverlay;
import v8.fr.dams4k.cpsdisplay.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;


public class ModEventHandler {
	private List<Long> leftClicks = new ArrayList<Long>();
	private List<Long> rightClicks = new ArrayList<Long>();

	private GameSettings gs = Minecraft.getMinecraft().gameSettings;

	@SubscribeEvent
	public void onRenderGui(RenderGameOverlayEvent.Post gameOverlayEvent) {
		if (gameOverlayEvent.type == ElementType.HOTBAR && !(Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu)) {
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
		long currentTime = System.currentTimeMillis();
		this.leftClicks.removeIf(e -> (e.longValue() + 1000l < currentTime));
		return leftClicks.size();
	}

	public int getRightCPS() {
		long currentTime = System.currentTimeMillis();
		this.rightClicks.removeIf(e -> (e.longValue() + 1000l < currentTime));
		return rightClicks.size();
	}
}
