package com.therainbowville.minegasm.config;

import com.therainbowville.minegasm.common.Minegasm;
import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.AbstractOption;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.IteratableOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.StringTextComponent;
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
    private OptionsRowList optionsRowList;

    private static final ClientConfig clientConfig = ConfigHolder.getClientInstance();

    public ConfigScreen(Screen parentScreen) {
        super(new StringTextComponent(Minegasm.NAME));
        this.lastScreen = parentScreen;
    }

    private static boolean vibrate = false;
    private static int mode = 0;
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
        this.children.add(this.optionsRowList);

        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                "Done",
                button -> this.onClose()
        ));
    }

    private OptionsRowList createOptions() {
        OptionsRowList optionsRowList = new OptionsRowList(
                Objects.requireNonNull(this.minecraft), this.width, this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );

        String VIBRATE = "gui.minegasm.config.vibrate";
        AbstractOption vibrateOption = new BooleanOption(
                VIBRATE,
                (gameSettings) -> vibrate,
                (gameSettings, newValue) -> vibrate = newValue
        );

        String MODE = "gui.minegasm.config.mode";
        AbstractOption modeOption = new IteratableOption(
                MODE,
                (gameSettings, newValue) -> mode = (mode + newValue) % ClientConfig.GameplayMode.values().length,
                (gameSettings, option) -> MODE + ": " + ClientConfig.GameplayMode.values()[mode].getTranslateKey());

        // Intensity
        String ATTACK_INTENSITY = "gui.minegasm.config.intensity.attack";
        AbstractOption attackIntensityOption = new SliderPercentageOption(
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
        AbstractOption hurtIntensityOption = new SliderPercentageOption(
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
        AbstractOption mineIntensityOption = new SliderPercentageOption(
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
        AbstractOption xpChangeIntensityOption = new SliderPercentageOption(
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
        AbstractOption harvestIntensityOption = new SliderPercentageOption(
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
        AbstractOption vitalityIntensityOption = new SliderPercentageOption(
                VITALITY_INTENSITY,
                0.0, 100, 10.0F,
                (gameSettings) -> (double) vitalityIntensity,
                (gameSettings, newValue) -> {
                    vitalityIntensity = newValue.intValue();
                    mode = ClientConfig.GameplayMode.CUSTOM.ordinal();
                },
                (gameSettings, option) -> getPercentValueComponent(VITALITY_INTENSITY, gameSettings, option)
        );

        optionsRowList.func_214334_a(vibrateOption, modeOption);
        optionsRowList.func_214334_a(attackIntensityOption, hurtIntensityOption);
        optionsRowList.func_214334_a(mineIntensityOption, xpChangeIntensityOption);
        optionsRowList.func_214334_a(harvestIntensityOption, vitalityIntensityOption);

        return optionsRowList;
    }

    private String getPercentValueComponent(String translationKey, GameSettings gameSettings, SliderPercentageOption option) {
        return translationKey + ": " + (option.func_216726_a(option.get(gameSettings)) == 0.0D ? "Off" : (int) option.get(gameSettings) + "%");
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.children.remove(this.optionsRowList);
        this.optionsRowList = createOptions();
        this.children.add(this.optionsRowList);
        this.optionsRowList.render(mouseX, mouseY, partialTicks);

        drawCenteredString(this.font, this.title.getString(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);

        super.render(mouseX, mouseY, partialTicks);
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
        Objects.requireNonNull(this.minecraft).displayGuiScreen(this.lastScreen instanceof IngameMenuScreen ? null : this.lastScreen);
    }
}