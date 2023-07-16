package fr.dams4k.cpsdisplay.proxy;

import org.lwjgl.input.Keyboard;

import fr.dams4k.cpsdisplay.commands.ConfigCommand;
import fr.dams4k.cpsdisplay.config.ModConfig;
import fr.dams4k.cpsdisplay.config.VersionManagerConfig;
import fr.dams4k.cpsdisplay.events.ModEvents;
import fr.dams4k.cpsdisplay.events.VersionCheckerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	public static final KeyBinding CPS_OVERLAY_CONFIG = new KeyBinding("cpsdisplay.key.opengui", Keyboard.KEY_P, "cpsdisplay.category.cpsdisplay");
	
	@Override
	public void preInit() {
		ModConfig.preInit();
		VersionManagerConfig.preInit();
	}

	@Override
	public void init() {
		ClientRegistry.registerKeyBinding(CPS_OVERLAY_CONFIG);
		MinecraftForge.EVENT_BUS.register(new ModEvents());
		MinecraftForge.EVENT_BUS.register(new VersionCheckerEvent());
		ClientCommandHandler.instance.registerCommand(new ConfigCommand());
	}

	public static boolean getUnicodeFlag() {
		// Doing this for futur minecraft version, not all minecraft version have "fontRenderObj" called this way
		return Minecraft.getMinecraft().fontRendererObj.getUnicodeFlag();
	}
}
