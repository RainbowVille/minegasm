package com.therainbowville.minegasm.config;


import com.therainbowville.minegasm.common.Minegasm;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = Minegasm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PauseMenuButtonScreen {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onGuiInit(GuiScreenEvent.InitGuiEvent event) {
        if (event.getGui() instanceof IngameMenuScreen) { // Make sure GUI is Escape menu
            PauseMenuButton.MenuRows menu = PauseMenuButton.MenuRows.INGAME_MENU;
            int rowIdx = 3;
            int offsetX = 4;
            boolean onLeft = offsetX < 0;
            String target = (onLeft ? menu.leftButtons : menu.rightButtons).get(rowIdx - 1);
            LOGGER.info(target);

            int offsetX_ = offsetX;
            MutableObject<Widget> toAdd = new MutableObject<>(null);
            event.getWidgetList()
                    .stream()
                    .filter(w -> w instanceof Widget)
                    .map(w -> (Widget) w)
                    .filter(w -> w.getMessage()
                            .getString()
                            .equals(target))
                    .findFirst()
                    .ifPresent(w -> toAdd
                            .setValue(new PauseMenuButton(w.x + offsetX_ + (onLeft ? -20 : w.getWidth()), w.y)));
            if (toAdd.getValue() != null)
                event.addWidget(toAdd.getValue());
        }
    }
}