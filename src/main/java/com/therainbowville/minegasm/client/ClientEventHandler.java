package com.therainbowville.minegasm.client;

import com.mojang.authlib.GameProfile;
import com.therainbowville.minegasm.common.Minegasm;
import com.therainbowville.minegasm.config.ClientConfig;
import com.therainbowville.minegasm.config.MinegasmConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Minegasm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    private static String playerName = null;
    private static UUID playerID = null;
    private static int tickCounter = -1;
    private static int clientTickCounter = -1;
    private static double[] state = new double[1200];
    private static boolean paused = false;

    private static void clearState() {
        playerName = null;
        playerID = null;
        tickCounter = -1;
        clientTickCounter = -1;
        Arrays.fill(state, 0);
        paused = false;
    }

    private static int getStateCounter() {
        return tickCounter / 20;
    }

    private static void setState(int start, int duration, int intensity, boolean decay) {
        if (duration <= 0) {
            return;
        }

        if (decay) {
            int safeDuration = Math.max(0, duration - 2);
            for (int i = 0; i < safeDuration; i++) {
                setState(start+i, intensity);
            }
            setState(start+safeDuration, intensity/2);
            setState(start+safeDuration+1, intensity/4);
        } else {
            for (int i = 0; i < duration; i++) {
                setState(start+i, intensity);
            }
        }
    }

    private static void setState(int counter, int intensity) {
        setState(counter, intensity, false);
    }

    private static void setState(int counter, int intensity, boolean accumulate) {
        int safeCounter = (counter+1) % state.length;
        if (accumulate) {
            state[safeCounter] = Math.min(1.0, state[safeCounter] + (intensity / 100.0));
        } else {
            state[safeCounter] = Math.min(1.0, Math.max(state[safeCounter], (intensity / 100.0)));
        }
    }

    private static int getIntensity(String type) {
        Map<String, Integer> normal = new HashMap<String, Integer>();
        normal.put("attack", 60);
        normal.put("hurt", 0);
        normal.put("mine", 80);
        normal.put("xpChange", 100);
        normal.put("harvest", 0);
        normal.put("vitality", 0);

        Map<String, Integer> masochist = new HashMap<String, Integer>();
        masochist.put("attack", 0);
        masochist.put("hurt", 100);
        masochist.put("mine", 0);
        masochist.put("xpChange", 0);
        masochist.put("harvest", 0);
        masochist.put("vitality", 10);

        Map<String, Integer> hedonist = new HashMap<String, Integer>();
        hedonist.put("attack", 60);
        hedonist.put("hurt", 10);
        hedonist.put("mine", 80);
        hedonist.put("xpChange", 100);
        hedonist.put("harvest", 20);
        hedonist.put("vitality", 10);

        Map<String, Integer> custom = new HashMap<String, Integer>();
        custom.put("attack", MinegasmConfig.attackIntensity);
        custom.put("hurt", MinegasmConfig.hurtIntensity);
        custom.put("mine", MinegasmConfig.mineIntensity);
        custom.put("xpChange", MinegasmConfig.xpChangeIntensity);
        custom.put("harvest", MinegasmConfig.harvestIntensity);
        custom.put("vitality", MinegasmConfig.vitalityIntensity);

        if (MinegasmConfig.mode.equals(ClientConfig.GameplayMode.MASOCHIST)) {
            return masochist.get(type);
        } else if (MinegasmConfig.mode.equals(ClientConfig.GameplayMode.HEDONIST)) {
            return hedonist.get(type);
        } else if (MinegasmConfig.mode.equals(ClientConfig.GameplayMode.CUSTOM)) {
            return custom.get(type);
        } else {
            return normal.get(type);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tickCounter = (tickCounter + 1) % 24000;

            if (tickCounter % 20 == 0) { // every 1 sec
                int stateCounter = getStateCounter();

                if (MinegasmConfig.mode.equals(ClientConfig.GameplayMode.MASOCHIST)) {
                    if (event.player.getHealth() > 0 && event.player.getHealth() <= 1) {
                        setState(stateCounter, getIntensity("vitality"));
                    }
                } else if (event.player.getHealth() >= 20 && event.player.getFoodStats().getFoodLevel() >= 20) {
                    setState(stateCounter, getIntensity("vitality"));
                }

                double newVibrationLevel = state[stateCounter];
                state[stateCounter] = 0;

                LOGGER.debug("Tick " + stateCounter + ": " + newVibrationLevel);

                if (ToyController.currentVibrationLevel != newVibrationLevel) {
                    ToyController.setVibrationLevel(newVibrationLevel);
                }
            }

            if (tickCounter % (5 * 20) == 0) { // 5 secs
                LOGGER.debug("Idle: " + event.player.getIdleTime());
                LOGGER.debug("Health: " + event.player.getHealth());
                LOGGER.debug("Food: " + event.player.getFoodStats().getFoodLevel());
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
                        ToyController.setVibrationLevel(0);
                    }

                    if (paused) {
                        //LOGGER.debug("Paused");
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event)
    {
        Entity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            GameProfile profile = player.getGameProfile();

            if (profile.getId().equals(playerID)) {
                setState(getStateCounter(), 3, getIntensity("attack"), true);
            }
        }
    }

    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event)
    {
        LOGGER.debug("Critical: " + event.isVanillaCritical());
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event)
    {
        Entity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            GameProfile profile = player.getGameProfile();

            if (profile.getId().equals(playerID)) {
                setState(getStateCounter(), 3, getIntensity("hurt"), true);
            }
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event)
    {
        Entity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            GameProfile profile = player.getGameProfile();

            if (profile.getId().equals(playerID)) {
                ToyController.setVibrationLevel(0);
            }
        }
    }

    @SubscribeEvent
    public static void onHarvest(PlayerEvent.HarvestCheck event)
    {
        float blockHardness = event.getTargetBlock().getBlock().getDefaultState().getBlockHardness(null, null);
        LOGGER.debug("Harvest: " + event.canHarvest() + "[" + blockHardness + "]");

        if (event.canHarvest()) {
            setState(getStateCounter(), 1, Math.toIntExact(Math.round((getIntensity("harvest")/100.0 * (blockHardness/50.0)) * 100)), false);
        }
    }

    @SubscribeEvent
    public static void onBreaking(PlayerEvent.BreakSpeed event)
    {
        //LOGGER.info("OLD SPEED" + event.getOriginalSpeed());
        //LOGGER.info("NEW SPEED" + event.getNewSpeed());
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event)
    {
        float blockHardness = event.getState().getBlock().getDefaultState().getBlockHardness(null, null);
        LOGGER.info("Breaking: " + event.getState().getBlock().toString());

        ItemStack mainhandItem = event.getPlayer().getHeldItemMainhand();
        boolean usingPickaxe = mainhandItem.getItem().getTranslationKey().contains("pickaxe");
        LOGGER.debug(mainhandItem);

        if (usingPickaxe) {
            int duration = 5; //TODO set depending on block
            setState(getStateCounter(), duration, Math.toIntExact(Math.round((getIntensity("mine") / 100.0 * (blockHardness / 50.0)) * 100)), true);
        }

        LOGGER.info("XP to drop: " + event.getExpToDrop());
    }

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event)
    {
        LOGGER.info("Pickup item: " + event.getItem().toString());
    }

    @SubscribeEvent
    public static void onXpPickup(PlayerXpEvent.PickupXp event)
    {
        //LOGGER.info("Pickup XP: " + event.getOrb().xpValue);
    }

    @SubscribeEvent
    public static void onXpChange(PlayerXpEvent.XpChange event)
    {
        int xpChange = event.getAmount();
        long duration = Math.round(Math.ceil(Math.log(xpChange+0.5)));

        LOGGER.info("XP CHANGE: " + xpChange);
        LOGGER.debug("duration: " + duration);

        setState(getStateCounter(), Math.toIntExact(duration), getIntensity("xpChange"), true);
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        clearState();
        ToyController.setVibrationLevel(0);
        populatePlayerInfo();
    }

    private static void populatePlayerInfo() {
        GameProfile profile = Minecraft.getInstance().getSession().getProfile();
        playerName = profile.getName();
        playerID = profile.getId();
        System.out.println("Current player: " + playerName + " " + playerID.toString());
    }

    @SubscribeEvent
    public static void onWorldLoaded(WorldEvent.Load event) {
        IWorld world = event.getWorld();
        System.out.println("World loaded: " + world.toString());

        populatePlayerInfo();
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
                        setState(getStateCounter(), 5);
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

