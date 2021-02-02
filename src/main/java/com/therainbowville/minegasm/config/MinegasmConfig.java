package com.therainbowville.minegasm.config;

import com.therainbowville.minegasm.common.Minegasm;
import net.minecraft.item.DyeColor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


@Mod.EventBusSubscriber(modid = Minegasm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinegasmConfig {
    private static final Logger LOGGER = LogManager.getLogger();

    // Client
    public static String serverUrl;
    public static boolean clientBoolean;
    public static List<String> clientStringList;
    public static DyeColor clientDyeColorEnum;

    public static boolean modelTranslucency;
    public static float modelScale;

    // Server
    public static boolean serverBoolean;
    public static List<String> serverStringList;
    public static DyeColor serverEnumDyeColor;

    public static int electricFurnaceEnergySmeltCostPerTick = 100;
    public static int heatCollectorTransferAmountPerTick = 100;

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Rebake the configs when they change
        if (config.getSpec() == ConfigHolder.CLIENT_SPEC) {
            ConfigHelper.bakeClient(config);
            LOGGER.debug("Baked client config");
        } else if (config.getSpec() == ConfigHolder.SERVER_SPEC) {
            ConfigHelper.bakeServer(config);
            LOGGER.debug("Baked server config");
        }
    }
}
