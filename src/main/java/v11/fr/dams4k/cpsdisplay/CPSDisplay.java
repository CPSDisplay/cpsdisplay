package fr.dams4k.cpsdisplay;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.dams4k.cpsdisplay.config.ModConfig;
import fr.dams4k.cpsdisplay.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
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

    public static String latestVersion = "0.0.0";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            URL githubTagsURL = new URL(References.MOD_GITHUB_LASTEST_RELEASE);
            
            Scanner scanner = new Scanner(githubTagsURL.openStream());
            String response = scanner.useDelimiter("\\Z").next();
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(response);

            CPSDisplay.latestVersion = String.join(".", jsonObject.get("tag_name").getAsString());
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
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
}
