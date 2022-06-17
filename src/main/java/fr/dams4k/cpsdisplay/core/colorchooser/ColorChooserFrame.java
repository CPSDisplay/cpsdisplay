package fr.dams4k.cpsdisplay.core.colorchooser;

import java.awt.Dimension;

import javax.swing.JFrame;

public class ColorChooserFrame extends JFrame {
    private Dimension baseDimension = new Dimension(288, 480);
    private ImagePanel backgroundImagePanel = new ImagePanel("assets/minecraft/textures/gui/options_background.png", true, 4, 0.75f);

    public ColorChooserFrame() {
        add(backgroundImagePanel);

        setSize(baseDimension);
        setMinimumSize(baseDimension);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Color Chooser");
        setVisible(true);
    }
}
