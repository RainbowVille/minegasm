package com.therainbowville.minegasm.common;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.therainbowville.minegasm.config.ConfigHolder;
import com.therainbowville.minegasm.config.ConfigHelper;
import com.therainbowville.minegasm.config.ConfigScreen;

@Mod(Minegasm.MOD_ID)
public class Minegasm
{
    public static final String MOD_ID = "minegasm";
    public static final String NAME = "Minegasm";
    private static final Logger LOGGER = LogManager.getLogger();

    public Minegasm(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
        container.registerConfig(ModConfig.Type.SERVER, ConfigHolder.SERVER_SPEC);

        NeoForge.EVENT_BUS.register(this);
    }
}
