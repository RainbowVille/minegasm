package com.therainbowville.minegasm.client;

import com.mojang.authlib.GameProfile;
import com.therainbowville.minegasm.Minegasm;
import com.therainbowville.minegasm.config.MinegasmConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;

@Mod.EventBusSubscriber(modid = Minegasm.MOD_ID, value = CLIENT)
public final class ClientEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();
    private static String playerName = null;
    private static UUID playerID = null;
    private static final int TICKS_PER_SECOND = 20;
    private static final int DAY_CYCLE = 20 * 60; // 20 minutes
    private static int tickCounter = -1;
    private static int clientTickCounter = -1;
    private static final double[] state = new double[DAY_CYCLE];
    private static boolean paused = false;
    private static boolean motdDisplayed = false;

    private static void clearState() {
        playerName = null;
        playerID = null;
        tickCounter = -1;
        clientTickCounter = -1;
        Arrays.fill(state, 0);
        paused = false;
    }

    private static int getStateCounter() {
        return tickCounter / TICKS_PER_SECOND;
    }

    private static void setState(int start, int duration, int intensity, boolean decay) {
        if (duration <= 0) {
            return;
        }

        if (decay) {
            int safeDuration = Math.max(0, duration - 2);
            for (int i = 0; i < safeDuration; i++) {
                setState(start + i, intensity);
            }
            setState(start + safeDuration, intensity / 2);
            setState(start + safeDuration + 1, intensity / 4);
        } else {
            for (int i = 0; i < duration; i++) {
                setState(start + i, intensity);
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
        custom.put("attack", MinegasmConfig.customMode.attack);
        custom.put("hurt", MinegasmConfig.customMode.hurt);
        custom.put("mine", MinegasmConfig.customMode.mine);
        custom.put("xpChange", MinegasmConfig.customMode.xpChange);
        custom.put("harvest", MinegasmConfig.customMode.harvest);
        custom.put("vitality", MinegasmConfig.customMode.vitality);

        if (MinegasmConfig.mode.equals(MinegasmConfig.Mode.MASOCHIST)) {
            return masochist.get(type);
        } else if (MinegasmConfig.mode.equals(MinegasmConfig.Mode.HEDONIST)) {
            return hedonist.get(type);
        } else if (MinegasmConfig.mode.equals(MinegasmConfig.Mode.CUSTOM)) {
            return custom.get(type);
        } else {
            return normal.get(type);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            //LOGGER.debug("TICK");
            EntityPlayer player = event.player;
            if (Thread.currentThread().getThreadGroup() != SidedThreadGroups.CLIENT) {
                //LOGGER.debug("server");
                GameProfile profile = player.getGameProfile();

                if (profile.getId().equals(playerID)) {
                    float playerHealth = player.getHealth();
                    float playerFoodLevel = player.getFoodStats().getFoodLevel();

                    tickCounter = (tickCounter + 1) % (DAY_CYCLE * TICKS_PER_SECOND);

                    if (tickCounter % TICKS_PER_SECOND == 0) { // every 1 sec
                        LOGGER.debug("1");

                        int stateCounter = getStateCounter();

                        /*if (MinegasmConfig.mode.equals(MinegasmConfig.Mode.MASOCHIST)) {
                            if (playerHealth > 0 && playerHealth <= 1) {
                                setState(stateCounter, getIntensity("vitality"));
                            }
                        } else if (playerHealth >= 20 && playerFoodLevel >= 20) {
                            setState(stateCounter, getIntensity("vitality"));
                        }*/

                        double newVibrationLevel = state[stateCounter];
                        state[stateCounter] = 0;

                        LOGGER.trace("Tick " + stateCounter + ": " + newVibrationLevel);

                        if (ToyController.currentVibrationLevel != newVibrationLevel) {
                            ToyController.setVibrationLevel(newVibrationLevel);
                        }

                    }

                    if (tickCounter % (5 * TICKS_PER_SECOND) == 0) { // 5 secs
                        LOGGER.debug("Health: " + playerHealth);
                        LOGGER.debug("Food: " + playerFoodLevel);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        Entity entity = event.getEntityLiving();
        LOGGER.debug("AX");
        LOGGER.debug(entity instanceof EntityPlayer);
        LOGGER.debug((Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER));
        LOGGER.debug((Thread.currentThread().getThreadGroup() == SidedThreadGroups.CLIENT));

        if ((entity instanceof EntityPlayer) && (Thread.currentThread().getThreadGroup() != SidedThreadGroups.CLIENT)) {
            EntityPlayer player = (EntityPlayer) entity;
            GameProfile profile = player.getGameProfile();

            LOGGER.debug("A");
            LOGGER.debug(profile);

            if (profile.getId().equals(playerID)) {
                LOGGER.debug("ATTACK!");
                setState(getStateCounter(), 3, getIntensity("attack"), true);
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingAttackEvent event) {
        Entity entity = event.getEntityLiving();
        LOGGER.debug("HX");
        LOGGER.debug(entity instanceof EntityPlayer);
        LOGGER.debug((Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER));
        LOGGER.debug((Thread.currentThread().getThreadGroup() == SidedThreadGroups.CLIENT));

        if ((entity instanceof EntityPlayer) && (Thread.currentThread().getThreadGroup() != SidedThreadGroups.CLIENT)) {
            EntityPlayer player = (EntityPlayer) entity;
            GameProfile profile = player.getGameProfile();

            LOGGER.debug("H");
            LOGGER.debug(profile);

            if (profile.getId().equals(playerID)) {
                LOGGER.debug("HURT!");
                setState(getStateCounter(), 3, getIntensity("hurt"), true);
            }
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        Entity entity = event.getEntityLiving();
        LOGGER.debug("DX");
        LOGGER.debug(entity instanceof EntityPlayer);
        LOGGER.debug((Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER));
        LOGGER.debug((Thread.currentThread().getThreadGroup() == SidedThreadGroups.CLIENT));

        if ((entity instanceof EntityPlayer) && (Thread.currentThread().getThreadGroup() != SidedThreadGroups.CLIENT)) {
            EntityPlayer player = (EntityPlayer) entity;
            GameProfile profile = player.getGameProfile();

            LOGGER.debug("D");
            LOGGER.debug(profile);

            if (profile.getId().equals(playerID)) {
                clearState();
                ToyController.setVibrationLevel(0);
            }
        }
    }

    private static void populatePlayerInfo() {
        GameProfile profile = Minecraft.getMinecraft().player.getGameProfile();
        playerName = profile.getName();
        playerID = profile.getId();
        System.out.println("Current player: " + playerName + " " + playerID.toString());
    }

    @SubscribeEvent
    public static void onWorldLoaded(WorldEvent.Load event) {
        World world = event.getWorld();
        System.out.println("World loaded: " + world.toString());
    }

    @SubscribeEvent
    public static void onWorldEntry(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP p = (EntityPlayerMP) entity;
            System.out.println("Entered world MP: " + entity);
            System.out.println("Profile: " + p.getGameProfile());
        }

        if (entity instanceof EntityPlayerSP) {
            clearState();
            System.out.println("Entered world: " + entity);

            if (playerName == null) {
                populatePlayerInfo();
            }

            if (playerName != null) {
                EntityPlayer player = (EntityPlayer) entity;
                GameProfile profile = player.getGameProfile();

                if (profile.getId().equals(playerID)) {
                    System.out.println("Player in: " + playerName + " " + playerID);
                    LOGGER.debug(ToyController.isConnected);
                    if (!ToyController.isConnected) {
                        if (ToyController.connectDevice()) {
                            LOGGER.debug("Toy connected");
                            if (player.getHealth() > 0) {
                                setState(getStateCounter(), 2);
                            }
                            player.sendStatusMessage(new TextComponentString(String.format("Connected to " + TextFormatting.GREEN + "%s" + TextFormatting.RESET + " [%d]", ToyController.getDeviceName(), ToyController.getDeviceId())), true);
                        } else {
                            LOGGER.debug("Failed to connect");
                            player.sendStatusMessage(new TextComponentString(String.format(TextFormatting.YELLOW + "Minegasm " + TextFormatting.RESET + "failed to start\n%s", ToyController.getLastErrorMessage())), false);
                        }
                    } else {
                        clearState();
                        ToyController.setVibrationLevel(0);
                        populatePlayerInfo();
                    }

                    if (!motdDisplayed) {
                        player.sendStatusMessage(new TextComponentString(String.format(TextFormatting.RED + "Minegasm 0.2.2 BETA 1: " + TextFormatting.RESET + " only attack and hurt events are implemented!\n")), false);
                        motdDisplayed = true;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWorldExit(WorldEvent.Unload event) {
        LOGGER.debug("World exit");
        clearState();
        ToyController.setVibrationLevel(0);
        motdDisplayed = false;
    }
}



















/*

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
    public static void onHarvest(PlayerEvent.HarvestCheck event)
    {
        PlayerEntity player = event.getPlayer();
        GameProfile profile = player.getGameProfile();

        if (profile.getId().equals(playerID)) {
            BlockState blockState = event.getTargetBlock();
            Block block = blockState.getBlock();

            // ToolType. AXE, HOE, PICKAXE, SHOVEL
            @SuppressWarnings("ConstantConditions") float blockHardness = block.defaultBlockState().getDestroySpeed(null, null);
            LOGGER.debug("Harvest: tool: " +
                    block.getHarvestTool(blockState) +
                    " can harvest? " + event.canHarvest() + " hardness: " + blockHardness);

            int intensity = Math.toIntExact(Math.round((getIntensity("harvest") / 100.0 * (blockHardness / 50.0)) * 100));

            if (event.canHarvest()) {
                setState(getStateCounter(), 1, intensity, false);
            }
        }
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event)
    {
        PlayerEntity player = event.getPlayer();
        GameProfile profile = player.getGameProfile();

        if (profile.getId().equals(playerID)) {
            BlockState blockState = event.getState();
            Block block = blockState.getBlock();
            @SuppressWarnings("ConstantConditions") float blockHardness = block.defaultBlockState().getDestroySpeed(null, null);

            LOGGER.info("Breaking: " + block.toString());

            ToolType blockHarvestTool = block.getHarvestTool(blockState);
            ItemStack mainhandItem = event.getPlayer().getMainHandItem();
            Set<ToolType> mainhandToolTypes = mainhandItem.getItem().getToolTypes(mainhandItem);

            boolean usingPickaxe = mainhandToolTypes.contains(ToolType.PICKAXE);
            boolean usingAppropriateTool = mainhandToolTypes.contains(blockHarvestTool);
            LOGGER.debug("mainhand: " + mainhandItem + " [" + mainhandToolTypes + "]");
            LOGGER.debug("using pickaxe: " + usingPickaxe + ", using appropriate tool: " + usingAppropriateTool);

            if (usingPickaxe && usingAppropriateTool) {
                int duration = Math.max(1, Math.min(5, Math.toIntExact(Math.round(Math.ceil(Math.log(blockHardness + 0.5))))));
                int intensity = Math.toIntExact(Math.round((getIntensity("mine") / 100.0 * (blockHardness / 50.0)) * 100));
                setState(getStateCounter(), duration, intensity, true);
            }

            LOGGER.info("XP to drop: " + event.getExpToDrop());
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
        PlayerEntity player = event.getPlayer();
        GameProfile profile = player.getGameProfile();

        if (profile.getId().equals(playerID)) {
            int xpChange = event.getAmount();
            long duration = Math.round(Math.ceil(Math.log(xpChange + 0.5)));

            LOGGER.info("XP CHANGE: " + xpChange);
            LOGGER.debug("duration: " + duration);

            setState(getStateCounter(), Math.toIntExact(duration), getIntensity("xpChange"), true);
        }
    }

*/