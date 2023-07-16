package fr.dams4k.cpstweaker;

import java.util.List;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;

public class CPSDelayedTweaker {
    public static boolean isRequired() {
        List<ITweaker> currentCycle = (List<ITweaker>) Launch.blackboard.get("Tweaks");
        return currentCycle.stream().anyMatch(it -> it.getClass().getName().equals("net.minecraftforge.fml.common.launcher.FMLTweaker"));
    }
}
