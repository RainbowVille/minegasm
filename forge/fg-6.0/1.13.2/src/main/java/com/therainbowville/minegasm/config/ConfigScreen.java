package com.therainbowville.minegasm.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextComponentString;

import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.client.config.GuiSlider;

import com.therainbowville.minegasm.common.Minegasm;
import com.therainbowville.minegasm.client.ToyController;
import com.therainbowville.minegasm.client.ClientEventHandler;
import com.therainbowville.minegasm.config.ClientConfig;
import com.therainbowville.minegasm.config.MinegasmConfig;
import com.therainbowville.minegasm.widgets.GuiButtonExtraExt;


import java.lang.reflect.Field;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigScreen extends GuiScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    private final GuiScreen previous;
    private boolean pauseMenu;
    GuiTextField wsHost = null;

    public ClientConfig.TickFrequencyOptions tickValue;
    private PlainTextLabel connectResponse;

    public ConfigScreen(GuiScreen previous) {
        LOGGER.info("Called");
//        super();
        //new TextComponentString("Minegasm Config")
        this.previous = previous;
        pauseMenu = false;
        tickValue = ClientConfig.TickFrequencyOptions.fromInt(MinegasmConfig.tickFrequency);
    }
    
    public ConfigScreen(GuiScreen previous, boolean pause) {
        super();
        //new TextComponentString("Minegasm Config")
        this.previous = previous;
        pauseMenu = pause;
        tickValue = ClientConfig.TickFrequencyOptions.fromInt(MinegasmConfig.tickFrequency);
    }

    @Override
    protected void initGui() {
        wsHost = new GuiTextField(1, Minecraft.getInstance().fontRenderer, this.width / 2 - 100, this.height / 6, 200, 20);
        wsHost.setText(MinegasmConfig.serverUrl);
//        this.children.add(wsHost);
        
        wsHost.setTextAcceptHandler((i, s) -> { // Set responder function
            LOGGER.info(s);
            MinegasmConfig.serverUrl = s;
		});
        
        this.addButton(new GuiButtonExtraExt(
            this.width / 2 - 155, this.height / 6 + 25, 150, 20,
           "Reset Server Url", button -> {
                ConfigHolder.getClientInstance().resetConfigUrl();
                MinegasmConfig.serverUrl = ConfigHolder.getClientInstance().serverUrl.get();
                wsHost.setText(MinegasmConfig.serverUrl);
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
            ("Edit Custom Settings"), button ->  this.mc.displayGuiScreen(new CustomModeConfigScreen(this, pauseMenu))));

        this.addButton(new GuiButtonExtraExt(this.width / 2 - 100, this.height / 6 + 25 * 5, 200, 20,
            ("Tick Frequency: " + tickFrequencyToString(MinegasmConfig.tickFrequency)), (button) -> {
                tickValue = tickValue.next();
                MinegasmConfig.tickFrequency = tickValue.getInt();
                MinegasmConfig.ticksPerSecond = Math.max(1, Math.toIntExact(20 / MinegasmConfig.tickFrequency));
                button.setMessage(("Tick Frequency: " + tickFrequencyToString(MinegasmConfig.tickFrequency)));
            }
        ));

        this.addButton(new GuiButtonExtraExt(
            this.width / 2 - 100, this.height - 27, 200, 20,
            ("Done"), button -> this.close()));
    }
    
    private String gameplayModeToString(ClientConfig.GameplayMode mode){
        switch (mode) {
            case NORMAL: return "Normal";
            case MASOCHIST: return "Masochist";
            case HEDONIST: return "Hedonist";
            case ACCUMULATION: return "Accumulation";
            case CUSTOM: return "Custom";
            default: return "";
        }
    }

    private String tickFrequencyToString(Integer frequency){
        switch (frequency) {
            case 1: return "Every Tick";
            case 2: return "Every Other Tick";
            case 5: return "Every 5 Ticks";
            case 10: return "Every 10 Ticks";
            case 20: return "Every Second";
            default: return "Every " + Float.toString(frequency / 20f)+ " Seconds";
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
    public void close() {
        connectResponse.setValue("");
        this.mc.displayGuiScreen(this.previous);
        MinegasmConfig.save();
    }

    @Override
    public void render(int i, int j, float f) {
        if (pauseMenu)
            this.drawWorldBackground(1);
        else
            this.drawDefaultBackground();
        drawCenteredString(this.fontRenderer, "Minegasm Config", this.width / 2, 15, 0xFFFFFF);
        this.wsHost.drawTextField(i, j, f);
        super.render(i, j, f);
    }
    
    @Override
    public boolean keyPressed(int i, int j, int k)
    {
        LOGGER.info("Pressed");
        this.wsHost.keyPressed(i, j, k);
        return super.keyPressed(i, j, k);
    }
    
    @Override
    public boolean charTyped(char i, int j)
    {
        this.wsHost.charTyped(i, j);
        return super.charTyped(i, j);
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        this.wsHost.mouseClicked(x, y, btn);
        return super.mouseClicked(x, y, btn);
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

        public void setValue(String value)
        {
            text = new TextComponentString(value);
        }

        @Override
        public void render(int i, int j, float f) {
            if (text == null || text.getString().isEmpty())
                return;

            drawCenteredString(Minecraft.getInstance().fontRenderer, text.getString(), Minecraft.getInstance().currentScreen.width / 2, this.y + this.height / 4, 0xFFFFFF);
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
    protected void initGui() {
        try{
        this.addButton(new GuiButtonExtraExt(
                this.width / 2 + 5, this.height - 27, 150, 20,
                ("Done"), button -> this.close()));

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
        
        this.addButton(new GuiButtonExtraExt(
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
    public void close() {
        IntensitiySliderBar.sliders.clear();
        this.mc.displayGuiScreen(this.previous);
    }

    @Override
    public void render(int i, int j, float f) {
        if (pauseMenu)
            this.drawWorldBackground(1);
        else
            this.drawDefaultBackground();
        drawCenteredString(this.fontRenderer, "Minegasm Custom Config", this.width / 2, 15, 0xFFFFFF);
        super.render(i, j, f);
    }
}

class IntensitiySliderBar extends GuiSlider
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static ArrayList<IntensitiySliderBar> sliders = new ArrayList<IntensitiySliderBar>();
    private Field fieldReference;
    
    IntensitiySliderBar(CustomModeConfigScreen parent, String prefix, Field field) throws Exception
    {            
        super(1, parent.width / 2 + (sliders.size() % 2 == 1 ? 5 : -155), // x pos
            parent.height / 6 + 25 * (int)Math.floor(sliders.size() / 2), // y pos 
            150, 20, // Width, height
            prefix, // Prefix
            "", // Suffix
            0, 100, field.getInt(null), false, true); // Min, Max, Default value, percision, drawstring
        fieldReference = field;
        sliders.add(this);
        
        this.parent = (slider) -> {        
            try {
                fieldReference.set(null, slider.getValueInt());
            } catch (Throwable e) {
                LOGGER.throwing(e);
            }
        };
    }

    public static void refreshAllValues()
    {
        for (IntensitiySliderBar slider : sliders)
        {
            slider.refreshValue();
        }
    }

    private void refreshValue()
    {
        try {
            this.setValue(fieldReference.getInt(null));
        } catch (Throwable e) {
            LOGGER.throwing(e);
        }
    }
}