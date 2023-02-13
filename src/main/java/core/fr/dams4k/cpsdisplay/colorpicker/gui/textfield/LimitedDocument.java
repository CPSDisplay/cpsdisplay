package fr.dams4k.cpsdisplay.colorpicker.gui.textfield;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class LimitedDocument extends PlainDocument {
    public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DIGITS = "0123456789";
    public static final String PUNCTUATIONS = "!\"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~";

    public boolean letters = false;
    public boolean digits = false;
    public boolean punctuation = false;
    public boolean anythings = true;

    private int limit;

    public LimitedDocument(int limit) {
        this.limit = limit;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) return;
        if (getLength() + str.length() > limit) return;

        for (char c : str.toCharArray()) {
            if (!anythings) {
                if (LETTERS.indexOf(c) > -1 && !letters) return;
                if (DIGITS.indexOf(c) > -1 && !digits) return;
                if (PUNCTUATIONS.indexOf(c) > -1 && !punctuation) return;
            }
        }

        super.insertString(offset, str, attr);
    }
}
