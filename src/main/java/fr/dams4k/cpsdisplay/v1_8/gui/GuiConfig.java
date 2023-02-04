package fr.dams4k.cpsdisplay.v1_8.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import fr.dams4k.cpsdisplay.v1_8.config.ModConfig;
import fr.dams4k.cpsdisplay.v1_8.enums.ColorsEnum;
import fr.dams4k.cpsdisplay.v1_8.enums.MouseModeEnum;
import fr.dams4k.cpsdisplay.v1_8.enums.ShowTextEnum;
import fr.dams4k.cpsdisplay.v1_8.gui.buttons.GuiColorButton;
import fr.dams4k.cpsdisplay.v1_8.gui.buttons.GuiSlider;
import fr.dams4k.cpsdisplay.v1_8.gui.buttons.GuiSlider.FormatHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.common.MinecraftForge;


public class GuiConfig extends GuiScreen {
	// Text
	private GuiButton showTextButton;
	private GuiSlider scaleSlider;
	private GuiButton baseColorChangerButton;
	private GuiButton mouseModeChangerButton;
	private GuiTextField textField;
	private GuiColorButton colorButton;
	

	// Rainbow
	private GuiSlider rainbowSpeedSlider;
	private GuiSlider rainbowPrecisionSlider;

	private MouseModeEnum mouseModeSelected;
	private ColorsEnum colorSelected;
	private ShowTextEnum showText;

	private int top = 20;
	
	private int diffX = 0;
	private int diffY = 0;

	@Override
	public void initGui() {
		MinecraftForge.EVENT_BUS.register(this);

		mouseModeSelected = MouseModeEnum.getByText(ModConfig.text);
		colorSelected = ColorsEnum.getByHex(ModConfig.textHexColor);
		showText = ShowTextEnum.getByBool(ModConfig.showText);
		
		if (ModConfig.rainbow) {
			colorSelected = ColorsEnum.getById(9);
		}

		showTextButton = new GuiButton(0, width / 2 - 152, 10 + top, 150, 20, "Show text: " + showText.getText());
		mouseModeChangerButton = new GuiButton(3, width / 2 - 152, 60 + top, 150, 20, "Display mode: " + mouseModeSelected.getName());
		textField = new GuiTextField(2, fontRendererObj,  width / 2 - 152, 85 + top, 150, 20);
		textField.setMaxStringLength(999);
		textField.setText(ModConfig.text);
		
		baseColorChangerButton = new GuiButton(10, width / 2 + 2, 10 + top, 150, 20, "Color: " + colorSelected.getName());
		colorButton = new GuiColorButton(11, width / 2 + 2, 35 + top, 150, 20);
		colorButton.setColor(ModConfig.getTextColor());

		rainbowSpeedSlider = new GuiSlider(13, width / 2 + 2, 60 + top, 150, 20, "", 0.1f, 3f, 0.1f, (float) ModConfig.rainbowSpeed, 10);
		rainbowSpeedSlider.setFormatHelper(new FormatHelper() {

			@Override
			public String getText(int id, String name, float value) {
				return "Speed: " + value + "x";
			}
			
		});
		rainbowPrecisionSlider = new GuiSlider(14, width / 2 + 2, 85 + top, 150, 20, "Precision", 0.1f, 1f, 0.1f, (float) ModConfig.rainbowPrecision, 10);
		rainbowPrecisionSlider.setFormatHelper(new FormatHelper() {

			@Override
			public String getText(int id, String name, float value) {
				return "Precision: " + value + "";
			}
			
		});

		scaleSlider = new GuiSlider(100, width / 2 - 152, 35 + top, 150, 20, "Scale", 0.1f * 100, 4 * 100, 0.01f, (float) (ModConfig.textScale * 100), 10);
		
		buttonList.add(showTextButton);
		buttonList.add(scaleSlider);
		buttonList.add(mouseModeChangerButton);
		
		buttonList.add(baseColorChangerButton);
		buttonList.add(rainbowSpeedSlider);
		buttonList.add(rainbowPrecisionSlider);
		buttonList.add(colorButton);

		updateButtons();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		textField.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		textField.mouseClicked(mouseX, mouseY, mouseButton);
		
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
		textField.updateCursorCounter();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground();
		textField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);

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
		ModConfig.text = textField.getText();
		ModConfig.textHexColor = Integer.toHexString(colorButton.getColor().getRGB()).substring(2);
		ModConfig.textScale = scaleSlider.getValue() / 100d;
		ModConfig.rainbowSpeed = rainbowSpeedSlider.getValue();
		ModConfig.rainbowPrecision = rainbowPrecisionSlider.getValue();
		ModConfig.showText = showText.getBool();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == baseColorChangerButton) {
			colorSelected = ColorsEnum.getById(colorSelected.getId() + 1);
			baseColorChangerButton.displayString = "Color: " + colorSelected.getName();
			
			if (colorSelected != ColorsEnum.CUSTOM && colorSelected != ColorsEnum.RAINBOW) {
				ModConfig.textHexColor = colorSelected.getHex();
			}
			
			if (colorSelected == ColorsEnum.RAINBOW) {
				ModConfig.rainbow = true;
			} else {
				ModConfig.rainbow = false;
			}
			
			
			colorButton.setColor(ModConfig.getTextColor());
		} else if (button == mouseModeChangerButton) {
			mouseModeSelected = MouseModeEnum.getById(mouseModeSelected.getId() + 1);
			mouseModeChangerButton.displayString = "Mode: " + mouseModeSelected.getName();

			if (mouseModeSelected != MouseModeEnum.CUSTOM) {
				ModConfig.text = mouseModeSelected.getText();
			}
			textField.setText(ModConfig.text);
		} else if (button == showTextButton) {
			showText = ShowTextEnum.getByBool(!showText.getBool());
			showTextButton.displayString = "Show text: " + showText.getText();
		}

		saveConfig();
	}
	
	public void updateButtons() {
		// Color
		if (colorSelected != ColorsEnum.RAINBOW) {
			rainbowPrecisionSlider.visible = false;
			rainbowSpeedSlider.visible = false;
			
			if (colorSelected == ColorsEnum.CUSTOM) {
				colorButton.visible = true;
			} else {
				colorButton.visible = false;
			}
		} else {
			colorButton.visible = true;
			rainbowPrecisionSlider.visible = true;
			rainbowSpeedSlider.visible = true;
		}
		
		// Display mode
		if (mouseModeSelected == MouseModeEnum.CUSTOM) {
			textField.setVisible(true);
		} else {
			textField.setVisible(false);
		}
	}
}
