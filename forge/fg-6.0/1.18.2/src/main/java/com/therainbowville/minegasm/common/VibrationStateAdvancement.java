package com.therainbowville.minegasm.common;

import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.Advancement;
import net.minecraftforge.event.entity.player.AdvancementEvent;

public class VibrationStateAdvancement extends AbstractVibrationState
{
    public VibrationStateAdvancement()
    {
        super(0);
    }
    
    public void onAdvancement(AdvancementEvent event)
    {
        if (getIntensity("advancement") == 0) return;
    try {
        LOGGER.info("Advancement Event: " + event);
        Advancement advancement = event.getAdvancement();
        if (advancement == null) return;
        FrameType type = advancement.getDisplay().getFrame();
        int duration = switch (type) {
            case TASK -> 5;
            case GOAL -> 7;
            case CHALLENGE -> 10;
        };

        vibrationCountdown = duration * 20;
        vibrationFeedbackCountdown = 1.5f * 20;
    } catch (Throwable e) {
            LOGGER.throwing(e);
    }
    }
    
    public int getIntensity()
    {
        if (accumulationEnabled())
            return Math.toIntExact(Math.round(intensity));
        else if (vibrationFeedbackCountdown > 0)
            return Math.min(100, getIntensity("advancement") + 20);
        else if (vibrationCountdown > 0)
            return getIntensity("advancement");
        else return 0;
    }
}