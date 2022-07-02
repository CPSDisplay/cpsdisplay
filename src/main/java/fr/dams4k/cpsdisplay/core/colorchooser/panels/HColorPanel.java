package fr.dams4k.cpsdisplay.core.colorchooser.panels;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import fr.dams4k.cpsdisplay.core.colorchooser.ImageGenerators;

public class HColorPanel extends ImagePanel implements MouseInputListener {
    private static float selectorLineScale = 1;

    private int yPos = 0;

    public HColorPanel() {
        super(ImageGenerators.hColorSelector(), false, 0f, 4);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void updateAxis(int posY) {
        yPos = SelectorsDrawer.clamp(posY, topSideImage.getHeight(this), getHeight()-bottomSideImage.getHeight(this)-(int) (selectorLineScale/2));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // make life easier
        int minY = topSideImage.getHeight(this);
        int maxY = getHeight()-bottomSideImage.getHeight(this); 
        
        // clamping as not to draw on borders
        int x1 = leftSideImage.getWidth(this); 
        int x2 = getWidth()-leftSideImage.getWidth(this)-rightSideImage.getWidth(this);
    
        // draw axis
        SelectorsDrawer.drawAxis(true, yPos, x1, x2, minY, maxY, selectorLineScale, graphics);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        updateAxis(event.getY());
    }

    @Override
    public void mouseMoved(MouseEvent event) {}

    @Override
    public void mouseClicked(MouseEvent event) {
        updateAxis(event.getY());
    }

    @Override
    public void mouseEntered(MouseEvent event) {}

    @Override
    public void mouseExited(MouseEvent event) {}

    @Override
    public void mousePressed(MouseEvent event) {
        // System.out.println(event.getX());
        // System.out.println(event.getY());
    }

    @Override
    public void mouseReleased(MouseEvent event) {}
}
