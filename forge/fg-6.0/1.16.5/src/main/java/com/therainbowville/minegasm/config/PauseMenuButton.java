package com.therainbowville.minegasm.config;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
		super(x, y, 20, 20, new StringTextComponent(""), PauseMenuButton::clicked);
        xPos = x;
        yPos = y;
	}
        
	@Override
	public void renderBg(MatrixStack mstack, Minecraft mc, int mouseX, int mouseY) {
		mstack.pushPose();
		mstack.translate(xPos + width / 2 - (64 * 0.25f) / 2, yPos + height / 2 - (64 * 0.25f) / 2, 0); 
		mstack.scale(0.25f, 0.25f, 1);
        Minecraft.getInstance().getTextureManager().bind(LOGO);
		AbstractGui.blit(mstack, 0, 0, 0, 0, 0, 64, 64, 64, 64);
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

}