package com.therainbowville.minegasm.mixin;
import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.client.multiplayer.PlayerController;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.ActionResultType;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.util.math.BlockPos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerController.class)
public class PlayerControllerMixin {
    private static Logger LOGGER = LogManager.getLogger();
    
    boolean placedBlock;

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    public void onDestroyBlock(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (Minecraft.getInstance().isLocalServer()) { return; }
        
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            BreakEvent event = new BreakEvent(player.level, blockPos, player.level.getBlockState(blockPos), player);
            ClientEventHandler.onBreak(event);
        }
    }
    
    @Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;useOn(Lnet/minecraft/item/ItemUseContext;)Lnet/minecraft/util/ActionResultType;", shift = At.Shift.AFTER), cancellable = true)
    public void onUseItemOn(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockRayTraceResult result, CallbackInfoReturnable<ActionResultType> cir) {
        if (Minecraft.getInstance().isLocalServer() ) { return; }
        
        this.placedBlock = true;
        
    }
    
    @Inject(method = "useItemOn", at = @At("RETURN"), cancellable = true)
    public void onUseItemOnReturn(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockRayTraceResult result, CallbackInfoReturnable<ActionResultType> cir) {
        if (Minecraft.getInstance().isLocalServer() || !this.placedBlock) { return; }
        
        if (player.getItemInHand(hand).getItem() instanceof BlockItem && cir.getReturnValue() == ActionResultType.SUCCESS) {
            ClientEventHandler.onPlace();
        }
        this.placedBlock = false;
    }

}