package com.therainbowville.minegasm.widgets;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.Minecraft;

import net.minecraftforge.fml.client.config.GuiButtonExt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiButtonExtraExt extends GuiButtonExt
{
    private ButtonPress onPress;
    
    public GuiButtonExtraExt(int x, int y, int width, int height, java.lang.String displayString, ButtonPress onPress)
    {
        super(16, x, y, width, height, displayString);
        this.onPress = onPress;
    }

    @Override()
    public boolean mousePressed(Minecraft mc, int i, int j)
    {
        if (isMouseOver() && this.enabled == true)
            onPress.onClick(this);
        return super.mousePressed(mc, i, j);
    }
    
    public void setMessage(java.lang.String message)
    {
        this.displayString = message;
    }
    
}