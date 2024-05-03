package com.therainbowville.minegasm.common;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.math.Vec3d;

import com.therainbowville.minegasm.config.MinegasmConfig;


public class VibrationStateFish extends AbstractVibrationState
{    

    public VibrationStateFish()
    {
        super(0);
    }
    
    public void onTick(EntityPlayer player)
    {
        if (player == null) return;
        
        if (player.fishEntity != null)
        {
            double x = player.fishEntity.motionX;
            double y = player.fishEntity.motionY;
            double z = player.fishEntity.motionZ;
            if (y < -0.075 && player.fishEntity.isInWater() && x == 0 && z == 0)
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