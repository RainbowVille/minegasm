package com.therainbowville.minegasm.mixin;
import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.server.SAdvancementInfoPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ClientAdvancementManager.class)
public class ClientAdvancementManagerMixin {
    private static Logger LOGGER = LogManager.getLogger();
    private static final Minecraft minecraft = Minecraft.getInstance();

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    public void onUpdate(SAdvancementInfoPacket advancementInfoPacket, CallbackInfo ci) {
        if (Minecraft.getInstance().isLocalServer()) { return; }                                            
        LOGGER.info("Advancement updated");

        for(Map.Entry<ResourceLocation, AdvancementProgress> entry : advancementInfoPacket.getProgress().entrySet()) {
            Advancement advancement = ((ClientAdvancementManager) (Object) this).getAdvancements().get(entry.getKey());
            PlayerEntity player = minecraft.player;
            AdvancementEvent event = new AdvancementEvent(player, advancement);
            ClientEventHandler.onAdvancementEvent(event);
        }
    }
}