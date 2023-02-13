package fr.dams4k.cpsdisplay.renderer;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;

// I need to rewrote an existing code of minecraft because mc.toggleFullscreen(); do weird things lol
public class WindowDisplay {
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    private static final List<DisplayMode> macDisplayModes = Lists.newArrayList(new DisplayMode[] {new DisplayMode(2560, 1600), new DisplayMode(2880, 1800)});
    
    public static void updateDisplayMode() {
        try {
            Set<DisplayMode> set = Sets.<DisplayMode>newHashSet();
            Collections.addAll(set, Display.getAvailableDisplayModes());
            DisplayMode displaymode = Display.getDesktopDisplayMode();

            if (!set.contains(displaymode) && Util.getOSType() == Util.EnumOS.OSX) {
                label53:

                for (DisplayMode displaymode1 : macDisplayModes) {
                    boolean flag = true;

                    for (DisplayMode displaymode2 : set) {
                        if (displaymode2.getBitsPerPixel() == 32 && displaymode2.getWidth() == displaymode1.getWidth() && displaymode2.getHeight() == displaymode1.getHeight()) {
                            flag = false;
                            break;
                        }
                    }

                    if (!flag) {
                        Iterator<DisplayMode> iterator = set.iterator();
                        DisplayMode displaymode3;

                        while (true) {
                            if (!iterator.hasNext()) {
                                continue label53;
                            }

                            displaymode3 = (DisplayMode)iterator.next();

                            if (displaymode3.getBitsPerPixel() == 32 && displaymode3.getWidth() == displaymode1.getWidth() / 2 && displaymode3.getHeight() == displaymode1.getHeight() / 2) {
                                break;
                            }
                        }

                        displaymode = displaymode3;
                    }
                }
            }

            Display.setDisplayMode(displaymode);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static void enableFullscreen() {
        try {
            WindowDisplay.updateDisplayMode();

            Display.setFullscreen(true);
            Display.setVSyncEnabled(mc.gameSettings.enableVsync);
            mc.updateDisplay();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static void disableFullscreen() {
        try {
            int displayWidth = Display.getDisplayMode().getWidth();
            int displayHeight = Display.getDisplayMode().getHeight();

            Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            mc.displayWidth = displayWidth;
            mc.displayHeight = displayHeight;

            Display.setFullscreen(false);
            Display.setVSyncEnabled(mc.gameSettings.enableVsync);
            mc.updateDisplay();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
}
