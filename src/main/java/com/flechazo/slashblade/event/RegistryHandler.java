package com.flechazo.slashblade.event;

import com.flechazo.slashblade.recipe.SlashBladeIngredient;
import com.flechazo.slashblade.registry.SlashBladeJsonManager;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;

public class RegistryHandler {
    public static void initJsonManager() {
        SlashBladeJsonManager.getInstance().init();
    }

    public static void registerIngredientSerializer() {
        CustomIngredientSerializer.register(SlashBladeIngredient.Serializer.INSTANCE);
    }
}