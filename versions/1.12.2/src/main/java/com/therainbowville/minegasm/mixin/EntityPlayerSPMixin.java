package com.therainbowville.minegasm.mixin;

import com.therainbowville.minegasm.client.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public class EntityPlayerSPMixin {
    private static Logger LOGGER = LogManager.getLogger();

/*    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    public void onHeal(float amount, CallbackInfo ci) {
        if (amount > 0) {
            LivingHealEvent event = new LivingHealEvent((LocalPlayer) (Object) this, amount);
            ClientEventHandler.onHeal(event);
        }
    }*/

    @Inject(method = "attackEntityFrom", at = @At("HEAD"), cancellable = true)
    public void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            return;
        }

        if (amount > 0) {
            LivingHurtEvent event = new LivingHurtEvent((EntityLivingBase) (Object) this, source, amount);
            ClientEventHandler.onHurt(event);
        }
    }

    @Inject(method = "setXPStats", at = @At("HEAD"), cancellable = true)
    public void onSetXPStats(float currentXp, int totalXp, int level, CallbackInfo ci) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            return;
        }

        int amount = totalXp - ((EntityPlayerSP) (Object) this).experienceTotal;

        if (amount > 0) {

            EntityXPOrb orb = new EntityXPOrb(((EntityPlayer) (Object) this).world);
            orb.xpValue = amount;

            LOGGER.info("Experience changed");
            PlayerPickupXpEvent event = new PlayerPickupXpEvent((EntityPlayer) (Object) this, orb);
            ClientEventHandler.onXpChange(event);

        }
    }
}