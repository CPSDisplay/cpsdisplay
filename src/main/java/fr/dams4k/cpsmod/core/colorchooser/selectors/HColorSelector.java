package fr.dams4k.cpsmod.core.colorchooser.selectors;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class HColorSelector extends SelectorBase {
    private SBColorSelector sbColorSelector;
    private float H = 0f;

    public HColorSelector(BufferedImage baseBufferedImage, SBColorSelector sbColorSelector, float H) {
        super(baseBufferedImage, true, false);

        this.sbColorSelector = sbColorSelector;
        this.H = H;

        updateIcon(0, (int) this.H * getBaseBufferedImage().getHeight());
    }
    
    @Override
    public void updateIcon(MouseEvent event) {
        super.updateIcon(event);

        this.H = this.clamp(event.getY(), 0, 255) / (float) this.getIcon().getIconHeight();
        this.sbColorSelector.refreshIcon(this.H);        
    }
}
