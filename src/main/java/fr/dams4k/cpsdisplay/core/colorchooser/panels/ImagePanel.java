package fr.dams4k.cpsdisplay.core.colorchooser.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private Image image;

    protected Image topLeftImage;
    protected Image topRightImage;
    protected Image bottomLeftImage;
    protected Image bottomRightImage;
    
    protected Image leftSideImage;
    protected Image rightSideImage;
    protected Image topSideImage;
    protected Image bottomSideImage;

    private float borderScale = 0;

    private boolean tile;
    private float brightness;

    public ImagePanel(String resourcePath, boolean tile, float scale, float brightness, float borderScale) {
        enableBorder(borderScale);
        try {
            URL iconURL = getClass().getClassLoader().getResource(resourcePath);
            Image baseImage = ImageIO.read(iconURL);
            this.image = resizeImage((BufferedImage) baseImage, scale);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.tile = tile;
        this.brightness = brightness;
    }


    public ImagePanel(BufferedImage bufferedImage, boolean tile, float brightness, float borderScale) {
        enableBorder(borderScale);
        this.tile = tile;
        this.brightness = brightness;
        this.image = bufferedImage;
    }


    public void enableBorder(float borderScale) {
        if (borderScale == 0) return;

        this.borderScale = borderScale;
        URL iconURL = getClass().getClassLoader().getResource("assets/minecraft/textures/gui/widgets.png");
        BufferedImage baseImage = null;
        try {
            baseImage = ImageIO.read(iconURL);
            BufferedImage topLeftCorner = baseImage.getSubimage(0, 0, 3, 3);
            BufferedImage topRightCorner = baseImage.getSubimage(179, 0, 3, 3);
            BufferedImage bottomLeftCorner = baseImage.getSubimage(0, 19, 3, 3);
            BufferedImage bottomRightCorner = baseImage.getSubimage(179, 19, 3, 3);

            BufferedImage leftSideBufferedImage = baseImage.getSubimage(0, 3, 3, 16);
            BufferedImage rightSideBufferedImage = baseImage.getSubimage(179, 3, 3, 16);
            BufferedImage topSideBufferedImage = baseImage.getSubimage(4, 0, 16, 3);
            BufferedImage bottomSideBufferedImage = baseImage.getSubimage(4, 19, 16, 3);

            this.topLeftImage = resizeImage((BufferedImage) topLeftCorner, borderScale);
            this.topRightImage = resizeImage((BufferedImage) topRightCorner, borderScale);
            this.bottomLeftImage = resizeImage((BufferedImage) bottomLeftCorner, borderScale);
            this.bottomRightImage = resizeImage((BufferedImage) bottomRightCorner, borderScale);

            this.leftSideImage = resizeImage((BufferedImage) leftSideBufferedImage, borderScale);
            this.rightSideImage = resizeImage((BufferedImage) rightSideBufferedImage, borderScale);
            this.topSideImage = resizeImage((BufferedImage) topSideBufferedImage, borderScale);
            this.bottomSideImage = resizeImage((BufferedImage) bottomSideBufferedImage, borderScale);
        } catch (IOException e) {
            e.printStackTrace();
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


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (borderScale > 0) {
            // draw sides before corners if sides walks on corners
            drawSides(g);
            drawCorners(g);
        }
        if (tile) {
            int iw = image.getWidth(this) - (int) (2*borderScale);
            int ih = image.getHeight(this) - (int) (2*borderScale);

            if (iw > 0 && ih > 0) {
                for (int x = 0; x < getWidth(); x += iw) {
                    for (int y = 0; y < getHeight(); y += ih) {
                        g.drawImage(image, x, y, iw, ih, this);
                    }
                }
            }
        } else {
            int x = topLeftImage == null ? 0 : topLeftImage.getWidth(this);
            int y = topLeftImage == null ? 0 : topLeftImage.getHeight(this);
            
            if (borderScale > 0) {
                g.drawImage(image, x, y, getWidth()-topLeftImage.getWidth(this)-topRightImage.getWidth(this), getHeight()-topLeftImage.getHeight(this)-topRightImage.getHeight(this), this);
            } else {
                g.drawImage(image, x, y, getWidth(), getHeight(), this);
            }
        }

        // make the background more darker if needed
        g.setColor(new Color(0f, 0f, 0f, brightness));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawCorners(Graphics g) {
        g.drawImage(topLeftImage, 0, 0, topLeftImage.getWidth(this), topLeftImage.getHeight(this), this);
        g.drawImage(bottomLeftImage, 0, getHeight()-bottomLeftImage.getHeight(this), bottomLeftImage.getWidth(this), bottomLeftImage.getHeight(this), this);
        g.drawImage(bottomRightImage, getWidth()-bottomRightImage.getWidth(this), getHeight()-bottomRightImage.getHeight(this), bottomRightImage.getWidth(this), bottomRightImage.getHeight(this), this);
        g.drawImage(topRightImage, getWidth()-topRightImage.getWidth(this), 0, topRightImage.getWidth(this), topRightImage.getHeight(this), this);
    }

    private void drawSides(Graphics g) {
        //-- LEFT SIDE
        int ls_max_height = getHeight() - bottomLeftImage.getHeight(this); // height of the panel - bottom left corner height
        int ls_height = leftSideImage.getHeight(this);
        int ls_width = leftSideImage.getWidth(this);

        for (int y = topLeftImage.getHeight(this); y < ls_max_height; y += ls_height) {
            g.drawImage(leftSideImage, 0, y, ls_width, ls_height, this);
        }

        //-- RIGHT SIDE
        int rs_max_height = getHeight() - bottomRightImage.getHeight(this); // height of the panel - bottom left corner height
        int rs_height = rightSideImage.getHeight(this);
        int rs_width = rightSideImage.getWidth(this);

        for (int y = topRightImage.getHeight(this); y < rs_max_height; y += rs_height) {
            g.drawImage(rightSideImage, getWidth()-rightSideImage.getWidth(this), y, rs_width, rs_height, this);
        }

        //-- TOP SIDE
        int ts_max_width = getWidth() - topRightImage.getWidth(this); // height of the panel - bottom left corner height
        int ts_height = topSideImage.getHeight(this);
        int ts_width = topSideImage.getWidth(this);

        for (int x = topLeftImage.getWidth(this); x < ts_max_width; x += ts_width) {
            g.drawImage(topSideImage, x, 0, ts_width, ts_height, this);
        }

        //-- BOTTOM SIDE
        int bs_max_width = getWidth() - bottomRightImage.getWidth(this); // height of the panel - bottom left corner height
        int bs_height = bottomSideImage.getHeight(this);
        int bs_width = bottomSideImage.getWidth(this);

        for (int x = bottomLeftImage.getWidth(this); x < bs_max_width; x += bs_width) {
            g.drawImage(bottomSideImage, x, getHeight()-bottomSideImage.getHeight(this), bs_width, bs_height, this);
        }
    }
}
