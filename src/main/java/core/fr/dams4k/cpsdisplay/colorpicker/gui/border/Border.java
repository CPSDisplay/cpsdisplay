package fr.dams4k.cpsdisplay.colorpicker.gui.border;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;


public class Border {
    private final Minecraft mc = Minecraft.getMinecraft();
    
    private BufferedImage baseImage;

    public float imageScaleFactor = 1f;
    public float scale = 1f;
    public Insets insets = new Insets(0, 0, 0, 0);

    public Image topLeftImage;
    public Image bottomLeftImage;
    public Image bottomRightImage;
    public Image topRightImage;
    public Image leftSideImage;
    public Image bottomSideImage;
    public Image rightSideImage;
    public Image topSideImage;

    public Image backgroundImage;

    public Border(String resourcePath, float scale) {
        this(resourcePath, scale, null);
    }

    public Border(ResourceLocation imageLocation, int[] originalSize, float scale, Insets insets) {
        this.scale = scale;
        if (insets != null) this.insets = insets;

        try {
            this.baseImage = TextureUtil.readBufferedImage(mc.getResourceManager().getResource(imageLocation).getInputStream());
            float xScaleFactor = this.baseImage.getWidth() / (float) originalSize[0];
            float yScaleFactor = this.baseImage.getHeight() / (float) originalSize[1];
            this.imageScaleFactor = xScaleFactor < yScaleFactor ? xScaleFactor : yScaleFactor;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Border(String resourcePath, float scale, Insets insets) {
        this.scale = scale;
        if (insets != null) this.insets = insets;

        try {
            URL iconURL = getClass().getClassLoader().getResource(resourcePath);
            this.baseImage = ImageIO.read(iconURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setInsets(Insets insets) {
        this.insets = insets;
    }
    public Insets getInsets() {
        return insets;
    }

    public void setBorder(BorderType borderType, int x, int y, int w, int h) {
        if (this.baseImage == null) {
            return; // Base image shouldn't be null
        }
        
        x = Math.round((x * this.imageScaleFactor));
        y = Math.round((y * this.imageScaleFactor));
        w = Math.max(Math.round((w * this.imageScaleFactor)), 1);
        h = Math.max(Math.round((h * this.imageScaleFactor)), 1);
        
        w = this.clamp(w, 0, baseImage.getWidth()-x);
        h = this.clamp(h, 0, baseImage.getHeight()-y);

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

        BufferedImage outImage = new BufferedImage(outWidth, outHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = outImage.createGraphics();
        graphics2D.drawImage(inImage, 0, 0, outWidth, outHeight, null);
        graphics2D.dispose();
        return outImage;
    }

    public void paintBorder(Graphics graphics, JComponent component, boolean drawBackground) {
        // draw sides before corners if sides walks on corners
        if (drawBackground) paintBackground(graphics, component);
        paintSides(graphics, component);
        paintCorners(graphics, component);
    }

    public int getCorrectXSize(int maxW, int imageWidth) {
        int w = maxW-insets.left-insets.right;
        return this.clamp(w, 1, imageWidth);
    }

    public int getCorrectYSize(int maxH, int imageH) {
        int h = maxH - insets.top-insets.bottom;
        return this.clamp(h, 1, imageH);
    }

    public void paintBackground(Graphics graphics, JComponent component) {
        if (backgroundImage == null) return;
        int startX = topLeftImage.getWidth(component) + insets.left;
        int startY = topLeftImage.getHeight(component) + insets.top;
        
        int iw = backgroundImage.getWidth(component);
        int ih = backgroundImage.getHeight(component);

        if (iw > 0 && ih > 0) {
            for (int x = startX; x < component.getWidth()-insets.right; x += iw) {
                for (int y = startY; y < component.getHeight()-insets.bottom; y += ih) {
                    int w = getCorrectXSize(component.getWidth()-x, iw);
                    int h = getCorrectYSize(component.getHeight()-y+insets.top, ih);
                    Image usedImage = ((BufferedImage) backgroundImage).getSubimage(0, 0, w, h);
                    graphics.drawImage(usedImage, x, y, w, h, component);
                }
            }
        }
    }

    public void paintCorners(Graphics graphics, JComponent component) {
        graphics.drawImage(topLeftImage, insets.left, insets.top, topLeftImage.getWidth(component), topLeftImage.getHeight(component), component);
        graphics.drawImage(bottomLeftImage, insets.left, component.getHeight()-bottomLeftImage.getHeight(component)-insets.bottom, bottomLeftImage.getWidth(component), bottomLeftImage.getHeight(component), component);
        graphics.drawImage(bottomRightImage, component.getWidth()-bottomRightImage.getWidth(component)-insets.right, component.getHeight()-bottomRightImage.getHeight(component)-insets.bottom, bottomRightImage.getWidth(component), bottomRightImage.getHeight(component), component);
        graphics.drawImage(topRightImage, component.getWidth()-topRightImage.getWidth(component)-insets.right, insets.top, topRightImage.getWidth(component), topRightImage.getHeight(component), component);
    }

    public void paintSides(Graphics graphics, JComponent component) {
        //-- LEFT SIDE
        int lsMaxHeight = component.getHeight() - bottomLeftImage.getHeight(component); // height of the component - bottom left corner height
        int lsHeight = leftSideImage.getHeight(component);
        int lsWidth = leftSideImage.getWidth(component);

        for (int y = topLeftImage.getHeight(component); y < lsMaxHeight; y += lsHeight) {
            int h = getCorrectYSize(component.getHeight()-y, lsHeight);
            Image usedLeftSideImage = ((BufferedImage) leftSideImage).getSubimage(0, 0, lsWidth, h);

            graphics.drawImage(usedLeftSideImage, insets.left, y+insets.top, lsWidth, h, component);
        }

        //-- RIGHT SIDE
        int rsMaxHeight = component.getHeight() - bottomRightImage.getHeight(component); // height of the component - bottom left corner height
        int rsHeight = rightSideImage.getHeight(component);
        int rsWidth = rightSideImage.getWidth(component);

        for (int y = topRightImage.getHeight(component); y < rsMaxHeight; y += rsHeight) {
            int h = getCorrectYSize(component.getHeight()-y, rsHeight);
            Image usedRightSideImage = ((BufferedImage) rightSideImage).getSubimage(0, 0, rsWidth, h);

            graphics.drawImage(usedRightSideImage, component.getWidth()-rightSideImage.getWidth(component)-insets.right, y+insets.top, rsWidth, h, component);
        }

        //-- TOP SIDE
        int tsMaxWidth = component.getWidth() - topRightImage.getWidth(component); // height of the component - bottom left corner height
        int tsHeight = topSideImage.getHeight(component);
        int tsWidth = topSideImage.getWidth(component);

        for (int x = topLeftImage.getWidth(component); x < tsMaxWidth; x += tsWidth) {
            int w = getCorrectXSize(component.getWidth()-x, tsWidth);
            Image usedTopSideImage = ((BufferedImage) topSideImage).getSubimage(0, 0, w, tsHeight);

            graphics.drawImage(usedTopSideImage, x+insets.left, insets.top, w, tsHeight, component);
        }

        //-- BOTTOM SIDE
        int bsMaxWidth = component.getWidth() - bottomRightImage.getWidth(component); // height of the component - bottom left corner height
        int bsHeight = bottomSideImage.getHeight(component);
        int bsWidth = bottomSideImage.getWidth(component);

        for (int x = bottomLeftImage.getWidth(component); x < bsMaxWidth; x += bsWidth) {
            int w = getCorrectXSize(component.getWidth()-x, bsWidth);
            Image usedBottomSideImage = ((BufferedImage) bottomSideImage).getSubimage(0, 0, w, bsHeight);

            graphics.drawImage(usedBottomSideImage, x+insets.left, component.getHeight()-bottomSideImage.getHeight(component)-insets.bottom, w, bsHeight, component);
        }
    }

    public int getWidth(ImageObserver observer) {
        return this.leftSideImage.getWidth(observer) + this.rightSideImage.getWidth(observer);
    }
    public int getHeight(ImageObserver observer) {
        return this.topSideImage.getHeight(observer) + this.bottomSideImage.getHeight(observer);
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
