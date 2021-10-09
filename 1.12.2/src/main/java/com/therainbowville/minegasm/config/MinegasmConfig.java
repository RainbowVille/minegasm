package com.therainbowville.minegasm.config;

import com.therainbowville.minegasm.Minegasm;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Minegasm.MOD_ID)
public final class MinegasmConfig {
    @Comment("Enable vibration")
    public static boolean vibrate = true;

    public enum Mode {
        NORMAL,
        MASOCHIST,
        HEDONIST,
        CUSTOM
    }

    @Comment("Game mode")
    public static Mode mode = Mode.NORMAL;

    @Comment("Intensity configurations for the CUSTOM game mode")
    @Config.Name("Custom Mode")
    public static CustomModeCfg customMode = new CustomModeCfg(60, 0, 0, 80, 0, 100);

    public static class CustomModeCfg {
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int attack;
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int harvest;
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int vitality;
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int mine;
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int hurt;
        @Config.RangeInt(min = 0, max = 100)
        @Config.SlidingOption
        public int xpChange;

        public CustomModeCfg(final int attack, final int harvest, final int vitality, final int mine, final int hurt, final int xpChange) {
            this.attack = attack;
            this.harvest = harvest;
            this.vitality = vitality;
            this.mine = mine;
            this.hurt = hurt;
            this.xpChange = xpChange;
        }
    }

    @Comment("Configurations for the Buttplug library used to control the toys")
    public static ButtplugCfg buttplug = new ButtplugCfg("ws://localhost:12345/buttplug");

    public static class ButtplugCfg {
        @Comment("The URL of the Intiface/Buttplug server")
        public String serverUrl;

        public ButtplugCfg(final String serverUrl) {
            this.serverUrl = serverUrl;
        }
    }

    @Mod.EventBusSubscriber(modid = Minegasm.MOD_ID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Minegasm.MOD_ID)) {
                ConfigManager.sync(Minegasm.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
