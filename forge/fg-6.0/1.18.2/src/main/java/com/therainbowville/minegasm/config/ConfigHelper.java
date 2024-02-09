package com.therainbowville.minegasm.config;

public final class ConfigHelper {

    public static void bakeClient() {
        MinegasmConfig.serverUrl = ConfigHolder.CLIENT.serverUrl.get();
        MinegasmConfig.vibrate = ConfigHolder.CLIENT.vibrate.get();
        MinegasmConfig.mode = ConfigHolder.CLIENT.mode.get();
        MinegasmConfig.stealth = ConfigHolder.CLIENT.stealth.get();
        MinegasmConfig.attackIntensity = ConfigHolder.CLIENT.attackIntensity.get();
        MinegasmConfig.hurtIntensity = ConfigHolder.CLIENT.hurtIntensity.get();
        MinegasmConfig.mineIntensity = ConfigHolder.CLIENT.mineIntensity.get();
        MinegasmConfig.xpChangeIntensity = ConfigHolder.CLIENT.xpChangeIntensity.get();
        MinegasmConfig.harvestIntensity = ConfigHolder.CLIENT.harvestIntensity.get();
        MinegasmConfig.vitalityIntensity = ConfigHolder.CLIENT.vitalityIntensity.get();
    }

    public static void bakeServer() {
    }

}
