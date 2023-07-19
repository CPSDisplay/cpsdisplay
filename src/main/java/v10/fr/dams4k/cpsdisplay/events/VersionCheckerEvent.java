package fr.dams4k.cpsdisplay.events;

import fr.dams4k.cpsdisplay.CPSDisplay;
import fr.dams4k.cpsdisplay.References;
import fr.dams4k.cpsdisplay.VersionChecker;
import fr.dams4k.cpsdisplay.VersionManager;
import fr.dams4k.cpsdisplay.config.VersionManagerConfig;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class VersionCheckerEvent {
    @SubscribeEvent
    public void onClientJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayerSP) {
            EntityPlayerSP player = (EntityPlayerSP) event.getEntity();

            // Check if a new version has been installed
            if (!VersionManagerConfig.latestVersion.equals(References.MOD_VERSION)) {
                VersionManagerConfig.latestVersion = References.MOD_VERSION;
                VersionManagerConfig.saveConfig();

                ITextComponent modNameMessage = new TextComponentString(I18n.format("cpsdisplay.version.mod_name", new Object[0]));
                
                String modInstalledString = I18n.format("cpsdisplay.version.installed", new Object[0]);
                ITextComponent modInstalledMessage = new TextComponentString(modInstalledString.replace("{version}", References.MOD_VERSION));

                ITextComponent message = new TextComponentString("");
                message.appendSibling(modNameMessage);
                message.appendText(" ");
                message.appendSibling(modInstalledMessage);

                player.addChatMessage(message);
            }

            // Check if a new version available
            VersionManager versionManager = CPSDisplay.versionManager;
            VersionChecker versionChecker = new VersionChecker(References.MOD_VERSION);
            if (versionChecker.compareTo(versionManager.latestVersion) == VersionChecker.LOWER) {
                // MOD NAME
                ITextComponent modNameMessage = new TextComponentString(I18n.format("cpsdisplay.version.mod_name", new Object[0]));

                // DESCRIPTION
                ITextComponent description = new TextComponentString(I18n.format("cpsdisplay.version.description", new Object[0]));

                // LINK
                ITextComponent link = new TextComponentString(I18n.format("cpsdisplay.version.url", new Object[0]));
                Style style = new Style().setClickEvent(new ClickEvent(Action.OPEN_URL, versionManager.latestReleaseURL));
                link.setStyle(style);

                // WHOLE MESSAGE
                ITextComponent message = new TextComponentString("");

                message.appendSibling(modNameMessage);
                message.appendText(" ");
                message.appendSibling(description);
                message.appendText(" ");
                message.appendSibling(link);

                player.addChatMessage(message);
            }
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}
