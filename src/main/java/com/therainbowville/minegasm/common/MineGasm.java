package com.therainbowville.minegasm.common;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent.PickupXp;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import org.metafetish.buttplug.core.Messages.SingleMotorVibrateCmd;
import org.metafetish.buttplug.core.Messages.StopAllDevices;
import org.metafetish.buttplug.client.*;

import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.UUID;
import java.net.URI;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("minegasm")
public class MineGasm
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    private static String playerName = null;
    private static UUID playerID = null;
    private final ButtplugWSClient client = new ButtplugWSClient("Minegasm");
    private ButtplugClientDevice device = null;

    public MineGasm() {
        // Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);

        connectDevice();
    }

    private void connectDevice() {
        try {
            client.Connect(new URI("ws://localhost:12345/buttplug"), true);
            client.startScanning();

            Thread.sleep(5000);
            client.requestDeviceList();

            for (ButtplugClientDevice dev : client.getDevices()) {
                if (dev.allowedMessages.contains(SingleMotorVibrateCmd.class.getSimpleName())) {
                    device = dev;
                    client.sendDeviceMessage(device, new SingleMotorVibrateCmd(device.index, 0, client.getNextMsgId()));
                    break;
                }
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    LOGGER.info("Disconnecting devices...");
                    client.sendDeviceMessage(device, new SingleMotorVibrateCmd(device.index, 0, client.getNextMsgId()));
                    client.stopAllDevices();
                    client.Disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void vibrate() {
        try {
            client.sendDeviceMessage(device, new SingleMotorVibrateCmd(device.index, (1.0 - Math.random()), client.getNextMsgId()));
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            try {
                                client.sendDeviceMessage(device, new SingleMotorVibrateCmd(device.index, 0, client.getNextMsgId()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    Math.round(100 + 5000 * (1.0 - Math.random()))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("minegasm", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
   
    @SubscribeEvent
    public void onPickupXp(PickupXp event)
    {
        System.out.println("XP picked up");
        // Commands.SendPlayerSystemMessage((PlayerEntity) event.getEntity(), TextFormatting.RED + "Test");
    }

    /*@SubscribeEvent
    public void onPickupItem(EntityItemPickupEvent event) {
        System.out.println("Item picked up!");
        LOGGER.info(event);
    }*/

    /*@SubscribeEvent
    public void onDestroyItem(PlayerDestroyItemEvent event)
    {
        System.out.println("Item destroyed");
    }*/

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event)
    {
        Entity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            GameProfile profile = player.getGameProfile();
            System.out.println("Player dead: " + profile.getName() + " " + profile.getId().toString());
        }
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event)
    {
        Entity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            GameProfile profile = player.getGameProfile();
            System.out.println("Player hurt: " + profile.getName() + " " + profile.getId().toString());
            System.out.println("Player hurt: " + playerID);

            if (profile.getId().equals(playerID)) {
                vibrate();
            }
        }
    }

    /*@SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event)
    {
        System.out.println("Tick");
    }*/

    @SubscribeEvent
    public void onWorldLoaded(WorldEvent.Load event) {
        IWorld world = event.getWorld();
        System.out.println("World loaded: " + world.toString());

        GameProfile profile = Minecraft.getInstance().getSession().getProfile();
        playerName = profile.getName();
        playerID = profile.getId();
        System.out.println("Current player: " + playerName + " " + playerID.toString());

        /*System.out.println("n players: " + world.getPlayers().size());

        for (PlayerEntity player : world.getPlayers()) {
            if (player.getGameProfile().getId().equals(playerID)) {
                vibrate();
                break;
            }
        }*/
    }

    @SubscribeEvent
    public void onWorldEntry(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ClientPlayerEntity) {
            System.out.println("Entered world: " + entity.toString());

            if (playerName != null) {
                PlayerEntity player = (PlayerEntity) entity;
                GameProfile profile = player.getGameProfile();
                //playerName = profile.getName();
                //playerID = profile.getId();

                //System.out.println(profile.toString() + " " + playerID);
                //System.out.println(profile.getId().equals(playerID));

                if (profile.getId().equals(playerID)) {
                    System.out.println("Player in: " + playerName + " " + playerID.toString());
                    vibrate();
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldExit(EntityLeaveWorldEvent event) {
        Entity entity = event.getEntity();
        if ((entity instanceof PlayerEntity) && (playerName != null)) {
            playerName = null;
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
