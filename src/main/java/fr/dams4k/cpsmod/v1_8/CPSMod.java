package fr.dams4k.cpsmod.v1_8;

import fr.dams4k.cpsmod.v1_8.commands.ConfigCommand;
import fr.dams4k.cpsmod.v1_8.config.Config;
import fr.dams4k.cpsmod.v1_8.gui.RenderGuiHandler;
import fr.dams4k.cpsmod.v1_8.proxy.ClientProxy;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, clientSideOnly = true, canBeDeactivated = true)
public class CPSMod {
	@SidedProxy(clientSide = "fr.dams4k.cpsmod.v1_8.proxy.ClientProxy")
	public static ClientProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.preInit();
		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
		ClientCommandHandler.instance.registerCommand(new ConfigCommand());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());
		MinecraftForge.EVENT_BUS.register(new Config());
	}
}
