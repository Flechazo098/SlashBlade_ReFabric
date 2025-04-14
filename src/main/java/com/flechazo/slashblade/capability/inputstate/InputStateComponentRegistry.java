package com.flechazo.slashblade.capability.inputstate;

import com.flechazo.slashblade.SlashBladeRefabriced;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class InputStateComponentRegistry implements EntityComponentInitializer {

    public static final ComponentKey<InputStateComponent> INPUT_STATE =
            ComponentRegistry.getOrCreate(
                    new ResourceLocation(SlashBladeRefabriced.MODID, "input_state"),
                    InputStateComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // 为所有LivingEntity注册输入状态组件
        registry.registerFor(LivingEntity.class, INPUT_STATE, entity -> new InputStateComponentImpl());
    }
}