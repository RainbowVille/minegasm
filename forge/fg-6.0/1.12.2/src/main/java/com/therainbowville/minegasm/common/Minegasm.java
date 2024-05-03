package com.therainbowville.minegasm.common;

import com.therainbowville.minegasm.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Minegasm.MOD_ID, name = Minegasm.NAME, version = Minegasm.MODVERSION)
public class Minegasm {
    public static final String MOD_ID = "minegasm";
    public static final String NAME = "Minegasm";
    public static final String MODVERSION = "0.2.2";

    @SidedProxy(clientSide = "com.therainbowville.minegasm.client.ClientProxy", serverSide = "com.therainbowville.minegasm.server.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Minegasm instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("PreInit...");
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Init...");
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("PostInit...");
        proxy.postInit(event);
    }
}