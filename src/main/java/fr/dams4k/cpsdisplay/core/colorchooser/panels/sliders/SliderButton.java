package fr.dams4k.cpsdisplay.core.colorchooser.panels.sliders;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import fr.dams4k.cpsdisplay.core.colorchooser.borders.buttonborder.ButtonBorder;
import fr.dams4k.cpsdisplay.core.colorchooser.borders.buttonborder.ButtonMode;
import fr.dams4k.cpsdisplay.core.colorchooser.panels.ImagePanel;

public class SliderButton extends ImagePanel {
    public SliderButton() {
        try {
            URL url = getClass().getClassLoader().getResource("assets/minecraft/textures/gui/widgets.png");
            BufferedImage baseImage = ImageIO.read(url);
            setImage(resizeImage(baseImage.getSubimage(3, 69, 24, 14), 4));
            tile = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        addBorder(new ButtonBorder(4, ButtonMode.NORMAL));
        setMinimumSize(new Dimension(100, 20));
    }
}
