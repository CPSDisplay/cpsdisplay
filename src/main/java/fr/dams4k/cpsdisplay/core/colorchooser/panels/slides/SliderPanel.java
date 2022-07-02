package fr.dams4k.cpsdisplay.core.colorchooser.panels.slides;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import fr.dams4k.cpsdisplay.core.colorchooser.borders.ButtonBorder;
import fr.dams4k.cpsdisplay.core.colorchooser.panels.ImagePanel;

public class SliderPanel extends ImagePanel {

    public SliderPanel() {
        // super("assets/minecraft/textures/gui/options_background.png", false, 1, 0);
        super();
        
        try {
            URL url = getClass().getClassLoader().getResource("assets/minecraft/textures/gui/widgets.png");
            BufferedImage baseImage = ImageIO.read(url);
            setImage(resizeImage(baseImage.getSubimage(3, 49, 24, 14), 4));
            tile = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        addBorder(new ButtonBorder(4));
    }
}
