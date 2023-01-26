package fr.dams4k.cpsdisplay.core.colorchooser.panels.selectors;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import fr.dams4k.cpsdisplay.core.colorchooser.ImageGenerators;

public class HSelectorPanel extends SelectorPanel implements MouseInputListener {
    private static float selectorLineScale = 1;

    private int yPos = 0;
    
    public HSelectorPanel() {
        super(ImageGenerators.hColorSelector(), false, 0f);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void setHValue(float h) {
        int gradientSize = getHeight() - border.topSideImage.getHeight(this)-border.bottomSideImage.getHeight(this);
        System.out.println(gradientSize);
        System.out.println(gradientSize * h);
        updateAxis((int) (gradientSize * h), true);
    }

    public float getHValue() {
        float yPosInGradient = yPos-border.topSideImage.getHeight(this); // min value of yPos is not 0 cause of the topSideImage height
        float gradientHeight = getHeight()-border.topSideImage.getHeight(this)-border.bottomSideImage.getHeight(this)-(int) (selectorLineScale/2); // remove borders
        return clamp(yPosInGradient/gradientHeight, 0f, 1f); // prevent bugs
    }

    public void updateAxis(int posY, boolean alert) {
        yPos = clamp(posY, border.topSideImage.getHeight(this), getHeight()-border.bottomSideImage.getHeight(this)-(int) (selectorLineScale/2));
        if (alert) { // for listeners
            for (SelectorListener listener : listeners) {
                listener.HColorChanger(getHValue());
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // make life easier
        int minY = border.topSideImage.getHeight(this);
        int maxY = getHeight()-border.bottomSideImage.getHeight(this); 
        
        // clamping as not to draw on borders
        int x1 = border.leftSideImage.getWidth(this); 
        int x2 = getWidth()-border.leftSideImage.getWidth(this)-border.rightSideImage.getWidth(this);
    
        // draw axis
        drawAxis(true, yPos, x1, x2, minY, maxY, selectorLineScale, graphics);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        updateAxis(event.getY(), true);
    }

    @Override
    public void mouseMoved(MouseEvent event) {}

    @Override
    public void mouseClicked(MouseEvent event) {
        updateAxis(event.getY(), true);
    }

    @Override
    public void mouseEntered(MouseEvent event) {}

    @Override
    public void mouseExited(MouseEvent event) {}

    @Override
    public void mousePressed(MouseEvent event) {}

    @Override
    public void mouseReleased(MouseEvent event) {}
}
