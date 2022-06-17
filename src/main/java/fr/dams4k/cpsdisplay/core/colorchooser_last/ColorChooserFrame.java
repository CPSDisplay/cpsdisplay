package fr.dams4k.cpsdisplay.core.colorchooser_last;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.dams4k.cpsdisplay.core.colorchooser_last.panels.SelectorPanel;
import fr.dams4k.cpsdisplay.core.colorchooser_last.panels.SlidersPanel;
import fr.dams4k.cpsdisplay.core.colorchooser_last.selectors.SelectorListener;
import fr.dams4k.cpsdisplay.core.colorchooser_last.sliders.SliderListener;

public class ColorChooserFrame extends JFrame implements SelectorListener, SliderListener {
    private float H = 0;
    private float S = 0;
    private float B = 0;

    private SelectorPanel selectorPanel;
    private SlidersPanel slidersPanel;

    public ColorChooserFrame() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        selectorPanel = new SelectorPanel();
        selectorPanel.hSelector.addListener(this);
        selectorPanel.sbSelector.addListener(this);

        // slidersPanel = new SlidersPanel();
        // slidersPanel.redSlider.addListener(this);
        // slidersPanel.greenSlider.addListener(this);
        // slidersPanel.blueSlider.addListener(this);

        mainPanel.add(selectorPanel);
        // mainPanel.add(slidersPanel);
        
        add(mainPanel);

        setSize(new Dimension(326, 400));
        setMinimumSize(new Dimension(326, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Color Chooser");
        setVisible(true);
    }

    public void onSelectorColorChanged(float H, float S, float B) {
        this.H = H;
        this.S = S;
        this.B = B;

        Color color = Color.getHSBColor(H, S, B);

        // this.slidersPanel.redSlider.slider.setValue(color.getRed());
        // this.slidersPanel.greenSlider.slider.setValue(color.getGreen());
        // this.slidersPanel.blueSlider.slider.setValue(color.getBlue());
    }

    public void onSliderColorChanged(int Red, int Green, int Blue) {
        float[] hsb = Color.RGBtoHSB(Red, Green, Blue, null);

        this.H = hsb[0];
        this.S = hsb[1];
        this.B = hsb[2];

        System.out.println(Red);
        System.out.println(Green);
        System.out.println(Blue);
        System.out.println();

        this.selectorPanel.hSelector.updateIcon(0, (int) H);
        this.selectorPanel.sbSelector.updateIcon((int) S * 256, (int) B * 256);
    }
    
    @Override
    public void HColorChange(float H) { onSelectorColorChanged(H, S, B); }

    @Override
    public void SColorChange(float S) { onSelectorColorChanged(H, S, B); }

    @Override
    public void BColorChange(float B) { onSelectorColorChanged(H, S, B); }

    @Override
    public void RColorChange(int red) {
        Color color = Color.getHSBColor(this.H, this.S, this.B);
        onSliderColorChanged(red, color.getBlue(), color.getGreen());
    }

    @Override
    public void GColorChange(int green) {
        Color color = Color.getHSBColor(this.H, this.S, this.B);
        onSliderColorChanged(color.getRed(), green, color.getBlue());
    }

    @Override
    public void BColorChange(int blue) {
        Color color = Color.getHSBColor(this.H, this.S, this.B);
        onSliderColorChanged(color.getRed(), color.getGreen(), blue);
    }
}
