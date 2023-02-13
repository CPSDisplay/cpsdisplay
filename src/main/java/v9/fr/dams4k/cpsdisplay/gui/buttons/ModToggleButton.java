package fr.dams4k.cpsdisplay.gui.buttons;

import fr.dams4k.cpsdisplay.enums.ToggleEnum;
import net.minecraft.client.gui.GuiButton;

public class ModToggleButton extends GuiButton {
    private String prefix;
    private String suffix;
    private ToggleEnum value;

    public ModToggleButton(int id, int x, int y, int width, int height, String prefix, String suffix, boolean value) {
        super(id, x, y, width, height, "");
        this.prefix = prefix;
        this.suffix = suffix;
        this.value = ToggleEnum.get(value);

        this.displayString = getDisplayString();
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        super.mouseReleased(mouseX, mouseY);
        this.value = this.value.toggle();
        this.displayString = getDisplayString();
    }

    public String getDisplayString() {
        return this.prefix + this.value.getText() + this.suffix;
    }

    public void setValue(boolean newValue) {
        this.value = ToggleEnum.get(newValue);
    }
    public boolean getValue() {
        return this.value.isEnabled();
    }
}
