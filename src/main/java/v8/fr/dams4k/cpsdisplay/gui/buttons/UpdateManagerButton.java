package fr.dams4k.cpsdisplay.gui.buttons;

import fr.dams4k.cpsdisplay.CPSDisplay;
import fr.dams4k.cpsdisplay.References;
import fr.dams4k.cpsdisplay.VersionChecker;
import fr.dams4k.cpsdisplay.VersionChecker.VersionDiff;
import fr.dams4k.cpsdisplay.VersionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class UpdateManagerButton extends GuiButton {
    private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("textures/entity/experience_orb.png");

    public UpdateManagerButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }


    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        super.drawButton(mc, mouseX, mouseY);
        

        VersionChecker versionChecker = new VersionChecker(References.MOD_VERSION);
        VersionManager versionManager = CPSDisplay.versionManager;
        if (versionChecker.compareTo(versionManager.latestVersion) != VersionChecker.LOWER) return;
        
        VersionDiff versionDiff = versionChecker.getVersionDifference(versionManager.latestVersion);
        int xpValue = (int) Math.pow(10, versionDiff.ordinal());
        int i = getTextureByXP(xpValue);
        
        int orbWidth = 17;
        int orbX = xPosition + width - (orbWidth / 2 + 2);
        int orbY = yPosition - (orbWidth / 2 - 2);

        mc.getTextureManager().bindTexture(EXPERIENCE_ORB_TEXTURES);
        // Calculate the texture coords in uv space
        float f = (float)(i % 4 * 16 + 0) / 64.0F;
        float f1 = (float)(i % 4 * 16 + 16) / 64.0F;
        float f2 = (float)(i / 4 * 16 + 0) / 64.0F;
        float f3 = (float)(i / 4 * 16 + 16) / 64.0F;

        // Calculate the color
        float partialTicks = 0f;
        float f9 = (1f + partialTicks) / 2.0F;
        int l = (int)((MathHelper.sin(f9 + 0.0F) + 1.0F) * 0.5F * 255.0F);
        int j1 = (int)((MathHelper.sin(f9 + 4.1887903F) + 1.0F) * 0.1F * 255.0F);

        // Draw the orb
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldRenderer.pos(orbX,              orbY + orbWidth,    this.zLevel).tex(f,    f3).color(l, 255, j1, 255).endVertex();
        worldRenderer.pos(orbX + orbWidth,   orbY + orbWidth,    this.zLevel).tex(f1,   f3).color(l, 255, j1, 255).endVertex();
        worldRenderer.pos(orbX + orbWidth,   orbY,               this.zLevel).tex(f1,   f2).color(l, 255, j1, 255).endVertex();
        worldRenderer.pos(orbX,              orbY,               this.zLevel).tex(f,    f2).color(l, 255, j1, 255).endVertex();
        tessellator.draw();
    }

    public int getTextureByXP(int xpValue) {
        return xpValue >= 2477?10:(xpValue >= 1237?9:(xpValue >= 617?8:(xpValue >= 307?7:(xpValue >= 149?6:(xpValue >= 73?5:(xpValue >= 37?4:(xpValue >= 17?3:(xpValue >= 7?2:(xpValue >= 3?1:0)))))))));
    }
}
