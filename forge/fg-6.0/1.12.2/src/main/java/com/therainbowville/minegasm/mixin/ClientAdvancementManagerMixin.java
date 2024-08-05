package com.therainbowville.minegasm.mixin;

import com.therainbowville.minegasm.client.ClientEventHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.util.ResourceLocation;
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

    @Inject(method = "read", at = @At("HEAD"), cancellable = true)
    public void onUpdate(SPacketAdvancementInfo advancementInfoPacket, CallbackInfo ci) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            return;
        }
        LOGGER.info("Advancement updated");

        for (Map.Entry<ResourceLocation, AdvancementProgress> entry : advancementInfoPacket.getProgressUpdates().entrySet()) {
            Advancement advancement = ((ClientAdvancementManager) (Object) this).getAdvancementList().getAdvancement(entry.getKey());
            if (advancement != null) {
                EntityPlayer player = Minecraft.getMinecraft().player;
                AdvancementEvent event = new AdvancementEvent(player, advancement);
                ClientEventHandler.onAdvancementEvent(event);
            }
        }
    }
}