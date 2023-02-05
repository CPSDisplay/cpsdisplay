package fr.dams4k.cpsdisplay.v1_8.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.dams4k.cpsdisplay.v1_8.config.ModConfig;
import fr.dams4k.cpsdisplay.v1_8.enums.MouseModeEnum;
import fr.dams4k.cpsdisplay.v1_8.enums.ShowTextEnum;
import fr.dams4k.cpsdisplay.v1_8.gui.buttons.ModColorButton;
import fr.dams4k.cpsdisplay.v1_8.gui.buttons.ModSlider;
import fr.dams4k.cpsdisplay.v1_8.gui.buttons.ModTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;


public class GuiConfig extends GuiScreen {
	public enum GuiButtons {
		SHOW_TEXT(0),
		SCALE_TEXT(1),
		COLOR_TEXT(2),
		MODE_TEXT(3),
		TEXT(4),

		COLOR_BACKGROUND(10),
		PADDING_BACKGROUND(11);

		public final int id;

		GuiButtons(int id) {
			this.id = id;
		}

		public int getY(int y) {
			return y + (this.id % 10) * 25;
		}
	}

	private List<GuiTextField> textFieldList = new ArrayList<>();

	// Text
	private GuiButton showTextButton;
	private ModSlider scaleTextSlider;
	private GuiButton modeTextButton;
	private GuiTextField textField;
	private ModColorButton colorTextButton;

	// Background
	private ModColorButton colorBackgroundButton;
	private ModTextField paddingBackgroundField;

	// Rainbow
	// private GuiSlider rainbowSpeedSlider;
	// private GuiSlider rainbowPrecisionSlider;

	private MouseModeEnum mouseModeSelected;
	private ShowTextEnum showText;

	private int top = 20;
	
	private int diffX = 0;
	private int diffY = 0;

	@Override
	public void initGui() {
		MinecraftForge.EVENT_BUS.register(this);

		// rainbowSpeedSlider = new GuiSlider(13, width / 2 + 2, 60 + top, 150, 20, "", 0.1f, 3f, 0.1f, (float) ModConfig.rainbowSpeed, 10);
		// rainbowSpeedSlider.setFormatHelper(new FormatHelper() {

		// 	@Override
		// 	public String getText(int id, String name, float value) {
		// 		return "Speed: " + value + "x";
		// 	}
			
		// });
		// rainbowPrecisionSlider = new GuiSlider(14, width / 2 + 2, 85 + top, 150, 20, "Precision", 0.1f, 1f, 0.1f, (float) ModConfig.rainbowPrecision, 10);
		// rainbowPrecisionSlider.setFormatHelper(new FormatHelper() {

		// 	@Override
		// 	public String getText(int id, String name, float value) {
		// 		return "Precision: " + value + "";
		// 	}
			
		// });

		
		
		
		// buttonList.add(rainbowSpeedSlider);
		// buttonList.add(rainbowPrecisionSlider);
		this.addTextButtons(width / 2 - 152, 10 + top);
		this.addBackgroundButtons(width / 2 + 2, 10 + top);
		updateButtons();
	}

	public void addTextButtons(int x, int y) {
		mouseModeSelected = MouseModeEnum.getByText(ModConfig.text);
		showText = ShowTextEnum.getByBool(ModConfig.showText);

		showTextButton = new GuiButton(GuiButtons.SHOW_TEXT.id, x, GuiButtons.SHOW_TEXT.getY(y), 150, 20, "");
		updateShowTextButton();

		colorTextButton = new ModColorButton(
			GuiButtons.COLOR_TEXT.id, x, GuiButtons.COLOR_TEXT.getY(y), 150, 20,
			I18n.format("cpsdisplay.button.color_text", new Object[0]), false
		);
		colorTextButton.setColor(ModConfig.getTextColor());
		
		scaleTextSlider = new ModSlider(
			GuiButtons.SCALE_TEXT.id, x, GuiButtons.SCALE_TEXT.getY(y), 150, 20,
			I18n.format("cpsdisplay.button.scale_text", new Object[0]),
			0.1f * 100, 4 * 100, 0.01f, (float) (ModConfig.textScale * 100), 10
		);

		modeTextButton = new GuiButton(GuiButtons.MODE_TEXT.id, x, GuiButtons.MODE_TEXT.getY(y), 150, 20, "");
		updateMouseModeButton();

		textField = new GuiTextField(GuiButtons.TEXT.id, fontRendererObj, x, GuiButtons.TEXT.getY(y), 150, 20);
		textField.setMaxStringLength(999);
		textField.setText(ModConfig.text);
		
		buttonList.add(showTextButton);
		buttonList.add(scaleTextSlider);
		buttonList.add(modeTextButton);
		buttonList.add(colorTextButton);

		textFieldList.add(textField);
	}

	public void addBackgroundButtons(int x, int y) {
		colorBackgroundButton = new ModColorButton(
			GuiButtons.COLOR_BACKGROUND.id, x, GuiButtons.COLOR_BACKGROUND.getY(y), 150, 20,
			I18n.format("cpsdisplay.button.color_background", new Object[0]), true
		);
		colorBackgroundButton.setColor(ModConfig.getBackgroundColor());

		paddingBackgroundField = new ModTextField(GuiButtons.PADDING_BACKGROUND.id, fontRendererObj, x, GuiButtons.PADDING_BACKGROUND.getY(y), 150, 20);
		paddingBackgroundField.setMaxStringLength(2);
		paddingBackgroundField.letters = false;
		paddingBackgroundField.punctuation = false;
		paddingBackgroundField.anythings = false;

		buttonList.add(colorBackgroundButton);

		textFieldList.add(paddingBackgroundField);
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
		
		if (GuiOverlay.positionInOverlay(mouseX, mouseY)) {
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
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		for (GuiTextField field : this.textFieldList) {
			field.drawTextBox();
		}

		if (GuiOverlay.positionInOverlay(mouseX, mouseY)) {
			ArrayList<Integer> positions = GuiOverlay.getBackgroundPositions(0, 0, true);
			
			new GuiOverlay(Minecraft.getMinecraft(), 0, 0, ModConfig.getBackgroundColor());
			
			drawVerticalLine(positions.get(0), positions.get(1), positions.get(3), Color.RED.getRGB());
			drawVerticalLine(positions.get(2), positions.get(1), positions.get(3), Color.RED.getRGB());
			drawHorizontalLine(positions.get(0), positions.get(2), positions.get(1), Color.RED.getRGB());
			drawHorizontalLine(positions.get(0), positions.get(2), positions.get(3), Color.RED.getRGB());
		} else {
			new GuiOverlay(Minecraft.getMinecraft(), 0, 0);
		}

		saveConfig();
	}
	public void drawBackground() {
		ArrayList<Integer> positions = GuiOverlay.getBackgroundPositions(0, 0, true);

		int color = -1072689136;
		int padding = (int) (this.height / 10 * ModConfig.textScale);
		// TOP
		this.drawGradientRect(0, 0, this.width, positions.get(1)-padding, color, color);
		// BOTTOM
		this.drawGradientRect(0, positions.get(3)+padding, this.width, this.height, color, color);
		// LEFT
		this.drawGradientRect(0, positions.get(1)-padding, positions.get(0)-padding, positions.get(3)+padding, color, color);
		// RIGHT
		this.drawGradientRect(positions.get(2)+padding, positions.get(1)-padding, this.width, positions.get(3)+padding, color, color);
	}
	
	public void saveConfig() {
		changeConfig();
		ModConfig.syncConfig(false);
		updateButtons();
	}
	
	public void changeConfig() {
		if (this.textField != null) ModConfig.text = textField.getText();
		if (this.colorTextButton != null) ModConfig.setTextColor(colorTextButton.getColor());
		if (this.showText != null) ModConfig.showText = showText.getBool();
		if (this.scaleTextSlider != null) ModConfig.textScale = scaleTextSlider.getValue() / 100d;

		if (this.colorBackgroundButton != null) ModConfig.setBackgroundColor(this.colorBackgroundButton.getColor());

		// ModConfig.rainbowSpeed = rainbowSpeedSlider.getValue();
		// ModConfig.rainbowPrecision = rainbowPrecisionSlider.getValue();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == modeTextButton) {
			mouseModeSelected = MouseModeEnum.getById(mouseModeSelected.getId() + 1);
			updateMouseModeButton();

			if (mouseModeSelected != MouseModeEnum.CUSTOM) {
				ModConfig.text = I18n.format(mouseModeSelected.getText(), new Object[0]);
			}
			textField.setText(ModConfig.text);
		} else if (button == showTextButton) {
			showText = ShowTextEnum.getByBool(!showText.getBool());
			updateShowTextButton();
		}

		saveConfig();
	}
	
	public void updateShowTextButton() {
		showTextButton.displayString = I18n.format("cpsdisplay.button.show_text", new Object[0]) + showText.getText();
	}
	public void updateMouseModeButton() {
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
		
	}
}
