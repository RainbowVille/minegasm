package com.therainbowville.minegasm.widgets;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextComponentString;

import net.minecraftforge.fml.client.config.GuiButtonExt;


import com.therainbowville.minegasm.common.Minegasm;
import com.therainbowville.minegasm.client.ToyController;
import com.therainbowville.minegasm.client.ClientEventHandler;
import com.therainbowville.minegasm.config.ClientConfig;
import com.therainbowville.minegasm.config.MinegasmConfig;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiButtonExtraExt extends GuiButtonExt
{
    private ButtonPress onPress;
    
    public GuiButtonExtraExt(int x, int y, int width, int height, java.lang.String displayString, ButtonPress onPress)
    {
        super(0, x, y, width, height, displayString);
        this.onPress = onPress;
    }

    @Override()
    public void onClick(double i, double j)
    {
        onPress.onClick(this);
    }
    
    public void setMessage(java.lang.String message)
    {
        this.displayString = message;
    }
    
}