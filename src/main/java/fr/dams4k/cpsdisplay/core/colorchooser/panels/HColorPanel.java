package fr.dams4k.cpsdisplay.core.colorchooser.panels;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import fr.dams4k.cpsdisplay.core.colorchooser.ImageGenerators;

public class HColorPanel extends ImagePanel implements MouseMotionListener {
    private static float selectorLineScale = 1; // even numbers are preferred, cause i do (int) selectorLineScale/2 sometimes

    private int yPos = 0;

    public HColorPanel() {
        super(ImageGenerators.hColorSelector(), false, 0f, 4);
        addMouseMotionListener(this);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        yPos = SelectorsDrawer.clamp(event.getY(), topSideImage.getHeight(this), getHeight()-bottomSideImage.getHeight(this)-(int) (selectorLineScale/2));
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent event) {}

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // make life easier
        int minY = topSideImage.getHeight(this);
        int maxY = getHeight()-bottomSideImage.getHeight(this); 
        
        // clamping as not to draw on borders
        int x1 = leftSideImage.getWidth(this); 
        int x2 = getWidth()-leftSideImage.getWidth(this)-rightSideImage.getWidth(this);

        // SelectorsDrawer.drawXAxis(yPos, x1, x2, minY, maxY, selectorLineScale, graphics);        
        SelectorsDrawer.drawAxis(true, yPos, x1, x2, minY, maxY, selectorLineScale, graphics);
    }
}
