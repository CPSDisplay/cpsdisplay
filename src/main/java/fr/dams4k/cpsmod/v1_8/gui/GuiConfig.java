package fr.dams4k.cpsmod.v1_8.gui;

import java.io.IOException;

import fr.dams4k.cpsmod.v1_8.config.Config;
import fr.dams4k.cpsmod.v1_8.enums.ColorsEnum;
import fr.dams4k.cpsmod.v1_8.enums.MouseModeEnum;
import fr.dams4k.cpsmod.v1_8.enums.ShowTextEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiConfig extends GuiScreen {
	public static GuiConfig instance;
	// Text
	private GuiButtonExt showTextButton;
	private GuiSlider scaleSlider;
	private GuiButtonExt baseColorChangerButton;
	private GuiButtonExt moveButton;
	private GuiButtonExt mouseModeChangerButton;
	private GuiTextField textField;
	private GuiTextField colorField;
	
	// Rainbow
	private GuiSlider rainbowSpeedSlider;
	private GuiSlider rainbowPrecision;
	
	private MouseModeEnum mouseModeSelected;
	private ColorsEnum colorSelected;
	private ShowTextEnum showText;
	
	private int top = 20;
	
	@Override
	public void initGui() {
		mouseModeSelected = MouseModeEnum.getByText(Config.text);
		colorSelected = ColorsEnum.getByHex(Config.text_color);
		showText = ShowTextEnum.getByBool(Config.show_text);
		
		if (Config.rainbow) {
			colorSelected = ColorsEnum.getById(8);
		}
		
		showTextButton = new GuiButtonExt(0, width / 2 - 152, 10 + top, 150, 20, "Show text: " + showText.getText());
		scaleSlider = new GuiSlider(1, width / 2 - 152, 35 + top, 150, 20, "Scale : ", "%", 0.5 * 100, 5 * 100, Config.text_scale * 100, false, true);
		mouseModeChangerButton = new GuiButtonExt(3, width / 2 - 152, 60 + top, 150, 20, "Display mode: " + mouseModeSelected.getName());
		textField = new GuiTextField(2, fontRendererObj,  width / 2 - 152, 85 + top, 150, 20);
		textField.setMaxStringLength(999);
		textField.setText(Config.text);
		moveButton = new GuiButtonExt(3,  width / 2 - 152, 110 + top, 150, 20, "Move");
		
		baseColorChangerButton = new GuiButtonExt(10, width / 2 + 2, 10 + top, 150, 20, "Color: " + colorSelected.getName());
		colorField = new GuiTextField(11, fontRendererObj, width / 2 + 2, 35 + top, 150, 20);
		colorField.setMaxStringLength(6);
		colorField.setText(Config.text_color);
		
		rainbowSpeedSlider = new GuiSlider(13, width / 2 + 2, 60 + top, 150, 20, "Speed: ", "ms", 1, 500, Config.rainbow_speed, false, true);
		rainbowPrecision = new GuiSlider(14, width / 2 + 2, 85 + top, 150, 20, "Precision: ", "", 0.01, 1, Config.rainbow_precision, true, true);

		buttonList.add(showTextButton);
		buttonList.add(scaleSlider);
		buttonList.add(mouseModeChangerButton);
		buttonList.add(moveButton);
		
		buttonList.add(baseColorChangerButton);
		buttonList.add(rainbowSpeedSlider);
		buttonList.add(rainbowPrecision);

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
		new GuiOverlay(Minecraft.getMinecraft(), 0, 0);
		saveConfig();
	}
	
	public void saveConfig() {
		changeConfig();
		Config.syncConfig(false);
		updateButtons();
	}
	
	public void changeConfig() {
		Config.text = textField.getText();
		if (colorField.getText().length() == 6) {
			Config.text_color = colorField.getText();
		}
		Config.text_scale = Double.parseDouble(String.format("%.2f", scaleSlider.getValue() / 100).replace(",", "."));
		Config.show_text = showText.getBool();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == baseColorChangerButton) {
			colorSelected = ColorsEnum.getById(colorSelected.getId() + 1);
			baseColorChangerButton.displayString = "Color: " + colorSelected.getName();
			
			if (colorSelected != ColorsEnum.CUSTOM && colorSelected != ColorsEnum.RAINBOW) {
				Config.text_color = colorSelected.getHex();
			}
			
			if (colorSelected == ColorsEnum.RAINBOW) {
				Config.rainbow = true;
			} else {
				Config.rainbow = false;
			}
			
			
			
			colorField.setText(Config.text_color);
		} else if (button == mouseModeChangerButton) {
			mouseModeSelected = MouseModeEnum.getById(mouseModeSelected.getId() + 1);
			mouseModeChangerButton.displayString = "Mode: " + mouseModeSelected.getName();

			if (mouseModeSelected != MouseModeEnum.CUSTOM) {
				Config.text = mouseModeSelected.getText();
			}
			textField.setText(Config.text);
		} else if (button == showTextButton) {
			showText = ShowTextEnum.getByBool(!showText.getBool());
			showTextButton.displayString = "Show text: " + showText.getText();
		}
		saveConfig();
		
		if (button == moveButton) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiMoveText());
		}
	}
	
	public void updateButtons() {
		// Color
		if (colorSelected != ColorsEnum.RAINBOW) {
			rainbowPrecision.visible = false;
			rainbowSpeedSlider.visible = false;
			
			if (colorSelected == ColorsEnum.CUSTOM) {
				colorField.setVisible(true);
			} else {
				colorField.setVisible(false);
			}
		} else {
			colorField.setVisible(true);
			rainbowPrecision.visible = true;
			rainbowSpeedSlider.visible = true;
		}
		
		// Display mode
		if (mouseModeSelected == MouseModeEnum.CUSTOM) {
			textField.setVisible(true);
			moveButton.yPosition = 110 + top;
		} else {
			textField.setVisible(false);
			moveButton.yPosition = 85 + top;
		}
	}
}
