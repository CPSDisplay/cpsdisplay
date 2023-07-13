package fr.dams4k.cpsdisplay.colorpicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.dams4k.cpsdisplay.ColorConverter;
import fr.dams4k.cpsdisplay.colorpicker.gui.Button;
import fr.dams4k.cpsdisplay.colorpicker.gui.ButtonListener;
import fr.dams4k.cpsdisplay.colorpicker.gui.ColorPreview;
import fr.dams4k.cpsdisplay.colorpicker.gui.Label;
import fr.dams4k.cpsdisplay.colorpicker.gui.border.Border;
import fr.dams4k.cpsdisplay.colorpicker.gui.border.InventoryBorder;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.ImagePanel;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.ImageType;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.pointer.HPointerListener;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.pointer.HPointerPanel;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.pointer.PointerPanel;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.pointer.SVPointerListener;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.pointer.SVPointerPanel;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.pointer.slider.Slider;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.pointer.slider.SliderListener;
import fr.dams4k.cpsdisplay.colorpicker.gui.textfield.LimitedDocument;
import fr.dams4k.cpsdisplay.colorpicker.gui.textfield.TextField;
import fr.dams4k.cpsdisplay.colorpicker.gui.textfield.TextFieldListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class ColorPicker extends JFrame implements HPointerListener, SVPointerListener, SliderListener, TextFieldListener, WindowFocusListener {
    private List<ColorPickerListener> listeners = new ArrayList<>();
   
    private final ResourceLocation BACKGROUND_RESOURCE = new ResourceLocation("cpsdisplay", "textures/gui/options_background.png");

    private final float TEXTURES_SCALE = 3f;
    private final int BORDER_SIZE = 8;
    private final String HEX_CODE = "0123456789abcdef";

    private boolean alphaChannel;
    private Dimension size;

    private ImagePanel background;

    private SVPointerPanel svPointerPanel = new SVPointerPanel();
    private HPointerPanel hPointerPanel = new HPointerPanel();

    private Slider hSlider = new Slider("H", 0, 360, this.TEXTURES_SCALE);
    private Slider sSlider = new Slider("S", 0, 100, this.TEXTURES_SCALE);
    private Slider vSlider = new Slider("V", 0, 100, this.TEXTURES_SCALE);
    private Slider aSlider = new Slider("A", 0, 100, this.TEXTURES_SCALE);

    private Label hexColorLabel = new Label(I18n.format("cpsdisplay.external.label.hex_code", new Object[0]));
    private TextField hexColorField;

    private ColorPreview oldColorPreview = new ColorPreview(Color.WHITE, this.TEXTURES_SCALE);
    private ColorPreview newColorPreview = new ColorPreview(Color.WHITE, this.TEXTURES_SCALE);

    private Button okButton = new Button(I18n.format("cpsdisplay.external.button.ok", new Object[0]), this.TEXTURES_SCALE);
    private Button cancelButton = new Button(I18n.format("cpsdisplay.external.button.cancel", new Object[0]), this.TEXTURES_SCALE);


    private float h = 0f;
    private float s = 1f;
    private float v = 1f;
    private float a = 1f;

    public ColorPicker(Color oldColor, boolean alphaChannel) {
        this.setColor(oldColor);

        this.alphaChannel = alphaChannel;

        this.setTitle("ColorPicker");

        background = new ImagePanel(BACKGROUND_RESOURCE, ImageType.TILING, this.TEXTURES_SCALE);
        background.setDarkness(0.5f);
        background.setLayout(new BoxLayout(background, BoxLayout.PAGE_AXIS));
        this.setContentPane(background);

        int sizeY = alphaChannel == true ? 640 : 600;
        size = new Dimension(320, sizeY);
        this.setSize(size);
        this.setMinimumSize(size);

        //TODO: replace all gradients by shader???
        this.addColorPointers();
        this.addGradientSliders();
        this.addHexTextField();
        this.addPreviews(oldColor);
        this.addCloseButtons();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                for (ColorPickerListener listener : listeners) {
                    listener.closed();
                }
            }
        });
        addWindowFocusListener(this);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addColorPointers() {
        Border inventoryImageBorder = new InventoryBorder(this.TEXTURES_SCALE);

        JPanel colorsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, this.BORDER_SIZE, this.BORDER_SIZE));
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
        this.updateHSlider(true, true, true);
        sliders.add(this.hSlider);

        this.sSlider.addListener(this);
        this.sSlider.setValue((int) (this.s * 100));
        this.updateSSlider(true, true, true);
        sliders.add(this.sSlider);

        this.vSlider.addListener(this);
        this.vSlider.setValue((int) (this.v * 100));
        this.updateVSlider(true, true, true);
        sliders.add(this.vSlider);

        if (this.alphaChannel) {
            this.aSlider.addListener(this);
            this.aSlider.setValue((int) (this.a * 100));
            this.updateASlider(true, true, true);
            sliders.add(this.aSlider);
        }
    }

    public void addHexTextField() {
        JPanel hexColorPanel = new JPanel(new FlowLayout());
        hexColorPanel.setOpaque(false);
        hexColorPanel.setPreferredSize(new Dimension(size.width, 44));
        hexColorPanel.setMaximumSize(new Dimension(size.width, 44));

        hexColorLabel.setPreferredSize(new Dimension((size.width-16)/2, 24));
        hexColorLabel.setMaximumSize(new Dimension((size.width-16)/2, 24));
        hexColorPanel.add(hexColorLabel);

        hexColorField = new TextField(this.TEXTURES_SCALE, alphaChannel == true ? 8 : 6);
        hexColorField.setPreferredSize(new Dimension(140, 32));
        hexColorField.setMaximumSize(new Dimension(140, 32));
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
        int colorPreviewWidth = 140;
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

        cancelButton.setPreferredSize(new Dimension(140, 48));
        cancelButton.addButtonListener(new ButtonListener() {
            @Override
            public void buttonClicked() {
                close();
                for (ColorPickerListener listener : listeners) {
                    listener.closed();
                }
            }
            
        });
        closeButtons.add(cancelButton);

        okButton.setPreferredSize(new Dimension(140, 48));
        okButton.addButtonListener(new ButtonListener() {
            @Override
            public void buttonClicked() {
                close();
                for (ColorPickerListener listener : listeners) {
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

    
    //== LISTENERS ==//
    //-   Changed   -//
    @Override
    public void HColorChanged(float h) {
        this.HColorChanging(h);
        this.updateHexTextField();
        this.updateHSlider(false, false, true);
    }
    @Override
    public void SColorChanged(float s) {
        this.SColorChanging(s);
        this.updateHexTextField();
        this.updateSSlider(false, false, true);
    }
    @Override
    public void VColorChanged(float v) {
        this.VColorChanging(v);
        this.updateHexTextField();
        this.updateVSlider(false, false, true);
    }
    @Override
    public void sliderValueChanged(String sliderName, int value) {
        this.sliderValueChanging(sliderName, value);
        this.updateHexTextField();
    }
    
    //-  Changing   -//
    //   Pointers    //
    @Override
    public void HColorChanging(float h) {
        this.h = h;
        this.svPointerPanel.setImage(ColorPickerImages.svColorSelector(this.h, SVPointerPanel.sizeX, SVPointerPanel.sizeY));

        this.updateAllSliders(true, true, false);
        this.updateColorPreview();
    }
    @Override
    public void SColorChanging(float s) {
        this.s = s;

        this.updateAllSliders(true, true, false);
        this.updateColorPreview();
    }
    @Override
    public void VColorChanging(float v) {
        this.v = 1f-v;

        this.updateAllSliders(true, true, false);
        this.updateColorPreview();
    }

    //-  Changing   -//
    //    Sliders    //
    @Override
    public void sliderValueChanging(String sliderName, int value) {
        switch (sliderName) {
            case "H":
                this.h = value/360f;
                break;
            case "S":
                this.s = value / 100f;
                break;
            case "V":
                this.v = value / 100f;
                break;
            case "A":
                this.a = value / 100f;
                break;
        }

        this.updateAllSliders(false, true, false);
        this.updateAllPointers();
        this.updateColorPreview();
    }

    //== GUI UPDATES ==//
    public void updateAllPointers() {
        this.updateHPointer();
        this.updateSVPointer();
    }

    public void updateAllSliders(boolean updateValue, boolean updatePointer, boolean updateText) {
        this.updateHSlider(updateValue, updatePointer, updateText);
        this.updateSSlider(updateValue, updatePointer, updateText);
        this.updateVSlider(updateValue, updatePointer, updateText);
        this.updateASlider(updateValue, updatePointer, updateText);
    }

    public void updateHPointer() {
        this.hPointerPanel.setPointerY(this.h);
        this.svPointerPanel.setImage(ColorPickerImages.svColorSelector(this.h, SVPointerPanel.sizeX, SVPointerPanel.sizeY));
    }
    public void updateSVPointer() {
        this.svPointerPanel.setPointerX(this.s);
        this.svPointerPanel.setPointerY(1f-this.v);
    }

    public void updateColorPreview() {
        this.newColorPreview.setColor(this.getColor());
    }

    public void updateHSlider(boolean updateValue, boolean updatePointer, boolean updateText) {
        this.hSlider.setValue(Math.round(this.h*360), updateValue, updatePointer, updateText);
        
        PointerPanel pointerPanel = this.hSlider.getPointerPanel();
        BufferedImage image = ColorPickerImages.hColorSelector(this.hSlider.gradientSizeX, this.hSlider.gradientSizeY, ColorPickerImages.ImageDisposition.HORIZONTAL);
        pointerPanel.setImage(image);
        pointerPanel.setBrightness(1f-this.s);
        pointerPanel.setDarkness(1f-this.v);
    }

    public void updateSSlider(boolean updateValue, boolean updatePointer, boolean updateText) {
        this.sSlider.setValue(Math.round(this.s*100), updateValue, updatePointer, updateText);

        List<Color> colors = Arrays.asList(Color.WHITE, Color.getHSBColor(this.h, 1f, 1f));
        this.sSlider.setGradient(colors, 1f-this.v, 0f);
    }

    public void updateVSlider(boolean updateValue, boolean updatePointer, boolean updateText) {
        this.vSlider.setValue(Math.round(this.v*100), updateValue, updatePointer, updateText);

        List<Color> colors = Arrays.asList(Color.BLACK, Color.getHSBColor(this.h, this.s, 1f));
        this.vSlider.setGradient(colors);
    }

    public void updateASlider(boolean updateValue, boolean updatePointer, boolean updateText) {
        this.aSlider.setValue(Math.round(this.a*100), updateValue, updatePointer, updateText);

        Color color = this.getColor();
        Color colorAlpha0 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
        List<Color> colors = Arrays.asList(colorAlpha0, this.getColorNoAlpha());
        this.aSlider.setAGradient(colors);
    }

    public void updateHexTextField() {
        String hexString = ColorConverter.ColorToHex(this.getColor()); // Is 8" long
        hexColorField.setText(this.alphaChannel == true ? hexString : hexString.substring(0, 6));
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
        for (char c : after.toCharArray()) {
            if (this.HEX_CODE.indexOf(c) == -1) return; 
        }

        int size = this.alphaChannel == true ? 8 : 6;
        if (after.length() == size) {
            Color color = ColorConverter.HexToColor(after, size);
            this.setColor(color);
    
            this.updateAllSliders(true, true, true);
            this.updateAllPointers();
            this.updateColorPreview();
        }
    }

    @Override
    public void windowGainedFocus(WindowEvent event) { /* Nothing to do */ }

    @Override
    public void windowLostFocus(WindowEvent event) {
        this.close();
    }
}