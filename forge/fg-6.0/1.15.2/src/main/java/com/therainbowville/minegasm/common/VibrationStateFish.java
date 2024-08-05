package com.therainbowville.minegasm.common;

import com.therainbowville.minegasm.config.MinegasmConfig;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;


public class VibrationStateFish extends AbstractVibrationState {

    public VibrationStateFish() {
        super(0);
    }

    public void onTick(ClientPlayerEntity player) {
        if (player == null) return;

        if (player.fishing != null) {
            Vec3d vector = player.fishing.getDeltaMovement();
            double x = vector.x();
            double y = vector.y();
            double z = vector.z();
            if (y < -0.075 && player.fishing.isInWater() && x == 0 && z == 0) {
                vibrationCountdown = 1.5f * MinegasmConfig.ticksPerSecond;
                LOGGER.info("Fishing!");
            }
        }
    }

    public int getIntensity() {
        if (vibrationCountdown > 0)
            return getIntensity("fishing");
        else return 0;
    }
}