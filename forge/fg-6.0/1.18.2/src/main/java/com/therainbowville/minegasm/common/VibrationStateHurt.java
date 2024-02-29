package com.therainbowville.minegasm.common;

public class VibrationStateHurt extends AbstractVibrationState
{
    public VibrationStateHurt()
    {
        super(3);
    }
    
	public void onHurt() {
        if (getIntensity("hurt") == 0) return;
        
        if (accumulationEnabled())
        {
            intensity = Math.min(100, intensity + 10);
            vibrationCountdown = streakCountdownAmount;
            vibrationFeedbackCountdown = 1 * 20;
        } else {
            vibrationCountdown = 3 * 20;
            vibrationFeedbackCountdown = 1 * 20;
        }
	}
    
    public int getIntensity()
    {
        if (accumulationEnabled())
            return Math.toIntExact(Math.round(intensity));
        else if (vibrationFeedbackCountdown > 0)
            return Math.min(100, getIntensity("hurt") + 20);
        else if (vibrationCountdown > 0)
            return getIntensity("hurt");
        else return 0;
    }
}