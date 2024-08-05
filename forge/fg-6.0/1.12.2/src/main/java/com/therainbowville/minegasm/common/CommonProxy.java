package com.therainbowville.minegasm.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
public class CommonProxy {
    public static Logger logger;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        logger.info("Common RegisterBlocks...");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        logger.info("Common RegisterItems...");
    }

    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Common PreInit...");
    }

    public void init(FMLInitializationEvent event) {
        logger.info("Common Init...");
    }

    public void postInit(FMLPostInitializationEvent event) {
        logger.info("Common PostInit...");
    }
}