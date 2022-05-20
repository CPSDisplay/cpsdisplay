package fr.dams4k.cpsmod.v1_8.renderer;

import java.util.Locale;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

// Original code from deerangle, https://forums.minecraftforge.net/topic/78188-1122-drawing-text-with-gradients/
public class ModFontRenderer extends FontRenderer {
    private static final String charmap = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";
    private static final String styleCharsmap = "0123456789abcdefklmnor";
    private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];


    /** Used to specify new red value for the current color. */
    private float red;
    /** Used to specify new blue value for the current color. */
    private float blue;
    /** Used to specify new green value for the current color. */
    private float green;
    /** Used to speify new alpha value for the current color. */
    private float alpha;
    /** Text color of the currently rendering string. */
    private int textColor;
    /** Set if the "k" style (random) is active in currently rendering string */
    private boolean randomStyle;
    /** Set if the "l" style (bold) is active in currently rendering string */
    private boolean boldStyle;
    /** Set if the "o" style (italic) is active in currently rendering string */
    private boolean italicStyle;
    /** Set if the "n" style (underlined) is active in currently rendering string */
    private boolean underlineStyle;
    /** Set if the "m" style (strikethrough) is active in currently rendering string */
    private boolean strikethroughStyle;

    private int[] colorCode = new int[32];

    public ModFontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
        super(gameSettingsIn, location, textureManagerIn, unicode);

        for (int i = 0; i < 32; ++i)
        {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i >> 0 & 1) * 170 + j;

            if (i == 6)
            {
                k += 85;
            }

            if (gameSettingsIn.anaglyph)
            {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }

            if (i >= 16)
            {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }



    public int drawGradientString(String text, float x, float y, int topColor, int bottomColor, boolean dropShadow, boolean horizontal) {
        enableAlpha();
        resetStyles();
        int i;

        if (dropShadow) {
            i = this.renderGradientString(text, x + 1.0F, y + 1.0F, topColor, bottomColor, true, horizontal);
            resetStyles();
            i = Math.max(i, this.renderGradientString(text, x, y, topColor, bottomColor, false, horizontal));
        } else {
            i = this.renderGradientString(text, x, y, topColor, bottomColor, false, horizontal);
        }

        return i;
    }

    private int renderGradientString(String text, float x, float y, int startColor, int endColor, boolean dropShadow, boolean horizontal) {
        if (text == null) {
            return 0;
        } else {

            if ((startColor & -67108864) == 0) {
                startColor |= -16777216;
            }

            if ((endColor & -67108864) == 0) {
                endColor |= -16777216;
            }

            if (dropShadow) {
                startColor = (startColor & 16579836) >> 2 | startColor & -16777216;
                endColor = (endColor & 16579836) >> 2 | endColor & -16777216;
            }
            
            this.red = (float)(startColor >> 16 & 255) / 255.0F;
            this.blue = (float)(startColor >> 8 & 255) / 255.0F;
            this.green = (float)(startColor & 255) / 255.0F;
            this.alpha = (float)(startColor >> 24 & 255) / 255.0F;
            setColor(this.red, this.blue, this.green, this.alpha);

            this.posX = x;
            this.posY = y;
            this.renderGradientStringAtPos(text, dropShadow, startColor, endColor, horizontal);
            return (int) this.posX;
        }
    }
    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    private void renderGradientStringAtPos(String text, boolean shadow, int startColor, int endColor, boolean horizontal) {
        float totalWidth = this.getStringWidth(text);
        float currentCountWidth = 0;
        
        for (int i = 0; i < text.length(); i++) {
            char c0 = text.charAt(i);

            // check if char == § (167) and if there is another char after
            if (c0 == 167 && i+1 < text.length()) {
                int i1 = styleCharsmap.indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                if (i1 < 16) {
                    resetStyles();

                    if (i1 < 0 || i1 > 15) i1 = 15;
                    if (shadow) i1 += 16;

                    int j1 = this.colorCode[i1];
                    this.textColor = j1;
                    setColor((float)(j1 >> 16) / 255.0F, (float)(j1 >> 8 & 255) / 255.0F, (float)(j1 & 255) / 255.0F, this.alpha);
                } else if (i1 == 16) {
                    this.randomStyle = true;
                } else if (i1 == 17) {
                    this.boldStyle = true;
                } else if (i1 == 18) {
                    this.strikethroughStyle = true;
                } else if (i1 == 19) {
                    this.underlineStyle = true;
                } else if (i1 == 20) {
                    this.italicStyle = true;
                } else if (i1 == 21) {
                    resetStyles();
                    setColor(this.red, this.blue, this.green, this.alpha);
                }

                ++i;
            } else {
                int j = charmap.indexOf(c0);

                if (this.randomStyle && j != -1) {
                    int k = this.getCharWidth(c0);
                    char c1;

                    while (true)
                    {
                        j = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length());
                        c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(j);

                        if (k == this.getCharWidth(c1)) break;
                    }

                    c0 = c1;
                }

                float f1 = j == -1 || this.getUnicodeFlag() ? 0.5f : 1f;
                boolean flag = (c0 == 0 || j == -1 || this.getUnicodeFlag()) && shadow;

                if (flag) {
                    this.posX -= f1;
                    this.posY -= f1;
                }

                float f;
                float nextCharWidth = this.getCharWidth(c0);
                float firstMix = currentCountWidth / totalWidth;
                float lastMix = (currentCountWidth + nextCharWidth) / totalWidth;
                
                if (horizontal) {
                    int firstColor = colorMix(startColor, endColor, firstMix);
                    int lastColor = colorMix(startColor, endColor, lastMix);
                    f = this.renderGradientChar(c0, firstColor, lastColor, true, this.italicStyle);
                    currentCountWidth += f;
                } else {
                    f = this.renderGradientChar(c0, startColor, endColor, false, this.italicStyle);
                }

                if (flag) {
                    this.posX += f1;
                    this.posY += f1;
                }

                if (this.boldStyle) {
                    this.posX += f1;

                    if (flag) {
                        this.posX -= f1;
                        this.posY -= f1;
                    }

                    if (horizontal) {
                        int firstColor = colorMix(startColor, endColor, firstMix);
                        int lastColor = colorMix(startColor, endColor, lastMix);
                        this.renderGradientChar(c0, firstColor, lastColor, horizontal, this.italicStyle);
                    } else {
                        this.renderGradientChar(c0, startColor, endColor, horizontal, this.italicStyle);
                    }
                    
                    this.posX -= f1;

                    if (flag) {
                        this.posX += f1;
                        this.posY += f1;
                    }

                    ++f;
                }
                
                if (horizontal) {
                    int firstColor = colorMix(startColor, endColor, firstMix);
                    int lastColor = colorMix(startColor, endColor, lastMix);
                    doDraw(f, firstColor, lastColor, horizontal);
                } else {
                    doDraw(f, startColor, endColor, horizontal);
                }
            }
        }
    }

    protected void doDraw(float f, int startColor, int endColor, boolean horizontal) {
        float startAlpha = ((startColor >> 24) & 0xFF) / 255f;
        float startRed = ((startColor >> 16) & 0xFF) / 255f;
        float startGreen = ((startColor >> 8) & 0xFF) / 255f;
        float startBlue = (startColor & 0xFF) / 255f;

        float endAlpha = ((endColor >> 24) & 0xFF) / 255f;
        float endRed = ((endColor >> 16) & 0xFF) / 255f;
        float endGreen = ((endColor >> 8) & 0xFF) / 255f;
        float endBlue = (endColor & 0xFF) / 255f;

        if (this.strikethroughStyle) {
            // GlStateManager.shadeModel(GL11.GL_SMOOTH);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION);
            worldrenderer.pos((double)this.posX, (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0.0D).endVertex();
            // if (horizontal) {
            //     GlStateManager.color(startRed, startGreen, startBlue, startAlpha);
            // } else {
            //     GlStateManager.color(endRed, endGreen, endBlue, endAlpha);
            // }
            worldrenderer.pos((double)(this.posX + f), (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0.0D).endVertex();
            worldrenderer.pos((double)(this.posX + f), (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();

            // if (horizontal) {
            //     GlStateManager.color(endRed, endGreen, endBlue, endAlpha);
            // } else {
            //     GlStateManager.color(startRed, startGreen, startBlue, startAlpha);
            // }
            worldrenderer.pos((double)this.posX, (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }

        if (this.underlineStyle) {
            Tessellator tessellator1 = Tessellator.getInstance();
            WorldRenderer worldrenderer1 = tessellator1.getWorldRenderer();
            GlStateManager.disableTexture2D();
            worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
            int l = this.underlineStyle ? -1 : 0;
            worldrenderer1.pos((double)(this.posX + (float)l), (double)(this.posY + (float)this.FONT_HEIGHT), 0.0D).endVertex();
            worldrenderer1.pos((double)(this.posX + f), (double)(this.posY + (float)this.FONT_HEIGHT), 0.0D).endVertex();
            worldrenderer1.pos((double)(this.posX + f), (double)(this.posY + (float)this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
            worldrenderer1.pos((double)(this.posX + (float)l), (double)(this.posY + (float)this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
            tessellator1.draw();
            GlStateManager.enableTexture2D();
        }

        this.posX += (float)((int)f);
    }

    private int colorMix(int startColor, int endColor, float mix) {
        float startAlpha = ((startColor >> 24) & 0xFF) / 255f;
        float startRed = ((startColor >> 16) & 0xFF) / 255f;
        float startGreen = ((startColor >> 8) & 0xFF) / 255f;
        float startBlue = (startColor & 0xFF) / 255f;

        float endAlpha = ((endColor >> 24) & 0xFF) / 255f;
        float endRed = ((endColor >> 16) & 0xFF) / 255f;
        float endGreen = ((endColor >> 8) & 0xFF) / 255f;
        float endBlue = (endColor & 0xFF) / 255f;

        int mixAlpha = (int) (((1 - mix) * startAlpha + mix * endAlpha) * 255);
        int mixRed = (int) (((1 - mix) * startRed + mix * endRed) * 255);
        int mixGreen = (int) (((1 - mix) * startGreen + mix * endGreen) * 255);
        int mixBlue = (int) (((1 - mix) * startBlue + mix * endBlue) * 255);

        return (mixAlpha << 24) | (mixRed << 16) | (mixGreen << 8) | mixBlue;
    }

    private float renderGradientChar(char ch, int startColor, int endColor, boolean horizontal, boolean italic) {
        if (ch == 32) return 4.0F; // space
        if (ch == ' ') {
            return 4.0F;
        } else {
            int i = charmap.indexOf(ch);
            
            return i != -1 && !this.getUnicodeFlag() ? this.renderGradientDefaultChar(i, startColor, endColor, horizontal, italic) : this.renderGradientUnicodeChar(ch, startColor, endColor, horizontal, italic);
        }
    }

    private ResourceLocation getUnicodePageLocation(int page) {
        if (unicodePageLocations[page] == null) {
            unicodePageLocations[page] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", new Object[] {Integer.valueOf(page)}));
        }

        return unicodePageLocations[page];
    }

    private void loadGlyphTexture(int page) {
        bindTexture(this.getUnicodePageLocation(page));
    }

    protected float renderGradientUnicodeChar(char ch, int startColor, int endColor, boolean horizontal, boolean italic) {
        if (this.glyphWidth[ch] == 0) return 0f;

        float startAlpha = ((startColor >> 24) & 0xFF) / 255f;
        float startRed = ((startColor >> 16) & 0xFF) / 255f;
        float startGreen = ((startColor >> 8) & 0xFF) / 255f;
        float startBlue = (startColor & 0xFF) / 255f;

        float endAlpha = ((endColor >> 24) & 0xFF) / 255f;
        float endRed = ((endColor >> 16) & 0xFF) / 255f;
        float endGreen = ((endColor >> 8) & 0xFF) / 255f;
        float endBlue = (endColor & 0xFF) / 255f;

        int i = ch /256;
        this.loadGlyphTexture(i);
        int j = this.glyphWidth[ch] >>> 4;
        int k = this.glyphWidth[ch] & 15;
        float f = (float)j;
        float f1 = (float)(k + 1);
        float charXPos = (float)(ch % 16 * 16) + f;
        float charYPos = (float)((ch & 255) / 16 * 16);
        float width = f1 - f - 0.02F;
        float f5 = italic ? 1.0F : 0.0F;

        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);

        GlStateManager.color(startRed, startGreen, startBlue, startAlpha);
        GL11.glTexCoord2f(charXPos / 256.0F, charYPos / 256.0F);
        GL11.glVertex3f(this.posX + f5, this.posY, 0.0F);

        if (horizontal) {
            GlStateManager.color(startRed, startGreen, startBlue, startAlpha);
        } else {
            GlStateManager.color(endRed, endGreen, endBlue, endAlpha);
        }
        GL11.glTexCoord2f(charXPos / 256.0F, (charYPos + 15.98F) / 256.0F);
        GL11.glVertex3f(this.posX - f5, this.posY + 7.99F, 0.0F);

        GlStateManager.color(endRed, endGreen, endBlue, endAlpha);
        GL11.glTexCoord2f((charXPos + width) / 256.0F, (charYPos + 15.98F) / 256.0F);
        GL11.glVertex3f(this.posX + width / 2.0F - f5, this.posY + 7.99F, 0.0F);
        
        if (horizontal) {
            GlStateManager.color(endRed, endGreen, endBlue, endAlpha);
        } else {
            GlStateManager.color(startRed, startGreen, startBlue, startAlpha);
        }
        GL11.glTexCoord2f((charXPos + width) / 256.0F, charYPos / 256.0F);
        GL11.glVertex3f(this.posX + width / 2.0F + f5, this.posY, 0.0F);


        GL11.glEnd();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        return (f1 - f) / 2.0F + 1.0F;
    }

    protected float renderGradientDefaultChar(int ch, int startColor, int endColor, boolean horizontal, boolean italic) {
        float startAlpha = ((startColor >> 24) & 0xFF) / 255f;
        float startRed = ((startColor >> 16) & 0xFF) / 255f;
        float startGreen = ((startColor >> 8) & 0xFF) / 255f;
        float startBlue = (startColor & 0xFF) / 255f;

        float endAlpha = ((endColor >> 24) & 0xFF) / 255f;
        float endRed = ((endColor >> 16) & 0xFF) / 255f;
        float endGreen = ((endColor >> 8) & 0xFF) / 255f;
        float endBlue = (endColor & 0xFF) / 255f;

        float k = italic ? 1f : 0f;
        float charXPos = ch % 16 * 8f;
        float charYPos = (ch / 16) * 8f;
        bindTexture(this.locationFontTexture);
        int charWidth = this.charWidth[ch];
        float width = (float) charWidth - 0.01F;

        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);

        GlStateManager.color(startRed, startGreen, startBlue, startAlpha);
        GL11.glTexCoord2f(charXPos / 128.0F, charYPos / 128.0F); // 0 0
        GL11.glVertex3f(this.posX + k, this.posY, 0.0F);

        if (horizontal) {
            GlStateManager.color(startRed, startGreen, startBlue, startAlpha);
        } else {
            GlStateManager.color(endRed, endGreen, endBlue, endAlpha);
        }
        
        GL11.glTexCoord2f(charXPos / 128.0F, (charYPos + 7.99F) / 128.0F); // 0 1
        GL11.glVertex3f(this.posX - k, this.posY + 7.99F, 0.0F);

        GlStateManager.color(endRed, endGreen, endBlue, endAlpha);
        GL11.glTexCoord2f((charXPos + width - 1.0F) / 128.0F, (charYPos + 7.99F) / 128.0F); // 1 1
        GL11.glVertex3f(this.posX + width - 1.0F - k, this.posY + 7.99F, 0.0F);

        if (horizontal) {
            GlStateManager.color(endRed, endGreen, endBlue, endAlpha);
        } else {
            GlStateManager.color(startRed, startGreen, startBlue, startAlpha);
        }
        GL11.glTexCoord2f((charXPos + width - 1.0F) / 128.0F, charYPos / 128.0F); // 1 0
        GL11.glVertex3f(this.posX + width - 1.0F + k, this.posY, 0.0F);

        GL11.glEnd();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        return (float) charWidth;
    }
}
