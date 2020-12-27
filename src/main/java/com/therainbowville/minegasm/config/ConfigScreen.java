package com.therainbowville.minegasm.config;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.therainbowville.minegasm.common.Minegasm;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.util.text.StringTextComponent;

import java.util.Objects;

public final class ConfigScreen extends Screen {
    private static final int TITLE_HEIGHT = 8;
    /** Distance from top of the screen to the options row list's top */
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    /** Distance from bottom of the screen to the options row list's bottom */
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    /** Height of each item in the options row list */
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    /** Width of a button */
    private static final int BUTTON_WIDTH = 200;
    /** Height of a button */
    private static final int BUTTON_HEIGHT = 20;
    /** Distance from bottom of the screen to the "Done" button's top */
    private static final int DONE_BUTTON_TOP_OFFSET = 26;

    private final Screen lastScreen;
    private OptionsRowList optionsRowList;

    public ConfigScreen(Screen parentScreen) {
        super(new StringTextComponent(Minegasm.NAME));
        this.lastScreen = parentScreen;
    }

    @Override
    protected void init() {
        this.optionsRowList = new OptionsRowList(
                this.minecraft, this.width, this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );

        this.children.add(this.optionsRowList);

        // "Done" button
        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new StringTextComponent("Done"),
                button -> this.closeScreen()
        ));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
        this.drawCenteredString(matrixStack, this.font, this.title,
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        // ConfigManager.save();
    }

    @Override
    public void closeScreen() {
        Objects.requireNonNull(this.minecraft).displayGuiScreen(this.lastScreen instanceof IngameMenuScreen ? null : this.lastScreen);
    }
}