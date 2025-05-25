package com.flechazo.slashblade.util;

import com.flechazo.slashblade.network.util.PlayMessages;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class EntitySpawnFactoryRegistryHelper {
    private static final Map<EntityType<?>, BiFunction<PlayMessages.SpawnEntity, Level, ? extends Entity>> FACTORIES = new HashMap<>();

    public static <T extends Entity> void register(EntityType<T> type, BiFunction<PlayMessages.SpawnEntity, Level, T> factory) {
        FACTORIES.put(type, factory);
    }

    @Nullable
    public static Entity create(EntityType<?> type, PlayMessages.SpawnEntity packet, Level world) {
        BiFunction<PlayMessages.SpawnEntity, Level, ? extends Entity> factory = FACTORIES.get(type);
        if (factory != null) {
            return factory.apply(packet, world);
        } else {
            return type.create(world);
        }
    }
}
