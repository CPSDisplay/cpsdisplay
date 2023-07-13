package fr.dams4k.cpsdisplay;

import fr.dams4k.cpsdisplay.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = References.MOD_ID, name = References.MOD_NAME, clientSideOnly = true, acceptableRemoteVersions = "*", version = References.MOD_VERSION)
public class CPSDisplay {
	@SidedProxy(clientSide = "fr.dams4k.cpsdisplay.proxy.ClientProxy")
	public static ClientProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}

	public static boolean getUnicodeFlag() {
		// Doing this for futur minecraft version, not all minecraft version have "fontRenderObj" called this way
		return Minecraft.getMinecraft().fontRendererObj.getUnicodeFlag();
	}
}
