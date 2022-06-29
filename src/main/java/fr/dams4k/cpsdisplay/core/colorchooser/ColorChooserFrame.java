package fr.dams4k.cpsdisplay.core.colorchooser;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ColorChooserFrame extends JFrame {
    private Dimension baseDimension = new Dimension(288, 480);
    private ImagePanel backgroundImagePanel = new ImagePanel("assets/minecraft/textures/gui/options_background.png", true, 4, 0.75f);
    private ImagePanel SBColor = new ImagePanel(ImageGenerators.sbColorSelector(0f), false, 0f);
    private ImagePanel HColor = new ImagePanel(ImageGenerators.hColorSelector(), false, 0f);

    public ColorChooserFrame() {
        backgroundImagePanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1f;

        c.insets = new Insets(25, 25, 25, 25);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.90;
        c.insets.right = 5;

        backgroundImagePanel.add(SBColor, c);

        c.weightx = 0.1;
        c.insets.right = 25;
        c.insets.left = 5;
        backgroundImagePanel.add(HColor, c);
        
        add(backgroundImagePanel);

        setSize(baseDimension);
        setMinimumSize(baseDimension);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Color Chooser");
        setVisible(true);
    }
}
