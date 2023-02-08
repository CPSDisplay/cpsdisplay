package fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.pointer.slider;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import fr.dams4k.cpsdisplay.core.colorpicker.ColorPickerImages;
import fr.dams4k.cpsdisplay.core.colorpicker.Label;
import fr.dams4k.cpsdisplay.core.colorpicker.border.InventoryBorder;
import fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.ImageType;
import fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.pointer.PointerListener;
import fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.pointer.PointerPanel;

public class Slider extends JPanel implements PointerListener {
    private List<SliderListener> listeners = new ArrayList<>();

    private Label label;
    private PointerPanel pointerPanel;
    public int gradientSizeX = 256;
    public int gradientSizeY = 1;

    private int value = 0;
    private int min = 0;
    private int max = 0;

    public Slider(String name, int min, int max, float textureScale) {
        this.setName(name);
        this.setOpaque(false);
        this.setLayout(new FlowLayout());

        this.min = min;
        this.max = max;

        InventoryBorder border = new InventoryBorder(textureScale);
        
        this.label = new Label(name);

        List<Color> colors = new ArrayList<>();
        colors.add(new Color(1f, 1f, 1f));
        colors.add(new Color(0f, 0f, 0f));

        this.pointerPanel = new PointerPanel(ColorPickerImages.createGradient(this.gradientSizeX, this.gradientSizeY, colors), ImageType.STRETCHING, 1f, true, false);
        this.pointerPanel.setImageBorder(border);
        this.pointerPanel.setPreferredSize(new Dimension(200, 32));
        this.pointerPanel.addListener(this);
        this.pointerPanel.setOpaque(false);

        this.add(label);
        this.add(this.pointerPanel);
    }

    public int getValue() {
        return this.value;
    }
    public void setValue(int value) {
        this.value = this.clampValue(value);
        float fValue = this.value/(float) (max);
        this.pointerPanel.defaultX = fValue;
        this.pointerPanel.setPointerX(fValue);
    }

    public void setGradient(List<Color> colors) {
        this.pointerPanel.setImage(ColorPickerImages.createGradient(this.gradientSizeX, this.gradientSizeY, colors));
    }
    public void setAGradient(List<Color> colors) {
        this.pointerPanel.setImage(ColorPickerImages.createAGradient(this.gradientSizeX, this.gradientSizeY, colors));
    }
    public void setGradient(List<Color> colors, float darkness, float brightness) {
        this.pointerPanel.setImage(ColorPickerImages.createGradient(this.gradientSizeX, this.gradientSizeY, colors));
        this.pointerPanel.setDarkness(darkness);
        this.pointerPanel.setBrightness(brightness);
    }

    public PointerPanel getPointerPanel() {
        return pointerPanel;
    }

    private int clampValue(int value) {
        return Math.max(this.min, Math.min(this.max, value));
    }


    public void callListeners() {
        for (SliderListener listener : this.listeners) {
            listener.sliderValueChanged(this.getName(), this.value);
        }
    }

    public void addListener(SliderListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void xPointerChanged(float x) {
        this.value = Math.round(x * this.max);
        callListeners();
    }

    @Override
    public void yPointerChanged(float y) { }
}
