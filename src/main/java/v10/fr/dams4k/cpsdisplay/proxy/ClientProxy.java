package fr.dams4k.cpsdisplay.proxy;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;

public class ClientProxy extends CommonProxy {
	public static final KeyBinding CPS_OVERLAY_CONFIG = new KeyBinding("cpsdisplay.key.opengui", Keyboard.KEY_P, "cpsdisplay.category.cpsdisplay");
}
