package fr.dams4k.cpsmod.core.colorchooser.selectors;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class SelectorBase extends JLabel implements MouseMotionListener, MouseListener {
    public List<SelectorListener> listeners = new ArrayList<SelectorListener>();

    private BufferedImage baseBufferedImage;

    private boolean showXAxis;
    private boolean showYAxis;

    private int selectX;
    private int selectY;

    private Color inlineColor = Color.BLACK;
    private Color outlineColor = Color.WHITE;

    public SelectorBase(BufferedImage baseBufferedImage, boolean showXAxis, boolean showYAxis) {
        super(new ImageIcon(baseBufferedImage));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        this.baseBufferedImage = baseBufferedImage;
        this.showXAxis = showXAxis;
        this.showYAxis = showYAxis;

        addMouseMotionListener(this);
        addMouseListener(this);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        this.updateIcon(event);
    }

    @Override
    public void mouseMoved(MouseEvent event) {
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        this.updateIcon(event);
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }

    public void updateIcon(MouseEvent event) {
        this.selectX = event.getX();
        this.selectY = event.getY();

        this.setIcon(new ImageIcon(this.bufferedImageGenAxis(selectX, selectY)));
    }

    public void updateIcon(int selectX, int selectY) {
        this.selectX = selectX;
        this.selectY = selectY;

        this.setIcon(new ImageIcon(this.bufferedImageGenAxis(selectX, selectY)));
    }

    public BufferedImage bufferedImageGenAxis(int event_x, int event_y) {
        BufferedImage newBufferedImage = this.copyBuffered(baseBufferedImage);

        if (this.showXAxis) {
            for (int x = 0; x < newBufferedImage.getWidth(); x++) {
                newBufferedImage.setRGB(x, clamp(event_y, 0, newBufferedImage.getHeight() - 1), inlineColor.getRGB());

                if (event_y - 1 >= 0 && (x != event_x || !this.showYAxis)) {
                    newBufferedImage.setRGB(x, clamp(event_y - 1, 0, newBufferedImage.getHeight() - 1),
                            outlineColor.getRGB());
                }
                if (event_y + 1 < newBufferedImage.getHeight() - 1 && (x != event_x || !this.showYAxis)) {
                    newBufferedImage.setRGB(x, clamp(event_y + 1, 0, newBufferedImage.getHeight() - 1),
                            outlineColor.getRGB());
                }
            }
        }

        if (this.showYAxis) {
            for (int y = 0; y < newBufferedImage.getWidth(); y++) {
                newBufferedImage.setRGB(clamp(event_x, 0, newBufferedImage.getWidth() - 1), y, inlineColor.getRGB());

                if (event_x - 1 >= 0 && (y != event_y || !this.showXAxis)) {
                    newBufferedImage.setRGB(clamp(event_x - 1, 0, newBufferedImage.getWidth() - 1), y,
                            outlineColor.getRGB());
                }
                if (event_x + 1 < newBufferedImage.getWidth() - 1 && (y != event_y || !this.showXAxis)) {
                    newBufferedImage.setRGB(clamp(event_x + 1, 0, newBufferedImage.getWidth() - 1), y,
                            outlineColor.getRGB());
                }
            }
        }

        return newBufferedImage;
    }

    public BufferedImage copyBuffered(BufferedImage bufferedImage) {
        ColorModel colorModel = bufferedImage.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = bufferedImage.copyData(null);
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    public int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public void setBaseBufferedImage(BufferedImage baseBufferedImage) {
        this.baseBufferedImage = baseBufferedImage;
        this.setIcon(new ImageIcon(this.bufferedImageGenAxis(this.selectX, this.selectY)));
    }

    public BufferedImage getBaseBufferedImage() {
        return baseBufferedImage;
    }

    public void addListener(SelectorListener newListener) {
        listeners.add(newListener);
    }
}
