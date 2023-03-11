package fr.dams4k.cpsdisplay.renderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.lwjgl.opengl.GL11;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

// Original code from deerangle, https://forums.minecraftforge.net/topic/78188-1122-drawing-text-with-gradients/
// (i change a lot of things sooo, it's not really his code anymore, but the idea is the same)
public class ModFontRenderer extends FontRenderer {
    private static final String charmap = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";
    private static final String styleCharsmap = "0123456789abcdefklmnor";
    private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];

    private boolean bidiFlag;
    
    private float red;
    private float blue;
    private float green;
    private float alpha;

    private boolean randomStyle;
    private boolean boldStyle;
    private boolean italicStyle;
    private boolean underlineStyle;
    private boolean strikethroughStyle;

    private int[] colorCode = new int[32];

    public ModFontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
        super(gameSettingsIn, location, textureManagerIn, unicode);

        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i >> 0 & 1) * 170 + j;

            if (i == 6) {
                k += 85;
            }

            if (gameSettingsIn.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }

            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }

    public int drawString(String text, float x, float y, int color, boolean dropShadow) {
        GlStateManager.enableAlpha();
        this.resetStyles();
        int i;

        if (dropShadow) {
            i = this.renderString(text, x + 1.0F, y + 1.0F, color, true);
            resetStyles();
            i = Math.max(i, this.renderString(text, x, y, color, false));
        } else {
            i = this.renderString(text, x, y, color, false);
        }

        return i;
    }
    private int renderString(String text, float x, float y, int color, boolean dropShadow) {
        if (text == null) {
            return 0;
        } else {
            if (this.bidiFlag) {
                text = this.bidiReorder(text);
            }

            if ((color & -67108864) == 0) {
                color |= -16777216;
            }

            if (dropShadow) {
                color = (color & 16579836) >> 2 | color & -16777216;
            }

            this.red = (float)(color >> 16 & 255) / 255.0F;
            this.blue = (float)(color >> 8 & 255) / 255.0F;
            this.green = (float)(color & 255) / 255.0F;
            this.alpha = (float)(color >> 24 & 255) / 255.0F;
            GlStateManager.color(this.red, this.blue, this.green, this.alpha);
            this.posX = x;
            this.posY = y;
            this.renderStringAtPos(text, dropShadow);
            return (int)this.posX;
        }
    }

    private void renderStringAtPos(String p_78255_1_, boolean p_78255_2_) {
        for (int i = 0; i < p_78255_1_.length(); ++i) {
            char c0 = p_78255_1_.charAt(i);

            if (c0 == 167 && i + 1 < p_78255_1_.length()) {
                int i1 = "0123456789abcdefklmnor".indexOf(p_78255_1_.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                if (i1 < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;

                    if (i1 < 0 || i1 > 15) {
                        i1 = 15;
                    }

                    if (p_78255_2_) {
                        i1 += 16;
                    }

                    int j1 = this.colorCode[i1];
                    GlStateManager.color((float)(j1 >> 16) / 255.0F, (float)(j1 >> 8 & 255) / 255.0F, (float)(j1 & 255) / 255.0F, this.alpha);
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
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    GlStateManager.color(this.red, this.blue, this.green, this.alpha);
                }

                ++i;
            } else {
                int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c0);

                if (this.randomStyle && j != -1) {
                    int k = this.getCharWidth(c0);
                    char c1;

                    while (true) {
                        j = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length());
                        c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(j);

                        if (k == this.getCharWidth(c1)) {
                            break;
                        }
                    }

                    c0 = c1;
                }

                float f1 = this.getUnicodeFlag() ? 0.5F : 1.0F;
                boolean flag = (c0 == 0 || j == -1 || this.getUnicodeFlag()) && p_78255_2_;

                if (flag) {
                    this.posX -= f1;
                    this.posY -= f1;
                }

                float f = this.renderChar(c0, this.italicStyle);

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

                    this.renderChar(c0, this.italicStyle);
                    this.posX -= f1;

                    if (flag) {
                        this.posX += f1;
                        this.posY += f1;
                    }

                    ++f;
                }

                if (this.strikethroughStyle) {
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    GlStateManager.disableTexture2D();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
                    worldrenderer.pos((double)this.posX, (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0.0D).endVertex();
                    worldrenderer.pos((double)(this.posX + f), (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0.0D).endVertex();
                    worldrenderer.pos((double)(this.posX + f), (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
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
        }
    }

    private float renderChar(char p_181559_1_, boolean p_181559_2_) {
        if (p_181559_1_ == 32) {
            return 4.0F;
        } else {
            int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(p_181559_1_);
            return i != -1 && !this.getUnicodeFlag() ? this.renderDefaultChar(i, p_181559_2_) : this.renderUnicodeChar(p_181559_1_, p_181559_2_);
        }
    }

    private String bidiReorder(String p_147647_1_) {
        try {
            Bidi bidi = new Bidi((new ArabicShaping(8)).shape(p_147647_1_), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        } catch (ArabicShapingException var3) {
            return p_147647_1_;
        }
    }

    public int drawGradientString(String text, float x, float y, List<Color> colors, boolean dropShadow, boolean horizontal) {
        GlStateManager.enableAlpha();

        resetStyles();
        int i;

        if (dropShadow) {
            i = 0;
            i = this.renderGradientString(text, x + 1.0F, y + 1.0F, colors, true, horizontal);
            resetStyles();
            i = Math.max(i, this.renderGradientString(text, x, y, colors, false, horizontal));
        } else {
            i = this.renderGradientString(text, x, y, colors, false, horizontal);
        }

        return i;
    }

    private int renderGradientString(String text, float x, float y, List<Color> colors, boolean dropShadow, boolean horizontal) {
        if (text == null) {
            return 0;
        } else {
            List<Integer> iColors = new ArrayList<>();
            for (Color color : colors) {
                Integer rgb = color.getRGB();
                if ((rgb & -67108864) == 0) {
                    rgb |= -16777216;
                }
                iColors.add(rgb);
            }

            if (dropShadow) {
                for (int i = 0; i < iColors.size(); i++) {
                    Integer rgb = iColors.get(i);
                    iColors.set(i, (rgb & 16579836) >> 2 | rgb & -16777216);
                }
            }

            this.posX = x;
            this.posY = y;

            this.renderGradientStringAtPos(text, dropShadow, iColors, horizontal);

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

    public int getNonModifiedCharWidth(char character) {
        if (character == 167) {
            return -1;
        } else if (character == 32) {
            return 4;
        } else {
            int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(character);

            if (character > 0 && i != -1 && !this.getUnicodeFlag())
            {
                return this.charWidth[i];
            }
            else if (this.glyphWidth[character] != 0)
            {
                int j = this.glyphWidth[character] >>> 4;
                int k = this.glyphWidth[character] & 15;
                ++k;
                return k - j;
            }
            else
            {
                return 0;
            }
        }
    }

    private void renderGradientStringAtPos(String text, boolean shadow, List<Integer> colors, boolean horizontal) {
        double totalWidth = this.getStringWidth(text);
        double gradientWidth = totalWidth / (colors.size()-1);
        double currentCountWidth = 0;
        
        for (int i = 0; i < text.length(); i++) {
            char c0 = text.charAt(i);

            // check if char == § (167) and if there is another char after
            if (c0 == 167 && i+1 < text.length()) {
                int i1 = styleCharsmap.indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                if (i1 < 16) {
                    resetStyles();

                    if (i1 < 0 || i1 > 15) i1 = 15;
                    if (shadow) i1 += 16;
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
                }

                ++i;
            } else {
                int j = charmap.indexOf(c0);

                if (this.randomStyle && j != -1) {
                    int k = this.getCharWidth(c0);
                    char c1;

                    while (true) {
                        j = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length());
                        c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(j);

                        if (k == this.getCharWidth(c1)) break;
                    }

                    c0 = c1;
                }

                float f1 = this.getUnicodeFlag() ? 0.5f : 1f;
                boolean flag = (c0 == 0 || j == -1 || this.getUnicodeFlag()) && shadow;

                if (flag) {
                    this.posX -= f1;
                    this.posY -= f1;
                }

                float f;
                float nextCharWidth = this.getNonModifiedCharWidth(c0);

                // if firstMix > lastMix, we have 3 colors
                double firstMix = (currentCountWidth % gradientWidth) / gradientWidth;
                double lastMix = ((currentCountWidth + nextCharWidth) % gradientWidth) / gradientWidth;

                int startColorPos;
                startColorPos = (int) (currentCountWidth / gradientWidth);

                if (horizontal) {
                    int startGColor = colors.get(startColorPos);
                    int endGColor = colors.get(startColorPos+1);
                    
                    List<Integer> charColors = new ArrayList<>();
                    charColors.add(colorMix(startGColor, endGColor, firstMix));
                    if (firstMix > lastMix && startColorPos+2 < colors.size()) {
                        charColors.add(endGColor);

                        startGColor = endGColor;
                        endGColor = colors.get(startColorPos+2);
                    } else if (firstMix > lastMix) {
                        lastMix = 1d;
                    }
                    charColors.add(colorMix(startGColor, endGColor, lastMix));
                    List<Float> charPositions = Arrays.asList(0f, nextCharWidth);

                    if (charColors.size() == 2) {
                        f = this.renderGradientChar(c0, charColors, charPositions, true, this.italicStyle);
                    } else {
                        charPositions = Arrays.asList(0f, (float) (nextCharWidth - ((currentCountWidth + nextCharWidth) % gradientWidth)), nextCharWidth);

                        f = this.renderGradientChar(c0, charColors, charPositions, true, this.italicStyle);
                    }

                    for (int h = 0; h < charColors.size()-1; h++) {
                        int startColor = charColors.get(h);
                        float sa = ((startColor >> 24) & 0xff) / 255f;
                        float sr = ((startColor >> 16) & 0xff) / 255f;
                        float sg = ((startColor >> 8) & 0xff) / 255f;
                        float sb = (startColor & 0xff) / 255f;

                        int endColor = charColors.get(h+1);
                        float ea = ((endColor >> 24) & 0xff) / 255f;
                        float er = ((endColor >> 16) & 0xff) / 255f;
                        float eg = ((endColor >> 8) & 0xff) / 255f;
                        float eb = (endColor & 0xff) / 255f;
                    
                        double charXPos = charPositions.get(h);
                        double xPos = this.posX+charPositions.get(h);
                        double width = charPositions.get(h+1)-charXPos;

                        if (this.strikethroughStyle) {
                            Tessellator tessellator = Tessellator.getInstance();
                            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                            GlStateManager.disableTexture2D();
                            GlStateManager.enableBlend();
                            GlStateManager.disableAlpha();
                            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                            GlStateManager.shadeModel(7425);
                            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                            worldrenderer.pos((double)xPos, (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0d).color(sr, sg, sb, sa).endVertex();
                            worldrenderer.pos((double)(xPos + width), (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0d).color(er, eg, eb, ea).endVertex();
                            worldrenderer.pos((double)(xPos + width), (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1f), 0d).color(er, eg, eb, ea).endVertex();
                            worldrenderer.pos((double)xPos, (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1f), 0d).color(sr, sg, sb, sa).endVertex();
                            tessellator.draw();
                            GlStateManager.shadeModel(7424);
                            GlStateManager.disableBlend();
                            GlStateManager.enableAlpha();
                            GlStateManager.enableTexture2D();
                        }
                        if (this.underlineStyle) {
                            Tessellator tessellator = Tessellator.getInstance();
                            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                            GlStateManager.disableTexture2D();
                            GlStateManager.enableBlend();
                            GlStateManager.disableAlpha();
                            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                            GlStateManager.shadeModel(7425);
                            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                            worldrenderer.pos((double)xPos, (double)(this.posY + (float)this.FONT_HEIGHT), 0d).color(sr, sg, sb, sa).endVertex();
                            worldrenderer.pos((double)(xPos + width), (double)(this.posY + (float)this.FONT_HEIGHT), 0d).color(er, eg, eb, ea).endVertex();
                            worldrenderer.pos((double)(xPos + width), (double)(this.posY + (float)this.FONT_HEIGHT - 1f), 0d).color(er, eg, eb, ea).endVertex();
                            worldrenderer.pos((double)xPos, (double)(this.posY + (float)this.FONT_HEIGHT - 1f), 0d).color(sr, sg, sb, sa).endVertex();
                            tessellator.draw();
                            GlStateManager.shadeModel(7424);
                            GlStateManager.disableBlend();
                            GlStateManager.enableAlpha();
                            GlStateManager.enableTexture2D();
                        }
                    }

                    currentCountWidth += f;
                } else {
                    f = 0;
                }

                if (flag) {
                    this.posX += f1;
                    this.posY += f1;
                }
                
                this.posX += (float)((int)f);
            }
        }
    }

    private int colorMix(int startColor, int endColor, double mix) {
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

    private float renderGradientChar(char ch, List<Integer> colors, List<Float> positions, boolean horizontal, boolean italic) {
        if (ch == 32) { // space
            return 4.0F;
        } else {
            int i = charmap.indexOf(ch);
            return i != -1 && !this.getUnicodeFlag() ? this.renderGradientDefaultChar(i, colors, positions, horizontal, italic) : this.renderGradientUnicodeChar(ch, colors, positions, horizontal, italic);
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

    protected float renderGradientUnicodeChar(int ch, List<Integer> colors, List<Float> positions, boolean horizontal, boolean italic) {
        if (this.glyphWidth[ch] == 0) return 0f;

        this.loadGlyphTexture(ch/256);
        int j = this.glyphWidth[ch] >>> 4;
        int k = this.glyphWidth[ch] & 15;
        float f = (float)j;
        float f1 = (float)(k + 1);
        float currentCharXPos = (float)(ch % 16 * 16f) + j;
        float currentCharYPos = (float)((ch & 255) / 16 * 16f);
        float width = f1 - f;
        float f5 = italic ? 1.0F : 0.0F;

        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);
        for (int i = 0; i < colors.size()-1 && i < positions.size()-1; i++) {
            int startColor = colors.get(i);
            float sa = ((startColor >> 24) & 0xff) / 255f;
            float sr = ((startColor >> 16) & 0xff) / 255f;
            float sg = ((startColor >> 8) & 0xff) / 255f;
            float sb = (startColor & 0xff) / 255f;

            int endColor = colors.get(i+1);
            float ea = ((endColor >> 24) & 0xff) / 255f;
            float er = ((endColor >> 16) & 0xff) / 255f;
            float eg = ((endColor >> 8) & 0xff) / 255f;
            float eb = (endColor & 0xff) / 255f;

            float currentPartWidth = Math.min(positions.get(i+1) - positions.get(i), width - positions.get(i));
            if (currentPartWidth <= 0) continue;

            if (horizontal) {
                GlStateManager.color(sr, sg, sb, sa);

                GL11.glTexCoord2f(currentCharXPos / 256f, currentCharYPos / 256f); // 0 0
                GL11.glVertex3f(this.posX + positions.get(i) / 2f + f5, this.posY, 0f);

                GL11.glTexCoord2f(currentCharXPos / 256f, (currentCharYPos + 15.98f) / 256f); // 0 1
                GL11.glVertex3f(this.posX + positions.get(i) / 2f - f5, this.posY + 7.99f, 0f);

                GlStateManager.color(er, eg, eb, ea);

                GL11.glTexCoord2f((currentCharXPos + currentPartWidth) / 256f, (currentCharYPos + 15.98f) / 256f); // x 1
                GL11.glVertex3f(this.posX + (positions.get(i) + currentPartWidth) / 2f - f5, this.posY + 7.99f, 0f);

                GL11.glTexCoord2f((currentCharXPos + currentPartWidth) / 256f, currentCharYPos / 256f); // x 0
                GL11.glVertex3f(this.posX + (positions.get(i) + currentPartWidth) / 2f + f5, this.posY, 0f);

                currentCharXPos += currentPartWidth;
            }
        }

        GL11.glEnd();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        return (f1 - f) / 2.0f + 1.0f;
    }

    protected float renderGradientDefaultChar(int ch, List<Integer> colors, List<Float> positions, boolean horizontal, boolean italic) {
        float k = italic ? 1f : 0f;
        bindTexture(this.locationFontTexture);

        int charWidth = this.charWidth[ch];
        float width = (float) charWidth - 0.01F;

        float currentCharXPos = ch % 16 * 8f;
        float currentCharYPos = (ch / 16) * 8f;

        for (int i = 0; i < colors.size()-1 && i < positions.size()-1; i++) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            GL11.glBegin(GL11.GL_QUADS);

            int startColor = colors.get(i);
            float sa = ((startColor >> 24) & 0xff) / 255f;
            float sr = ((startColor >> 16) & 0xff) / 255f;
            float sg = ((startColor >> 8) & 0xff) / 255f;
            float sb = (startColor & 0xff) / 255f;

            int endColor = colors.get(i+1);
            float ea = ((endColor >> 24) & 0xff) / 255f;
            float er = ((endColor >> 16) & 0xff) / 255f;
            float eg = ((endColor >> 8) & 0xff) / 255f;
            float eb = (endColor & 0xff) / 255f;

            float currentPartWidth = Math.min(positions.get(i+1) - positions.get(i), width - positions.get(i));
            float f5 = i+1 == positions.size()-1 ? 1f : 0f;
            if (currentPartWidth <= 0) continue;

            if (horizontal) {
                GlStateManager.color(sr, sg, sb, sa);

                GL11.glTexCoord2f(currentCharXPos / 128f, currentCharYPos / 128f); // 0 0
                GL11.glVertex3f(this.posX + positions.get(i) + k, this.posY, 0f);

                GL11.glTexCoord2f(currentCharXPos / 128f, (currentCharYPos + 7.99f) / 128f); // 0 1
                GL11.glVertex3f(this.posX + positions.get(i) - k, this.posY + 7.99f, 0f);

                GlStateManager.color(er, eg, eb, ea);

                GL11.glTexCoord2f((currentCharXPos + currentPartWidth - f5) / 128f, (currentCharYPos + 7.99f) / 128f); // x 1
                GL11.glVertex3f(this.posX + positions.get(i) + currentPartWidth - f5 - k, this.posY + 7.99f, 0f);

                GL11.glTexCoord2f((currentCharXPos + currentPartWidth - f5) / 128f, currentCharYPos / 128f); // x 0
                GL11.glVertex3f(this.posX + positions.get(i) + currentPartWidth - f5 + k, this.posY, 0f);

                GL11.glEnd();
                GlStateManager.shadeModel(GL11.GL_FLAT);
                
                currentCharXPos += currentPartWidth - f5;
            }
        }
        
        return (float) charWidth;
    }
}
