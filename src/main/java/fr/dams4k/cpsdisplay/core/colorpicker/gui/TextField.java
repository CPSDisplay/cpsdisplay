package fr.dams4k.cpsdisplay.core.colorpicker.gui;

import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import fr.dams4k.cpsdisplay.core.colorpicker.gui.border.Border;
import fr.dams4k.cpsdisplay.core.colorpicker.gui.border.TextFieldBorder;

public class TextField extends Label implements MouseListener, KeyListener {
    private Border imageBorder;

    private boolean isFocused = false;

    public TextField(float textureScale) {
        super("");
        this.imageBorder = new TextFieldBorder(textureScale);

        this.setFocusable(true);
        this.addMouseListener(this);
        this.addKeyListener(this);
    }

    protected void paintComponent(Graphics g) {
        this.imageBorder.drawBorder(g, this, true);
        super.paintComponent(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.isFocused = true;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!this.isFocused) return;

        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (this.text.length() <= 0) return;
            this.text = this.text.substring(0, this.text.length()-1);
        } else if (e.getKeyCode() == KeyEvent.VK_V && (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
            String data = "";
            try {
                data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            } catch (HeadlessException | UnsupportedFlavorException | IOException e1) {
                e1.printStackTrace();
            }
            this.text += data;
        } else {
            this.text += e.getKeyChar();
        }

        System.out.println(this.text.length());

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
