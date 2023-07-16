package fr.dams4k.cpsdisplay.events;

import fr.dams4k.cpsdisplay.CPSDisplay;
import fr.dams4k.cpsdisplay.References;
import fr.dams4k.cpsdisplay.VersionChecker;
import fr.dams4k.cpsdisplay.VersionManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class VersionCheckerEvent {
    @SubscribeEvent
    public void onClientJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayerSP) {
            VersionManager versionManager = CPSDisplay.versionManager;
            VersionChecker versionChecker = new VersionChecker(References.MOD_VERSION);
            if (versionChecker.compareTo(versionManager.latestVersion) == VersionChecker.LOWER) {
                EntityPlayerSP player = (EntityPlayerSP) event.entity;

                // MOD NAME
                IChatComponent modNameMessage = new ChatComponentText(
                        I18n.format("cpsdisplay.version.mod_name", new Object[0]));

                // DESCRIPTION
                IChatComponent description = new ChatComponentText(
                        I18n.format("cpsdisplay.version.description", new Object[0]));

                // LINK
                IChatComponent link = new ChatComponentText(I18n.format("cpsdisplay.version.url", new Object[0]));
                ChatStyle style = new ChatStyle()
                        .setChatClickEvent(new ClickEvent(Action.OPEN_URL, versionManager.latestReleaseURL));
                link.setChatStyle(style);

                // WHOLE MESSAGE
                IChatComponent message = new ChatComponentText("");

                message.appendSibling(modNameMessage);
                message.appendText(" ");
                message.appendSibling(description);
                message.appendText(" ");
                message.appendSibling(link);

                player.addChatMessage(message);
            }
        }
    }
}
