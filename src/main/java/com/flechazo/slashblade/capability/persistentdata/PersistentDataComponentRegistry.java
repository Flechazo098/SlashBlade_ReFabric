package com.flechazo.slashblade.capability.persistentdata;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.world.entity.Entity;

public class PersistentDataComponentRegistry implements EntityComponentInitializer {

    public static final ComponentKey<PersistentDataComponent> PERSISTENT_DATA =
            ComponentRegistry.getOrCreate(
                    new net.minecraft.resources.ResourceLocation("slashblade", "persistent_data"),
                    PersistentDataComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(Entity.class, PERSISTENT_DATA, entity -> new PersistentDataComponentImpl());
    }
}