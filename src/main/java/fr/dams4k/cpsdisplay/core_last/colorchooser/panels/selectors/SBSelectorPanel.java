package fr.dams4k.cpsdisplay.core_last.colorchooser.panels.selectors;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import fr.dams4k.cpsdisplay.core_last.colorchooser.ImageGenerators;

public class SBSelectorPanel extends SelectorPanel implements MouseInputListener, SelectorListener {
    private static float selectorLineScale = 1;

    private int yPos = 0;
    private int xPos = 0;

    public SBSelectorPanel() {
        super(ImageGenerators.sbColorSelector(0f), false, 0f);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void updateAxis(int posX, int posY, boolean alert) {
        yPos = clamp(posY, border.topSideImage.getHeight(this), getHeight()-border.bottomSideImage.getHeight(this)-(int) (selectorLineScale/2));
        xPos = clamp(posX, border.leftSideImage.getWidth(this), getWidth()-border.rightSideImage.getWidth(this)-(int) (selectorLineScale/2));
        
        if (alert) { // for listeners
            for (SelectorListener listener : listeners) {
                listener.SColorChanger(getSValue());
                listener.BColorChanger(getBValue());
            }
        }

        repaint();
    }


    public float getSValue() {
        float realPos = xPos-border.leftSideImage.getWidth(this);
        float size = getWidth()-border.leftSideImage.getWidth(this)-border.rightSideImage.getWidth(this)-(int) (selectorLineScale/2); // remove borders
        return clamp(realPos/size, 0f, 1f); // prevent bugs
    }

    public float getBValue() {
        float realPos = yPos-border.topSideImage.getHeight(this);
        float size = getHeight()-border.topSideImage.getHeight(this)-border.bottomSideImage.getHeight(this)-(int) (selectorLineScale/2); // remove borders
        return 1-clamp(realPos/size, 0f, 1f); // prevent bugs
    }


    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // make life easier
        int minY = border.topSideImage.getHeight(this);
        int maxY = getHeight()-border.bottomSideImage.getHeight(this); 
        int minX = border.leftSideImage.getWidth(this);
        int maxX = getWidth()-border.rightSideImage.getWidth(this);
        
        // clamping as not to draw on borders
        int x1 = border.leftSideImage.getWidth(this); 
        int x2 = getWidth()-border.leftSideImage.getWidth(this)-border.rightSideImage.getWidth(this);
        int y1 = border.topLeftImage.getHeight(this);
        int y2 = getHeight()-border.topLeftImage.getWidth(this)-border.bottomSideImage.getHeight(this);

        // draw axis
        drawAxis(true, yPos, x1, x2, minY, maxY, selectorLineScale, graphics);
        drawAxis(false, xPos, y1, y2, minX, maxX, selectorLineScale, graphics);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        updateAxis(event.getX(), event.getY(), true);
    }

    @Override
    public void mouseMoved(MouseEvent event) {}

    @Override
    public void mouseClicked(MouseEvent event) {
        updateAxis(event.getX(), event.getY(), true);
    }

    @Override
    public void mouseEntered(MouseEvent event) {}

    @Override
    public void mouseExited(MouseEvent event) {}

    @Override
    public void mousePressed(MouseEvent event) {}

    @Override
    public void mouseReleased(MouseEvent event) {}

    @Override
    public void HColorChanger(float h) {
        setImage(ImageGenerators.sbColorSelector(h));
        repaint();
    }

    @Override
    public void SColorChanger(float s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void BColorChanger(float b) {
        // TODO Auto-generated method stub
        
    }
}
