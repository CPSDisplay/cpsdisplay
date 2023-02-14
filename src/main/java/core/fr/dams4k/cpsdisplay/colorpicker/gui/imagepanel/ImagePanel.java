package fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import fr.dams4k.cpsdisplay.colorpicker.gui.border.Border;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class ImagePanel extends JPanel {
    private final Minecraft mc = Minecraft.getMinecraft();

    private Image image;

    public ImageType imageType = ImageType.NORMAL;
    
    private float darkness = 0f;
    private float brightness = 0f;

    public float scale = 1f;
    public boolean drawBackground = true;

    protected Border imageBorder;

    public ImagePanel() {}

    public ImagePanel(Image image, ImageType imageType, float scale) {
        this.image = image;

        this.imageType = imageType;
        this.scale = scale;
    }

    public ImagePanel(String path, ImageType imageType, float scale) {
        try {
            URL url = getClass().getClassLoader().getResource(path);
            Image image = ImageIO.read(url);
            this.image = image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.imageType = imageType;
        this.scale = scale;
    }

    public ImagePanel(ResourceLocation imageLocation, ImageType imageType, float scale) {
        try {
            this.image = TextureUtil.readBufferedImage(mc.getResourceManager().getResource(imageLocation).getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.imageType = imageType;
        this.scale = scale;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int topX = this.imageBorder == null ? 0 : this.imageBorder.topLeftImage.getWidth(this);
        int topY = this.imageBorder == null ? 0 : this.imageBorder.topLeftImage.getHeight(this);
        int borderWidth = this.imageBorder == null ? 0 : this.imageBorder.getWidth(this);
        int borderHeight = this.imageBorder == null ? 0 : this.imageBorder.getHeight(this);

        int width = 0;
        int height = 0;

        if (this.image != null) {
            switch (this.imageType) {
                case NORMAL:
                    width = (int) (this.image.getWidth(this) * this.scale) - borderWidth;
                    height = (int) (this.image.getHeight(this) * this.scale) - borderHeight;
                    break;
                case STRETCHING:
                    width = (int) (this.getWidth() * this.scale) - borderWidth;
                    height = (int) (this.getHeight() * this.scale) - borderHeight;
                    break;
                case TILING:
                    width = (int) (this.image.getWidth(this) * this.scale) - borderWidth;
                    height = (int) (this.image.getHeight(this) * this.scale) - borderHeight;
    
                    if (width > 0 && height > 0) {
                        for (int x = topX; x < this.getWidth(); x += width) {
                            for (int y = topY; y < this.getHeight(); y += height) {
                                g.drawImage(this.image, x, y, width, height, this);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
            if (!this.imageType.equals(ImageType.TILING)) {
                g.drawImage(this.image, topX, topY, width, height, this);
            }
        }

        if (this.imageBorder != null) {
            this.imageBorder.paintBorder(g, this, this.drawBackground);
        }

        g.setColor(new Color(1f, 1f, 1f, this.brightness));
        g.fillRect(topX, topY, this.getWidth()-borderWidth, this.getHeight()-borderHeight);
        g.setColor(new Color(0f, 0f, 0f, this.darkness));
        g.fillRect(topX, topY, this.getWidth()-borderWidth, this.getHeight()-borderHeight);
    }

    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
        this.repaint();
    }

    public Border getImageBorder() {
        return imageBorder;
    }
    public void setImageBorder(Border imageBorder) {
        this.imageBorder = imageBorder;
    }

    public float getBrightness() {
        return brightness;
    }
    public void setBrightness(float brightness) {
        this.brightness = brightness;
        this.repaint();
    }
    public float getDarkness() {
        return darkness;
    }
    public void setDarkness(float darkness) {
        this.darkness = darkness;
        this.repaint();
    }
}
