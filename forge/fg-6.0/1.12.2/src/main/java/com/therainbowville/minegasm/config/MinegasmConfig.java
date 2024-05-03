package com.therainbowville.minegasm.config;

import com.therainbowville.minegasm.common.Minegasm;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

@Config(modid = Minegasm.MOD_ID)
public class MinegasmConfig {
    private static final Logger LOGGER = LogManager.getLogger();
    
    static final String DEFAULT_SERVER_URL = "ws://localhost:12345/buttplug";
    static final boolean DEFAULT_VIBRATE = true;
    static final GameplayMode DEFAULT_MODE = GameplayMode.NORMAL;
    static final boolean DEFAULT_STEALTH = false;
    
    static final int DEFAULT_ATTACK_INTENSITY = 60;
    static final int DEFAULT_HURT_INTENSITY = 0;
    static final int DEFAULT_MINE_INTENSITY = 80;
    static final int DEFAULT_PLACE_INTENSITY = 20;
    static final int DEFAULT_XP_CHANGE_INTENSITY = 100;
    static final int DEFAULT_FISHING_INTENSITY = 50;
    static final int DEFAULT_HARVEST_INTENSITY = 0;
    static final int DEFAULT_VITALITY_INTENSITY = 0;
    static final int DEFAULT_ADVANCEMENT_INTENSITY = 100;

    public static ServerUrlConfig serverUrl = new ServerUrlConfig();

    public static class ServerUrlConfig {
        @Comment("The URL of the Intiface/Buttplug server")
        public String serverUrl;

        public ServerUrlConfig() {
            this.serverUrl = DEFAULT_SERVER_URL;
        }
    }

    @Comment("Enable vibration")
    public static boolean vibrate = true;
    
    @Comment("Game mode")
    public static GameplayMode mode = GameplayMode.NORMAL;
    
    public static boolean stealth = false;
    
    public static TickFrequencyOptions tickFrequency = TickFrequencyOptions.EVERY_TICK;
    
    @Ignore
    public static int ticksPerSecond = Math.max(1, Math.toIntExact(20 / tickFrequency.getInt()));

    public static void updateTicksPerSecond() {
        ticksPerSecond = Math.max(1, Math.toIntExact(20 / tickFrequency.getInt()));
    }
    
    @Comment("Intensity configurations for the CUSTOM game mode")
    public static IntensityConfig intensity = new IntensityConfig();
    
    public static class IntensityConfig {
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int attackIntensity;
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int hurtIntensity;
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int mineIntensity;
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int placeIntensity;
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int xpChangeIntensity;
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int fishingIntensity;
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int harvestIntensity;
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int vitalityIntensity;
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int advancementIntensity;
        
        public IntensityConfig(){
            resetConfigValues();
        }
        
        public void resetConfigValues()
        {
            this.attackIntensity = DEFAULT_ATTACK_INTENSITY;
            this.hurtIntensity = DEFAULT_HURT_INTENSITY;
            this.mineIntensity = DEFAULT_MINE_INTENSITY;
            this.placeIntensity = DEFAULT_PLACE_INTENSITY;
            this.xpChangeIntensity = DEFAULT_XP_CHANGE_INTENSITY;
            this.fishingIntensity = DEFAULT_FISHING_INTENSITY;
            this.harvestIntensity = DEFAULT_HARVEST_INTENSITY;
            this.vitalityIntensity = DEFAULT_VITALITY_INTENSITY;
            this.advancementIntensity = DEFAULT_ADVANCEMENT_INTENSITY;
        }
    }
    
    public static void resetConfigUrl()
    {
        serverUrl.serverUrl = DEFAULT_SERVER_URL;
    }
    
    public static void resetConfigCustom()
    {
        intensity.attackIntensity = DEFAULT_ATTACK_INTENSITY;
        intensity.hurtIntensity = DEFAULT_HURT_INTENSITY;
        intensity.mineIntensity = DEFAULT_MINE_INTENSITY;
        intensity.placeIntensity = DEFAULT_PLACE_INTENSITY;
        intensity.xpChangeIntensity = DEFAULT_XP_CHANGE_INTENSITY;
        intensity.fishingIntensity = DEFAULT_FISHING_INTENSITY;
        intensity.harvestIntensity = DEFAULT_HARVEST_INTENSITY;
        intensity.vitalityIntensity = DEFAULT_VITALITY_INTENSITY;
        intensity.advancementIntensity = DEFAULT_ADVANCEMENT_INTENSITY;
    }

    @Mod.EventBusSubscriber(modid = Minegasm.MOD_ID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Minegasm.MOD_ID)) {
                ConfigManager.sync(Minegasm.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
    
    public enum GameplayMode {
        NORMAL("gui." + Minegasm.MOD_ID + ".config.mode.normal"),
        MASOCHIST("gui." + Minegasm.MOD_ID + ".config.mode.masochist"),
        HEDONIST("gui." + Minegasm.MOD_ID + ".config.mode.hedonist"),
        ACCUMULATION("gui." + Minegasm.MOD_ID + ".config.mode.accumulation"),
        CUSTOM("gui." + Minegasm.MOD_ID + ".config.mode.custom");

        private final String translateKey;

        GameplayMode(String translateKey) {
            this.translateKey =
                Objects.requireNonNull(translateKey, "translateKey");
        }

        public String getTranslateKey() {
            return this.translateKey;
        }
        
        public GameplayMode next()
        {
            boolean foundCurrent = false;
            for (GameplayMode type : values())
            {
                if (foundCurrent)
                    return type;
                else if (this == type) 
                    foundCurrent = true;
            }
            return values()[0];
        }
        
    }
    
    public enum TickFrequencyOptions {
        EVERY_TICK(1), 
        EVERY_OTHER_TICK(2), 
        EVERY_5_TICKS(5), 
        EVERY_10_TICKS(10), 
        EVERY_20_TICKS(20), 
        EVERY_30_TICKS(30), 
        EVERY_40_TICKS(40), 
        EVERY_50_TICKS(50);
        
        private int value;
        
        TickFrequencyOptions(int value) {
            this.value = value;
        }
        
        public int getInt()
        {
            return value;
        }
        
        public static TickFrequencyOptions fromInt(int value)
        {
            for (TickFrequencyOptions type : values()) {
                if (type.getInt() == value) {
                    return type;
                }
            }
            return null;
        }
        
        public TickFrequencyOptions next()
        {
            boolean foundCurrent = false;
            for (TickFrequencyOptions type : values())
            {
                if (foundCurrent)
                    return type;
                else if (this == type) 
                    foundCurrent = true;
            }
            return values()[0];
        }
    }

    // Server
    // -- none at the moment


}