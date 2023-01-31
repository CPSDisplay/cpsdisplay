package fr.dams4k.cpsdisplay.core_last.colorchooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import fr.dams4k.cpsdisplay.core_last.colorchooser.panels.ImagePanel;
import fr.dams4k.cpsdisplay.core_last.colorchooser.panels.selectors.HSelectorPanel;
import fr.dams4k.cpsdisplay.core_last.colorchooser.panels.selectors.SBSelectorPanel;
import fr.dams4k.cpsdisplay.core_last.colorchooser.panels.selectors.SelectorListener;
import fr.dams4k.cpsdisplay.core_last.colorchooser.panels.sliders.MinecraftSlider;
import fr.dams4k.cpsdisplay.core_last.colorchooser.panels.sliders.SliderPanel;

public class ColorChooserFrame extends JFrame implements SelectorListener {
    private List<ColorChooserListener> listeners = new ArrayList<>();

    private Dimension baseDimension = new Dimension(480, 480);
    private ImagePanel backgroundImagePanel = new ImagePanel("assets/minecraft/textures/gui/options_background.png", true, 4, 0.75f);
    private SBSelectorPanel SBColor = new SBSelectorPanel();
    private HSelectorPanel HColor = new HSelectorPanel();

    private SliderPanel redSlider = new SliderPanel();
    private MinecraftSlider greenSlider = new MinecraftSlider();

    public ColorChooserFrame() {
        HColor.addListener(SBColor);
        HColor.addListener(this);
        SBColor.addListener(this);

        backgroundImagePanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1f;

        c.insets = new Insets(25, 25, 25, 25);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.85;
        c.insets.right = 5;

        backgroundImagePanel.add(SBColor, c);

        c.weightx = 0.15;
        c.insets.right = 25;
        c.insets.left = 5;
        backgroundImagePanel.add(HColor, c);

        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 0.2;
        c.insets = new Insets(25, 25, 25, 25);

        // System.out.println(redSlider.border);
        // backgroundImagePanel.add(redSlider, c);
        backgroundImagePanel.add(greenSlider, c);

        add(backgroundImagePanel);

        setSize(baseDimension);
        // setMinimumSize(baseDimension);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Color Picker");
    }

    public void setColor(Color color) {
        float[] hsv = {0, 0, 0};
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsv);
        HColor.setHValue(hsv[0]);
    }

    public Color getColor() {
        Color color = Color.getHSBColor(HColor.getHValue(), SBColor.getSValue(), SBColor.getBValue());
        return color;
    }

    public void addListener(ColorChooserListener listener) {
        listeners.add(listener);
    }

    @Override
    public void HColorChanger(float h) {
        System.out.println(h);
        for (ColorChooserListener listener : listeners) {
            listener.colorChanged(getColor());
        }
    }

    @Override
    public void SColorChanger(float s) {
        System.out.println(s);
        for (ColorChooserListener listener : listeners) {
            listener.colorChanged(getColor());
        }
    }

    @Override
    public void BColorChanger(float b) {
        System.out.println(b);
        for (ColorChooserListener listener : listeners) {
            listener.colorChanged(getColor());
        }
    }
}
