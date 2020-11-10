package com.therainbowville.minegasm.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.therainbowville.minegasm.common.MineGasm;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = MineGasm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MineGasmConfig {
    public static void register(final ModLoadingContext context) {
        context.registerConfig(ModConfig.Type.CLIENT, Client.SPEC);
    }

    public static class Client {
        public static final Impl INSTANCE;
        public static final ForgeConfigSpec SPEC;
        public static String serverUrl;

        static {
            final Pair<Impl, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Impl::new);
            SPEC = specPair.getRight();
            INSTANCE = specPair.getLeft();
        }

        public static void bake() {

        }

        static class Impl {

            final ForgeConfigSpec.ConfigValue<String> serverUrl;

            private Impl(final ForgeConfigSpec.Builder builder) {
                serverUrl = builder
                        .translation(MineGasm.MOD_ID + ".config.serverUrl")
                        .define("serverUrl", "ws://localhost:12345/buttplug");
            }

        }

    }
}
