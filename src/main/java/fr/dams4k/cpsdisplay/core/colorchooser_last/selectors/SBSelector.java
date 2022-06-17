package fr.dams4k.cpsdisplay.core.colorchooser_last.selectors;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import fr.dams4k.cpsdisplay.core.colorchooser_last.ImageGenerators;

public class SBSelector extends SelectorBase {
    float S;
    float B;

    public SBSelector(BufferedImage baseBufferedImage) {
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
        this.B = this.clamp(255-event.getY(), 0, 255) / (float) this.getIcon().getIconHeight();

        for (SelectorListener listener : listeners) {
            listener.SColorChange(S);
            listener.BColorChange(B);
        }
    }
}
