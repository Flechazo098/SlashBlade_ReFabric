package com.flechazo.slashblade.event;

import com.flechazo.slashblade.event.drop.EntityDropEntry;
import com.flechazo.slashblade.recipe.SlashBladeIngredient;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import io.github.fabricators_of_create.porting_lib.registries.DynamicRegistryHandler;
import io.github.fabricators_of_create.porting_lib.registries.RegistryEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.resources.RegistryDataLoader;

public class RegistryHandler {

    public static void initDatapack() {
//        RegistryEvents.NEW_DATAPACK_REGISTRY.register(RegistryHandler::registerDataPackRegistries);
        DynamicRegistries.registerSynced(SlashBladeDefinition.NAMED_BLADES_KEY, SlashBladeDefinition.CODEC);
        DynamicRegistries.registerSynced(EntityDropEntry.REGISTRY_KEY, EntityDropEntry.CODEC);
    }

//    public static void registerDataPackRegistries(RegistryEvents.NewDatapackRegistry registry) {
//        registry.register(
//                new RegistryDataLoader.RegistryData<>(SlashBladeDefinition.NAMED_BLADES_KEY, SlashBladeDefinition.CODEC),
//                SlashBladeDefinition.CODEC
//        );
//
//        registry.register(
//                new RegistryDataLoader.RegistryData<>(EntityDropEntry.REGISTRY_KEY, EntityDropEntry.CODEC),
//                EntityDropEntry.CODEC
//        );
//    }
    public static void registerIngredientSerializer () {
        CustomIngredientSerializer.register(SlashBladeIngredient.Serializer.INSTANCE);
    }
}