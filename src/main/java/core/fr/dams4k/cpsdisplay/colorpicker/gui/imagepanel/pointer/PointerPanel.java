package fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.pointer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.MouseInputListener;

import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.ImagePanel;
import fr.dams4k.cpsdisplay.colorpicker.gui.imagepanel.ImageType;

public class PointerPanel extends ImagePanel implements MouseInputListener {
    private List<PointerListener> listeners = new ArrayList<>();

    private boolean valuesInitialized = false;

    public float defaultX = 0f;
    public float defaultY = 0f;

    private int x = 0;
    private int y = 0;

    private boolean showXAxis = true;
    private boolean showYAxis = true;

    public PointerPanel(Image image, ImageType imageType, float scale, boolean showXAxis, boolean showYAxis) {
        super(image, imageType, scale);
        this.showXAxis = showXAxis;
        this.showYAxis = showYAxis;

        addMouseListener(this);
        addMouseMotionListener(this);
    }
    

    public float getPointerX() {
        return (this.x-getLeftPadding())/((float) (this.getWidth()-this.getLeftPadding()-this.getRightPadding()));
    }
    public float getPointerY() {
        return (this.y-getTopPadding())/((float) (this.getHeight()-this.getTopPadding()-this.getBottomPadding()));
    }
    public void setPointerX(float x) {
        this.x = (int) (x * ((float) (this.getWidth()-this.getLeftPadding()-this.getRightPadding()))) + this.getLeftPadding();
        repaint();
    }
    public void setPointerY(float y) {
        this.y = (int) (y * ((float) (this.getHeight()-this.getTopPadding()-this.getBottomPadding()))) + this.getTopPadding();
        this.repaint();
    }

    public int getLeftPadding() {
        return this.imageBorder == null ? 0 : this.imageBorder.leftSideImage.getWidth(this);
    }
    public int getTopPadding() {
        return this.imageBorder == null ? 0 : this.imageBorder.topSideImage.getHeight(this);
    }
    public int getRightPadding() {
        return this.imageBorder == null ? 0 : this.imageBorder.rightSideImage.getWidth(this);
    }
    public int getBottomPadding() {
        return this.imageBorder == null ? 0 : this.imageBorder.bottomSideImage.getHeight(this);
    }

    public void drawXAxis(Graphics g, Color color, int size) {
        g.setColor(color);
        g.fillRect(x - (int) (size/2), getTopPadding(), size, this.getHeight()-getTopPadding()-getBottomPadding());
    }
    public void drawYAxis(Graphics g, Color color, int size) {
        g.setColor(color);
        g.fillRect(getLeftPadding(), y - (int) (size/2), this.getWidth()-getLeftPadding()-getRightPadding(), size);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if (!valuesInitialized) {
            this.setPointerX(this.defaultX);
            this.setPointerY(this.defaultY);
            valuesInitialized = true;
        }

        super.paintComponent(g);
        this.x = this.clampX(this.x);
        this.y = this.clampY(this.y);

        // Ugly code goes BRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
        if (showXAxis) {
            drawXAxis(g, Color.WHITE, 3);
        }
        if (showYAxis) {
            drawYAxis(g, Color.WHITE, 3);
        }
        if (showXAxis) {
            drawXAxis(g, Color.BLACK, 1);
        }
        if (showYAxis) {
            drawYAxis(g, Color.BLACK, 1);
        }

        if (this.imageBorder != null) {
            this.imageBorder.paintBorder(g, this, this.drawBackground);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.x = this.clampX(e.getX());
        this.y = this.clampY(e.getY());
        this.callChangedListeners();
        this.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        this.x = this.clampX(e.getX());
        this.y = this.clampY(e.getY());
        this.callChangedListeners();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = this.clampX(e.getX());
        y = this.clampY(e.getY());
        this.callChangingListeners();
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    public int clampX(int x) {
        return this.clamp(x, getLeftPadding(), this.getWidth()-getRightPadding());
    }
    public int clampY(int y) {
        return this.clamp(y, getTopPadding(), this.getHeight()-getBottomPadding());
    }
    
    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    protected void callChangedListeners() {
        for (PointerListener listener : this.listeners) {
            listener.xPointerChanged(this.getPointerX());
            listener.yPointerChanged(this.getPointerY());
        }
    }

    protected void callChangingListeners() {
        for (PointerListener listener : this.listeners) {
            listener.xPointerChanging(this.getPointerX());
            listener.yPointerChanging(this.getPointerY());
        }
    }
    public void addListener(PointerListener listener) {
        this.listeners.add(listener);
    }
}
