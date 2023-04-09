package fr.dams4k.cpsdisplay.commands;

import java.util.Arrays;
import java.util.List;

import fr.dams4k.cpsdisplay.References;
import fr.dams4k.cpsdisplay.gui.GuiConfig;
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
    public List<String> getCommandAliases() {
        return Arrays.asList("cpsd");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cpsdisplay";
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
        // we need to wait a tick lol
        Minecraft.getMinecraft().displayGuiScreen(new GuiConfig());
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
