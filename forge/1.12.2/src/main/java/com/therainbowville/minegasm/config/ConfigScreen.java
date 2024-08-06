package com.therainbowville.minegasm.config;

import com.therainbowville.minegasm.client.ClientEventHandler;
import com.therainbowville.minegasm.client.ToyController;
import com.therainbowville.minegasm.common.Minegasm;
import com.therainbowville.minegasm.widgets.GuiButtonExtraExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ConfigScreen extends GuiScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    private final GuiScreen previous;
    public MinegasmConfig.TickFrequencyOptions tickValue;
    GuiTextField wsHost = null;
    private boolean pauseMenu;
    private PlainTextLabel connectResponse;

    public ConfigScreen(GuiScreen previous) {
        LOGGER.info("Called");
//        super();
        //new TextComponentString("Minegasm Config")
        this.previous = previous;
        pauseMenu = false;
        tickValue = MinegasmConfig.tickFrequency;
    }

    public ConfigScreen(GuiScreen previous, boolean pause) {
        super();
        //new TextComponentString("Minegasm Config")
        this.previous = previous;
        pauseMenu = pause;
        tickValue = MinegasmConfig.tickFrequency;
    }


    @Override
    public void initGui() {
        wsHost = new GuiTextField(1, Minecraft.getMinecraft().fontRenderer, this.width / 2 - 100, this.height / 6, 200, 20);
        wsHost.setText(MinegasmConfig.serverUrl.serverUrl);
//        this.children.add(wsHost);

        class Responder implements GuiPageButtonList.GuiResponder {
            public void setEntryValue(int id, int value) {
                LOGGER.info(value);
            }

            public void setEntryValue(int id, boolean value) {
                LOGGER.info(value);
            }

            public void setEntryValue(int id, float value) {
                LOGGER.info(value);
            }

            public void setEntryValue(int id, String value) {
                LOGGER.info(value);
                MinegasmConfig.serverUrl.serverUrl = value;
            }
        }

        wsHost.setGuiResponder(new Responder());

        this.addButton(new GuiButtonExtraExt(
                this.width / 2 - 155, this.height / 6 + 25, 150, 20,
                "Reset Server Url", button -> {
            MinegasmConfig.resetConfigUrl();
            wsHost.setText(MinegasmConfig.serverUrl.serverUrl);
        }
        ));

        connectResponse = new PlainTextLabel(this.width / 2 - 155, this.height / 6 + 50, 310, 15, ("" + TextFormatting.GREEN));

        this.addButton(connectResponse);

        GuiButtonExtraExt reconnectButton = new GuiButtonExtraExt(
                this.width / 2 + 5, this.height / 6 + 25, 150, 20,
                "Reconnect", button -> {
            button.enabled = false;
            connectResponse.setValue("Connecting");
            new Thread(() -> {
                if (ToyController.connectDevice()) {
                    ClientEventHandler.afterConnect();
                    button.enabled = true;
                    connectResponse.setValue(String.format("Connected to " + TextFormatting.GREEN + "%s" + TextFormatting.RESET + " [%d]", ToyController.getDeviceName(), ToyController.getDeviceId()));
                } else {
                    button.enabled = true;
                    connectResponse.setValue(String.format(TextFormatting.YELLOW + "Minegasm " + TextFormatting.RESET + "failed to start: %s", ToyController.getLastErrorMessage()));
                }

            }).start();
        }
        );
        this.addButton(reconnectButton);

        reconnectButton.enabled = pauseMenu;

        this.addButton(new GuiButtonExtraExt(this.width / 2 - 155, this.height / 6 + 25 * 3, 150, 20,
                ("Vibration: " + (MinegasmConfig.vibrate ? "True" : "False")), (button) -> {
            MinegasmConfig.vibrate = !MinegasmConfig.vibrate;
            button.setMessage(("Vibration: " + (MinegasmConfig.vibrate ? "True" : "False")));
        }));

        this.addButton(new GuiButtonExtraExt(this.width / 2 + 5, this.height / 6 + 25 * 3, 150, 20,
                ("Stealth: " + (MinegasmConfig.stealth ? "True" : "False")), (button) -> {
            MinegasmConfig.stealth = !MinegasmConfig.stealth;
            button.setMessage(("Stealth: " + (MinegasmConfig.stealth ? "True" : "False")));
        }));


        this.addButton(new GuiButtonExtraExt(this.width / 2 - 155, this.height / 6 + 25 * 4, 150, 20,
                ("Mode: " + gameplayModeToString(MinegasmConfig.mode)), (button) -> {
            MinegasmConfig.mode = MinegasmConfig.mode.next();
            button.setMessage(("Mode: " + gameplayModeToString(MinegasmConfig.mode)));
        }
        ));

        this.addButton(new GuiButtonExtraExt(
                this.width / 2 + 5, this.height / 6 + 25 * 4, 150, 20,
                ("Edit Custom Settings"), button -> this.mc.displayGuiScreen(new CustomModeConfigScreen(this, pauseMenu))));

        this.addButton(new GuiButtonExtraExt(this.width / 2 - 100, this.height / 6 + 25 * 5, 200, 20,
                "Tick Frequency: " + tickFrequencyToString(MinegasmConfig.tickFrequency.getInt()), (button) -> {
            tickValue = tickValue.next();
            MinegasmConfig.tickFrequency = tickValue;
            MinegasmConfig.ticksPerSecond = Math.max(1, Math.toIntExact(20 / MinegasmConfig.tickFrequency.getInt()));
            button.setMessage(("Tick Frequency: " + tickFrequencyToString(MinegasmConfig.tickFrequency.getInt())));
        }
        ));

        this.addButton(new GuiButtonExtraExt(
                this.width / 2 - 100, this.height - 27, 200, 20,
                ("Done"), button -> this.close()));
    }

    private String gameplayModeToString(MinegasmConfig.GameplayMode mode) {
        switch (mode) {
            case NORMAL:
                return "Normal";
            case MASOCHIST:
                return "Masochist";
            case HEDONIST:
                return "Hedonist";
            case ACCUMULATION:
                return "Accumulation";
            case CUSTOM:
                return "Custom";
            default:
                return "";
        }
    }

    private String tickFrequencyToString(Integer frequency) {
        switch (frequency) {
            case 1:
                return "Every Tick";
            case 2:
                return "Every Other Tick";
            case 5:
                return "Every 5 Ticks";
            case 10:
                return "Every 10 Ticks";
            case 20:
                return "Every Second";
            default:
                return "Every " + Float.toString(frequency / 20f) + " Seconds";
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        // Add ticking logic for TextFieldWidget in editBox
        if (this.wsHost != null)
            this.wsHost.updateCursorCounter();
    }

    public void close() {
        connectResponse.setValue("");
        ConfigManager.sync(Minegasm.MOD_ID, Config.Type.INSTANCE);
        this.mc.displayGuiScreen(this.previous);
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        if (pauseMenu)
            this.drawWorldBackground(1);
        else
            this.drawDefaultBackground();
        drawCenteredString(this.fontRenderer, "Minegasm Config", this.width / 2, 15, 0xFFFFFF);
        this.wsHost.drawTextBox();
        super.drawScreen(i, j, f);
    }

    @Override
    public void keyTyped(char i, int j) throws IOException {
        this.wsHost.textboxKeyTyped(i, j);
        super.keyTyped(i, j);
    }

    @Override
    public void mouseClicked(int x, int y, int btn) throws IOException {
        this.wsHost.mouseClicked(x, y, btn);
        super.mouseClicked(x, y, btn);
    }

    class PlainTextLabel extends GuiButton {

        private TextComponentString text = new TextComponentString("");
        private int x;
        private int y;

        public PlainTextLabel(int x, int y, int width, int height, String text) {
            super(0, x, y, width, height, text);
            this.x = x;
            this.y = y;
        }

        public void setValue(String value) {
            text = new TextComponentString(value);
        }

        @Override
        public void drawButton(Minecraft mc, int i, int j, float f) {
            if (text == null || text.getText().isEmpty())
                return;

            drawCenteredString(Minecraft.getMinecraft().fontRenderer, text.getText(), Minecraft.getMinecraft().currentScreen.width / 2, this.y + this.height / 4, 0xFFFFFF);
        }
    }

}

class CustomModeConfigScreen extends GuiScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    private final GuiScreen previous;
    private boolean pauseMenu;

    public CustomModeConfigScreen(GuiScreen previous) {
        super();
        //new TextComponentString("Minegasm Custom Config")
        this.previous = previous;
        pauseMenu = false;
    }

    public CustomModeConfigScreen(GuiScreen previous, boolean pause) {
        super();
        //new TextComponentString("Minegasm Custom Config")
        this.previous = previous;
        pauseMenu = pause;
    }

    @Override
    public void initGui() {
        try {
            this.addButton(new GuiButtonExtraExt(
                    this.width / 2 + 5, this.height - 27, 150, 20,
                    ("Done"), button -> this.close()));

            IntensitiySliderBar.sliders.clear();

            // Attack
            this.addButton(new IntensitiySliderBar(this, "Attack: ", MinegasmConfig.IntensityConfig.class.getField("attackIntensity")));

            // Hurt
            this.addButton(new IntensitiySliderBar(this, "Hurt: ", MinegasmConfig.IntensityConfig.class.getField("hurtIntensity")));

            // Mine
            this.addButton(new IntensitiySliderBar(this, "Mine: ", MinegasmConfig.IntensityConfig.class.getField("mineIntensity")));

            // Place
            this.addButton(new IntensitiySliderBar(this, "Place: ", MinegasmConfig.IntensityConfig.class.getField("placeIntensity")));

            // XP Change
            this.addButton(new IntensitiySliderBar(this, "XP Change: ", MinegasmConfig.IntensityConfig.class.getField("xpChangeIntensity")));

            // Fishing
            this.addButton(new IntensitiySliderBar(this, "Fishing: ", MinegasmConfig.IntensityConfig.class.getField("fishingIntensity")));

            // Harvest
            this.addButton(new IntensitiySliderBar(this, "Harvest: ", MinegasmConfig.IntensityConfig.class.getField("harvestIntensity")));

            // Vitality
            this.addButton(new IntensitiySliderBar(this, "Vitality: ", MinegasmConfig.IntensityConfig.class.getField("vitalityIntensity")));

            // Advancement
            this.addButton(new IntensitiySliderBar(this, "Advancement: ", MinegasmConfig.IntensityConfig.class.getField("advancementIntensity")));

            this.addButton(new GuiButtonExtraExt(
                    this.width / 2 - 155, this.height - 27, 150, 20,
                    ("Reset Values"), button -> {
                MinegasmConfig.resetConfigCustom();
                LOGGER.info("Resetting");
                IntensitiySliderBar.refreshAllValues();
            }
            ));


        } catch (Throwable e) {
            LOGGER.throwing(e);
        }
    }

    public void close() {
        IntensitiySliderBar.sliders.clear();
        this.mc.displayGuiScreen(this.previous);
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        if (pauseMenu)
            this.drawWorldBackground(1);
        else
            this.drawDefaultBackground();
        drawCenteredString(this.fontRenderer, "Minegasm Custom Config", this.width / 2, 15, 0xFFFFFF);
        super.drawScreen(i, j, f);
    }
}

class IntensitiySliderBar extends GuiSlider {
    private static final Logger LOGGER = LogManager.getLogger();
    public static ArrayList<IntensitiySliderBar> sliders = new ArrayList<IntensitiySliderBar>();
    private Field fieldReference;

    IntensitiySliderBar(CustomModeConfigScreen parent, String prefix, Field field) throws Exception {
        super(1, parent.width / 2 + (sliders.size() % 2 == 1 ? 5 : -155), // x pos
                parent.height / 6 + 25 * (int) Math.floor(sliders.size() / 2), // y pos
                150, 20, // Width, height
                prefix, // Prefix
                "", // Suffix
                0, 100, field.getInt(MinegasmConfig.intensity), false, true); // Min, Max, Default value, percision, drawstring
        fieldReference = field;
        sliders.add(this);

        this.parent = (slider) -> {
            try {
                fieldReference.set(MinegasmConfig.intensity, slider.getValueInt());
            } catch (Throwable e) {
                LOGGER.throwing(e);
            }
        };
    }

    public static void refreshAllValues() {
        for (IntensitiySliderBar slider : sliders) {
            slider.refreshValue();
        }
    }

    private void refreshValue() {
        try {
            LOGGER.info(fieldReference.getInt(MinegasmConfig.intensity));
            this.setValue(fieldReference.getInt(MinegasmConfig.intensity));
            this.updateSlider();
        } catch (Throwable e) {
            LOGGER.throwing(e);
        }
    }
}