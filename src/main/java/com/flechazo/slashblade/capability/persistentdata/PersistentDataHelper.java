package com.flechazo.slashblade.capability.persistentdata;

import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class PersistentDataHelper {
    public static Optional<PersistentDataComponent> getPersistentData(Entity entity) {
        return PersistentDataComponentRegistry.PERSISTENT_DATA.maybeGet(entity);
    }
}