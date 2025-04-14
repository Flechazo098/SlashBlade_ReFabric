package com.flechazo.slashblade.capability.mobeffect;

import com.flechazo.slashblade.SlashBladeRefabriced;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class MobEffectComponentRegistry implements EntityComponentInitializer {

    public static final ComponentKey<MobEffectComponent> MOB_EFFECT =
            ComponentRegistry.getOrCreate(
                    new ResourceLocation(SlashBladeRefabriced.MODID, "mob_effect"),
                    MobEffectComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // 为所有LivingEntity注册MobEffect组件
        registry.registerFor(LivingEntity.class, MOB_EFFECT, entity -> new MobEffectComponentImpl());
    }
}