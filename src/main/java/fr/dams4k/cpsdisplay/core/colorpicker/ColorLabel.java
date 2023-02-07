package fr.dams4k.cpsdisplay.core.colorpicker;

import java.awt.Graphics;

import javax.swing.JPanel;

import net.minecraft.util.ResourceLocation;

public class ColorLabel extends JPanel {
    private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
    
    private String text;

    public ColorLabel(String text) {
        this.text = text;
    }
    
    private ResourceLocation getUnicodePageLocation(int page) {
        if (unicodePageLocations[page] == null) {
            unicodePageLocations[page] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", new Object[] {Integer.valueOf(page)}));
        }

        return unicodePageLocations[page];
    }
    
    private void paintCharacter(Graphics g, char c) {
        int page = c / 256;
        ResourceLocation unicodePageLocation = this.getUnicodePageLocation(c);
        // unicodePageLocation.getResourcePath()
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (char c : this.text.toCharArray()) {
            paintCharacter(g, c);
        }
    }
}
