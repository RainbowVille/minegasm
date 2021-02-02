package com.therainbowville.minegasm.config;

import com.therainbowville.minegasm.common.Minegasm;
import net.minecraftforge.fml.config.ModConfig;

public final class ConfigHelper {

    public static void bakeClient(final ModConfig config) {
        MinegasmConfig.serverUrl = ConfigHolder.CLIENT.serverUrl.get();
        MinegasmConfig.clientBoolean = ConfigHolder.CLIENT.clientBoolean.get();
        MinegasmConfig.modelTranslucency = ConfigHolder.CLIENT.modelTranslucency.get();
        MinegasmConfig.modelScale = ConfigHolder.CLIENT.modelScale.get().floatValue();
    }

    public static void bakeServer(final ModConfig config) {
        MinegasmConfig.serverBoolean = ConfigHolder.SERVER.serverBoolean.get();
        MinegasmConfig.electricFurnaceEnergySmeltCostPerTick = ConfigHolder.SERVER.electricFurnaceEnergySmeltCostPerTick.get();
        MinegasmConfig.heatCollectorTransferAmountPerTick = ConfigHolder.SERVER.heatCollectorTransferAmountPerTick.get();
    }

}