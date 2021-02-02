package com.therainbowville.minegasm.config;

import com.therainbowville.minegasm.common.Minegasm;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

final class ClientConfig {
    final ForgeConfigSpec.ConfigValue<String> serverUrl;
    final ForgeConfigSpec.BooleanValue clientBoolean;
    final ForgeConfigSpec.BooleanValue modelTranslucency;
    final ForgeConfigSpec.DoubleValue modelScale;

    ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        serverUrl = builder
                .translation(Minegasm.MOD_ID + ".config.serverUrl")
                .define("serverUrl", "ws://localhost:12345/buttplug");
        clientBoolean = builder
                .comment("An example boolean in the client config")
                .translation(Minegasm.MOD_ID + ".config.clientBoolean")
                .define("clientBoolean", true);
        modelTranslucency = builder
                .comment("If the model should be rendered translucent")
                .translation(Minegasm.MOD_ID + ".config.modelTranslucency")
                .define("modelTranslucency", true);
        modelScale = builder
                .comment("The scale to render the model at")
                .translation(Minegasm.MOD_ID + ".config.modelScale")
                .defineInRange("modelScale", 0.0625F, 0.0001F, 100F);
        builder.pop();
    }

}