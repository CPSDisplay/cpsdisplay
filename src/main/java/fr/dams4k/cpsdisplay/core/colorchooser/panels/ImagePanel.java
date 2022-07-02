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

import fr.dams4k.cpsdisplay.core.colorchooser.borders.BorderBase;

public class ImagePanel extends JPanel {
    private Image image;

    // protected Image topLeftImage;
    // protected Image topRightImage;
    // protected Image bottomLeftImage;
    // protected Image bottomRightImage;
    
    // protected Image leftSideImage;
    // protected Image rightSideImage;
    // protected Image topSideImage;
    // protected Image bottomSideImage;

    // private float borderScale = 0;
    public BorderBase border = null;

    private boolean tile;
    private float brightness;

    public ImagePanel(String resourcePath, boolean tile, float scale, float brightness) {
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


    public ImagePanel(BufferedImage bufferedImage, boolean tile, float brightness) {
        this.tile = tile;
        this.brightness = brightness;
        this.image = bufferedImage;
    }


    public void addBorder(BorderBase border) {
        this.border = border;
    }

    public void removeBorder() {
        this.border = null;
    }

    // public void enableBorder(float borderScale) {
    //     if (borderScale == 0) return;

    //     this.borderScalegraphics = borderScale;
    //     URL iconURL = getClass().getClassLoader().getResource("assets/minecraft/textures/gui/widgets.png");
    //     BufferedImage baseImage = null;
    //     try {
    //         baseImage = ImageIO.read(iconURL);
    //         BufferedImage topLeftCorner = baseImage.getSubimage(0, 0, 3, 3);
    //         BufferedImage topRightCorner = baseImage.getSubimage(179, 0, 3, 3);
    //         BufferedImage bottomLeftCorner = baseImage.getSubimage(0, 19, 3, 3);
    //         BufferedImage bottomRightCorner = baseImage.getSubimage(179, 19, 3, 3);

    //         BufferedImage leftSideBufferedImage = baseImage.getSubimage(0, 3, 3, 16);
    //         BufferedImage rightSideBufferedImage = baseImage.getSubimage(179, 3, 3, 16);
    //         BufferedImage topSideBufferedImage = baseImage.getSubimage(4, 0, 16, 3);
    //         BufferedImage bottomSideBufferedImage = baseImage.getSubimage(4, 19, 16, 3);

    //         this.topLeftImage = resizeImage((BufferedImage) topLeftCorner, borderScale);
    //         this.topRightImage = resizeImage((BufferedImage) topRightCorner, borderScale);
    //         this.bottomLeftImage = resizeImage((BufferedImage) bottomLeftCorner, borderScale);
    //         this.bottomRightImage = resizeImage((BufferedImage) bottomRightCorner, borderScale);

    //         this.leftSideImage = resizeImage((BufferedImage) leftSideBufferedImage, borderScale);
    //         this.rightSideImage = resizeImage((BufferedImage) rightSideBufferedImage, borderScale);
    //         this.topSideImage = resizeImage((BufferedImage) topSideBufferedImage, borderScale);
    //         this.bottomSideImage = resizeImage((BufferedImage) bottomSideBufferedImage, borderScale);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }


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
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (border != null) {
            // draw sides before corners if sides walks on corners
            border.drawBorder(graphics, this);
        }
        if (tile) {
            int iw = image.getWidth(this);
            int ih = image.getHeight(this);

            if (iw > 0 && ih > 0) {
                for (int x = 0; x < getWidth(); x += iw) {
                    for (int y = 0; y < getHeight(); y += ih) {
                        graphics.drawImage(image, x, y, iw, ih, this);
                    }
                }
            }
        } else {
            if (border != null) {
                graphics.drawImage(image, border.topLeftImage.getWidth(this), border.topLeftImage.getHeight(this), getWidth()-border.topLeftImage.getWidth(this)-border.topRightImage.getWidth(this), getHeight()-border.topLeftImage.getHeight(this)-border.topRightImage.getHeight(this), this);
            } else {
                graphics.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }

        // make the background more darker if needed
        graphics.setColor(new Color(0f, 0f, 0f, brightness));
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    
    public void setImage(Image image) {
        this.image = image;
    }
}
