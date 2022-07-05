package fr.dams4k.cpsdisplay.core.colorchooser.borders;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import fr.dams4k.cpsdisplay.core.utils.Utils;

public class BorderBase {
    private BufferedImage baseImage;

    private float scale = 1;
    private Insets padding = new Insets(0, 0, 0, 0);

    public Image topLeftImage;
    public Image bottomLeftImage;
    public Image bottomRightImage;
    public Image topRightImage;
    public Image leftSideImage;
    public Image bottomSideImage;
    public Image rightSideImage;
    public Image topSideImage;

    public Image backgroundImage;

    public BorderBase(String resourcePath, float scale) {
        this(resourcePath, scale, null);
    }

    public BorderBase(String resourcePath, float scale, Insets padding) {
        this.scale = scale;
        if (padding != null) this.padding = padding;

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
            case BACKGROUND:
                backgroundImage = borderImage;
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

    public void drawBorder(Graphics graphics, JComponent component) {
        drawBorder(graphics, component, false);
    }

    public void drawBorder(Graphics graphics, JComponent component, boolean drawBackground) {
        // draw sides before corners if sides walks on corners
        if (drawBackground) drawBackground(graphics, component);
        drawSides(graphics, component);
        drawCorners(graphics, component);
    }

    public void drawBackground(Graphics graphics, JComponent component) {
        int startX = topLeftImage.getWidth(component) + padding.left;
        int startY = topLeftImage.getHeight(component) + padding.top;
        
        int iw = backgroundImage.getWidth(component);
        int ih = backgroundImage.getHeight(component);

        BufferedImage backgroundBufferedImage = (BufferedImage) backgroundImage;

        if (iw > 0 && ih > 0) {
            for (int x = startX; x < component.getWidth(); x += iw) {
                for (int y = startY; y < component.getHeight(); y += ih) {
                    int w = Math.min(component.getWidth()-x-padding.left-padding.right, iw);
                    w = Utils.clamp(w, 1, iw);
                    int h = Math.min(component.getHeight()-y-padding.top-padding.bottom, ih);
                    h = Utils.clamp(h, 1, ih);
                    
                    Image usedImage = backgroundBufferedImage.getSubimage(0, 0, w, h);
                    graphics.drawImage(usedImage, x, y, w, h, component);
                }
            }
        }
    }

    public void drawCorners(Graphics graphics, JComponent component) {
        graphics.drawImage(topLeftImage, padding.left, padding.top, topLeftImage.getWidth(component), topLeftImage.getHeight(component), component);
        graphics.drawImage(bottomLeftImage, padding.left, component.getHeight()-bottomLeftImage.getHeight(component)-padding.bottom, bottomLeftImage.getWidth(component), bottomLeftImage.getHeight(component), component);
        graphics.drawImage(bottomRightImage, component.getWidth()-bottomRightImage.getWidth(component)-padding.right, component.getHeight()-bottomRightImage.getHeight(component)-padding.bottom, bottomRightImage.getWidth(component), bottomRightImage.getHeight(component), component);
        graphics.drawImage(topRightImage, component.getWidth()-topRightImage.getWidth(component)-padding.right, padding.top, topRightImage.getWidth(component), topRightImage.getHeight(component), component);
    }

    public void drawSides(Graphics graphics, JComponent component) {
        //-- LEFT SIDE
        int ls_max_height = component.getHeight() - bottomLeftImage.getHeight(component); // height of the component - bottom left corner height
        int ls_height = leftSideImage.getHeight(component);
        int ls_width = leftSideImage.getWidth(component);

        for (int y = topLeftImage.getHeight(component); y < ls_max_height; y += ls_height) {
            graphics.drawImage(leftSideImage, padding.left, y+padding.top, ls_width, ls_height, component);
        }

        //-- RIGHT SIDE
        int rs_max_height = component.getHeight() - bottomRightImage.getHeight(component); // height of the component - bottom left corner height
        int rs_height = rightSideImage.getHeight(component);
        int rs_width = rightSideImage.getWidth(component);

        for (int y = topRightImage.getHeight(component); y < rs_max_height; y += rs_height) {
            graphics.drawImage(rightSideImage, component.getWidth()-rightSideImage.getWidth(component)-padding.right, y+padding.top, rs_width, rs_height, component);
        }

        //-- TOP SIDE
        int ts_max_width = component.getWidth() - topRightImage.getWidth(component); // height of the component - bottom left corner height
        int ts_height = topSideImage.getHeight(component);
        int ts_width = topSideImage.getWidth(component);

        for (int x = topLeftImage.getWidth(component); x < ts_max_width; x += ts_width) {
            graphics.drawImage(topSideImage, x+padding.left, padding.top, ts_width, ts_height, component);
        }

        //-- BOTTOM SIDE
        int bs_max_width = component.getWidth() - bottomRightImage.getWidth(component); // height of the component - bottom left corner height
        int bs_height = bottomSideImage.getHeight(component);
        int bs_width = bottomSideImage.getWidth(component);

        for (int x = bottomLeftImage.getWidth(component); x < bs_max_width; x += bs_width) {
            graphics.drawImage(bottomSideImage, x+padding.left, component.getHeight()-bottomSideImage.getHeight(component)-padding.bottom, bs_width, bs_height, component);
        }
    }
}
