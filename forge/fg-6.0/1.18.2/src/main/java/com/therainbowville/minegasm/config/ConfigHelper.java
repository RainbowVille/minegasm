package com.therainbowville.minegasm.config;

import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.common.ForgeConfigSpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ConfigHelper {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void bakeClient() {
        MinegasmConfig.serverUrl = ConfigHolder.CLIENT.serverUrl.get();
        MinegasmConfig.vibrate = ConfigHolder.CLIENT.vibrate.get();
        MinegasmConfig.mode = ConfigHolder.CLIENT.mode.get();
        MinegasmConfig.stealth = ConfigHolder.CLIENT.stealth.get();
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
        
        // Needs to sleep inbetween, or it can get confused and reset the config

        ConfigHolder.CLIENT.serverUrl.set(buffer.serverUrl);
        Thread.sleep(5);
        ConfigHolder.CLIENT.vibrate.set(buffer.vibrate);
        Thread.sleep(5);
        ConfigHolder.CLIENT.mode.set(buffer.mode);
        Thread.sleep(5);
        ConfigHolder.CLIENT.stealth.set(buffer.stealth);
        Thread.sleep(5);
        ConfigHolder.CLIENT.attackIntensity.set(buffer.attackIntensity);
        Thread.sleep(5);
        ConfigHolder.CLIENT.hurtIntensity.set(buffer.hurtIntensity);
        Thread.sleep(5);
        ConfigHolder.CLIENT.mineIntensity.set(buffer.mineIntensity);
        Thread.sleep(5);
        ConfigHolder.CLIENT.placeIntensity.set(buffer.placeIntensity);
        Thread.sleep(5);
        ConfigHolder.CLIENT.xpChangeIntensity.set(buffer.xpChangeIntensity);
        Thread.sleep(5);
        ConfigHolder.CLIENT.fishingIntensity.set(buffer.fishingIntensity);
        Thread.sleep(5);
        ConfigHolder.CLIENT.harvestIntensity.set(buffer.harvestIntensity);
        Thread.sleep(5);
        ConfigHolder.CLIENT.vitalityIntensity.set(buffer.vitalityIntensity);
        Thread.sleep(5);
        ConfigHolder.CLIENT.advancementIntensity.set(buffer.advancementIntensity);
        } catch (Throwable e)
        {}
    }
    
    public static ConfigGuiHandler.ConfigGuiFactory createConfigGuiFactory() {
        return new ConfigGuiHandler.ConfigGuiFactory((minecraft, screen) -> new ConfigScreen(screen));
    }

}
