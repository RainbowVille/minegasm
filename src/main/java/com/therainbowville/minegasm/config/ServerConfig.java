package com.therainbowville.minegasm.config;

import com.therainbowville.minegasm.common.Minegasm;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

final class ServerConfig {

    ServerConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        builder.pop();
    }
}