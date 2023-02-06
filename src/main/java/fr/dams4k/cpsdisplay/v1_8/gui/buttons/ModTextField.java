package fr.dams4k.cpsdisplay.v1_8.gui.buttons;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class ModTextField extends GuiTextField {
    private final FontRenderer fontRendererInstance;

    private int disabledColor = 7368816;

    public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DIGITS = "0123456789";
    public static final String PUNCTUATIONS = "!\"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~";

    public boolean letters = true;
    public boolean digits = true;
    public boolean punctuation = true;
    public boolean anythings = true;

    public String placeHolder = "";

    public ModTextField(int id, FontRenderer fontRenderer, int x, int y, int width, int height) {
        super(id, fontRenderer, x, y, width, height);
        this.fontRendererInstance = fontRenderer;
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

    @Override
    public void drawTextBox() {
        super.drawTextBox();

        if (this.getText().isEmpty()) {
            int color = this.disabledColor;
            int x = this.getEnableBackgroundDrawing() ? this.xPosition + 4 : this.xPosition;
            int y = this.getEnableBackgroundDrawing() ? this.yPosition + (this.height - 8) / 2 : this.yPosition;
            
            this.fontRendererInstance.drawStringWithShadow(this.placeHolder, x, y, color);
        }
    }
}
