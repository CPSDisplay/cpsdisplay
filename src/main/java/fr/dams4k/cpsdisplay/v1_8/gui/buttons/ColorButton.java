package fr.dams4k.cpsdisplay.v1_8.gui.buttons;

import java.awt.Color;

import fr.dams4k.cpsdisplay.core.colorpicker.ColorPicker;
import fr.dams4k.cpsdisplay.core.colorpicker.ColorPickerListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class ColorButton extends GuiButton implements ColorPickerListener {
    private Color color = Color.GREEN;
    private ColorPicker colorPicker;
    
    private int x_gap = 0;
    private int y_gap = 0;

    public ColorButton(int id, int x, int y, int width, int height) {
        this(id, x, y, width, height, "", width / 4, height / 4);
    }

    public ColorButton(int id, int x, int y, int width, int height, String text) {
        this(id, x, y, width, height, text, width / 4, height / 4);
    }

    public ColorButton(int id, int x, int y, int width, int height, String text, int x_gap, int y_gap) {
        super(id, x, y, width, height, text);
        this.x_gap = x_gap;
        this.y_gap = y_gap;

        colorPicker = new ColorPicker(color);
        this.colorPicker.addListener(this);
    }
    
    @Override
    public void drawButton(Minecraft p_drawButton_1_, int p_drawButton_2_, int p_drawButton_3_) {
        super.drawButton(p_drawButton_1_, p_drawButton_2_, p_drawButton_3_);
        
        int x = xPosition + x_gap;
        int y = yPosition + y_gap;

        drawRect(x, y, x + width - 2*x_gap, y + height - 2*y_gap, color.getRGB());
    }
    
    @Override
    public void mouseReleased(int x, int y) {
        super.mouseReleased(x, y);
        colorPicker.popup();
    }

    @Override
    public void newColor(Color color) {
        this.color = color;
    }
}
