package com.therainbowville.minegasm.common;

import net.minecraft.block.state.IBlockState;

import com.therainbowville.minegasm.config.MinegasmConfig;

public class VibrationStateMine extends AbstractVibrationState
{
    public VibrationStateMine()
    {
        super(5);
    }
    
	public void onBreak(IBlockState block) {
        if (getIntensity("mine") == 0) return;
        
        String blockName = block.getBlock().getNameTextComponent().getString();
        if (accumulationEnabled())
        {
            if (blockName.contains("Ore")) {
                intensity = Math.min(100, intensity + 1);
                vibrationCountdown = streakCountdownAmount * MinegasmConfig.ticksPerSecond;
                vibrationFeedbackCountdown = 1 * MinegasmConfig.ticksPerSecond;
            } else {
                intensity = Math.min(100, intensity + .25f);
                vibrationCountdown = streakCountdownAmount * MinegasmConfig.ticksPerSecond;
            }
        } else {
            if (blockName.contains("Ore")) {
                vibrationCountdown = 3 * MinegasmConfig.ticksPerSecond;
                vibrationFeedbackCountdown = 1 * MinegasmConfig.ticksPerSecond;
            } else  
                vibrationCountdown = 3 * MinegasmConfig.ticksPerSecond;
        }
	}
    
	public void onHarvest() {
        if (accumulationEnabled()){
            vibrationCountdown = Math.max(3, vibrationCountdown);
        }
	}
    
    public int getIntensity()
    {
        if (accumulationEnabled())
            return Math.toIntExact(Math.round(intensity));
        else if (vibrationFeedbackCountdown > 0)
            return Math.min(100, getIntensity("mine") + 20);
        else if (vibrationCountdown > 0)
            return getIntensity("mine");
        else return 0;
    }
}