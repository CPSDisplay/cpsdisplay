package fr.dams4k.cpsdisplay.v1_8.gui.buttons;

import java.awt.Color;

import org.lwjgl.LWJGLException;

import fr.dams4k.cpsdisplay.core.colorpicker.ColorPicker;
import fr.dams4k.cpsdisplay.core.colorpicker.ColorPickerListener;
import fr.dams4k.cpsdisplay.v1_8.renderer.WindowDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiColorButton extends GuiButton implements ColorPickerListener {
    private final Minecraft mc = Minecraft.getMinecraft();

    private Color color = Color.GREEN;
    
    private int xGap = 0;
    private int yGap = 0;

    private boolean alphaChannel = false;

    private WindowDisplay windowDisplay = new WindowDisplay();

    private boolean wasOriginallyFullscreen;
    private ColorPicker colorPicker;

    public GuiColorButton(int id, int x, int y, int width, int height, String text, boolean alphaChannel) {
        this(id, x, y, width, height, text, alphaChannel, width / 4, height / 4);
    }

    public GuiColorButton(int id, int x, int y, int width, int height, String text, boolean alphaChannel, int xGap, int yGap) {
        super(id, x, y, width, height, text);
        this.alphaChannel = alphaChannel;
        this.xGap = xGap;
        this.yGap = yGap;
    }
    
    @Override
    public void drawButton(Minecraft p_drawButton_1_, int p_drawButton_2_, int p_drawButton_3_) {
        super.drawButton(p_drawButton_1_, p_drawButton_2_, p_drawButton_3_);
        
        if (this.visible) {
            int x = xPosition + xGap;
            int y = yPosition + yGap;
    
            drawRect(x, y, x + width - 2*xGap, y + height - 2*yGap, color.getRGB());
        }
        if (colorPicker == null && wasOriginallyFullscreen) {
            wasOriginallyFullscreen = false;
            try {
                windowDisplay.enableFullscreen();
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void mouseReleased(int x, int y) {
        super.mouseReleased(x, y);

        if (mc.isFullScreen()) {
            this.wasOriginallyFullscreen = true;
            try {
                windowDisplay.disableFullscreen();
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
        }
        this.colorPicker = new ColorPicker(color, this.alphaChannel);
        this.colorPicker.addListener(this);
        this.colorPicker.popup();
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return color;
    }

    @Override
    public void newColor(Color color) {
        this.color = color;
    }

    @Override
    public void closed() {
        this.colorPicker = null;
    }
}
