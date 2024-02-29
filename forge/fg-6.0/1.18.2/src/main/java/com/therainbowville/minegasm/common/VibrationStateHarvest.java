package com.therainbowville.minegasm.common;

import net.minecraft.world.level.block.state.BlockState;

public class VibrationStateHarvest extends AbstractVibrationState
{
    private VibrationStateMine mineState;
    
    public VibrationStateHarvest(VibrationStateMine state)
    {
        super(0);
        mineState = state;
    }

	public void onHarvest() {
        vibrationCountdown = 3;
        mineState.onHarvest();
	}
    
    public int getIntensity()
    {
        if (vibrationCountdown > 0){
            if (accumulationEnabled())
                return Math.min(100, mineState.getIntensity() + 19);
            else 
                return getIntensity("harvest");            
        }
        else return 0;
    }
}