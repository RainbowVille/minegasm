package com.therainbowville.minegasm.config;

import com.therainbowville.minegasm.client.ClientEventHandler;
import com.therainbowville.minegasm.client.ToyController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ConfigScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Screen previous;
    public ClientConfig.TickFrequencyOptions tickValue;
    TextFieldWidget wsHost = null;
    private boolean pauseMenu;
    private PlainTextLabel connectResponse;

    public ConfigScreen(Screen previous) {
        super(new StringTextComponent("Minegasm Config"));
        this.previous = previous;
        pauseMenu = false;
        tickValue = ClientConfig.TickFrequencyOptions.fromInt(MinegasmConfig.tickFrequency);
    }

    public ConfigScreen(Screen previous, boolean pause) {
        super(new StringTextComponent("Minegasm Config"));
        this.previous = previous;
        pauseMenu = pause;
        tickValue = ClientConfig.TickFrequencyOptions.fromInt(MinegasmConfig.tickFrequency);
    }

    @Override
    protected void init() {
        wsHost = new TextFieldWidget(Minecraft.getInstance().font, this.width / 2 - 100, this.height / 6, 200, 20, "ws://localhost:12345");
        wsHost.setValue(MinegasmConfig.serverUrl);
        this.addButton(wsHost);

        wsHost.setResponder(s -> {
            LOGGER.info(s);
            MinegasmConfig.serverUrl = s;
        });

        this.addButton(new ExtendedButton(
                this.width / 2 - 155, this.height / 6 + 25, 150, 20,
                "Reset Server Url", button -> {
            ConfigHolder.getClientInstance().resetConfigUrl();
            MinegasmConfig.serverUrl = ConfigHolder.getClientInstance().serverUrl.get();
            wsHost.setValue(MinegasmConfig.serverUrl);
        }
        ));

        connectResponse = new PlainTextLabel(this.width / 2 - 155, this.height / 6 + 50, 310, 15, ("" + TextFormatting.GREEN));

        this.addButton(connectResponse);

        Button reconnectButton = new ExtendedButton(
                this.width / 2 + 5, this.height / 6 + 25, 150, 20,
                "Reconnect", button -> {
            button.active = false;
            connectResponse.setValue("Connecting");
            new Thread(() -> {
                if (ToyController.connectDevice()) {
                    ClientEventHandler.afterConnect();
                    button.active = true;
                    connectResponse.setValue(String.format("Connected to " + TextFormatting.GREEN + "%s" + TextFormatting.RESET + " [%d]", ToyController.getDeviceName(), ToyController.getDeviceId()));
                } else {
                    button.active = true;
                    connectResponse.setValue(String.format(TextFormatting.YELLOW + "Minegasm " + TextFormatting.RESET + "failed to start: %s", ToyController.getLastErrorMessage()));
                }

            }).start();
        }
        );
        this.addButton(reconnectButton);

        reconnectButton.active = pauseMenu;

        this.addButton(new ExtendedButton(this.width / 2 - 155, this.height / 6 + 25 * 3, 150, 20,
                ("Vibration: " + (MinegasmConfig.vibrate ? "True" : "False")), (button) -> {
            MinegasmConfig.vibrate = !MinegasmConfig.vibrate;
            button.setMessage(("Vibration: " + (MinegasmConfig.vibrate ? "True" : "False")));
        }));

        this.addButton(new ExtendedButton(this.width / 2 + 5, this.height / 6 + 25 * 3, 150, 20,
                ("Stealth: " + (MinegasmConfig.stealth ? "True" : "False")), (button) -> {
            MinegasmConfig.stealth = !MinegasmConfig.stealth;
            button.setMessage(("Stealth: " + (MinegasmConfig.stealth ? "True" : "False")));
        }));


        this.addButton(new ExtendedButton(this.width / 2 - 155, this.height / 6 + 25 * 4, 150, 20,
                ("Mode: " + gameplayModeToString(MinegasmConfig.mode)), (button) -> {
            MinegasmConfig.mode = MinegasmConfig.mode.next();
            button.setMessage(("Mode: " + gameplayModeToString(MinegasmConfig.mode)));
        }
        ));

        this.addButton(new ExtendedButton(
                this.width / 2 + 5, this.height / 6 + 25 * 4, 150, 20,
                ("Edit Custom Settings"), button -> minecraft.setScreen(new CustomModeConfigScreen(this, pauseMenu))));

        this.addButton(new ExtendedButton(this.width / 2 - 100, this.height / 6 + 25 * 5, 200, 20,
                ("Tick Frequency: " + tickFrequencyToString(MinegasmConfig.tickFrequency)), (button) -> {
            tickValue = tickValue.next();
            MinegasmConfig.tickFrequency = tickValue.getInt();
            MinegasmConfig.ticksPerSecond = Math.max(1, Math.toIntExact(20 / MinegasmConfig.tickFrequency));
            button.setMessage(("Tick Frequency: " + tickFrequencyToString(MinegasmConfig.tickFrequency)));
        }
        ));

        this.addButton(new ExtendedButton(
                this.width / 2 - 100, this.height - 27, 200, 20,
                ("Done"), button -> this.onClose()));
    }

    private String gameplayModeToString(ClientConfig.GameplayMode mode) {
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
    public void tick() {
        super.tick();

        // Add ticking logic for TextFieldWidget in editBox
        if (this.wsHost != null)
            this.wsHost.tick();
    }

    @Override
    public void onClose() {
        connectResponse.setValue("");
        this.minecraft.setScreen(this.previous);
        MinegasmConfig.save();
    }

    @Override
    public void render(int i, int j, float f) {
        if (pauseMenu)
            this.renderBackground();
        else
            this.renderDirtBackground(0);
        drawCenteredString(this.font, this.title.getString(), this.width / 2, 15, 0xFFFFFF);
        super.render(i, j, f);
    }

    class PlainTextLabel extends Widget {

        private StringTextComponent text = new StringTextComponent("");
        private int x;
        private int y;

        public PlainTextLabel(int x, int y, int width, int height, String text) {
            super(x, y, width, height, text);
            this.x = x;
            this.y = y;
        }

        public void setValue(String value) {
            text = new StringTextComponent(value);
        }

        @Override
        public void render(int i, int j, float f) {
            if (text == null || text.getString().isEmpty())
                return;

            drawCenteredString(Minecraft.getInstance().font, text.getString(), Minecraft.getInstance().screen.width / 2, this.y + this.height / 4, 0xFFFFFF);
        }
    }

}

class CustomModeConfigScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Screen previous;
    private boolean pauseMenu;

    public CustomModeConfigScreen(Screen previous) {
        super(new StringTextComponent("Minegasm Custom Config"));
        this.previous = previous;
        pauseMenu = false;
    }

    public CustomModeConfigScreen(Screen previous, boolean pause) {
        super(new StringTextComponent("Minegasm Custom Config"));
        this.previous = previous;
        pauseMenu = pause;
    }

    @Override
    protected void init() {
        try {
            this.addButton(new ExtendedButton(
                    this.width / 2 + 5, this.height - 27, 150, 20,
                    ("Done"), button -> this.onClose()));

            IntensitiySliderBar.sliders.clear();

            // Attack
            this.addButton(new IntensitiySliderBar(this, "Attack: ", MinegasmConfig.class.getField("attackIntensity")));

            // Hurt
            this.addButton(new IntensitiySliderBar(this, "Hurt: ", MinegasmConfig.class.getField("hurtIntensity")));

            // Mine
            this.addButton(new IntensitiySliderBar(this, "Mine: ", MinegasmConfig.class.getField("mineIntensity")));

            // Place
            this.addButton(new IntensitiySliderBar(this, "Place: ", MinegasmConfig.class.getField("placeIntensity")));

            // XP Change
            this.addButton(new IntensitiySliderBar(this, "XP Change: ", MinegasmConfig.class.getField("xpChangeIntensity")));

            // Fishing
            this.addButton(new IntensitiySliderBar(this, "Fishing: ", MinegasmConfig.class.getField("fishingIntensity")));

            // Harvest
            this.addButton(new IntensitiySliderBar(this, "Harvest: ", MinegasmConfig.class.getField("harvestIntensity")));

            // Vitality
            this.addButton(new IntensitiySliderBar(this, "Vitality: ", MinegasmConfig.class.getField("vitalityIntensity")));

            // Advancement
            this.addButton(new IntensitiySliderBar(this, "Advancement: ", MinegasmConfig.class.getField("advancementIntensity")));

            this.addButton(new ExtendedButton(
                    this.width / 2 - 155, this.height - 27, 150, 20,
                    ("Reset Values"), button -> {
                ConfigHolder.getClientInstance().resetConfigCustom();
                IntensitiySliderBar.refreshAllValues();
            }
            ));


        } catch (Throwable e) {
            LOGGER.throwing(e);
        }
    }

    @Override
    public void onClose() {
        IntensitiySliderBar.sliders.clear();
        this.minecraft.setScreen(this.previous);
    }

    @Override
    public void render(int i, int j, float f) {
        if (pauseMenu)
            this.renderBackground();
        else
            this.renderDirtBackground(0);
        drawCenteredString(this.font, this.title.getString(), this.width / 2, 15, 0xFFFFFF);
        super.render(i, j, f);
    }
}

class IntensitiySliderBar extends ForgeSlider {
    private static final Logger LOGGER = LogManager.getLogger();
    public static ArrayList<IntensitiySliderBar> sliders = new ArrayList<IntensitiySliderBar>();
    private Field fieldReference;

    IntensitiySliderBar(CustomModeConfigScreen parent, String prefix, Field field) throws Exception {
        super(parent.width / 2 + (sliders.size() % 2 == 1 ? 5 : -155), // x pos
                parent.height / 6 + 25 * (int) Math.floor(sliders.size() / 2), // y pos
                150, 20, // Width, height
                prefix, // Prefix
                "", // Suffix
                0, 100, field.getInt(null), 1, 1, true); // Min, Max, Default value, stepsize, percision, drawstring
        fieldReference = field;
        sliders.add(this);
    }

    public static void refreshAllValues() {
        for (IntensitiySliderBar slider : sliders) {
            slider.refreshValue();
        }
    }

    @Override
    public void applyValue() {
        try {
            fieldReference.set(null, this.getValueInt());
        } catch (Throwable e) {
            LOGGER.throwing(e);
        }
    }

    private void refreshValue() {
        try {
            this.setValue(fieldReference.getInt(null));
        } catch (Throwable e) {
            LOGGER.throwing(e);
        }
    }
}