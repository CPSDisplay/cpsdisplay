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

    public BorderBase border = null;

    public boolean tile = false;
    public float brightness = 0f;
    public float scale = 1f;


    public ImagePanel() {}

    public ImagePanel(String resourcePath, boolean tile, float scale, float brightness) {
        try {
            URL iconURL = getClass().getClassLoader().getResource(resourcePath);
            Image baseImage = ImageIO.read(iconURL);
            this.image = baseImage;
            // this.image = resizeImage((BufferedImage) baseImage, scale);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.scale = scale;
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
        int startX = 0;
        int startY = 0;
        if (border != null) {
            // change start pos
            startX = border.topLeftImage.getWidth(this);
            startY = border.topLeftImage.getHeight(this);
        }
        if (tile) {
            int iw = (int) (image.getWidth(this) * scale);
            int ih = (int) (image.getHeight(this) * scale);

            if (iw > 0 && ih > 0) {
                for (int x = startX; x < getWidth(); x += iw) {
                    for (int y = startY; y < getHeight(); y += ih) {
                        graphics.drawImage(image, x, y, iw, ih, this);
                    }
                }
            }
        } else {
            if (border != null) {
                graphics.drawImage(image, border.topLeftImage.getWidth(this), border.topLeftImage.getHeight(this), getWidth()-border.topLeftImage.getWidth(this)-border.topRightImage.getWidth(this), getHeight()-border.topLeftImage.getHeight(this)-border.topRightImage.getHeight(this), this);
            } else {
                graphics.drawImage(image, startX, startY, getWidth(), getHeight(), this);
            }
        }

        if (border != null) {
            // draw sides before corners if sides walks on corners
            border.drawBorder(graphics, this);
        }

        // make the background more darker if needed
        graphics.setColor(new Color(0f, 0f, 0f, brightness));
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    
    public void setImage(Image image) {
        this.image = image;
    }
}
