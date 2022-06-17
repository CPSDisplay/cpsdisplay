package fr.dams4k.cpsdisplay.v1_8.commands;

import java.lang.ref.Reference;

import fr.dams4k.cpsdisplay.v1_8.References;
import fr.dams4k.cpsdisplay.v1_8.gui.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ConfigCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return References.MOD_ID;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Open gui config";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiConfig());
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
