package fr.dams4k.cpsmod.core.colorchooser.selectors;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import fr.dams4k.cpsmod.core.colorchooser.ImageGenerators;

public class SBColorSelector extends SelectorBase {
    float S;
    float B;

    public SBColorSelector(BufferedImage baseBufferedImage) {
        super(baseBufferedImage, true, true);
        updateIcon(0, 0);
    }

    public void refreshIcon(float H) {
        setBaseBufferedImage(ImageGenerators.sbColorSelector(H));
    }

    @Override
    public void updateIcon(MouseEvent event) {
        super.updateIcon(event);

        this.S = this.clamp(event.getX(), 0, 255) / (float) this.getIcon().getIconWidth();
        this.B = this.clamp(event.getY(), 0, 255) / (float) this.getIcon().getIconHeight();
    }
}
