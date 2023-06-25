package com.therainbowville.minegasm.config;

import com.mojang.blaze3d.vertex.PoseStack;
import com.therainbowville.minegasm.common.Minegasm;
import net.minecraft.client.CycleOption;
import net.minecraft.client.Options;
import net.minecraft.client.ProgressOption;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public final class ConfigScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int TITLE_HEIGHT = 8;
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24; // top screen to top option list
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32; // bottom screen to bottom option list
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int DONE_BUTTON_TOP_OFFSET = 26; // bottom screen to done button top

    private final Screen lastScreen;
    private OptionsList optionsRowList;

    private static final ClientConfig clientConfig = ConfigHolder.getClientInstance();

    public ConfigScreen(Screen parentScreen) {
        super(new TextComponent(Minegasm.NAME));
        this.lastScreen = parentScreen;
    }

    private static boolean vibrate = false;
    private static int mode = ClientConfig.GameplayMode.NORMAL.ordinal();
    private static int attackIntensity = 0;
    private static int hurtIntensity = 0;
    private static int mineIntensity = 0;
    private static int xpChangeIntensity = 0;
    private static int harvestIntensity = 0;
    private static int vitalityIntensity = 0;

    @Override
    protected void init() {
        LOGGER.debug("Initializing config screen");

        vibrate = MinegasmConfig.vibrate;
        mode = MinegasmConfig.mode.ordinal();
        attackIntensity = MinegasmConfig.attackIntensity;
        hurtIntensity = MinegasmConfig.hurtIntensity;
        mineIntensity = MinegasmConfig.mineIntensity;
        xpChangeIntensity = MinegasmConfig.xpChangeIntensity;
        harvestIntensity = MinegasmConfig.harvestIntensity;
        vitalityIntensity = MinegasmConfig.vitalityIntensity;

        this.optionsRowList = createOptions();
        this.renderables.add(this.optionsRowList);

        this.renderables.add(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new TextComponent("Done"),
                button -> this.onClose()
        ));
    }

    private OptionsList createOptions() {
        OptionsList optionsRowList = new OptionsList(
                Objects.requireNonNull(this.minecraft), this.width, this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );

        String VIBRATE = "gui.minegasm.config.vibrate";
        CycleOption<Boolean> vibrateOption = CycleOption.createOnOff(
                VIBRATE,
                new TranslatableComponent(VIBRATE),
                (options) -> vibrate,
                (options, option, newValue) -> vibrate = newValue
        );

        String MODE = "gui.minegasm.config.mode";
        CycleOption<Integer> modeOption = CycleOption.create(
                MODE,
                new Integer[]  {0,1,2,3},
                (m) -> new TranslatableComponent(MODE).append(": ")
                        .append(new TranslatableComponent(ClientConfig.GameplayMode.values()[m].getTranslateKey())),
                (options) -> mode,
                (options, option, newValue) -> mode = newValue);


        // Intensity
        String ATTACK_INTENSITY = "gui.minegasm.config.intensity.attack";
        ProgressOption attackIntensityOption = new ProgressOption(
                ATTACK_INTENSITY,
                0.0, 100, 10.0F,
                (gameSettings) -> (double) attackIntensity,
                (gameSettings, newValue) -> {
                    attackIntensity = newValue.intValue();
                    mode = ClientConfig.GameplayMode.CUSTOM.ordinal();
                },
                (gameSettings, option) -> getPercentValueComponent(ATTACK_INTENSITY, gameSettings, option)
        );

        String HURT_INTENSITY = "gui.minegasm.config.intensity.hurt";
        ProgressOption hurtIntensityOption = new ProgressOption(
                HURT_INTENSITY,
                0.0, 100, 10.0F,
                (gameSettings) -> (double) hurtIntensity,
                (gameSettings, newValue) -> {
                    hurtIntensity = newValue.intValue();
                    mode = ClientConfig.GameplayMode.CUSTOM.ordinal();
                },
                (gameSettings, option) -> getPercentValueComponent(HURT_INTENSITY, gameSettings, option)
        );

        String MINE_INTENSITY = "gui.minegasm.config.intensity.mine";
        ProgressOption mineIntensityOption = new ProgressOption(
                MINE_INTENSITY,
                0.0, 100, 10.0F,
                (gameSettings) -> (double) mineIntensity,
                (gameSettings, newValue) -> {
                    mineIntensity = newValue.intValue();
                    mode = ClientConfig.GameplayMode.CUSTOM.ordinal();
                },
                (gameSettings, option) -> getPercentValueComponent(MINE_INTENSITY, gameSettings, option)
        );

        String XP_CHANGE_INTENSITY = "gui.minegasm.config.intensity.xp";
        ProgressOption xpChangeIntensityOption = new ProgressOption(
                XP_CHANGE_INTENSITY,
                0.0, 100, 10.0F,
                (gameSettings) -> (double) xpChangeIntensity,
                (gameSettings, newValue) -> {
                    xpChangeIntensity = newValue.intValue();
                    mode = ClientConfig.GameplayMode.CUSTOM.ordinal();
                },
                (gameSettings, option) -> getPercentValueComponent(XP_CHANGE_INTENSITY, gameSettings, option)
        );

        String HARVEST_INTENSITY = "gui.minegasm.config.intensity.harvest";
        ProgressOption harvestIntensityOption = new ProgressOption(
                HARVEST_INTENSITY,
                0.0, 100, 10.0F,
                (gameSettings) -> (double) harvestIntensity,
                (gameSettings, newValue) -> {
                    harvestIntensity = newValue.intValue();
                    mode = ClientConfig.GameplayMode.CUSTOM.ordinal();
                },
                (gameSettings, option) -> getPercentValueComponent(HARVEST_INTENSITY, gameSettings, option)
        );

        String VITALITY_INTENSITY = "gui.minegasm.config.intensity.vitality";
        ProgressOption vitalityIntensityOption = new ProgressOption(
                VITALITY_INTENSITY,
                0.0, 100, 10.0F,
                (options) -> (double) vitalityIntensity,
                (options, newValue) -> {
                    vitalityIntensity = newValue.intValue();
                    mode = ClientConfig.GameplayMode.CUSTOM.ordinal();
                },
                (options, option) -> getPercentValueComponent(VITALITY_INTENSITY, options, option)
        );

        optionsRowList.addSmall(vibrateOption, modeOption);
        optionsRowList.addSmall(attackIntensityOption, hurtIntensityOption);
        optionsRowList.addSmall(mineIntensityOption, xpChangeIntensityOption);
        optionsRowList.addSmall(harvestIntensityOption, vitalityIntensityOption);

        return optionsRowList;
    }

    private Component getPercentValueComponent(String translationKey, Options options, ProgressOption option) {
        return new TranslatableComponent(translationKey).append(": ").append(option.toPct(option.get(options)) == 0.0D ? "Off" : ((int) option.get(options) + "%"));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.renderables.remove(this.optionsRowList);
        this.optionsRowList = createOptions();
        this.renderables.add(this.optionsRowList);
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);

        //noinspection SuspiciousNameCombination
        drawCenteredString(matrixStack, this.font, this.title,
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        LOGGER.debug("saving...");
        clientConfig.vibrate.set(vibrate);
        clientConfig.mode.set(ClientConfig.GameplayMode.values()[mode]);
        clientConfig.attackIntensity.set(attackIntensity);
        clientConfig.hurtIntensity.set(hurtIntensity);
        clientConfig.mineIntensity.set(mineIntensity);
        clientConfig.xpChangeIntensity.set(xpChangeIntensity);
        clientConfig.harvestIntensity.set(harvestIntensity);
        clientConfig.vitalityIntensity.set(vitalityIntensity);
        ConfigHolder.CLIENT_SPEC.save();
        ConfigHelper.bakeClient();
        Objects.requireNonNull(this.minecraft).setScreen(this.lastScreen instanceof OptionsSubScreen ? null : this.lastScreen);
    }
}