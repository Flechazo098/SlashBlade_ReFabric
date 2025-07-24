package com.flechazo.slashblade.event;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.event.drop.EntityDropEntry;
import com.flechazo.slashblade.recipe.SlashBladeIngredient;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import io.github.fabricators_of_create.porting_lib.registries.DynamicRegistryHandler;
import io.github.fabricators_of_create.porting_lib.registries.RegistryEvents;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.resources.RegistryDataLoader;

public class RegistryHandler {

    public static void initDatapack() {
        SlashBladeRefabriced.LOGGER.info("Registering datapack registries...");
        DynamicRegistryHandler.REGISTRIES.add(new RegistryEvents.RegistryDataWithNetworkCodec<>(
                new RegistryDataLoader.RegistryData<>(SlashBladeDefinition.NAMED_BLADES_KEY, SlashBladeDefinition.CODEC),
                SlashBladeDefinition.CODEC
        ));

        DynamicRegistryHandler.REGISTRIES.add(new RegistryEvents.RegistryDataWithNetworkCodec<>(
                new RegistryDataLoader.RegistryData<>(EntityDropEntry.REGISTRY_KEY, EntityDropEntry.CODEC),
                EntityDropEntry.CODEC
        ));
    }

    public static void registerIngredientSerializer() {
        CustomIngredientSerializer.register(SlashBladeIngredient.Serializer.INSTANCE);
    }
}