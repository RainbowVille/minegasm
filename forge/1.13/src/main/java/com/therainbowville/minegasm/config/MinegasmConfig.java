package com.therainbowville.minegasm.config;

import com.therainbowville.minegasm.common.Minegasm;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = Minegasm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinegasmConfig {
    private static final Logger LOGGER = LogManager.getLogger();

    // Client
    public static String serverUrl;

    public static boolean vibrate;
    public static Enum<ClientConfig.GameplayMode> mode;
    public static int attackIntensity;
    public static int hurtIntensity;
    public static int mineIntensity;
    public static int xpChangeIntensity;
    public static int harvestIntensity;
    public static int vitalityIntensity;

    // Server
    // -- none at the moment

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Re-bake the configs when they change
        if (config.getSpec() == ConfigHolder.CLIENT_SPEC) {
            ConfigHelper.bakeClient();
            LOGGER.debug("Baked client config");
        } else if (config.getSpec() == ConfigHolder.SERVER_SPEC) {
            ConfigHelper.bakeServer();
            LOGGER.debug("Baked server config");
        }
    }
}
