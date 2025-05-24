package com.flechazo.slashblade.event;

import com.flechazo.slashblade.capability.slashblade.BladeStateComponent;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BladeMotionEvent extends SlashBladeEvent {

    public static final EventBus<BladeMotionEvent> BLADE_MOTION = new EventBus<>();

    private final LivingEntity entity;
    private final ResourceLocation combo;

    public BladeMotionEvent(ItemStack blade, BladeStateComponent state, LivingEntity entity, ResourceLocation combo) {
        super(blade, state);
        this.entity = entity;
        this.combo = combo;
    }

    public BladeMotionEvent(LivingEntity entity, ResourceLocation combo) {
        super(entity.getMainHandItem(), BladeStateHelper.getBladeState(entity.getMainHandItem()).orElse(null));
        this.entity = entity;
        this.combo = combo;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public ResourceLocation getCombo() {
        return this.combo;
    }
}