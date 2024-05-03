package com.therainbowville.minegasm.config;


import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;

import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.therainbowville.minegasm.common.Minegasm;

@Mod.EventBusSubscriber(modid = Minegasm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PauseMenuButtonScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    
    @SubscribeEvent
    public static void onGuiInit(GuiScreenEvent.InitGuiEvent event) {
        if(event.getGui() instanceof GuiIngameMenu) { // Make sure GUI is Escape menu
            PauseMenuButton.MenuRows menu = PauseMenuButton.MenuRows.INGAME_MENU;
            int rowIdx = 3;
            int offsetX = 4;
            boolean onLeft = offsetX < 0;
            String target = (onLeft ? menu.leftButtons : menu.rightButtons).get(rowIdx - 1);
            LOGGER.info(target);

            int offsetX_ = offsetX;
            MutableObject<GuiButton> toAdd = new MutableObject<>(null);
            event.getButtonList()
                .stream()
                .filter(w -> w instanceof GuiButton)
                .map(w -> (GuiButton) w)
                .filter(w -> w.displayString
                    .equals(target))
                .findFirst()
                .ifPresent(w -> toAdd
                    .setValue(new PauseMenuButton(w.x + offsetX_ + (onLeft ? -20 : w.getWidth()), w.y)));
            if (toAdd.getValue() != null)
                event.addButton(toAdd.getValue());
        }
    }
}