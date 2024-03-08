package com.therainbowville.minegasm.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.Vec3d;

import com.therainbowville.minegasm.config.MinegasmConfig;


public class VibrationStateFish extends AbstractVibrationState
{    

    public VibrationStateFish()
    {
        super(0);
    }
    
    public void onTick(ClientPlayerEntity player)
    {
        if (player == null) return;
        
        if (player.fishing != null)
        {
            Vec3d vector = player.fishing.getDeltaMovement();
            double x = vector.x();
            double y = vector.y();
            double z = vector.z();
            if (y < -0.075 && player.fishing.isInWater() && x == 0 && z == 0)
            {
                vibrationCountdown = 1.5f * MinegasmConfig.ticksPerSecond;
                LOGGER.info("Fishing!");
            }
        }
    }
    
    public int getIntensity()
    {
        if (vibrationCountdown > 0)
            return getIntensity("fishing");
        else return 0;
    }
}