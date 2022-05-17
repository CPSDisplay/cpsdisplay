package fr.dams4k.cpsmod.core.colorchooser.selectors;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class HSelector extends SelectorBase {
    private SBSelector sbColorSelector;
    private float H = 0f;

    public HSelector(BufferedImage baseBufferedImage, SBSelector sbColorSelector, float H) {
        super(baseBufferedImage, true, false);

        this.sbColorSelector = sbColorSelector;
        this.H = H;

        updateIcon(0, (int) this.H * getBaseBufferedImage().getHeight());
    }
    
    @Override
    public void updateIcon(MouseEvent event) {
        super.updateIcon(event);

        H = this.clamp(event.getY(), 0, 255) / (float) getIcon().getIconHeight();
        sbColorSelector.refreshIcon(H);
        
        for (SelectorListener listener : listeners) {
            listener.HColorChange(H);
        }
    }
}
