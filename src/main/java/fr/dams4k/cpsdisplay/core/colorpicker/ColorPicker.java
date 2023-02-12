package fr.dams4k.cpsdisplay.core.colorpicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.dams4k.cpsdisplay.core.colorpicker.gui.Button;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.ButtonListener;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.ColorPreview;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.Label;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.border.Border;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.border.InventoryBorder;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.imagepanel.ImagePanel;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.imagepanel.ImageType;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.imagepanel.pointer.HPointerListener;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.imagepanel.pointer.HPointerPanel;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.imagepanel.pointer.PointerPanel;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.imagepanel.pointer.SVPointerListener;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.imagepanel.pointer.SVPointerPanel;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.imagepanel.pointer.slider.Slider;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.imagepanel.pointer.slider.SliderListener;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.textfield.LimitedDocument;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.textfield.TextField;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.textfield.TextFieldListener;

public class ColorPicker extends JFrame implements HPointerListener, SVPointerListener, SliderListener, TextFieldListener {
    private List<ColorPickerListener> listeners = new ArrayList<>();
    private final float texturesScale = 3f;
    private final int borderSize = 8;
    private boolean alphaChannel;
    private Dimension size;

    private ImagePanel background;

    private SVPointerPanel svPointerPanel = new SVPointerPanel();
    private HPointerPanel hPointerPanel = new HPointerPanel();

    private Slider hSlider = new Slider("H", 0, 360, this.texturesScale);
    private Slider sSlider = new Slider("S", 0, 100, this.texturesScale);
    private Slider vSlider = new Slider("V", 0, 100, this.texturesScale);
    private Slider aSlider = new Slider("A", 0, 100, this.texturesScale);

    private Label hexColorLabel = new Label("Hex color:");
    private TextField hexColorField;

    private ColorPreview oldColorPreview = new ColorPreview(Color.WHITE, this.texturesScale);
    private ColorPreview newColorPreview = new ColorPreview(Color.WHITE, this.texturesScale);

    private Button okButton = new Button("OK", this.texturesScale);
    private Button cancelButton = new Button("Cancel", this.texturesScale);


    private float h = 0f;
    private float s = 1f;
    private float v = 1f;
    private float a = 1f;

    public ColorPicker(Color oldColor, boolean alphaChannel) {
        this.setColor(oldColor);

        this.alphaChannel = alphaChannel;

        this.setTitle("ColorPicker");

        int sizeY = alphaChannel == true ? 640 : 600;
        size = new Dimension(320, sizeY);
        this.setSize(size);
        this.setMinimumSize(size);

        background = new ImagePanel("assets/minecraft/textures/gui/options_background.png", ImageType.TILING, texturesScale);
        background.setDarkness(0.5f);
        background.setLayout(new BoxLayout(background, BoxLayout.PAGE_AXIS));
        this.getContentPane().add(background);

        this.addColorPointers();
        this.addGradientSliders();
        this.addHexTextField();
        this.addPreviews(oldColor);
        this.addCloseButtons();
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addColorPointers() {
        Border inventoryImageBorder = new InventoryBorder(texturesScale);

        JPanel colorsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, borderSize, borderSize));
        colorsPanel.setOpaque(false);
        colorsPanel.setMaximumSize(new Dimension(500, 256));
        background.add(colorsPanel);

        this.svPointerPanel.setImage(ColorPickerImages.svColorSelector(this.h, SVPointerPanel.sizeX, SVPointerPanel.sizeY));
        this.svPointerPanel.setPreferredSize(new Dimension(250, 250));
        this.svPointerPanel.setImageBorder(inventoryImageBorder);
        this.svPointerPanel.addListener(this);
        this.svPointerPanel.defaultX = this.s;
        this.svPointerPanel.defaultY = 1f-this.v;
        colorsPanel.add(this.svPointerPanel);

        this.hPointerPanel.setPreferredSize(new Dimension(32, 250));
        this.hPointerPanel.setImageBorder(inventoryImageBorder);
        this.hPointerPanel.addListener(this);
        this.hPointerPanel.defaultY = this.h;
        colorsPanel.add(this.hPointerPanel);
    }

    private void addGradientSliders() {
        JPanel sliders = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sliders.setOpaque(false);
        this.background.add(sliders);

        this.hSlider.addListener(this);
        this.hSlider.setValue((int) (this.h * 360));
        this.updateHGradient();
        sliders.add(this.hSlider);

        this.sSlider.addListener(this);
        this.sSlider.setValue((int) (this.s * 100));
        this.updateSGradient();
        sliders.add(this.sSlider);

        this.vSlider.addListener(this);
        this.vSlider.setValue((int) (this.v * 100));
        this.updateVGradient();
        sliders.add(this.vSlider);

        if (this.alphaChannel) {
            this.aSlider.addListener(this);
            this.aSlider.setValue((int) (this.a * 100));
            this.updateAGradient();
            sliders.add(this.aSlider);
        }
    }

    public void addHexTextField() {
        JPanel hexColorPanel = new JPanel(new FlowLayout());
        hexColorPanel.setOpaque(false);
        hexColorPanel.setPreferredSize(new Dimension(size.width, 44));
        hexColorPanel.setMaximumSize(new Dimension(size.width, 44));

        hexColorLabel.setPreferredSize(new Dimension((size.width-12)/2, 24));
        hexColorLabel.setMaximumSize(new Dimension((size.width-12)/2, 24));
        hexColorPanel.add(hexColorLabel);

        hexColorField = new TextField(this.texturesScale, alphaChannel == true ? 8 : 6);
        hexColorField.setPreferredSize(new Dimension((size.width-12)/2, 32));
        hexColorField.setMaximumSize(new Dimension((size.width-12)/2, 32));
        LimitedDocument document = (LimitedDocument) this.hexColorField.getDocument();
        document.anythings = false;
        document.digits = true;
        document.letters = true;
        this.hexColorField.setDocument(document);
        this.updateHexTextField();
        this.hexColorField.addTextFieldListener(this);
        hexColorPanel.add(hexColorField);
        
        background.add(hexColorPanel);
    }

    public void addPreviews(Color oldColor) {
        JPanel colorsPreview = new JPanel(new FlowLayout());
        colorsPreview.setOpaque(false);
        colorsPreview.setPreferredSize(new Dimension(size.width, 44));
        colorsPreview.setMaximumSize(new Dimension(size.width, 44));
        background.add(colorsPreview);

        oldColorPreview.setColor(oldColor);
        updateColorPreview();
        int colorPreviewWidth = (int) (size.width/2)-48;
        int colorPreviewHeight = 36;
        newColorPreview.setPreferredSize(new Dimension(colorPreviewWidth, colorPreviewHeight));
        newColorPreview.setMaximumSize(new Dimension(colorPreviewWidth, colorPreviewHeight));
        oldColorPreview.setPreferredSize(new Dimension(colorPreviewWidth, colorPreviewHeight));
        oldColorPreview.setMaximumSize(new Dimension(colorPreviewWidth, colorPreviewHeight));

        colorsPreview.add(newColorPreview);
        colorsPreview.add(oldColorPreview);
    }

    public void addCloseButtons() {
        JPanel closeButtons = new JPanel(new FlowLayout());
        closeButtons.setOpaque(false);
        closeButtons.setPreferredSize(new Dimension(size.width, 64));
        closeButtons.setMaximumSize(new Dimension(size.width, 64));
        background.add(closeButtons);

        cancelButton.setPreferredSize(new Dimension(size.width / 2 - 8, 48));
        cancelButton.addButtonListener(new ButtonListener() {
            @Override
            public void buttonClicked() {
                close();
            }
            
        });
        closeButtons.add(cancelButton);

        okButton.setPreferredSize(new Dimension(size.width / 2 - 8, 48));
        okButton.addButtonListener(new ButtonListener() {
            @Override
            public void buttonClicked() {
                for (ColorPickerListener listener : listeners) {
                    close();
                    listener.newColor(getColor());
                    listener.closed();
                }
            }
        });
        closeButtons.add(okButton);
    }



    public void popup() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    
    @Override
    public void HColorChanged(float h) {
        this.HColorChanging(h);
        this.updateHexTextField();
        this.hSlider.setTextValue(this.hSlider.getValue());
    }

    @Override
    public void SColorChanged(float s) {
        this.SColorChanging(s);
        this.updateHexTextField();
        this.sSlider.setTextValue(this.sSlider.getValue());
    }

    @Override
    public void VColorChanged(float v) {
        this.VColorChanging(v);
        this.updateHexTextField();
        this.vSlider.setTextValue(this.vSlider.getValue());
    }
    
    
    @Override
    public void HColorChanging(float h) {
        this.h = h;
        this.svPointerPanel.setImage(ColorPickerImages.svColorSelector(this.h, SVPointerPanel.sizeX, SVPointerPanel.sizeY));
        this.hSlider.setValue((int) (this.h*360), true, false);

        this.updateAll();
    }

    @Override
    public void SColorChanging(float s) {
        this.s = s;
        this.sSlider.setValue((int) (this.s*100), true, false);

        this.updateAll();
    }

    @Override
    public void VColorChanging(float v) {
        this.v = 1f-v;
        this.vSlider.setValue((int) (this.v*100), true, false);

        this.updateAll();
    }

    @Override
    public void sliderValueChanging(String sliderName, int value) {
        switch (sliderName) {
            case "H":
                this.h = value/360f;
                this.hPointerPanel.setPointerY(this.h);
                this.svPointerPanel.setImage(ColorPickerImages.svColorSelector(this.h, SVPointerPanel.sizeX, SVPointerPanel.sizeY));
                break;
            case "S":
                this.s = value / 100f;
                this.svPointerPanel.setPointerX(this.s);
                break;
            case "V":
                this.v = value / 100f;
                this.svPointerPanel.setPointerY(1f-this.v);
                break;
            case "A":
                this.a = value / 100f;
                break;
        }

        this.updateAll();
    }

    @Override
    public void sliderValueChanged(String sliderName, int value) {
        this.sliderValueChanging(sliderName, value);
        this.updateHexTextField();
    }


    public void updateAll() {
        this.updateHGradient();
        this.updateSGradient();
        this.updateVGradient();
        this.updateAGradient();
        this.updateColorPreview();
    }

    public void updateColorPreview() {
        this.newColorPreview.setColor(this.getColor());
    }

    public void updateHGradient() {
        PointerPanel pointerPanel = this.hSlider.getPointerPanel();
        BufferedImage image = ColorPickerImages.hColorSelector(this.hSlider.gradientSizeX, this.hSlider.gradientSizeY, ColorPickerImages.ImageDisposition.HORIZONTAL);
        pointerPanel.setImage(image);
        pointerPanel.setBrightness(1f-this.s);
        pointerPanel.setDarkness(1f-this.v);
    }

    public void updateSGradient() {
        List<Color> colors = Arrays.asList(Color.WHITE, Color.getHSBColor(this.h, 1f, 1f));
        this.sSlider.setGradient(colors, 1f-this.v, 0f);
    }

    public void updateVGradient() {
        List<Color> colors = Arrays.asList(Color.BLACK, Color.getHSBColor(this.h, this.s, 1f));
        this.vSlider.setGradient(colors);
    }

    public void updateAGradient() {
        Color color = this.getColor();
        Color colorAlpha0 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
        List<Color> colors = Arrays.asList(colorAlpha0, this.getColorNoAlpha());
        this.aSlider.setAGradient(colors);
    }

    public void updateHexTextField() {
        String ARGBHex = Integer.toHexString(this.getColor().getRGB());
        String RGBAHex = ARGBHex.substring(2, 8) + ARGBHex.substring(0, 2);
        hexColorField.setText(this.alphaChannel == true ? RGBAHex : RGBAHex.substring(0, 6));
    }

    public Color getColorNoAlpha() {
        return Color.getHSBColor(this.h, this.s, this.v);
    }

    public void setColor(Color color) {
        float[] hsb =  Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.h = hsb[0];
        this.s = hsb[1];
        this.v = hsb[2];
        this.a = (float) color.getAlpha() / 255;
    }

    public Color getColor() {
        Color hsbColor = this.getColorNoAlpha();
        Color color = new Color(hsbColor.getRed(), hsbColor.getGreen(), hsbColor.getBlue(), (int) (this.a * 255));
        return color;
    }

    public void setOldColor(Color oldColor) {
        this.oldColorPreview.setColor(oldColor);
    }

    public void addListener(ColorPickerListener listener) {
        this.listeners.add(listener);
    }

    public void close() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void textChanged(String before, String after) {
        System.out.println(after);
        // int size = this.alphaChannel == true ? 8 : 6;
		// after = after.replace("#", "");;
		// after += String.join("", Collections.nCopies(Math.max(0, size-after.length()), "0"));

        // Color color = ModConfig.HexToColor(after, size);
        // this.setColor(color);

        // updateAll();
    }
}