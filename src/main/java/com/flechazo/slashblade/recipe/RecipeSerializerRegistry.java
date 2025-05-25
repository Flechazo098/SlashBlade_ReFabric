package com.flechazo.slashblade.recipe;

import com.flechazo.slashblade.SlashBladeRefabriced;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;


public class RecipeSerializerRegistry {
    public static final ResourceLocation ID_SHAPED_BLADE =
            new ResourceLocation(SlashBladeRefabriced.MODID, "shaped_blade");
    public static final ResourceLocation ID_PROUDSOUL =
            new ResourceLocation(SlashBladeRefabriced.MODID, "proudsoul");

    public static void register() {
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ID_SHAPED_BLADE, SlashBladeShapedRecipe.SERIALIZER);
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ID_PROUDSOUL, ProudsoulShapelessRecipe.SERIALIZER);
    }
}
