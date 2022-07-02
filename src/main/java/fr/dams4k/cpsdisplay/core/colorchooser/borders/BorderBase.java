package fr.dams4k.cpsdisplay.core.colorchooser.borders;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BorderBase {
    private BufferedImage baseImage;

    private float scale = 1;

    public Image topLeftImage;
    public Image bottomLeftImage;
    public Image bottomRightImage;
    public Image topRightImage;
    public Image leftSideImage;
    public Image bottomSideImage;
    public Image rightSideImage;
    public Image topSideImage;

    public BorderBase(String resourcePath, float scale) {
        this.scale = scale;
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
        BufferedImage borderImage = resizeImage(baseImage.getSubimage(x, y, w, h), scale);

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

    public BufferedImage resizeImage(BufferedImage inImage, float scale) {
        int outWidth = Math.round(inImage.getWidth() * scale);
        int outHeight = Math.round(inImage.getHeight() * scale);

        BufferedImage outImage = new BufferedImage(outWidth, outHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = outImage.createGraphics();
        graphics2D.drawImage(inImage, 0, 0, outWidth, outHeight, null);
        graphics2D.dispose();
        return outImage;
    }

    public void drawBorder(Graphics graphics, JPanel panel) {
        // draw sides before corners if sides walks on corners
        drawSides(graphics, panel);
        drawCorners(graphics, panel);
    }

    public void drawCorners(Graphics graphics, JPanel panel) {
        graphics.drawImage(topLeftImage, 0, 0, topLeftImage.getWidth(panel), topLeftImage.getHeight(panel), panel);
        graphics.drawImage(bottomLeftImage, 0, panel.getHeight()-bottomLeftImage.getHeight(panel), bottomLeftImage.getWidth(panel), bottomLeftImage.getHeight(panel), panel);
        graphics.drawImage(bottomRightImage, panel.getWidth()-bottomRightImage.getWidth(panel), panel.getHeight()-bottomRightImage.getHeight(panel), bottomRightImage.getWidth(panel), bottomRightImage.getHeight(panel), panel);
        graphics.drawImage(topRightImage, panel.getWidth()-topRightImage.getWidth(panel), 0, topRightImage.getWidth(panel), topRightImage.getHeight(panel), panel);
    }

    public void drawSides(Graphics graphics, JPanel panel) {
        //-- LEFT SIDE
        int ls_max_height = panel.getHeight() - bottomLeftImage.getHeight(panel); // height of the panel - bottom left corner height
        int ls_height = leftSideImage.getHeight(panel);
        int ls_width = leftSideImage.getWidth(panel);

        for (int y = topLeftImage.getHeight(panel); y < ls_max_height; y += ls_height) {
            graphics.drawImage(leftSideImage, 0, y, ls_width, ls_height, panel);
        }

        //-- RIGHT SIDE
        int rs_max_height = panel.getHeight() - bottomRightImage.getHeight(panel); // height of the panel - bottom left corner height
        int rs_height = rightSideImage.getHeight(panel);
        int rs_width = rightSideImage.getWidth(panel);

        for (int y = topRightImage.getHeight(panel); y < rs_max_height; y += rs_height) {
            graphics.drawImage(rightSideImage, panel.getWidth()-rightSideImage.getWidth(panel), y, rs_width, rs_height, panel);
        }

        //-- TOP SIDE
        int ts_max_width = panel.getWidth() - topRightImage.getWidth(panel); // height of the panel - bottom left corner height
        int ts_height = topSideImage.getHeight(panel);
        int ts_width = topSideImage.getWidth(panel);

        for (int x = topLeftImage.getWidth(panel); x < ts_max_width; x += ts_width) {
            graphics.drawImage(topSideImage, x, 0, ts_width, ts_height, panel);
        }

        //-- BOTTOM SIDE
        int bs_max_width = panel.getWidth() - bottomRightImage.getWidth(panel); // height of the panel - bottom left corner height
        int bs_height = bottomSideImage.getHeight(panel);
        int bs_width = bottomSideImage.getWidth(panel);

        for (int x = bottomLeftImage.getWidth(panel); x < bs_max_width; x += bs_width) {
            graphics.drawImage(bottomSideImage, x, panel.getHeight()-bottomSideImage.getHeight(panel), bs_width, bs_height, panel);
        }
    }
}
