package com.therainbowville.minegasm.client;

import com.therainbowville.minegasm.common.CommonProxy;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    public static Logger logger;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        super.preInit(event);
        logger.info("Client PreInit...");
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        logger.info("Client RegisterModels...");
    }
}