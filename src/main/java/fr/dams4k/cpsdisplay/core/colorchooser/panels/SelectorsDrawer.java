package fr.dams4k.cpsdisplay.core.colorchooser.panels;

import java.awt.Color;
import java.awt.Graphics;

public class SelectorsDrawer {
    public static void drawAxis(boolean xAxis, int pos, int edge1, int edge2, int min, int max, float scale, Graphics graphics) {
        int blackRectPos = clamp((int) (pos - scale/2), min, max);
        int whiteRectPos = clamp((int) (blackRectPos - scale), min, max);

        int whiteRectSize = clamp((int) scale*2-(whiteRectPos-blackRectPos), 0, max-whiteRectPos);

        graphics.setColor(Color.WHITE);
        if (xAxis) {
            graphics.fillRect(edge1, whiteRectPos, edge2, whiteRectSize);
        } else {
            graphics.fillRect(whiteRectPos, edge1, whiteRectSize, edge2);
        }
        graphics.setColor(Color.BLACK);
        if (xAxis) {
            graphics.fillRect(edge1, blackRectPos, edge2, (int) scale);
        } else {
            graphics.fillRect(blackRectPos, edge1, (int) scale, edge2);
        }
    }

    public static void drawXAxis(int yPos, int x1, int x2, int minY, int maxY, float scale, Graphics graphics) {
        int blackRectYPos = clamp((int) (yPos - scale/2), minY, maxY); // rect should be in middle of your mouse, and clamping as not to draw on the borders
        int whiteRectYPos = clamp((int) (blackRectYPos - scale), minY, maxY); // only minY is important, maxY is only here cause we need a high value, but it can be anything else big enough

        int whiteRectHeight = clamp((int) scale*2-(whiteRectYPos-blackRectYPos), 0, maxY-whiteRectYPos);

        graphics.setColor(Color.WHITE);
        graphics.fillRect(x1, whiteRectYPos, x2, whiteRectHeight);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(x1, blackRectYPos, x2, (int) scale);
    }

    public static void drawYAxis(int xPos, int y1, int y2, int minX, int maxX, float scale, Graphics graphics) {
        int blackRectXPos = clamp((int) (xPos - scale/2), minX, maxX); // rect should be in middle of your mouse, and clamping as not to draw on the borders
        int whiteRectXPos = clamp((int) (blackRectXPos - scale), minX, maxX); // only minY is important, maxY is only here cause we need a high value, but it can be anything else big enough

        int whiteRectWidth = clamp((int) scale*2-(whiteRectXPos-blackRectXPos), 0, maxX-whiteRectXPos);

        graphics.setColor(Color.WHITE);
        graphics.fillRect(whiteRectXPos, y1, whiteRectWidth, y2);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(blackRectXPos, y1, (int) scale, y2);
    }


    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
