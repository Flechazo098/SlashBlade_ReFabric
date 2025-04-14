package com.flechazo.slashblade.capability.mobeffect;

import net.minecraft.world.entity.LivingEntity;

public class MobEffectHelper {

    /**
     * 获取实体的MobEffect组件
     * @param entity 实体
     * @return MobEffect组件
     */
    public static MobEffectComponent getMobEffect(LivingEntity entity) {
        return MobEffectComponentRegistry.MOB_EFFECT.get(entity);
    }
}