package fr.dams4k.cpsdisplay.core.colorchooser.borders;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class BordersBase {
    private BufferedImage baseImage;

    private Image topLeftImage;
    private Image bottomLeftImage;
    private Image bottomRightImage;
    private Image topRightImage;

    private Image leftSideImage;
    private Image bottomSideImage;
    private Image rightSideImage;
    private Image topSideImage;

    public BordersBase(String resourcePath) {
        try {
            URL iconURL = getClass().getClassLoader().getResource(resourcePath);
            baseImage = ImageIO.read(iconURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBorder(BordersType borderType, int x, int y, int w, int h) {
        if (baseImage == null) {
            System.err.println("baseImage shouldn't be null");
            return;
        }
        BufferedImage borderImage = baseImage.getSubimage(x, y, w, h);

        switch (borderType) {
            case TOP_LEFT_CORNER:
                topLeftImage = borderImage;
                break;
            case BOTTOM_LEFT_CORNER:
                bottomLeftImage = borderImage;
                break;
            case BOTTOM_RIGHT_CORNER:
                bottomRightImage = borderImage;
                break;
            case TOP_RIGHT_CORNER:
                topRightImage = borderImage;
                break;
            case LEFT_SIDE:
                leftSideImage = borderImage;
                break;
            case BOTTOM_SIDE:
                bottomSideImage = borderImage;
                break;
            case RIGHT_SIDE:
                rightSideImage = borderImage;
                break;
            case TOP_SIDE:
                topSideImage = borderImage;
                break;
        }
    }
}
