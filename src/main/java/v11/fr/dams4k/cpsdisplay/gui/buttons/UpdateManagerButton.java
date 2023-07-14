package fr.dams4k.cpsdisplay.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class UpdateManagerButton extends GuiButton {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("textures/entity/experience_orb.png");

    public UpdateManagerButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }


    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        super.drawButton(mc, mouseX, mouseY);
        // TODO: draw xp orb on the top right corner when an update is available (the more the update is important, the more the xp orb is big)
    }

    public int getTextureByXP(int xpValue) {
        return xpValue >= 2477?10:(xpValue >= 1237?9:(xpValue >= 617?8:(xpValue >= 307?7:(xpValue >= 149?6:(xpValue >= 73?5:(xpValue >= 37?4:(xpValue >= 17?3:(xpValue >= 7?2:(xpValue >= 3?1:0)))))))));
     }
}
