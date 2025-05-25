package com.flechazo.slashblade.entity.ai;

import com.flechazo.slashblade.capability.mobeffect.MobEffectComponent;
import com.flechazo.slashblade.capability.mobeffect.MobEffectComponentRegistry;
import com.flechazo.slashblade.capability.mobeffect.MobEffectHelper;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class StunGoal extends Goal {
    private final PathfinderMob entity;

    public StunGoal(PathfinderMob creature) {
        this.entity = creature;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK, Flag.TARGET));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canUse() {

        return MobEffectHelper.getMobEffect(entity)
                .filter((state) -> state.isStun(this.entity.level().getGameTime())).isPresent();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by
     * another one
     */
    public void stop() {
        MobEffectHelper.getMobEffect(this.entity).ifPresent(MobEffectComponent::clearStunTimeOut);
    }
}
