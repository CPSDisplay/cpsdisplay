package fr.dams4k.cpsdisplay.core.colorchooser;

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
    private boolean tile;
    private float brightness;

    public ImagePanel(String resourcePath, boolean tile, float scale, float brightness) {
        URL iconURL = getClass().getClassLoader().getResource("assets/minecraft/textures/gui/options_background.png");
        Image baseImage = null;
        try {
            baseImage = ImageIO.read(iconURL);
            this.image = resizeImage((BufferedImage) baseImage, scale);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.tile = tile;
        this.brightness = brightness;
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
        if (tile) {
            int iw = image.getWidth(this);
            int ih = image.getHeight(this);

            if (iw > 0 && ih > 0) {
                for (int x = 0; x < getWidth(); x += iw) {
                    for (int y = 0; y < getHeight(); y += ih) {
                        g.drawImage(image, x, y, iw, ih, this);
                    }
                }
            }
        } else {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
        g.setColor(new Color(0f, 0f, 0f, brightness));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
