package fr.dams4k.cpsdisplay.core.colorchooser.panels.sliders;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import fr.dams4k.cpsdisplay.core.colorchooser.borders.buttonborder.ButtonBorder;
import fr.dams4k.cpsdisplay.core.colorchooser.borders.buttonborder.ButtonMode;
import fr.dams4k.cpsdisplay.core.colorchooser.panels.ImagePanel;

public class SliderPanel extends ImagePanel {
    public SliderButton button = new SliderButton();

    public SliderPanel() {
        try {
            URL url = getClass().getClassLoader().getResource("assets/minecraft/textures/gui/widgets.png");
            BufferedImage baseImage = ImageIO.read(url);
            setImage(resizeImage(baseImage.getSubimage(3, 49, 24, 14), 4));
            tile = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        addBorder(new ButtonBorder(4, ButtonMode.BACKGROUND));
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;

        add(button, c);
    }
}
