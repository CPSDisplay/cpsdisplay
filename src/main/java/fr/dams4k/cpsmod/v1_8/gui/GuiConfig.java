package fr.dams4k.cpsmod.v1_8.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import fr.dams4k.cpsmod.v1_8.config.ModConfig;
import fr.dams4k.cpsmod.v1_8.enums.ColorsEnum;
import fr.dams4k.cpsmod.v1_8.enums.MouseModeEnum;
import fr.dams4k.cpsmod.v1_8.enums.ShowTextEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiConfig extends GuiScreen {
	// Text
	private GuiButtonExt showTextButton;
	private GuiSlider scaleSlider;
	private GuiButtonExt baseColorChangerButton;
	private GuiButtonExt mouseModeChangerButton;
	private GuiTextField textField;
	private GuiTextField colorField;
	
	// Rainbow
	private GuiSlider rainbowSpeedSlider;
	private GuiSlider rainbowPrecisionSlider;

	private MouseModeEnum mouseModeSelected;
	private ColorsEnum colorSelected;
	private ShowTextEnum showText;
	
	private int top = 20;
	
	private int diff_x = 0;
	private int diff_y = 0;

	@Override
	public void initGui() {
		MinecraftForge.EVENT_BUS.register(this);

		mouseModeSelected = MouseModeEnum.getByText(ModConfig.text);
		colorSelected = ColorsEnum.getByHex(ModConfig.text_color);
		showText = ShowTextEnum.getByBool(ModConfig.show_text);
		
		if (ModConfig.rainbow) {
			colorSelected = ColorsEnum.getById(9);
		}
		
		showTextButton = new GuiButtonExt(0, width / 2 - 152, 10 + top, 150, 20, "Show text: " + showText.getText());
		scaleSlider = new GuiSlider(1, width / 2 - 152, 35 + top, 150, 20, "Scale : ", "%", 0.5 * 100, 4 * 100, ModConfig.text_scale * 100, false, true);
		mouseModeChangerButton = new GuiButtonExt(3, width / 2 - 152, 60 + top, 150, 20, "Display mode: " + mouseModeSelected.getName());
		textField = new GuiTextField(2, fontRendererObj,  width / 2 - 152, 85 + top, 150, 20);
		textField.setMaxStringLength(999);
		textField.setText(ModConfig.text);
		
		baseColorChangerButton = new GuiButtonExt(10, width / 2 + 2, 10 + top, 150, 20, "Color: " + colorSelected.getName());
		colorField = new GuiTextField(11, fontRendererObj, width / 2 + 2, 35 + top, 150, 20);
		colorField.setMaxStringLength(6);
		colorField.setText(ModConfig.text_color);
		
		rainbowSpeedSlider = new GuiSlider(13, width / 2 + 2, 60 + top, 150, 20, "Speed: ", "x", 0.1, 3f, ModConfig.rainbow_speed, true, true);
		rainbowPrecisionSlider = new GuiSlider(14, width / 2 + 2, 85 + top, 150, 20, "Precision: ", "", 0.01, 1, ModConfig.rainbow_precision, true, true);

		buttonList.add(showTextButton);
		buttonList.add(scaleSlider);
		buttonList.add(mouseModeChangerButton);
		
		buttonList.add(baseColorChangerButton);
		buttonList.add(rainbowSpeedSlider);
		buttonList.add(rainbowPrecisionSlider);

		updateButtons();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		colorField.textboxKeyTyped(typedChar, keyCode);
		textField.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		colorField.mouseClicked(mouseX, mouseY, mouseButton);
		textField.mouseClicked(mouseX, mouseY, mouseButton);
		
		int[] text_position = ModConfig.getText_position();

		diff_x = text_position[0] - mouseX;
		diff_y = text_position[1] - mouseY;
		
		ArrayList<Integer> positions = GuiOverlay.getBackgroundPositions(mc, 0, 0, true);
		if (positions.get(0) <= mouseX && mouseX <= positions.get(2) && positions.get(1) <= mouseY && mouseY <= positions.get(3)) {
			mc.displayGuiScreen(new MoveOverlayGui(diff_x, diff_y));
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void updateScreen() {
		colorField.updateCursorCounter();
		textField.updateCursorCounter();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		colorField.drawTextBox();
		textField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);

		ArrayList<Integer> positions = GuiOverlay.getBackgroundPositions(mc, 0, 0, true);
		if (positions.get(0) <= mouseX && mouseX <= positions.get(2) && positions.get(1) <= mouseY && mouseY <= positions.get(3)) {
			Color color = new Color(ModConfig.bg_color_r, ModConfig.bg_color_g, ModConfig.bg_color_b, (int) Math.round(ModConfig.bg_color_a * 0.5));
			new GuiOverlay(Minecraft.getMinecraft(), 0, 0, color);
			
			drawVerticalLine(positions.get(0), positions.get(1), positions.get(3), Color.RED.getRGB());
			drawVerticalLine(positions.get(2), positions.get(1), positions.get(3), Color.RED.getRGB());
			drawHorizontalLine(positions.get(0), positions.get(2), positions.get(1), Color.RED.getRGB());
			drawHorizontalLine(positions.get(0), positions.get(2), positions.get(3), Color.RED.getRGB());
		} else {
			new GuiOverlay(Minecraft.getMinecraft(), 0, 0);
		}

		saveConfig();
	}
	
	public void saveConfig() {
		changeConfig();
		ModConfig.syncConfig(false);
		updateButtons();
	}
	
	public void changeConfig() {
		ModConfig.text = textField.getText();
		if (colorField.getText().length() == 6) {
			ModConfig.text_color = colorField.getText();
		}
		ModConfig.text_scale = Double.parseDouble(String.format("%.2f", scaleSlider.getValue() / 100).replace(",", "."));
		ModConfig.rainbow_speed = Double.parseDouble(String.format("%.2f", rainbowSpeedSlider.getValue()).replace(",", "."));
		ModConfig.rainbow_precision = Double.parseDouble(String.format("%.2f", rainbowPrecisionSlider.getValue()).replace(",", "."));
		ModConfig.show_text = showText.getBool();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == baseColorChangerButton) {
			colorSelected = ColorsEnum.getById(colorSelected.getId() + 1);
			baseColorChangerButton.displayString = "Color: " + colorSelected.getName();
			
			if (colorSelected != ColorsEnum.CUSTOM && colorSelected != ColorsEnum.RAINBOW) {
				ModConfig.text_color = colorSelected.getHex();
			}
			
			if (colorSelected == ColorsEnum.RAINBOW) {
				ModConfig.rainbow = true;
			} else {
				ModConfig.rainbow = false;
			}
			
			
			
			colorField.setText(ModConfig.text_color);
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
				colorField.setVisible(true);
			} else {
				colorField.setVisible(false);
			}
		} else {
			colorField.setVisible(true);
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
