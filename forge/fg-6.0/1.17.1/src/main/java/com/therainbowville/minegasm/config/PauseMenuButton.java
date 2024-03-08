package com.therainbowville.minegasm.config;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.mutable.MutableObject;

import com.therainbowville.minegasm.common.Minegasm;
import com.therainbowville.minegasm.config.ClientConfig;
import com.therainbowville.minegasm.config.MinegasmConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Adapted from https://github.com/Creators-of-Create/Create/
public class PauseMenuButton extends Button
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static ResourceLocation LOGO = new ResourceLocation(Minegasm.MOD_ID, "textures/logo.png");
    
    int xPos;
    int yPos;
    
	public PauseMenuButton(int x, int y) {
		super(x, y, 20, 20, new TextComponent(""), PauseMenuButton::clicked);
        xPos = x;
        yPos = y;
	}
        
	@Override
	public void renderBg(PoseStack mstack, Minecraft mc, int mouseX, int mouseY) {
		mstack.pushPose();
		mstack.translate(xPos + width / 2 - (64 * 0.25f) / 2, yPos + height / 2 - (64 * 0.25f) / 2, 0); 
		mstack.scale(0.25f, 0.25f, 1);
		RenderSystem.setShaderTexture(0, LOGO);
		GuiComponent.blit(mstack, 0, 0, 0, 0, 0, 64, 64, 64, 64);
		mstack.popPose();
	}
    
    public static void clicked(Button button)
    {
        Minecraft.getInstance().setScreen(new ConfigScreen(Minecraft.getInstance().screen, true));
    }
    
    public static class SingleMenuRow {
		public final String left, right;
		public SingleMenuRow(String left, String right) {
			this.left = I18n.get(left);
			this.right = I18n.get(right);
		}
		public SingleMenuRow(String center) {
			this(center, center);
		}
	}
    
	public static class MenuRows {
		public static final MenuRows MAIN_MENU = new MenuRows(Arrays.asList(
			new SingleMenuRow("menu.singleplayer"),
			new SingleMenuRow("menu.multiplayer"),
			new SingleMenuRow("fml.menu.mods", "menu.online"),
			new SingleMenuRow("narrator.button.language", "narrator.button.accessibility")
		));

		public static final MenuRows INGAME_MENU = new MenuRows(Arrays.asList(
			new SingleMenuRow("menu.returnToGame"),
			new SingleMenuRow("gui.advancements", "gui.stats"),
			new SingleMenuRow("menu.sendFeedback", "menu.reportBugs"),
			new SingleMenuRow("menu.options", "menu.shareToLan"),
			new SingleMenuRow("menu.returnToMenu")
		));

		protected final List<String> leftButtons, rightButtons;

		public MenuRows(List<SingleMenuRow> variants) {
			leftButtons = variants.stream().map(r -> r.left).collect(Collectors.toList());
			rightButtons = variants.stream().map(r -> r.right).collect(Collectors.toList());
		}
	}
    
    @Mod.EventBusSubscriber(modid = Minegasm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public class PauseMenuButtonScreen {
        
        @SubscribeEvent
        public static void onGuiInit(GuiScreenEvent.InitGuiEvent event) {
            if(event.getGui() instanceof PauseScreen) { // Make sure GUI is Escape menu
                MenuRows menu = MenuRows.INGAME_MENU;
                int rowIdx = 3;
                int offsetX = 4;
                boolean onLeft = offsetX < 0;
                String target = (onLeft ? menu.leftButtons : menu.rightButtons).get(rowIdx - 1);

                int offsetX_ = offsetX;
                MutableObject<GuiEventListener> toAdd = new MutableObject<>(null);
                event.getWidgetList()
                    .stream()
                    .filter(w -> w instanceof AbstractWidget)
                    .map(w -> (AbstractWidget) w)
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
}
