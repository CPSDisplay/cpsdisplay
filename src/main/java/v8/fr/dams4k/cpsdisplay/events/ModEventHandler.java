package fr.dams4k.cpsdisplay.events;

import java.util.ArrayList;
import java.util.List;

import fr.dams4k.cpsdisplay.gui.GuiConfig;
import fr.dams4k.cpsdisplay.gui.GuiOverlay;
import fr.dams4k.cpsdisplay.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;


public class ModEventHandler {
    private boolean attackIsPressed = false;
    private boolean useIsPressed = false;

    private List<Long> attackClicks = new ArrayList<Long>();
	private List<Long> useClicks = new ArrayList<Long>();

	private GameSettings gs = Minecraft.getMinecraft().gameSettings;

	@SubscribeEvent
	public void onRenderGui(RenderGameOverlayEvent.Post gameOverlayEvent) {
		if (gameOverlayEvent.type == ElementType.HOTBAR && !(Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu)) {
			new GuiOverlay(Minecraft.getMinecraft(), this.getAttackCPS(), this.getUseCPS());
		}
	}
	
	@SubscribeEvent
	public void onNewTick(ClientTickEvent event) {
		if (ClientProxy.CPS_OVERLAY_CONFIG.isKeyDown()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiConfig());
		}
	}

    @SubscribeEvent
    public void onMouseEvent(MouseEvent e) {
        this.onKeyPress();
    }

    @SubscribeEvent
    public void onKeyboardEvent(KeyInputEvent e) {
        this.onKeyPress();
    }

	public void onKeyPress() {
        if (gs.keyBindAttack.isKeyDown()) {
            if (!attackIsPressed) {
                attackIsPressed = true;
                attackClicks.add(System.currentTimeMillis());
            }
        } else {
            attackIsPressed = false;
        }
        
        if (gs.keyBindUseItem.isKeyDown()) {
            if (!useIsPressed) {
                useIsPressed = true;
                useClicks.add(System.currentTimeMillis());
            }
        } else {
            useIsPressed = false;
        }
	}

	public int getAttackCPS() {
		long currentTime = System.currentTimeMillis();
		this.attackClicks.removeIf(e -> (e.longValue() + 1000l < currentTime));
		return attackClicks.size();
	}

	public int getUseCPS() {
		long currentTime = System.currentTimeMillis();
		this.useClicks.removeIf(e -> (e.longValue() + 1000l < currentTime));
		return useClicks.size();
	}
}
