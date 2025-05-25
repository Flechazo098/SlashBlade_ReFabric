package com.flechazo.slashblade.ability;

import com.flechazo.slashblade.capability.mobeffect.MobEffectComponent;
import com.flechazo.slashblade.capability.mobeffect.MobEffectHelper;
import com.flechazo.slashblade.entity.ai.StunGoal;
import io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * Created by Furia on 15/06/20.
 */
public class StunManager {

    private static final class SingletonHolder {
        private static final StunManager instance = new StunManager();
    }

    public static StunManager getInstance () {
        return StunManager.SingletonHolder.instance;
    }

    private StunManager () {
    }

    public void register () {
        LivingEntityEvents.LivingTickEvent.TICK.register(this::onEntityLivingUpdate);
        ServerEntityEvents.ENTITY_LOAD.register(this::onEntityLoad);
    }

    static final int DEFAULT_STUN_TICKS = 10;


    public void onEntityLivingUpdate (LivingEntityEvents.LivingTickEvent event) {
        LivingEntity target = event.getEntity();
        if (! (target instanceof PathfinderMob))
            return;
        if (target.level() == null)
            return;

        boolean onStun = MobEffectHelper.getMobEffect(target)
                .filter((state) -> state.isStun(target.level().getGameTime())).isPresent();

        if (onStun) {
            Vec3 motion = target.getDeltaMovement();
            if (5 < target.fallDistance)
                target.setDeltaMovement(motion.x, motion.y - 2.0f, motion.z);
            else if (motion.y < 0)
                target.setDeltaMovement(motion.x, motion.y * 0.25f, motion.z);
        }

    }

    public static void setStun (LivingEntity target, LivingEntity attacker) {
        setStun(target);
    }

    public static void setStun (LivingEntity target) {
        setStun(target, DEFAULT_STUN_TICKS);
    }

    public static void setStun (LivingEntity target, long duration) {
        if (! (target instanceof PathfinderMob))
            return;
        if (target.level() == null)
            return;

        MobEffectHelper.getMobEffect(target).ifPresent((state) -> {
            state.setManagedStun(target.level().getGameTime(), duration);
        });
    }

    public static void removeStun (LivingEntity target) {
        if (target.level() == null)
            return;
        if (! (target instanceof LivingEntity))
            return;

        MobEffectHelper.getMobEffect(target).ifPresent(MobEffectComponent::clearStunTimeOut);
    }

    /**
     * 当任意实体加载时被调用
     */
    private void onEntityLoad (Entity entity, ServerLevel world) {
        // 只对实现 PathfinderMob（带 goalSelector）的生物有效
        if (entity instanceof PathfinderMob mob) {
            // 给它的 GoalSelector 添加一个优先级为 -1（最高）的 StunGoal
            mob.goalSelector.addGoal(- 1, new StunGoal(mob));
        }
    }
}
