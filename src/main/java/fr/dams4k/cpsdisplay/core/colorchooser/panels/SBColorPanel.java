package fr.dams4k.cpsdisplay.core.colorchooser.panels;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import fr.dams4k.cpsdisplay.core.colorchooser.ImageGenerators;

public class SBColorPanel extends ImagePanel implements MouseInputListener {
    private static float selectorLineScale = 1;

    private int yPos = 0;
    private int xPos = 0;

    public SBColorPanel() {
        super(ImageGenerators.sbColorSelector(0f), false, 0f, 4);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void updateAxis(int posX, int posY) {
        yPos = SelectorsDrawer.clamp(posY, topSideImage.getHeight(this), getHeight()-bottomSideImage.getHeight(this)-(int) (selectorLineScale/2));
        xPos = SelectorsDrawer.clamp(posX, leftSideImage.getWidth(this), getWidth()-rightSideImage.getWidth(this)-(int) (selectorLineScale/2));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // make life easier
        int minY = topSideImage.getHeight(this);
        int maxY = getHeight()-bottomSideImage.getHeight(this); 
        int minX = leftSideImage.getWidth(this);
        int maxX = getWidth()-rightSideImage.getWidth(this);
        
        // clamping as not to draw on borders
        int x1 = leftSideImage.getWidth(this); 
        int x2 = getWidth()-leftSideImage.getWidth(this)-rightSideImage.getWidth(this);
        int y1 = topLeftImage.getHeight(this);
        int y2 = getHeight()-topLeftImage.getWidth(this)-bottomSideImage.getHeight(this);

        // draw axis
        SelectorsDrawer.drawAxis(true, yPos, x1, x2, minY, maxY, selectorLineScale, graphics);
        SelectorsDrawer.drawAxis(false, xPos, y1, y2, minX, maxX, selectorLineScale, graphics);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        updateAxis(event.getX(), event.getY());
    }

    @Override
    public void mouseMoved(MouseEvent event) {}

    @Override
    public void mouseClicked(MouseEvent event) {
        updateAxis(event.getX(), event.getY());
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        // TODO Auto-generated method stub
        
    }
}
