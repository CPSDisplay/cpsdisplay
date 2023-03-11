package fr.dams4k.cpsdisplay.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.dams4k.cpsdisplay.References;
import fr.dams4k.cpsdisplay.config.ModConfig;
import fr.dams4k.cpsdisplay.gui.buttons.ModToggleButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class VersionConfig extends ModScreen {
    public enum GuiButtons {
		MAJOR_CHECK(0),
		MINOR_CHECK(1),
		PATCH_CHECK(2),
        NEXT_UPDATE_LABEL(3),
        DONE(5);

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

        int x = width / 2 - 75;
        int y = 10 + this.top;
        this.addToggleButtons(x, y);

        buttonList.add(new GuiButton(GuiButtons.DONE.id, x, GuiButtons.DONE.getY(y), 150, 20, "Done"));
    }

    public void addToggleButtons(int x, int y) {
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

        this.updateButtons();

        buttonList.add(majorToggle);
        buttonList.add(minorToggle);
        buttonList.add(patchToggle);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        String nextVersion = VersionConfig.nextVersion();

        int x = width / 2 - 75;
        int y = GuiButtons.NEXT_UPDATE_LABEL.getY(10 + this.top);
        int width = 150;
        if (nextVersion.equals(References.MOD_VERSION)) {
            fontRendererObj.drawSplitString(
                "§eYou will not be informed of new releases",
                x, y, width, 0xffffff
            );
        } else {
            fontRendererObj.drawSplitString(
                "§eThe next version that will be communicated to you will be v{next_version} (or higher)".replaceAll("\\{next_version\\}", nextVersion),
                x, y, width, 0xffffff
            );
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    public void updateButtons() {
        ModConfig.patchUpdate = patchToggle.getValue();
        if (ModConfig.patchUpdate) {
            minorToggle.setValue(true);
            minorToggle.enabled = false;
        } else {
            minorToggle.enabled = true;
        }

        ModConfig.minorUpdate = minorToggle.getValue();
        if (ModConfig.minorUpdate) {
            majorToggle.setValue(true);
            majorToggle.enabled = false;
        } else {
            majorToggle.enabled = true;
        }

        ModConfig.majorUpdate = majorToggle.getValue();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.updateButtons();
        ModConfig.syncConfig(false);

        if (button.id == GuiButtons.DONE.id) {
            mc.displayGuiScreen(this.parent);
        }
    }

    public static String nextVersion() {
        String[] currentVersionSplited = References.MOD_VERSION.split("\\.");

        int major = Integer.parseInt(currentVersionSplited[0]);
        int minor = Integer.parseInt(currentVersionSplited[1]);
        int patch = Integer.parseInt(currentVersionSplited[2]);

        if (ModConfig.patchUpdate) {
            patch += 1;
        } else if (ModConfig.minorUpdate) {
            patch = 0;
            minor += 1;
        } else if (ModConfig.majorUpdate) {
            patch = 0;
            minor = 0;
            major += 1;
        }

        // Quality/20
        List<String> nextVersion = new ArrayList<>();
        nextVersion.add(Integer.toString(major));
        nextVersion.add(Integer.toString(minor));
        nextVersion.add(Integer.toString(patch));

        return String.join(".", nextVersion);
    }
}
