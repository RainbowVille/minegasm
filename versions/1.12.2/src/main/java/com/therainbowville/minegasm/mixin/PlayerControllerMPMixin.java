package com.therainbowville.minegasm.mixin;

import com.therainbowville.minegasm.client.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {
    private static Logger LOGGER = LogManager.getLogger();

    boolean placedBlock;

    @Inject(method = "onPlayerDestroyBlock", at = @At("HEAD"), cancellable = true)
    public void onDestroyBlock(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            return;
        }

        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            BreakEvent event = new BreakEvent(player.world, blockPos, player.world.getBlockState(blockPos), player);
            ClientEventHandler.onBreak(event);
        }
    }

    @Inject(method = "processRightClickBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;onItemUse(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Lnet/minecraft/util/EnumActionResult;",
            shift = At.Shift.AFTER), cancellable = true)
    public void onUseItemOn(EntityPlayerSP player, WorldClient worldIn, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand, CallbackInfoReturnable<EnumActionResult> cir) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            return;
        }

        if (player.getHeldItem(hand).getItem() instanceof ItemBlock) {
            this.placedBlock = true;
        }

    }

    @Inject(method = "processRightClickBlock", at = @At("RETURN"), cancellable = true)
    public void onUseItemOnReturn(EntityPlayerSP player, WorldClient worldIn, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand, CallbackInfoReturnable<EnumActionResult> cir) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning() || !this.placedBlock) {
            return;
        }

        if (cir.getReturnValue() == EnumActionResult.SUCCESS) {
            ClientEventHandler.onPlace();
        }
        this.placedBlock = false;
    }

}