package com.therainbowville.minegasm;

import com.therainbowville.minegasm.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Minegasm.MOD_ID, name = Minegasm.NAME, version = Minegasm.MODVERSION)
public class Minegasm {
    public static final String MOD_ID = "minegasm";
    public static final String NAME = "Minegasm";
    public static final String MODVERSION = "0.2.2";

    @SidedProxy(clientSide = "com.therainbowville.minegasm.client.ClientProxy")
    private static CommonProxy proxy;


    @Mod.Instance
    public static Minegasm instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
}
