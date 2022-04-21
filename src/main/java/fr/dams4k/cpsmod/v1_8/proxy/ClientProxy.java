package fr.dams4k.cpsmod.v1_8.proxy;

import org.lwjgl.input.Keyboard;

import fr.dams4k.cpsmod.v1_8.commands.ConfigCommand;
import fr.dams4k.cpsmod.v1_8.config.Config;
import fr.dams4k.cpsmod.v1_8.events.ClientEvents;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	public static final KeyBinding CPS_OVERLAY_CONFIG = new KeyBinding("key.opengui", Keyboard.KEY_P, "category.cpsmod");
	
	@Override
	public void init() {
		ClientRegistry.registerKeyBinding(CPS_OVERLAY_CONFIG);
		MinecraftForge.EVENT_BUS.register(new ClientEvents());
		MinecraftForge.EVENT_BUS.register(new Config());
		ClientCommandHandler.instance.registerCommand(new ConfigCommand());
	}
}
