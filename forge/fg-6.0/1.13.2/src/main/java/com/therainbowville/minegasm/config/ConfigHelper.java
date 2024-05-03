package com.therainbowville.minegasm.config;

//import net.minecraftforge.fmlclient.ConfigGuiHandler;
//import net.minecraftforge.common.ForgeConfigSpec;

import net.minecraftforge.fml.config.ModConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.String;
import java.util.List;

public final class ConfigHelper {
    private static final Logger LOGGER = LogManager.getLogger();

    private static ModConfig config;

    public static void setConfig(final ModConfig config)
    {
        ConfigHelper.config = config;
    }

    public static void setConfigValue(java.util.List<java.lang.String> path, Object newValue)
    {
        ConfigHelper.config.getConfigData().set(path, newValue);
    }

    public static void bakeClient() {
        
        MinegasmConfig.serverUrl = ConfigHolder.CLIENT.serverUrl.get();
        MinegasmConfig.vibrate = ConfigHolder.CLIENT.vibrate.get();
        MinegasmConfig.mode = ConfigHolder.CLIENT.mode.get();
        MinegasmConfig.stealth = ConfigHolder.CLIENT.stealth.get();
        MinegasmConfig.tickFrequency = ConfigHolder.CLIENT.tickFrequency.get().getInt();
        MinegasmConfig.ticksPerSecond = Math.max(1, Math.toIntExact(20 / MinegasmConfig.tickFrequency));
        
        MinegasmConfig.attackIntensity = ConfigHolder.CLIENT.attackIntensity.get();
        MinegasmConfig.hurtIntensity = ConfigHolder.CLIENT.hurtIntensity.get();
        MinegasmConfig.mineIntensity = ConfigHolder.CLIENT.mineIntensity.get();
        MinegasmConfig.placeIntensity = ConfigHolder.CLIENT.placeIntensity.get();
        MinegasmConfig.xpChangeIntensity = ConfigHolder.CLIENT.xpChangeIntensity.get();
        MinegasmConfig.fishingIntensity = ConfigHolder.CLIENT.fishingIntensity.get();
        MinegasmConfig.harvestIntensity = ConfigHolder.CLIENT.harvestIntensity.get();
        MinegasmConfig.vitalityIntensity = ConfigHolder.CLIENT.vitalityIntensity.get();
        MinegasmConfig.advancementIntensity = ConfigHolder.CLIENT.advancementIntensity.get();
    }

    public static void bakeServer() {
    }
    
    public static void saveClient()
    {
        try{
        
        MinegasmConfigBuffer buffer = new MinegasmConfigBuffer();
        
        setConfigValue(ConfigHolder.CLIENT.serverUrl.getPath(), buffer.serverUrl);
        setConfigValue(ConfigHolder.CLIENT.vibrate.getPath(), buffer.vibrate);
        setConfigValue(ConfigHolder.CLIENT.mode.getPath(), buffer.mode);
        setConfigValue(ConfigHolder.CLIENT.stealth.getPath(), buffer.stealth);
        setConfigValue(ConfigHolder.CLIENT.tickFrequency.getPath(), ClientConfig.TickFrequencyOptions.fromInt(buffer.tickFrequency));
                
        setConfigValue(ConfigHolder.CLIENT.attackIntensity.getPath(), buffer.attackIntensity);
        setConfigValue(ConfigHolder.CLIENT.hurtIntensity.getPath(), buffer.hurtIntensity);
        setConfigValue(ConfigHolder.CLIENT.mineIntensity.getPath(), buffer.mineIntensity);
        setConfigValue(ConfigHolder.CLIENT.placeIntensity.getPath(), buffer.placeIntensity);
        setConfigValue(ConfigHolder.CLIENT.xpChangeIntensity.getPath(), buffer.xpChangeIntensity);
        setConfigValue(ConfigHolder.CLIENT.fishingIntensity.getPath(), buffer.fishingIntensity);
        setConfigValue(ConfigHolder.CLIENT.harvestIntensity.getPath(), buffer.harvestIntensity);
        setConfigValue(ConfigHolder.CLIENT.vitalityIntensity.getPath(), buffer.vitalityIntensity);
        setConfigValue(ConfigHolder.CLIENT.advancementIntensity.getPath(), buffer.advancementIntensity);
        } catch (Throwable e)
        {
            LOGGER.info(e);
        }
    }

}