package com.therainbowville.minegasm.config;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.fml.client.config.GuiConfig;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;

import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.therainbowville.minegasm.common.Minegasm;

@Mod.EventBusSubscriber(modid = Minegasm.MOD_ID)
public class ModListMenuScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    
    @SubscribeEvent
    public static void onGuiInit(GuiScreenEvent.InitGuiEvent event) {
        if(event.getGui() instanceof GuiConfig && ((GuiConfig)event.getGui()).modID.equals(Minegasm.MOD_ID)) { // Make sure GUI is Escape menu
            Minecraft.getMinecraft().displayGuiScreen(new ConfigScreen(((GuiConfig)event.getGui()).parentScreen));
        }
    }
}