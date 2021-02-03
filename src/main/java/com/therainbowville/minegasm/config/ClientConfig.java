package com.therainbowville.minegasm.config;

import com.therainbowville.minegasm.common.Minegasm;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Objects;

public final class ClientConfig {
    final ForgeConfigSpec.ConfigValue<String> serverUrl;

    final ForgeConfigSpec.BooleanValue vibrate;
    final ForgeConfigSpec.EnumValue<GameplayMode> mode;
    final ForgeConfigSpec.IntValue attackIntensity;
    final ForgeConfigSpec.IntValue hurtIntensity;
    final ForgeConfigSpec.IntValue mineIntensity;
    final ForgeConfigSpec.IntValue xpChangeIntensity;
    final ForgeConfigSpec.IntValue harvestIntensity;
    final ForgeConfigSpec.IntValue vitalityIntensity;

    ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("buttplug");

        serverUrl = builder
                .translation(Minegasm.MOD_ID + ".config.serverUrl")
                .define("serverUrl", "ws://localhost:12345/buttplug");

        builder.pop();

        builder.push("general");

        vibrate = builder
                .comment("Enable vibration")
                .translation(Minegasm.MOD_ID + ".config.vibrate")
                .define(
                        "vibrate", true);
        mode = builder
                .comment("Gameplay mode")
                .translation(Minegasm.MOD_ID + ".config.mode")
                .defineEnum("mode", GameplayMode.NORMAL);

        attackIntensity = builder
                .comment("Vibration intensity when attacking on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.attack")
                .defineInRange("attackIntensity", 60, 0, 100);

        hurtIntensity = builder
                .comment("Vibration intensity when hurting on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.hurt")
                .defineInRange("hurtIntensity", 0, 0, 100);

        mineIntensity = builder
                .comment("Vibration intensity when mining on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.mine")
                .defineInRange("mineIntensity", 80, 0, 100);

        xpChangeIntensity = builder
                .comment("Vibration intensity when gaining XP on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.xp_change")
                .defineInRange("xpChangeIntensity", 100, 0, 100);

        harvestIntensity = builder
                .comment("Vibration intensity when harvesting on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.harvest")
                .defineInRange("harvestIntensity", 0, 0, 100);

        vitalityIntensity = builder
                .comment("Vibration intensity on high level of player's vitality on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.vitality")
                .defineInRange("vitalityIntensity", 0, 0, 100);

        builder.pop();
    }
    public enum GameplayMode {
        NORMAL("gui." + Minegasm.MOD_ID + ".config.mode.normal"),
        MASOCHIST("gui." + Minegasm.MOD_ID + ".config.mode.masochist"),
        HEDONIST("gui." + Minegasm.MOD_ID + ".config.mode.hedonist"),
        CUSTOM("gui." + Minegasm.MOD_ID + ".config.mode.custom");

        private final String translateKey;

        GameplayMode(String translateKey) {
            this.translateKey =
                    Objects.requireNonNull(translateKey, "translateKey");
        }

        public String getTranslateKey() {
            return this.translateKey;
        }
    }
}