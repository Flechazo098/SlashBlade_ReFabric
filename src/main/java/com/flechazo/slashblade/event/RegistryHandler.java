package com.flechazo.slashblade.event;

import com.flechazo.slashblade.event.drop.EntityDropEntry;
import com.flechazo.slashblade.recipe.SlashBladeIngredient;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import io.github.fabricators_of_create.porting_lib.registries.RegistryEvents;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.resources.RegistryDataLoader;

public class RegistryHandler {

    private static void registerDataPackRegistries(RegistryEvents.NewDatapackRegistry registry) {
        // 注册 SlashBladeDefinition 数据包注册表
        registry.register(
                new RegistryDataLoader.RegistryData<>(SlashBladeDefinition.REGISTRY_KEY, SlashBladeDefinition.CODEC),
                SlashBladeDefinition.CODEC
        );

        // 注册 EntityDropEntry 数据包注册表
        registry.register(
                new RegistryDataLoader.RegistryData<>(EntityDropEntry.REGISTRY_KEY, EntityDropEntry.CODEC),
                EntityDropEntry.CODEC
        );
    }
    public static void registerIngredientSerializer () {
        CustomIngredientSerializer.register(SlashBladeIngredient.Serializer.INSTANCE);
    }
}