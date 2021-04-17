package com.therainbowville.minegasm.common;

import com.therainbowville.minegasm.config.ConfigHolder;
import com.therainbowville.minegasm.config.ConfigScreen;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Minegasm.MOD_ID)
public class Minegasm
{
    public static final String MOD_ID = "minegasm";
    public static final String NAME = "Minegasm";
    private static final Logger LOGGER = LogManager.getLogger();

    public Minegasm() {
        ModLoadingContext context = ModLoadingContext.get();
        context.registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        context.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
        context.registerConfig(ModConfig.Type.SERVER, ConfigHolder.SERVER_SPEC);

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setupCommon);
        eventBus.addListener(this::setupClient);

        context.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> new ConfigScreen(screen));
    }

    private void setupCommon(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Common setup...");
    }

    private void setupClient(final FMLClientSetupEvent event) {
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }
}
