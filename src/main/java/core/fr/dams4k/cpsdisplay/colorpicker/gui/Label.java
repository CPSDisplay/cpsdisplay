package fr.dams4k.cpsdisplay.colorpicker.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.commons.io.IOUtils;

import fr.dams4k.cpsdisplay.ColorConverter;
import fr.dams4k.cpsdisplay.proxy.ClientProxy;

public class Label extends JPanel {
    private static final int DEFAULT_CHAR_HEIGHT = 8;
    private static final int UNICODE_CHAR_HEIGHT = 16;

    private String unicodeFontPath = "assets/minecraft/textures/font/unicode_page_%02x.png";
    private String defaultFontPath = "assets/minecraft/textures/font/ascii.png";
    private BufferedImage defaultFontImage;
    private int[] charWidth = new int[256];
    private byte[] glyphWidth = new byte[65536];

    private boolean unicodeFlag = false;

    public String text;
    public float fontSize = 2;
    public boolean shadow = true;
    
    public Label(String text) {
        this.setOpaque(false);
        this.text = text;

        this.unicodeFlag = ClientProxy.getUnicodeFlag();
        this.fontSize = this.unicodeFlag ? 1 : 2;

        try {
            URL url = getClass().getClassLoader().getResource(defaultFontPath);
            defaultFontImage = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!this.unicodeFlag) {
            readFontTexture();
        } else {
            readGlyphSizes();
        }
    }

    private void readFontTexture() {
        int i = defaultFontImage.getWidth();
        int j = defaultFontImage.getHeight();
        int[] aint = new int[i * j];
        defaultFontImage.getRGB(0, 0, i, j, aint, 0, i);
        int k = j / 16;
        int l = i / 16;
        int i1 = 1;
        float f = 8.0F / (float)l;

        for (int j1 = 0; j1 < 256; ++j1) {
            int k1 = j1 % 16;
            int l1 = j1 / 16;

            if (j1 == 32) {
                this.charWidth[j1] = 3 + i1;
            }

            int i2;

            for (i2 = l - 1; i2 >= 0; --i2) {
                int j2 = k1 * l + i2;
                boolean flag = true;

                for (int k2 = 0; k2 < k && flag; ++k2) {
                    int l2 = (l1 * l + k2) * i;

                    if ((aint[j2 + l2] >> 24 & 255) != 0) {
                        flag = false;
                    }
                }

                if (!flag) {
                    break;
                }
            }

            ++i2;
            this.charWidth[j1] = (int)(0.5D + (double)((float)i2 * f)) + i1;
        }
    }

    private void readGlyphSizes() {
        InputStream inputstream = null;

        try {
            inputstream = getClass().getClassLoader().getResourceAsStream("assets/minecraft/font/glyph_sizes.bin");
            inputstream.read(this.glyphWidth);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }

    public int getCharWidth(char character) {
        if (character == 167) {
            return (int) (-1 * this.fontSize);
        } else if (character == 32) {
            return (int) (4 * this.fontSize);
        }

        int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(character);

        if (character > 0 && i != -1 && !this.unicodeFlag) {
            return (int) (this.charWidth[i] * this.fontSize);
        } else if (this.glyphWidth[character] != 0) {
            int j = this.glyphWidth[character] >>> 4;
            int k = this.glyphWidth[character] & 15;

            if (k > 7) {
                k = 15;
                j = 0;
            }

            ++k;
            return (int) (((k - j) + 1) * this.fontSize);
        } else {
            return 0;
        }
    }

    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }

        int i = 0;
        boolean flag = false;

        for (int j = 0; j < text.length(); ++j) {
            char c0 = text.charAt(j);
            int k = this.getCharWidth(c0);

            if (k < 0 && j < text.length() - 1) {
                ++j;
                c0 = text.charAt(j);

                if (c0 != 108 && c0 != 76) {
                    if (c0 == 114 || c0 == 82) {
                        flag = false;
                    }
                } else {
                    flag = true;
                }

                k = 0;
            }

            i += k;

            if (flag && k > 0) {
                i += this.fontSize;
            }
        }

        return i;
    }

    public float paintChar(Graphics g, char c, int x, int y, Color color) {
        if (c == 32) {
            return 4 * this.fontSize;
        }
        int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c);
        return i != -1 && !this.unicodeFlag ? this.paintDefaultCharacter(g, c, x, y, color) : this.paintUnicodeCharacter(g, c, x, y, color);
    }

    public float paintDefaultCharacter(Graphics g, char c, int x, int y, Color color) {
        if (defaultFontImage == null) return 0f;

        int cx = c % 16 * 8;
        int cy = c / 16 * 8;
        int cwidth = this.charWidth[c];
        int cheight = Label.DEFAULT_CHAR_HEIGHT;

        BufferedImage subImage = defaultFontImage.getSubimage(cx, cy, cwidth, cheight);
        g.drawImage(this.tintImage(subImage, color), x, y, (int) (subImage.getWidth(this) * this.fontSize), (int) (subImage.getHeight(this) * this.fontSize), this);

        return cwidth * this.fontSize;
    }

    public float paintUnicodeCharacter(Graphics g, char c, int x, int y, Color color) {
        if (this.glyphWidth[c] == 0) return 0;

        try {
            int j = this.glyphWidth[c] >>> 4;
            int k = this.glyphWidth[c] & 15;

            int cx = c % 16 * 16 + j;
            int cy = (c & 255) / 16 * 16;

            int cwidth = k+1 - j;
            int cheight = Label.UNICODE_CHAR_HEIGHT;

            int unicodePage = c / 256;

            URL url = getClass().getClassLoader().getResource(String.format(unicodeFontPath, new Object[] {Integer.valueOf(unicodePage)}));
            BufferedImage fontImage = ImageIO.read(url);
            BufferedImage subImage = fontImage.getSubimage(cx, cy, cwidth, cheight);

            g.drawImage(this.tintImage(subImage, color), x, y, (int) (subImage.getWidth(this) * this.fontSize), (int) (subImage.getHeight(this) * this.fontSize), this);

            return (cwidth + 1) * this.fontSize;
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    
    public void paintStringWithShadow(Graphics g, String str, int x, int y) {
        this.paintString(g, str, x + (int) this.fontSize, y + (int) this.fontSize, ColorConverter.HexToColor("383838", 6));
        this.paintString(g, str, x, y, ColorConverter.HexToColor("E0E0E0", 6));
    }

    protected void paintString(Graphics g, String str, int x, int y, Color color) {
        for (char c : str.toCharArray()) {
            x += (int) this.paintChar(g, c, x, y, color);
        }
    }

    protected void paintCenteredStringWithShadow(Graphics g, String str, int x, int y) {
        this.paintCenteredString(g, str, x + (int) this.fontSize, y + (int) this.fontSize, ColorConverter.HexToColor("383838", 6));
        this.paintCenteredString(g, str, x, y, ColorConverter.HexToColor("E0E0E0", 6));
    }

    protected void paintCenteredString(Graphics g, String str, int x, int y, Color color) {
        y -= getFontHeight()/2;
        this.paintString(g, str, (int) (x - this.getStringWidth(str)/2), y, color);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.paintCenteredStringWithShadow(g, this.text, this.getWidth()/2, this.getHeight()/2);
    }

    public BufferedImage tintImage(BufferedImage image, Color color) {
        BufferedImage tintedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TRANSLUCENT);
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixelColor = new Color(image.getRGB(x, y), true);
                if (pixelColor.getAlpha() != 0) {
                    tintedImage.setRGB(x, y, color.getRGB());
                }
            }
        }

        return tintedImage;
    }

    public boolean getUnicodeFlag() {
        return this.unicodeFlag;
    }

    public int getFontHeight() {
        return unicodeFlag == false ? (int) (Label.DEFAULT_CHAR_HEIGHT*this.fontSize) : (int) (Label.UNICODE_CHAR_HEIGHT*this.fontSize);
    }
}
