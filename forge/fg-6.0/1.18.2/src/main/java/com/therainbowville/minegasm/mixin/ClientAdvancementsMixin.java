package com.therainbowville.minegasm.mixin;

import com.therainbowville.minegasm.client.ClientEventHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ClientAdvancements.class)
public class ClientAdvancementsMixin {
    private static final Minecraft minecraft = Minecraft.getInstance();
    private static Logger LOGGER = LogManager.getLogger();

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    public void onUpdate(ClientboundUpdateAdvancementsPacket advancementInfoPacket, CallbackInfo ci) {
        if (Minecraft.getInstance().isLocalServer()) {
            return;
        }
        LOGGER.info("Advancement updated");

        for (Map.Entry<ResourceLocation, AdvancementProgress> entry : advancementInfoPacket.getProgress().entrySet()) {
            Advancement advancement = ((ClientAdvancements) (Object) this).getAdvancements().get(entry.getKey());
            Player player = minecraft.player;
            AdvancementEvent event = new AdvancementEvent(player, advancement);
            ClientEventHandler.onAdvancementEvent(event);
        }
    }
}