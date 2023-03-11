package fr.dams4k.cpsdisplay.gui;

import java.io.IOException;

import fr.dams4k.cpsdisplay.config.ModConfig;
import fr.dams4k.cpsdisplay.gui.buttons.ModToggleButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class VersionConfig extends ModScreen {
    public enum GuiButtons {
		MAJOR_CHECK(0),
		MINOR_CHECK(1),
		PATCH_CHECK(2),
        DONE(4);

		public final int id;

		GuiButtons(int id) {
			this.id = id;
		}

		public int getY(int y) {
			return y + (this.id % 10) * 25;
		}
	}

    private GuiScreen parent;

    private ModToggleButton majorToggle;
    private ModToggleButton minorToggle;
    private ModToggleButton patchToggle;

    public VersionConfig(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        super.initGui();

        this.addButtons(width / 2 - 75, 10 + this.top);
    }

    public void addButtons(int x, int y) {
        majorToggle = new ModToggleButton(
            GuiButtons.MAJOR_CHECK.id, x, GuiButtons.MAJOR_CHECK.getY(y), 150, 20,
            "Major Check: ", "", ModConfig.majorUpdate
        );
        minorToggle = new ModToggleButton(
            GuiButtons.MINOR_CHECK.id, x, GuiButtons.MINOR_CHECK.getY(y), 150, 20,
            "Minor Check: ", "", ModConfig.minorUpdate
        );
        patchToggle = new ModToggleButton(
            GuiButtons.PATCH_CHECK.id, x, GuiButtons.PATCH_CHECK.getY(y), 150, 20,
            "Patch Check: ", "", ModConfig.patchUpdate
        );

        buttonList.add(majorToggle);
        buttonList.add(minorToggle);
        buttonList.add(patchToggle);

        buttonList.add(new GuiButton(GuiButtons.DONE.id, x, GuiButtons.DONE.getY(y), 150, 20, "Done"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        ModConfig.majorUpdate = majorToggle.getValue();
        ModConfig.minorUpdate = minorToggle.getValue();
        ModConfig.patchUpdate = patchToggle.getValue();
        ModConfig.syncConfig(false);

        if (button.id == GuiButtons.DONE.id) {
            mc.displayGuiScreen(this.parent);
        }
    }
}
