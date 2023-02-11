package fr.dams4k.cpsdisplay.core.colorpicker.gui.textfield;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class LimitedDocument extends PlainDocument {
    private int limit;

    public LimitedDocument(int limit) {
        this.limit = limit;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) return;
        if (getLength() + str.length() > limit) return;
        
        super.insertString(offset, str, attr);
    }
}
