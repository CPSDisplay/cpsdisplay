package fr.dams4k.cpsmod.v1_8.events;

import fr.dams4k.cpsmod.v1_8.gui.GuiConfig;
import fr.dams4k.cpsmod.v1_8.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ClientEvents {
	@SubscribeEvent
	public void onUpdate(ClientTickEvent event) {
		if (ClientProxy.CPS_OVERLAY_CONFIG.isKeyDown()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiConfig());
		}
	}
}
