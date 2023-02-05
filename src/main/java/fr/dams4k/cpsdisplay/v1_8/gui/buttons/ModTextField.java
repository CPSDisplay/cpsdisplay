package fr.dams4k.cpsdisplay.v1_8.gui.buttons;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class ModTextField extends GuiTextField {
    public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DIGITS = "0123456789";
    public static final String PUNCTUATIONS = "!\"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~";

    public boolean letters = true;
    public boolean digits = true;
    public boolean punctuation = true;
    public boolean anythings = true;

    public ModTextField(int id, FontRenderer fontRenderer, int x, int y, int width, int height) {
        super(id, fontRenderer, x, y, width, height);
    }

    @Override
    public void writeText(String text) {
        if (!anythings) {
            if (LETTERS.contains(text) && !letters) return;
            if (DIGITS.contains(text) && !digits) return;
            if (PUNCTUATIONS.contains(text) && !punctuation) return;
        }

        super.writeText(text);
    }
}
