package com.therainbowville.minegasm.config;

import com.therainbowville.minegasm.common.Minegasm;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.mclanguageprovider.MinecraftModLanguageProvider;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import java.util.Objects;

public final class ClientConfig {
    final ForgeConfigSpec.ConfigValue<String> serverUrl;

    final ForgeConfigSpec.BooleanValue vibrate;
    final ForgeConfigSpec.EnumValue<GameplayMode> mode;
    final ForgeConfigSpec.BooleanValue stealth;
    final ForgeConfigSpec.IntValue attackIntensity;
    final ForgeConfigSpec.IntValue hurtIntensity;
    final ForgeConfigSpec.IntValue mineIntensity;
    final ForgeConfigSpec.IntValue placeIntensity;
    final ForgeConfigSpec.IntValue xpChangeIntensity;
    final ForgeConfigSpec.IntValue fishingIntensity;
    final ForgeConfigSpec.IntValue harvestIntensity;
    final ForgeConfigSpec.IntValue vitalityIntensity;
    final ForgeConfigSpec.IntValue advancementIntensity;
    
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

    ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("buttplug");

        serverUrl = builder
                .translation(Minegasm.MOD_ID + ".config.serverUrl")
                .define("serverUrl", DEFAULT_SERVER_URL);

        builder.pop();

        builder.push("minegasm");

        vibrate = builder
                .translation(Minegasm.MOD_ID + ".config.vibrate")
                .define("vibrate", DEFAULT_VIBRATE);
        mode = builder
                .translation(Minegasm.MOD_ID + ".config.mode")
                .defineEnum("mode", DEFAULT_MODE);
        stealth = builder
                .translation(Minegasm.MOD_ID + ".config.stealth")
                .define("stealth", DEFAULT_STEALTH);

        builder.push("intensity");

        attackIntensity = builder
                .comment("Vibration intensity when attacking on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.attack")
                .defineInRange("attackIntensity", DEFAULT_ATTACK_INTENSITY, 0, 100);

        hurtIntensity = builder
                .comment("Vibration intensity when hurting on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.hurt")
                .defineInRange("hurtIntensity", DEFAULT_HURT_INTENSITY, 0, 100);

        mineIntensity = builder
                .comment("Vibration intensity when mining on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.mine")
                .defineInRange("mineIntensity", DEFAULT_MINE_INTENSITY, 0, 100);
                
        placeIntensity = builder
                .comment("Vibration intensity when placing blocks on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.place")
                .defineInRange("placeIntensity", DEFAULT_PLACE_INTENSITY, 0, 100);

        xpChangeIntensity = builder
                .comment("Vibration intensity when gaining XP on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.xp_change")
                .defineInRange("xpChangeIntensity", DEFAULT_XP_CHANGE_INTENSITY, 0, 100);
                
        fishingIntensity = builder
                .comment("Vibration intensity when fishing on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.fishing")
                .defineInRange("fishingIntensity", DEFAULT_FISHING_INTENSITY, 0, 100);

        harvestIntensity = builder
                .comment("Vibration intensity when harvesting on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.harvest")
                .defineInRange("harvestIntensity", DEFAULT_HARVEST_INTENSITY, 0, 100);

        vitalityIntensity = builder
                .comment("Vibration intensity on high level of player's vitality on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.vitality")
                .defineInRange("vitalityIntensity", DEFAULT_VITALITY_INTENSITY, 0, 100);
                
        advancementIntensity = builder
                .comment("Vibration intensity on achieving advancement on custom mode")
                .translation(Minegasm.MOD_ID + ".config.intensity.advancement")
                .defineInRange("advancementIntensity", DEFAULT_ADVANCEMENT_INTENSITY, 0, 100);

        builder.pop();
        builder.pop();
    }
    
    public void resetConfigUrl()
    {
        ConfigHolder.CLIENT.serverUrl.set(DEFAULT_SERVER_URL);
    }
    
    public void resetConfigCustom()
    {
        ConfigHolder.CLIENT.attackIntensity.set(DEFAULT_ATTACK_INTENSITY);
        ConfigHolder.CLIENT.hurtIntensity.set(DEFAULT_HURT_INTENSITY);
        ConfigHolder.CLIENT.mineIntensity.set(DEFAULT_MINE_INTENSITY);
        ConfigHolder.CLIENT.placeIntensity.set(DEFAULT_PLACE_INTENSITY);
        ConfigHolder.CLIENT.xpChangeIntensity.set(DEFAULT_XP_CHANGE_INTENSITY);
        ConfigHolder.CLIENT.fishingIntensity.set(DEFAULT_FISHING_INTENSITY);
        ConfigHolder.CLIENT.harvestIntensity.set(DEFAULT_HARVEST_INTENSITY);
        ConfigHolder.CLIENT.vitalityIntensity.set(DEFAULT_VITALITY_INTENSITY);
        ConfigHolder.CLIENT.advancementIntensity.set(DEFAULT_ADVANCEMENT_INTENSITY);
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
