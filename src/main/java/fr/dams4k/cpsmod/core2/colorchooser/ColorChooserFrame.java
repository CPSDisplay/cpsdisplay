package fr.dams4k.cpsmod.core.colorchooser;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.dams4k.cpsmod.core.colorchooser.selectors.HColorSelector;
import fr.dams4k.cpsmod.core.colorchooser.selectors.SBColorSelector;
import fr.dams4k.cpsmod.core.colorchooser.sliders.SlidersContainer;

public class ColorChooserFrame extends JFrame {
    public ColorChooserFrame() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel colorWheelPanel = new JPanel();
        colorWheelPanel.setLayout(new BoxLayout(colorWheelPanel, BoxLayout.X_AXIS));

        SBColorSelector sbColorSelector = new SBColorSelector(ImageGenerators.sbColorSelector(0f));
        HColorSelector hColorSelector = new HColorSelector(ImageGenerators.hColorSelector(), sbColorSelector, 0f);
        colorWheelPanel.add(sbColorSelector);
        colorWheelPanel.add(hColorSelector);

        JPanel colorSliders = new SlidersContainer();

        mainPanel.add(colorWheelPanel);
        mainPanel.add(colorSliders);
        
        add(mainPanel);

        setSize(new Dimension(326, 400));
        setMinimumSize(new Dimension(326, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Color Chooser");
        setVisible(true);
    }

}
