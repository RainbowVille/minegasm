package com.therainbowville.minegasm.client;

import com.mojang.authlib.GameProfile;
import com.sun.media.jfxmedia.logging.Logger;
import com.therainbowville.minegasm.common.Minegasm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;

import java.util.Arrays;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Minegasm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    private static String playerName = null;
    private static UUID playerID = null;
    private static int tickCounter = -1;
    private static int clientTickCounter = -1;
    private static double[] toyLevel = new double[1200];
    private static boolean paused = false;

    private static void clearState() {
        playerName = null;
        playerID = null;
        tickCounter = -1;
        clientTickCounter = -1;
        Arrays.fill(toyLevel, 0);
        paused = false;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tickCounter = (tickCounter + 1) % 24000;

            if (tickCounter % 20 == 0) {
                LOGGER.debug("Tick " + tickCounter / 20 + ": " + toyLevel[tickCounter / 20]);
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (tickCounter >= 0) {
                if (tickCounter != clientTickCounter) {
                    clientTickCounter = tickCounter;
                    paused = false;
                } else {
                    if (!paused) {
                        paused = true;
                        LOGGER.debug("Pausing");
                        // stop vibrate
                    }

                    if (paused) {
                        LOGGER.debug("Paused");
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event)
    {
        Entity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            GameProfile profile = player.getGameProfile();

            if (profile.getId().equals(playerID)) {
                ToyController.vibrate();
            }
        }
    }

    @SubscribeEvent
    public static void onHarvest(PlayerEvent.HarvestCheck event)
    {
        LOGGER.info(event.canHarvest());
        LOGGER.info(event.getTargetBlock().getBlockState().getValues().toString());
    }

    @SubscribeEvent
    public static void onBreaking(PlayerEvent.BreakSpeed event)
    {
        LOGGER.info("OLD SPEED" + event.getOriginalSpeed());
        //LOGGER.info("NEW SPEED" + event.getNewSpeed());
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event)
    {
        LOGGER.info(event.getState().toString());
        LOGGER.info(event.getExpToDrop());
    }

    @SubscribeEvent
    public static void onXpChange(PlayerXpEvent.XpChange event)
    {
        LOGGER.info("XP CHANGE" + event.getAmount());
    }

    @SubscribeEvent
    public static void onWorldLoaded(WorldEvent.Load event) {
        IWorld world = event.getWorld();
        System.out.println("World loaded: " + world.toString());

        GameProfile profile = Minecraft.getInstance().getSession().getProfile();
        playerName = profile.getName();
        playerID = profile.getId();
        System.out.println("Current player: " + playerName + " " + playerID.toString());
    }

    @SubscribeEvent
    public static void onWorldEntry(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ClientPlayerEntity) {
            System.out.println("Entered world: " + entity.toString());

            if (playerName != null) {
                PlayerEntity player = (PlayerEntity) entity;
                GameProfile profile = player.getGameProfile();

                if (profile.getId().equals(playerID)) {
                    System.out.println("Player in: " + playerName + " " + playerID.toString());
                    if (ToyController.connectDevice()) {
                        ToyController.vibrate();
                        player.sendStatusMessage(new StringTextComponent(String.format("Connected to %s [%d]", ToyController.getDeviceName(), ToyController.getDeviceId())), true);
                    } else {
                        player.sendStatusMessage(new StringTextComponent(String.format(TextFormatting.YELLOW + "Minegasm " + TextFormatting.RESET + "failed to start\n%s", ToyController.getLastErrorMessage())), false);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWorldExit(EntityLeaveWorldEvent event) {
        Entity entity = event.getEntity();
        if ((entity instanceof PlayerEntity) && (playerName != null)) {
            clearState();
        }
    }
}

