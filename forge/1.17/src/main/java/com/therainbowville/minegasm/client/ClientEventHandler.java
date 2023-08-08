package com.therainbowville.minegasm.client;

import com.therainbowville.minegasm.common.Minegasm;
import com.therainbowville.minegasm.config.ClientConfig;
import com.therainbowville.minegasm.config.MinegasmConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ToolAction;
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
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;


@Mod.EventBusSubscriber(modid = Minegasm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    private static final int TICKS_PER_SECOND = 20;
    private static int tickCounter = -1;
    private static int clientTickCounter = -1;
    private static final double[] state = new double[1200];
    private static boolean paused = false;
    private static UUID playerId = null;


    private static void clearState() {
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
        boolean accumulate = false; //XXX reserved for future use
        setState(counter, intensity, accumulate);
    }

    private static void setState(int counter, int intensity, boolean accumulate) {
        int safeCounter = counter % state.length;
        if (accumulate) {
            state[safeCounter] = Math.min(1.0, state[safeCounter] + (intensity / 100.0));
        } else {
            state[safeCounter] = Math.min(1.0, Math.max(state[safeCounter], (intensity / 100.0)));
        }
    }

    private static int getIntensity(String type) {
        Map<String, Integer> normal = new HashMap<>();
        normal.put("attack", 60);
        normal.put("hurt", 0);
        normal.put("mine", 80);
        normal.put("xpChange", 100);
        normal.put("harvest", 0);
        normal.put("vitality", 0);

        Map<String, Integer> masochist = new HashMap<>();
        masochist.put("attack", 0);
        masochist.put("hurt", 100);
        masochist.put("mine", 0);
        masochist.put("xpChange", 0);
        masochist.put("harvest", 0);
        masochist.put("vitality", 10);

        Map<String, Integer> hedonist = new HashMap<>();
        hedonist.put("attack", 60);
        hedonist.put("hurt", 10);
        hedonist.put("mine", 80);
        hedonist.put("xpChange", 100);
        hedonist.put("harvest", 20);
        hedonist.put("vitality", 10);

        Map<String, Integer> custom = new HashMap<>();
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
        try {
        if (event.phase == TickEvent.Phase.END ) {
            Player player = event.player;;
            UUID uuid = player.getGameProfile().getId();

            float playerHealth = player.getHealth();
            float playerFoodLevel = player.getFoodData().getFoodLevel();

            tickCounter = (tickCounter + 1) % (20 * (60 * TICKS_PER_SECOND)); // 20 min day cycle

            if (tickCounter % TICKS_PER_SECOND == 0) { // every 1 sec
                if (uuid.equals(playerId)) {
                    int stateCounter = getStateCounter();

                    if (MinegasmConfig.mode.equals(ClientConfig.GameplayMode.MASOCHIST)) {
                        if (playerHealth > 0 && playerHealth <= 1) {
                            setState(stateCounter, getIntensity("vitality"));
                        }
                    } else if (playerHealth >= 20 && playerFoodLevel >= 20) {
                        setState(stateCounter, getIntensity("vitality"));
                    }

                    double newVibrationLevel = state[stateCounter];
                    state[stateCounter] = 0;

                    LOGGER.trace("Tick " + stateCounter + ": " + newVibrationLevel);

                    if (ToyController.currentVibrationLevel != newVibrationLevel) {
                        ToyController.setVibrationLevel(newVibrationLevel);
                    }
                }
            }

            if (tickCounter % (5 * TICKS_PER_SECOND) == 0) { // 5 secs
                LOGGER.debug("Health: " + playerHealth);
                LOGGER.debug("Food: " + playerFoodLevel);
            }
        }
        } catch (Throwable e) {
            LOGGER.throwing(e);
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
                        LOGGER.trace("Paused");
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event)
    {
        try {
        Entity entity = event.getEntityLiving();
        if (entity instanceof Player) {
            Player player = (Player) entity;;
            UUID uuid = player.getGameProfile().getId();

            if (uuid.equals(playerId)) {
                setState(getStateCounter(), 3, getIntensity("attack"), true);
            }
        }
    } catch (Throwable e) {
        LOGGER.throwing(e);
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
        try {
        Entity entity = event.getEntityLiving();
        if (entity instanceof Player) {
            Player player = (Player) entity;;
            UUID uuid = player.getGameProfile().getId();

            if (uuid.equals(playerId)) {
                setState(getStateCounter(), 3, getIntensity("hurt"), true);
            }
        }
        } catch (Throwable e) {
            LOGGER.throwing(e);
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event)
    {
        try {
        Entity entity = event.getEntityLiving();
        if (entity instanceof Player) {
            Player player = (Player) entity;;
            UUID uuid = player.getGameProfile().getId();

            if (uuid.equals(playerId)) {
                ToyController.setVibrationLevel(0);
            }
        }
    } catch (Throwable e) {
        LOGGER.throwing(e);
    }
    }

    @SubscribeEvent
    public static void onHarvest(PlayerEvent.HarvestCheck event)
    {
        try {
        Player player = event.getPlayer();;
            UUID uuid = player.getGameProfile().getId();

            if (uuid.equals(playerId)) {
            BlockState blockState = event.getTargetBlock();
            Block block = blockState.getBlock();

            // ToolType. AXE, HOE, PICKAXE, SHOVEL
            @SuppressWarnings("ConstantConditions") float blockHardness = block.defaultBlockState().getDestroySpeed(null, null);
            LOGGER.debug("Harvest: tool: " +
                    "?" +
                    " can harvest? " + event.canHarvest() + " hardness: " + blockHardness);

            int intensity = Math.toIntExact(Math.round((getIntensity("harvest") / 100.0 * (blockHardness / 50.0)) * 100));

            if (event.canHarvest()) {
                setState(getStateCounter(), 1, intensity, false);
            }
        }
        } catch (Throwable e) {
            LOGGER.throwing(e);
        }
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event)
    {
        try {
        Player player = event.getPlayer();;
            UUID uuid = player.getGameProfile().getId();

            if (uuid.equals(playerId)) {
            BlockState blockState = event.getState();
            Block block = blockState.getBlock();
            @SuppressWarnings("ConstantConditions") float blockHardness = block.defaultBlockState().getDestroySpeed(null, null);

            LOGGER.info("Breaking: " + block.toString());

            ItemStack mainhandItem = event.getPlayer().getMainHandItem();
            boolean usingAppropriateTool = mainhandItem.isCorrectToolForDrops(blockState);
            List<ToolAction> toolCan = ToolAction.getActions().stream().filter(a -> mainhandItem.canPerformAction(a)).collect(Collectors.toList());
            LOGGER.debug("mainhand: " + mainhandItem + " [" +  toolCan + "]");
            LOGGER.debug("using pickaxe: " + mainhandItem.toString() + ", using appropriate tool: " + usingAppropriateTool);

            if (toolCan.contains(ToolAction.get("AXE")) && usingAppropriateTool) {
                int duration = Math.max(1, Math.min(5, Math.toIntExact(Math.round(Math.ceil(Math.log(blockHardness + 0.5))))));
                int intensity = Math.toIntExact(Math.round((getIntensity("mine") / 100.0 * (blockHardness / 50.0)) * 100));
                setState(getStateCounter(), duration, intensity, true);
            }

            LOGGER.info("XP to drop: " + event.getExpToDrop());
        }
    } catch (Throwable e) {
        LOGGER.throwing(e);
    }
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
        try {
        Player player = event.getPlayer();;
            UUID uuid = player.getGameProfile().getId();

            if (uuid.equals(playerId)) {
            int xpChange = event.getAmount();
            long duration = Math.round(Math.ceil(Math.log(xpChange + 0.5)));

            LOGGER.info("XP CHANGE: " + xpChange);
            LOGGER.debug("duration: " + duration);

            setState(getStateCounter(), Math.toIntExact(duration), getIntensity("xpChange"), true);
        }
    } catch (Throwable e) {
        LOGGER.throwing(e);
    }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        Entity entity = event.getEntity();
        if( !entity.level.isClientSide() ) {
            return;
        }

        if (entity instanceof Player) {
            LOGGER.info("Client Entered world: " + entity.toString());

            try {
                Player player = (Player) entity;
                UUID uuid = player.getGameProfile().getId();

                if (uuid.equals(Minecraft.getInstance().player.getGameProfile().getId())) {
                    LOGGER.info("Player in: " + player.getGameProfile().getName() + " " + player.getGameProfile().getId().toString());
                    clearState();
                    ToyController.setVibrationLevel(0);
                    playerId = uuid;
                }
            } catch (Throwable e) {
                LOGGER.throwing(e);
            }
        }

    }


    @SubscribeEvent
    public static void onWorldLoaded(WorldEvent.Load event) {

        LevelAccessor world = event.getWorld();
        LOGGER.info("World loaded: " + world.toString());
    }

    @SubscribeEvent
    public static void onWorldEntry(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if( !entity.level.isClientSide() ) {
            return;
        }

        if (entity instanceof Player) {
            LOGGER.info("Player respawn world: " + entity.toString());

            try {
                Player player = (Player) entity;
                UUID uuid = player.getGameProfile().getId();

                if (uuid.equals(Minecraft.getInstance().player.getGameProfile().getId())) {
                    LOGGER.info("Player in: " + player.getGameProfile().getName() + " " + player.getGameProfile().getId().toString());
                    if (ToyController.connectDevice()) {
                        setState(getStateCounter(), 5);
                        player.displayClientMessage(new TextComponent(String.format("Connected to " + ChatFormatting.GREEN + "%s" + ChatFormatting.RESET + " [%d]", ToyController.getDeviceName(), ToyController.getDeviceId())), true);
                    } else {
                        player.displayClientMessage(new TextComponent(String.format(ChatFormatting.YELLOW + "Minegasm " + ChatFormatting.RESET + "failed to start\n%s", ToyController.getLastErrorMessage())), false);
                    }
                    playerId = uuid;
                }
            } catch (Throwable e) {
                LOGGER.throwing(e);
            }
        }
    }
}

