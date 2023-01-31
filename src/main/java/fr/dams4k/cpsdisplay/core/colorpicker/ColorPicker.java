package fr.dams4k.cpsdisplay.core.colorpicker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fr.dams4k.cpsdisplay.core.colorpicker.border.Border;
import fr.dams4k.cpsdisplay.core.colorpicker.border.InventoryBorder;
import fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.ImagePanel;
import fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.ImageType;
import fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.pointer.HPointerListener;
import fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.pointer.HPointerPanel;
import fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.pointer.PointerPanel;
import fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.pointer.SVPointerListener;
import fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.pointer.SVPointerPanel;
import fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.pointer.slider.Slider;
import fr.dams4k.cpsdisplay.core.colorpicker.imagepanel.pointer.slider.SliderListener;
import net.minecraft.util.ResourceLocation;

public class ColorPicker extends JFrame implements HPointerListener, SVPointerListener, SliderListener {
    private List<ColorPickerListener> listeners = new ArrayList<>();

    private SVPointerPanel svPointerPanel = new SVPointerPanel();
    private HPointerPanel hPointerPanel = new HPointerPanel();

    private Slider hSlider = new Slider("H", 0, 360);
    private Slider sSlider = new Slider("S", 0, 100);
    private Slider vSlider = new Slider("V", 0, 100);

    private ColorPreview oldColorPreview = new ColorPreview(Color.WHITE);
    private ColorPreview newColorPreview = new ColorPreview(Color.WHITE);

    private float h = 0f;
    private float s = 1f;
    private float v = 1f;

    public ColorPicker(Color oldColor) {
        float[] hsb =  Color.RGBtoHSB(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(), null);
        this.h = hsb[0];
        this.s = hsb[1];
        this.v = hsb[2];

        this.setTitle("ColorPicker");
        int borderSize = 8;

        Dimension size = new Dimension(280+48+4*borderSize, 600);
        this.setSize(size);
        this.setMinimumSize(size);
        // this.setResizable(false);

        Border inventoryImageBorder = new InventoryBorder(4f);

        ImagePanel background = new ImagePanel(new ResourceLocation("textures/gui/options_background.png"), ImageType.TILING, 4f);
        background.setDarkness(0.4f);
        background.setLayout(new BoxLayout(background, BoxLayout.PAGE_AXIS));
        background.setBorder(new EmptyBorder(borderSize, borderSize, borderSize, borderSize));
        this.getContentPane().add(background);


        JPanel colorsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, borderSize, borderSize));
        colorsPanel.setOpaque(false);
        colorsPanel.setMaximumSize(new Dimension(500, 256));
        background.add(colorsPanel, BorderLayout.PAGE_START);

        this.svPointerPanel.setImage(ColorPickerImages.svColorSelector(this.h, SVPointerPanel.sizeX, SVPointerPanel.sizeY));
        this.svPointerPanel.setPreferredSize(new Dimension(280, 280));
        this.svPointerPanel.setImageBorder(inventoryImageBorder);
        this.svPointerPanel.addListener(this);
        this.svPointerPanel.defaultX = this.s;
        this.svPointerPanel.defaultY = 1f-this.v;
        colorsPanel.add(this.svPointerPanel);

        this.hPointerPanel.setPreferredSize(new Dimension(48, 280));
        this.hPointerPanel.setImageBorder(inventoryImageBorder);
        this.hPointerPanel.addListener(this);
        this.hPointerPanel.defaultY = this.h;
        colorsPanel.add(this.hPointerPanel);


        JPanel sliders = new JPanel(new GridLayout(3, 0));
        sliders.setOpaque(false);
        background.add(sliders, BorderLayout.CENTER);

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

        JPanel colorsPreview = new JPanel();
        BoxLayout boxLayout = new BoxLayout(colorsPreview, BoxLayout.X_AXIS);
        colorsPreview.setLayout(boxLayout); 
        colorsPreview.setOpaque(false);

        oldColorPreview.setColor(oldColor);
        updateColorPreview();
        colorsPreview.add(newColorPreview);
        colorsPreview.add(Box.createRigidArea(new Dimension(16, 16)));
        colorsPreview.add(oldColorPreview);
        
        background.add(colorsPreview, BorderLayout.PAGE_END);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                for (ColorPickerListener listener : listeners) {
                    listener.newColor(getColor());
                }
            }
        });

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void popup() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void HColorChanged(float h) {
        this.h = h;
        this.svPointerPanel.setImage(ColorPickerImages.svColorSelector(this.h, SVPointerPanel.sizeX, SVPointerPanel.sizeY));
        this.hSlider.setValue((int) (this.h*360));

        this.updateColorPreview();
        this.updateSGradient();
        this.updateVGradient();
    }

    @Override
    public void SColorChanged(float s) {
        this.s = s;
        this.sSlider.setValue((int) (this.s*100));

        this.updateHGradient();
        this.updateVGradient();
        this.updateColorPreview();
    }

    @Override
    public void VColorChanged(float v) {
        this.v = 1f-v;
        this.vSlider.setValue((int) (this.v*100));

        this.updateHGradient();
        this.updateSGradient();
        this.updateColorPreview();
    }

    @Override
    public void sliderValueChanged(String sliderName, int value) {
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
        }

        this.updateHGradient();
        this.updateSGradient();
        this.updateVGradient();
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
        List<Color> vColors = Arrays.asList(Color.WHITE, Color.getHSBColor(this.h, 1f, 1f));
        this.sSlider.setGradient(vColors, 1f-this.v, 0f);
    }

    public void updateVGradient() {
        List<Color> vColors = Arrays.asList(Color.BLACK, Color.getHSBColor(this.h, this.s, 1f));
        this.vSlider.setGradient(vColors);
    }

    public Color getColor() {
        return Color.getHSBColor(this.h, this.s, this.v);
    }

    public void addListener(ColorPickerListener listener) {
        this.listeners.add(listener);
    }
}