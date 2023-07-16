package fr.dams4k.cpsdisplay.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.dams4k.cpsdisplay.CPSDisplay;
import fr.dams4k.cpsdisplay.References;
import fr.dams4k.cpsdisplay.config.VersionManagerConfig;
import fr.dams4k.cpsdisplay.gui.buttons.ModToggleButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class VersionConfig extends ModScreen {
    public enum GuiButtons {
		MAJOR_CHECK(0),
		MINOR_CHECK(1),
		PATCH_CHECK(2),
        NEXT_UPDATE_LABEL(3),
        AUTO_UPDATE(4),
        DONE(6);

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
    private ModToggleButton autoUpdateToggle;

    public VersionConfig(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        super.initGui();

        int x = width / 2 - 75;
        int y = 10 + this.top;
        this.addToggleButtons(x, y);

        buttonList.add(new GuiButton(GuiButtons.DONE.id, x, GuiButtons.DONE.getY(y), 150, 20, I18n.format("gui.done", new Object[0])));
    }

    public void addToggleButtons(int x, int y) {
        majorToggle = new ModToggleButton(
            GuiButtons.MAJOR_CHECK.id, x, GuiButtons.MAJOR_CHECK.getY(y), 150, 20,
            I18n.format("cpsdisplay.version.checker.major_update", new Object[0]), "", VersionManagerConfig.majorUpdate
        );
        minorToggle = new ModToggleButton(
            GuiButtons.MINOR_CHECK.id, x, GuiButtons.MINOR_CHECK.getY(y), 150, 20,
            I18n.format("cpsdisplay.version.checker.minor_update", new Object[0]), "", VersionManagerConfig.minorUpdate
        );
        patchToggle = new ModToggleButton(
            GuiButtons.PATCH_CHECK.id, x, GuiButtons.PATCH_CHECK.getY(y), 150, 20,
            I18n.format("cpsdisplay.version.checker.patch_update", new Object[0]), "", VersionManagerConfig.patchUpdate
        );

        autoUpdateToggle = new ModToggleButton(
            GuiButtons.AUTO_UPDATE.id, x, GuiButtons.AUTO_UPDATE.getY(y) + 10, 150, 20,
            I18n.format("cpsdisplay.version.auto_update", new Object[0]), "", VersionManagerConfig.autoUpdate
        );

        this.updateButtons();

        buttonList.add(majorToggle);
        buttonList.add(minorToggle);
        buttonList.add(patchToggle);
        buttonList.add(autoUpdateToggle);
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
                I18n.format("cpsdisplay.version.checker.disabled", new Object[0]),
                x, y, width, 0xffffff
            );
        } else {
            fontRendererObj.drawSplitString(
                I18n.format("cpsdisplay.version.checker.enabled", new Object[0]).replaceAll("\\{next_version\\}", nextVersion),
                x, y, width, 0xffffff
            );
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void updateButtons() {
        VersionManagerConfig.patchUpdate = patchToggle.getValue();
        if (VersionManagerConfig.patchUpdate) {
            minorToggle.setValue(true);
            minorToggle.enabled = false;
        } else {
            minorToggle.enabled = true;
        }

        VersionManagerConfig.minorUpdate = minorToggle.getValue();
        if (VersionManagerConfig.minorUpdate) {
            majorToggle.setValue(true);
            majorToggle.enabled = false;
        } else {
            majorToggle.enabled = true;
        }

        VersionManagerConfig.majorUpdate = majorToggle.getValue();
        if (!VersionManagerConfig.majorUpdate) {
            autoUpdateToggle.setValue(false);
            autoUpdateToggle.enabled = false;
        } else {
            autoUpdateToggle.enabled = true;
        }

        VersionManagerConfig.autoUpdate = autoUpdateToggle.getValue();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.updateButtons();

        if (button.id == GuiButtons.DONE.id) {
            mc.displayGuiScreen(this.parent);
            VersionManagerConfig.saveConfig();
            CPSDisplay.versionManager.loadLatestVersion();
        }
    }

    public static String nextVersion() {
        String[] currentVersionSplited = References.MOD_VERSION.split("\\.");

        int major = Integer.parseInt(currentVersionSplited[0]);
        int minor = Integer.parseInt(currentVersionSplited[1]);
        int patch = Integer.parseInt(currentVersionSplited[2]);

        if (VersionManagerConfig.patchUpdate) {
            patch += 1;
        } else if (VersionManagerConfig.minorUpdate) {
            patch = 0;
            minor += 1;
        } else if (VersionManagerConfig.majorUpdate) {
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
