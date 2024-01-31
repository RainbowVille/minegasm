package com.therainbowville.minegasm.client;

import com.therainbowville.minegasm.common.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

public class ClientProxy extends CommonProxy {
    public static Logger logger;

    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}