package fr.dams4k.cpsdisplay.core.colorchooser_last.panels;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import fr.dams4k.cpsdisplay.core.colorchooser_last.ImageGenerators;
import fr.dams4k.cpsdisplay.core.colorchooser_last.selectors.HSelector;
import fr.dams4k.cpsdisplay.core.colorchooser_last.selectors.SBSelector;

public class SelectorPanel extends JPanel {
    public SBSelector sbSelector;
    public HSelector hSelector;


    public SelectorPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        sbSelector = new SBSelector(ImageGenerators.sbColorSelector(0f));
        hSelector = new HSelector(ImageGenerators.hColorSelector(), sbSelector, 0f);
        
        add(sbSelector);
        add(hSelector);
    }
}
