package com.therainbowville.minegasm.mixin;
import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;

import net.minecraft.world.level.block.Block;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionResult;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.core.BlockPos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    private static Logger LOGGER = LogManager.getLogger();

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    public void onDestroyBlock(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            BreakEvent event = new BreakEvent(player.level, blockPos, player.level.getBlockState(blockPos), player);
            ClientEventHandler.onBreak(event);
        }
    }

    @Inject(method = "useItemOn", at = @At("RETURN"), cancellable = true)
    public void onUseItemOn(LocalPlayer player, ClientLevel world, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<InteractionResult> cir) {
        // check if item is a block
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();
        if (item instanceof BlockItem) {
            ClientEventHandler.onPlace();
        }
    }

}