package fr.dams4k.cpsdisplay.gui.buttons;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class ModSlider extends GuiButton {
    private float sliderPosition = 1.0f;
    public boolean isMouseDown;
    private String name;
    private final float min;
    private final float max;
    private final float step;
    private final int decimals;
    private final GuiPageButtonList.GuiResponder responder;
    private ModSlider.FormatHelper formatHelper;

    private List<ModSliderMainPoint> mainPoints = new ArrayList<>();

    public ModSlider(GuiPageButtonList.GuiResponder guiResponder, int idIn, int x, int y, int width, int height, String name, float min, float max, float step, float defaultValue, ModSlider.FormatHelper formatter, int decimals) {
        super(idIn, x, y, width, height, "");
        this.name = name;
        this.min = min;
        this.max = max;
        this.step = step;
        this.decimals = decimals;
        this.sliderPosition = this.valueToPosition(defaultValue);
        this.formatHelper = formatter;
        this.responder = guiResponder;
        this.displayString = this.getDisplayString();
    }

    public ModSlider(GuiPageButtonList.GuiResponder guiResponder, int idIn, int x, int y, String name, float min, float max, float step, float defaultValue, ModSlider.FormatHelper formatter, int decimals) {
        this(guiResponder, idIn, x, y, 150, 20, name, min, max, step, defaultValue, formatter, decimals);
    }

    public ModSlider(int idIn, int x, int y, int width, int height, String name, float min, float max, float step, float defaultValue, int decimals) {
        this(new GuiResponder() {
			@Override
			public void func_175321_a(int p_175321_1_, boolean p_175321_2_) {}

			@Override
			public void onTick(int id, float value) {}

			@Override
			public void func_175319_a(int p_175319_1_, String p_175319_2_) {}
			
		}, idIn, x, y, width, height, name, min, max, step, defaultValue, new FormatHelper() {

			@Override
			public String getText(int id, String name, float value) {
				return name + ": " + value + "%";
			}
			
		}, decimals);
    }

    public void setValue(float value) {
        this.sliderPosition = valueToPosition(value);
    }
    public float getValue() {
        return Math.round(this.positionToValue(this.sliderPosition) * (float) this.decimals) / (float) this.decimals;
    }

    public float valueToPosition(float value) {
        return (value - this.min) / (this.max - this.min);
    }
    public float positionToValue(float position) {
        float value = this.min + (this.max - this.min) * position;
        return value - (value + this.step / 2) % this.step + this.step / 2;
    }


    public ModSlider.FormatHelper getFormatHelper() {
        return formatHelper;
    }

    public void setFormatHelper(ModSlider.FormatHelper formatHelper) {
        this.formatHelper = formatHelper;
        this.displayString = this.getDisplayString();
    }

    public void func_175218_a(float value, boolean updateDisplay) {
        this.sliderPosition = this.valueToPosition(value);
        this.displayString = this.getDisplayString();

        if (updateDisplay) {
            this.responder.onTick(this.id, this.getValue());
        }
    }

    private String getDisplayString() {
        return this.formatHelper == null ? I18n.format(this.name, new Object[0]) + ": " + this.getValue() : this.formatHelper.getText(this.id, I18n.format(this.name, new Object[0]), this.getValue());
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver) {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            if (this.isMouseDown) {
                this.sliderPosition = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                for (ModSliderMainPoint mainPoint : this.mainPoints) {
                    if (mainPoint.isNear(this.getValue())) {
                        this.sliderPosition = this.valueToPosition(mainPoint.getValue());
                    }
                }
                
                if (this.sliderPosition < 0.0f) {
                    this.sliderPosition = 0.0f;
                }

                if (this.sliderPosition > 1.0f) {
                    this.sliderPosition = 1.0f;
                }
                
                this.displayString = this.getDisplayString();
                this.responder.onTick(this.id, this.getValue());
            }

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(this.xPosition + (int)(this.valueToPosition(this.getValue()) * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.valueToPosition(this.getValue()) * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    public void setSliderPosition(float x) {
        this.sliderPosition = x;
        this.displayString = this.getDisplayString();
        this.responder.onTick(this.id, this.getValue());
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderPosition = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);

            for (ModSliderMainPoint mainPoint : this.mainPoints) {
                if (mainPoint.isNear(this.getValue())) {
                    this.sliderPosition = this.valueToPosition(mainPoint.getValue());
                }
            }

            if (this.sliderPosition < 0.0f) {
                this.sliderPosition = 0.0f;
            }

            if (this.sliderPosition > 1.0f) {
                this.sliderPosition = 1.0f;
            }

            this.displayString = this.getDisplayString();
            this.responder.onTick(this.id, this.getValue());
            this.isMouseDown = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY) {
        this.isMouseDown = false;
    }

    public interface FormatHelper {
        String getText(int id, String name, float value);
    }

    public void addMainPoint(ModSliderMainPoint mainPoint) {
        this.mainPoints.add(mainPoint);
    }
}
