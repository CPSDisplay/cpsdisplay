package fr.dams4k.cpsdisplay.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import fr.dams4k.cpsdisplay.config.ModConfig;
import fr.dams4k.cpsdisplay.enums.MouseModeEnum;
import fr.dams4k.cpsdisplay.gui.buttons.ModColorButton;
import fr.dams4k.cpsdisplay.gui.buttons.ModSlider;
import fr.dams4k.cpsdisplay.gui.buttons.ModSliderMainPoint;
import fr.dams4k.cpsdisplay.gui.buttons.ModTextField;
import fr.dams4k.cpsdisplay.gui.buttons.ModToggleButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;


public class GuiConfig extends ModScreen {
	public enum GuiButtons {
		B_SHOW_TEXT(0),
		B_SCALE_TEXT(1),
        B_SHADOW_TEXT(2),
		B_MODE_TEXT(3),
		F_TEXT(4),


		B_SHOW_RAINBOW(21),
		B_SPEED_RAINBOW(22), COLOR_TEXT(32, 22),
		B_COLOR_BACKGROUND(23),
		L_MARGIN_BACKGROUND(24),
		F_MARGIN_BACKGROUND(24);

		public final int id;
		public final int posId;

		GuiButtons(int id) {
			this.id = id;
			this.posId = id;
		}

		GuiButtons(int id, int posId) {
			this.id = id;
			this.posId = posId;
		}

		public int getY(int y) {
			return y + (this.posId % 10) * 25;
		}
	}

	// Text
	private ModToggleButton showTextToggle;
	private ModSlider scaleTextSlider;
	private GuiButton modeTextButton;
    private ModToggleButton showTextShadowToggle;
	private GuiTextField textField;
	private ModColorButton colorTextButton;

	private MouseModeEnum mouseModeSelected;

	// Background
	private ModColorButton colorBackgroundButton;
	private GuiLabel marginBackgroundLabel;
	private ModTextField marginBackgroundField;

	// Rainbow
	private ModToggleButton showRainbowToggle;
	private ModSlider speedRainbowSlider;

	private int diffX = 0;
	private int diffY = 0;

	@Override
	public void initGui() {
		super.initGui();
		MinecraftForge.EVENT_BUS.register(this);

		// Enable/Disable the mod
		showTextToggle = new ModToggleButton(
			GuiButtons.B_SHOW_TEXT.id, width / 2 - 152, GuiButtons.B_SHOW_TEXT.getY(10 + top), 304, 20,
			I18n.format("cpsdisplay.button.show_text", new Object[0]), "", ModConfig.showText
		);
		buttonList.add(showTextToggle);

		this.addVisibilityButtons(width / 2 - 152, 16 + top);
		this.addColorButtons(width / 2 + 4, 16 + top);

        List<Integer> backgroundPositions = CPSOverlay.getBackgroundPositions(0, 0, true);
        int x = backgroundPositions.get(2) > width-100 - 10 ? 0 : width-100;
        int Y = backgroundPositions.get(3) > height-20 - 10 ? 0 : height-20;
        this.buttonList.add(new GuiButton(-1, x, Y, 100, 20, I18n.format("cpsdisplay.version.checker.button", new Object[0])));

		updateButtons();
	}

	public void addVisibilityButtons(int x, int y) {
		mouseModeSelected = MouseModeEnum.getByText(ModConfig.text);
		
		scaleTextSlider = new ModSlider(
			GuiButtons.B_SCALE_TEXT.id, x, GuiButtons.B_SCALE_TEXT.getY(y), 148, 20,
			I18n.format("cpsdisplay.slider.scale_text", new Object[0]),
			0.1f * 100, 4 * 100, 0.01f, (float) (ModConfig.scaleText * 100), 10
		);

		for (int i = 50; i < 400; i+=50) {
			scaleTextSlider.addMainPoint(new ModSliderMainPoint(i, 4f));
		}

        showTextShadowToggle = new ModToggleButton(
            GuiButtons.B_SHADOW_TEXT.id, x, GuiButtons.B_SHADOW_TEXT.getY(y), 148, 20,
            I18n.format("cpsdisplay.button.show_shadow", new Object[0]), "", ModConfig.showTextShadow
        );

		modeTextButton = new GuiButton(GuiButtons.B_MODE_TEXT.id, x, GuiButtons.B_MODE_TEXT.getY(y), 148, 20, "");
		updateMouseModeButton();

		textField = new GuiTextField(GuiButtons.F_TEXT.id, fontRendererObj, x, GuiButtons.F_TEXT.getY(y), 148, 20);
		textField.setMaxStringLength(999);
		textField.setText(ModConfig.text);
		
		buttonList.add(scaleTextSlider);
        buttonList.add(showTextShadowToggle);
		buttonList.add(modeTextButton);

		textFieldList.add(textField);
	}

	public void addColorButtons(int x, int y) {
		colorTextButton = new ModColorButton(
			GuiButtons.COLOR_TEXT.id, x, GuiButtons.COLOR_TEXT.getY(y), 148, 20,
			I18n.format("cpsdisplay.button.color_text", new Object[0]), false
		);
		colorTextButton.setColor(ModConfig.getTextColor());

		// Background
		colorBackgroundButton = new ModColorButton(
			GuiButtons.B_COLOR_BACKGROUND.id, x, GuiButtons.B_COLOR_BACKGROUND.getY(y), 148, 20,
			I18n.format("cpsdisplay.button.color_background", new Object[0]), true
		);
		colorBackgroundButton.setColor(ModConfig.getBackgroundColor());

        String labelString = I18n.format("cpsdisplay.button.margin_background", new Object[0]);
        int stringWidth = fontRendererObj.getStringWidth(labelString);
		marginBackgroundLabel = new GuiLabel(fontRendererObj, GuiButtons.L_MARGIN_BACKGROUND.id, x+5, GuiButtons.L_MARGIN_BACKGROUND.getY(y), stringWidth, 20, 0xffffff);
        marginBackgroundLabel.addLine(labelString);

		marginBackgroundField = new ModTextField(GuiButtons.L_MARGIN_BACKGROUND.id, fontRendererObj, x+5 + stringWidth + 10, GuiButtons.L_MARGIN_BACKGROUND.getY(y), 148 - 5 - stringWidth - 10, 20);
		marginBackgroundField.setMaxStringLength(2);
		marginBackgroundField.setText(Integer.toString(ModConfig.marginBackground));
		marginBackgroundField.letters = false;
		marginBackgroundField.punctuation = false;
		marginBackgroundField.anythings = false;
		marginBackgroundField.placeHolder = "§o2";

		// Rainbow
		showRainbowToggle = new ModToggleButton(
            GuiButtons.B_SHOW_RAINBOW.id, x, GuiButtons.B_SHOW_RAINBOW.getY(y), 148, 20,
            I18n.format("cpsdisplay.button.show_rainbow", new Object[0]), "", ModConfig.showRainbow
        );

		speedRainbowSlider = new ModSlider(
			GuiButtons.B_SPEED_RAINBOW.id, x, GuiButtons.B_SPEED_RAINBOW.getY(y), 148, 20,
			I18n.format("cpsdisplay.slider.speed_rainbow", new Object[0]), 0.1f, 3f, 0.1f, (float) ModConfig.speedRainbow, 10
		);


		// Add everythings to render
		buttonList.add(colorTextButton);

		buttonList.add(colorBackgroundButton);
		labelList.add(marginBackgroundLabel);
		textFieldList.add(marginBackgroundField);

		buttonList.add(showRainbowToggle);
		buttonList.add(speedRainbowSlider);
	}

	public void addTextButtons(int x, int y) {
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (GuiTextField field : this.textFieldList) {
			field.textboxKeyTyped(typedChar, keyCode);
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (GuiTextField field : this.textFieldList) {
			field.mouseClicked(mouseX, mouseY, mouseButton);
		}
		
		int[] textPosition = ModConfig.getTextPosition();

		diffX = textPosition[0] - mouseX;
		diffY = textPosition[1] - mouseY;
		
		if (CPSOverlay.positionInOverlay(mouseX, mouseY)) {
			mc.displayGuiScreen(new MoveOverlayGui(diffX, diffY));
		} else {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void updateScreen() {
		for (GuiTextField field : this.textFieldList) {
			field.updateCursorCounter();
		}
		super.updateScreen();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground();

		for (GuiTextField field : this.textFieldList) {
			field.drawTextBox();
		}
		super.drawScreen(mouseX, mouseY, partialTicks);

		if (CPSOverlay.positionInOverlay(mouseX, mouseY)) {
			ArrayList<Integer> positions = CPSOverlay.getBackgroundPositions(0, 0, false);
            int x1 = positions.get(0);
			int y1 = positions.get(1);
            int x2 = positions.get(2);
			int y2 = positions.get(3);

            int color = 0x99ff0000;
			
            GL11.glPushMatrix();
			GL11.glScaled(ModConfig.scaleText, ModConfig.scaleText, 1d);
			
            drawVerticalLine(x1-1, y1-1, y2, color);
            drawVerticalLine(x2, y1-1, y2, color);
            drawHorizontalLine(x1-1, x2, y1-1, color);
            drawHorizontalLine(x1-1, x2, y2, color);

            GL11.glPopMatrix();
		}

		updateConfig();
	}

	public void drawBackground() {
		ArrayList<Integer> positions = CPSOverlay.getBackgroundPositions(0, 0, true);

		int color = -1072689136;
		int margin = (int) (this.height / 10 * ModConfig.scaleText);
		// TOP
		this.drawGradientRect(0, 0, this.width, positions.get(1)-margin, color, color);
		// BOTTOM
		this.drawGradientRect(0, positions.get(3)+margin, this.width, this.height, color, color);
		// LEFT
		this.drawGradientRect(0, positions.get(1)-margin, positions.get(0)-margin, positions.get(3)+margin, color, color);
		// RIGHT
		this.drawGradientRect(positions.get(2)+margin, positions.get(1)-margin, this.width, positions.get(3)+margin, color, color);
	}
	
	public void updateConfig() {
		if (this.textField != null) ModConfig.text = textField.getText();
		if (this.colorTextButton != null) ModConfig.setTextColor(colorTextButton.getColor());
		if (this.showTextToggle != null) ModConfig.showText = showTextToggle.getValue();
		if (this.scaleTextSlider != null) ModConfig.scaleText = scaleTextSlider.getValue() / 100d;
        if (this.showTextShadowToggle != null) ModConfig.showTextShadow = showTextShadowToggle.getValue();

		if (this.colorBackgroundButton != null) ModConfig.setBackgroundColor(this.colorBackgroundButton.getColor());
		
		if (this.marginBackgroundField != null) {
			String sMargin = marginBackgroundField.getText();
			int margin = sMargin.isEmpty() ? 0 : Integer.valueOf(sMargin);
			ModConfig.marginBackground = margin;
		}

		if (this.showRainbowToggle != null) ModConfig.showRainbow = showRainbowToggle.getValue();
		if (this.speedRainbowSlider != null) ModConfig.speedRainbow = speedRainbowSlider.getValue();

		updateButtons();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == GuiButtons.B_MODE_TEXT.id) {
			mouseModeSelected = MouseModeEnum.getById(mouseModeSelected.getId() + 1);
			updateMouseModeButton();

			if (mouseModeSelected != MouseModeEnum.CUSTOM) {
				ModConfig.text = I18n.format(mouseModeSelected.getText(), new Object[0]);
			}
			textField.setText(ModConfig.text);
		}
        
		updateConfig();
		ModConfig.syncConfig(false);
        
        if (button.id == -1) {
            mc.displayGuiScreen(new VersionConfig(this));
        }
	}
	
	public void updateMouseModeButton() {
		if (modeTextButton == null) return;
		modeTextButton.displayString = I18n.format("cpsdisplay.button.display_mode", new Object[0]) + mouseModeSelected.getName();
	}

	public void updateButtons() {
		// Display mode
		if (textField != null) {
			if (mouseModeSelected == MouseModeEnum.CUSTOM) {
				textField.setVisible(true);
			} else {
				textField.setVisible(false);
			}
		}
		
		// Show speedRainbowSlider only if rainbow is enabled
		// Show colorTextButton only if rainbow is disabled
		if (showRainbowToggle.getValue()) {
			speedRainbowSlider.visible = true;
			colorTextButton.visible = false;
		} else {
			speedRainbowSlider.visible = false;
			colorTextButton.visible = true;
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		for (GuiButton button : this.buttonList) {
			if (button instanceof ModColorButton) {
				ModColorButton colorButton = (ModColorButton) button;
				colorButton.killColorPicker();
			}
		}
		ModConfig.syncConfig(false);
	}
}
