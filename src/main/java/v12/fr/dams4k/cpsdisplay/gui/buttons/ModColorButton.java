package fr.dams4k.cpsdisplay.gui.buttons;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.WindowEvent;

import fr.dams4k.cpsdisplay.colorpicker.ColorPicker;
import fr.dams4k.cpsdisplay.colorpicker.ColorPickerListener;
import fr.dams4k.cpsdisplay.renderer.WindowDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class ModColorButton extends GuiButton implements ColorPickerListener {
    private final Minecraft mc = Minecraft.getMinecraft();

    private Color color = Color.GREEN;

    private int colorWidth;
    private Insets padding;

    private boolean alphaChannel = false;

    private boolean wasOriginallyFullscreen;
    private ColorPicker colorPicker;

    public ModColorButton(int id, int x, int y, int width, int height, String text, boolean alphaChannel) {
        this(id, x, y, width, height, text, alphaChannel, 40, new Insets(4, 6, 4, 6));
    }

    public ModColorButton(int id, int x, int y, int width, int height, String text, boolean alphaChannel, int colorWidth, Insets padding) {
        super(id, x, y, width, height, text);
        this.alphaChannel = alphaChannel;
        this.colorWidth = colorWidth;
        this.padding = padding;
    }
    
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            FontRenderer fontRenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1f, 1f, 1f, 1f);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            
            int textButtonWidth = this.width - colorWidth;
            int colorButtonWidth = colorWidth;
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, textButtonWidth / 2, this.height); // LEFT SIDE
            this.drawTexturedModalRect(this.x + textButtonWidth / 2, this.y, 200 - textButtonWidth / 2, 46 + i * 20, textButtonWidth / 2, this.height); // RIGHT SIDE
            
            this.drawTexturedModalRect(this.x + textButtonWidth, this.y, 0, 46, colorButtonWidth / 2, this.height); // COLOR LEFT SIDE
            this.drawTexturedModalRect(this.x + textButtonWidth + colorButtonWidth/2, this.y, 200 - colorButtonWidth/2, 46, colorButtonWidth/2, this.height); // COLOR RIGHT SIDE
            

            this.mouseDragged(mc, mouseX, mouseY);
            int textColor = 14737632;

            if (!this.enabled) {
                textColor = 10526880;
            } else if (this.hovered) {
                textColor = 16777120;
            }
            this.drawCenteredString(fontRenderer, this.displayString, this.x + textButtonWidth / 2, this.y + (this.height - 8) / 2, textColor);
            
            int rectX = this.x + textButtonWidth + this.padding.left;
            int rectY = this.y + this.padding.top;
            drawRect(rectX, rectY, this.x + textButtonWidth + colorButtonWidth - this.padding.right, this.y + this.height - this.padding.bottom, color.getRGB());
        }
        if (colorPicker == null && wasOriginallyFullscreen) {
            wasOriginallyFullscreen = false;
            WindowDisplay.enableFullscreen();
        }
    }

    @Override
    public void mouseReleased(int x, int y) {
        super.mouseReleased(x, y);

        if (mc.isFullScreen()) {
            this.wasOriginallyFullscreen = true;
            WindowDisplay.disableFullscreen();
        }

        this.killColorPicker();
        this.colorPicker = new ColorPicker(color, this.alphaChannel);
        this.colorPicker.setTitle(String.join(" - ", I18n.format("cpsdisplay.external.color_picker", new Object[0]), this.displayString));
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

    public void killColorPicker() {
        if (this.colorPicker != null) {
            this.colorPicker.dispatchEvent(new WindowEvent(colorPicker, WindowEvent.WINDOW_CLOSING));
        }
    }
}
