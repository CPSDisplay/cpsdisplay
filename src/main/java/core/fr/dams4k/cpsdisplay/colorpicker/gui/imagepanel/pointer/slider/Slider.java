package fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.pointer.slider;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import fr.dams4k.cpsdisplay.colorpicker.ColorPickerImages;
import fr.dams4k.cpsdisplay.colorpicker.gui.Label;
import fr.dams4k.cpsdisplay.colorpicker.gui.border.InventoryBorder;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.ImageType;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.pointer.PointerListener;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.pointer.PointerPanel;
import fr.dams4k.cpsdisplay.colorpicker.gui.textfield.LimitedDocument;
import fr.dams4k.cpsdisplay.colorpicker.gui.textfield.TextField;
import fr.dams4k.cpsdisplay.colorpicker.gui.textfield.TextFieldListener;

public class Slider extends JPanel implements PointerListener, TextFieldListener {
    private List<SliderListener> listeners = new ArrayList<>();

    private Label label;
    private PointerPanel pointerPanel;
    private TextField textField;

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
        
        this.label = new Label(name + ":");
        this.label.setPreferredSize(new Dimension(32, 32));

        List<Color> colors = new ArrayList<>();
        colors.add(new Color(1f, 1f, 1f));
        colors.add(new Color(0f, 0f, 0f));

        this.pointerPanel = new PointerPanel(ColorPickerImages.createGradient(this.gradientSizeX, this.gradientSizeY, colors), ImageType.STRETCHING, 1f, true, false);
        this.pointerPanel.setImageBorder(border);
        this.pointerPanel.setPreferredSize(new Dimension(200, 32));
        this.pointerPanel.addListener(this);
        this.pointerPanel.setOpaque(false);

        this.textField = new TextField(textureScale, 3);
        this.textField.setText(Integer.toString(this.value));
        this.textField.setPreferredSize(new Dimension(48, 32));
        this.textField.addTextFieldListener(this);
        LimitedDocument document = (LimitedDocument) this.textField.getDocument();
        document.anythings = false;
        document.digits = true;
        this.textField.setDocument(document);

        this.add(this.label);
        this.add(this.pointerPanel);
        this.add(this.textField);
    }

    public int getValue() {
        return this.value;
    }
    public void setValue(int value) {
        this.setValue(value, true, true, true);
    }
    public void setValue(int value, boolean setValue, boolean setPointer, boolean setText) {
        if (setValue) this.value = this.clampValue(value);
        if (setPointer) this.setPointerValue(value);
        if (setText) this.setTextValue(value);
    }

    public void setPointerValue(int value) {
        float fValue = value/(float) (max);
        this.pointerPanel.defaultX = fValue;
        this.pointerPanel.setPointerX(fValue);
    }
    public void setTextValue(int value) {
        this.textField.setText(Integer.toString(this.value));
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


    public void callChangingListeners() {
        for (SliderListener listener : this.listeners) {
            listener.sliderValueChanging(this.getName(), this.value);
        }
    }
    public void callChangedListeners() {
        for (SliderListener listener : this.listeners) {
            listener.sliderValueChanged(this.getName(), this.value);
        }
    }

    public void addListener(SliderListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void xPointerChanging(float x) {
        this.value = Math.round(x * this.max);
        callChangingListeners();
    }

    @Override
    public void yPointerChanging(float y) {}

    @Override
    public void xPointerChanged(float x) {
        this.value = Math.round(x * this.max);
        this.textField.setText(Integer.toString(this.value));
        callChangedListeners();
    }

    @Override
    public void yPointerChanged(float y) {}

    @Override
    public void textChanged(String before, String after) {
        if (after.equals("")) {
            after = "0";
        }

        this.value = Integer.valueOf(after);
        setValue(value);
        callChangedListeners();
    }
}
