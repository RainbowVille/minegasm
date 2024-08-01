package com.therainbowville.minegasm.common;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementType;
import net.minecraftforge.event.entity.player.AdvancementEvent.AdvancementEarnEvent;

import com.therainbowville.minegasm.config.MinegasmConfig;

public class VibrationStateAdvancement extends AbstractVibrationState
{
    public VibrationStateAdvancement()
    {
        super(0);
    }
    
    public void onAdvancement(AdvancementEarnEvent event)
    {
        if (getIntensity("advancement") == 0) return;
    try {
        LOGGER.info("Advancement Event: " + event);
        Advancement advancement = event.getAdvancement().value();
        AdvancementType type = advancement.display().get().getType();
        int duration = switch (type) {
            case TASK -> 5;
            case GOAL -> 7;
            case CHALLENGE -> 10;
        };

        vibrationCountdown = duration * MinegasmConfig.ticksPerSecond;
        vibrationFeedbackCountdown = 1.5f * MinegasmConfig.ticksPerSecond;
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