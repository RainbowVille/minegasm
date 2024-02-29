package com.therainbowville.minegasm.common;

public class VibrationStateClient extends AbstractVibrationState
{
    public VibrationStateClient()
    {
        super(0);
    }
    
    public void setVibration(int intensity, int durationSeconds)
    {
        intensity = intensity;
        vibrationCountdown = durationSeconds * 20;
    }
    
    public int getIntensity()
    {
        if (vibrationCountdown > 0)
            return Math.toIntExact(Math.round(intensity));
        else return 0;
    }
}