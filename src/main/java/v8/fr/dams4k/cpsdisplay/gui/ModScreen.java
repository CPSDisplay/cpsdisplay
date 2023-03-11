package fr.dams4k.cpsdisplay.gui;

import java.util.ArrayList;
import java.util.List;

import fr.dams4k.cpsdisplay.References;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class ModScreen extends GuiScreen {
    public List<GuiTextField> textFieldList = new ArrayList<>();
    public int top = 20;

    @Override
    public void initGui() {
        super.initGui();
        
        textFieldList.clear();
		buttonList.clear();
		labelList.clear();

        String title = String.format("%s - v%s", References.MOD_NAME, References.MOD_VERSION);
        GuiLabel titleLabel = new GuiLabel(mc.fontRendererObj, -1, width/2-mc.fontRendererObj.getStringWidth(title)/2, top-10, 150, 20, 0xffffff);
        titleLabel.func_175202_a(title);
        labelList.add(titleLabel);
    }
}
