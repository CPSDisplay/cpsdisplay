package fr.dams4k.cpsdisplay.core.colorpicker;

import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JPanel;

import org.apache.commons.io.IOUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Label extends JPanel {
    protected byte[] glyphWidth = new byte[65536];
    private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
    
    private String text;

    public Label(String text) {
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
        System.out.println(unicodePageLocation.getResourcePath());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (char c : this.text.toCharArray()) {
            paintCharacter(g, c);
        }
    }

    private void readGlyphSizes() {
        InputStream inputstream = null;

        try {
            inputstream = getResourceInputStream(new ResourceLocation("font/glyph_sizes.bin"));
            inputstream.read(this.glyphWidth);
        } catch (IOException ioexception) {
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }

    protected InputStream getResourceInputStream(ResourceLocation location) throws IOException {
        return Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
    }
}
