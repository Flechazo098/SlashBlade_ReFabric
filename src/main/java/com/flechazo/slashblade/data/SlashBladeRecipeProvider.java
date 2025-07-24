package com.flechazo.slashblade.data;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.data.builtin.SlashBladeBuiltInRegistry;
import com.flechazo.slashblade.data.tag.SlashBladeItemTags;
import com.flechazo.slashblade.item.SwordType;
import com.flechazo.slashblade.recipe.RequestDefinition;
import com.flechazo.slashblade.recipe.SlashBladeIngredient;
import com.flechazo.slashblade.recipe.SlashBladeShapedRecipeBuilder;
import com.flechazo.slashblade.registry.SlashBladeRegister;
import com.flechazo.slashblade.registry.slashblade.EnchantmentDefinition;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class SlashBladeRecipeProvider extends RecipeProvider {

    public SlashBladeRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SlashBladeRegister.WOOD).pattern("  L").pattern(" L ")
                .pattern("B  ").define('B', Items.WOODEN_SWORD).define('L', ItemTags.LOGS)
                .unlockedBy(getHasName(Items.WOODEN_SWORD), has(Items.WOODEN_SWORD)).save(consumer);
        SlashBladeShapedRecipeBuilder.shaped(SlashBladeRegister.BAMBOO).pattern("  L").pattern(" L ").pattern("B  ")
                .define('B', SlashBladeRegister.WOOD).define('L', SlashBladeItemTags.BAMBOO)
                .unlockedBy(getHasName(SlashBladeRegister.WOOD), has(SlashBladeRegister.WOOD)).save(consumer);
        SlashBladeShapedRecipeBuilder.shaped(SlashBladeRegister.SILVER).pattern(" EI").pattern("SBD")
                .pattern("PS ").define('B', SlashBladeRegister.BAMBOO).define('I', Tags.Items.INGOTS_IRON)
                .define('S', Tags.Items.STRING).define('P', Items.PAPER).define('E', Items.EGG)
                .define('D', Tags.Items.DYES_BLACK)
                .unlockedBy(getHasName(SlashBladeRegister.BAMBOO), has(SlashBladeRegister.BAMBOO)).save(consumer);
        SlashBladeShapedRecipeBuilder.shaped(SlashBladeRegister.WHITE).pattern("  L").pattern(" L ").pattern("BG ")
                .define('B', SlashBladeRegister.WOOD).define('L', SlashBladeRegister.PROUDSOUL_INGOT)
                .define('G', Tags.Items.INGOTS_GOLD)
                .unlockedBy(getHasName(SlashBladeRegister.WOOD), has(SlashBladeRegister.WOOD)).save(consumer);

        SlashBladeShapedRecipeBuilder.shaped(SlashBladeBuiltInRegistry.YAMATO.location()).pattern("PPP").pattern("PBP")
                .pattern("PPP")
                .define('B',
                        SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                                .name(SlashBladeBuiltInRegistry.YAMATO.location()).addSwordType(SwordType.BROKEN)
                                .addSwordType(SwordType.SEALED).build()).toVanilla())
                .define('P', SlashBladeRegister.PROUDSOUL_SPHERE)
                .unlockedBy(getHasName(SlashBladeRegister.PROUDSOUL_SPHERE), has(SlashBladeRegister.PROUDSOUL_SPHERE))
                .save(consumer, SlashBladeRefabriced.prefix("yamato_fix"));

        SlashBladeShapedRecipeBuilder.shaped(SlashBladeRegister.SLASHBLADE).pattern(" EI").pattern("PBD").pattern("SI ")
                .define('B',
                        SlashBladeIngredient.of(SlashBladeRegister.WHITE,
                                RequestDefinition.Builder.newInstance().addSwordType(SwordType.BROKEN).build()).toVanilla())
                .define('I', Tags.Items.INGOTS_GOLD).define('S', Tags.Items.STRING).define('P', Tags.Items.DYES_BLUE)
                .define('E', Tags.Items.RODS_BLAZE).define('D', Tags.Items.STORAGE_BLOCKS_COAL)
                .unlockedBy(getHasName(SlashBladeRegister.WHITE), has(SlashBladeRegister.WHITE)).save(consumer);

        SlashBladeShapedRecipeBuilder.shaped(SlashBladeBuiltInRegistry.RUBY.location()).pattern("DPI").pattern("PB ")
                .pattern("S  ")
                .define('B',
                        SlashBladeIngredient.of(SlashBladeRegister.SILVER,
                                RequestDefinition.Builder.newInstance().addSwordType(SwordType.BROKEN).build()).toVanilla())
                .define('I', SlashBladeRegister.PROUDSOUL).define('S', Tags.Items.STRING).define('P', SlashBladeRegister.PROUDSOUL_INGOT)
                .define('D', Tags.Items.DYES_RED)
                .unlockedBy(getHasName(SlashBladeRegister.SILVER), has(SlashBladeRegister.SILVER))
                .save(consumer);

        SlashBladeShapedRecipeBuilder.shaped(SlashBladeBuiltInRegistry.FOX_BLACK.location()).pattern(" EF")
                .pattern("BCS").pattern("WQ ").define('W', Tags.Items.CROPS_WHEAT)
                .define('Q', Tags.Items.STORAGE_BLOCKS_QUARTZ).define('B', Items.BLAZE_POWDER)
                .define('S', SlashBladeRegister.PROUDSOUL_CRYSTAL).define('E', Tags.Items.OBSIDIAN)
                .define('F', Tags.Items.FEATHERS)
                .define('C', SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                        .name(SlashBladeBuiltInRegistry.RUBY.location())
                        .addEnchantment(new EnchantmentDefinition(getEnchantmentID(Enchantments.SMITE), 1)).build()).toVanilla())

                .unlockedBy(getHasName(SlashBladeRegister.SILVER), has(SlashBladeRegister.SILVER))
                .save(consumer);

        SlashBladeShapedRecipeBuilder.shaped(SlashBladeBuiltInRegistry.FOX_WHITE.location()).pattern(" EF")
                .pattern("BCS").pattern("WQ ").define('W', Tags.Items.CROPS_WHEAT)
                .define('Q', Tags.Items.STORAGE_BLOCKS_QUARTZ).define('B', Items.BLAZE_POWDER)
                .define('S', SlashBladeRegister.PROUDSOUL_CRYSTAL).define('E', Tags.Items.OBSIDIAN)
                .define('F', Tags.Items.FEATHERS)
                .define('C',
                        SlashBladeIngredient.of(
                                RequestDefinition.Builder.newInstance().name(SlashBladeBuiltInRegistry.RUBY.location())

                                        .addEnchantment(new EnchantmentDefinition(
                                                getEnchantmentID(Enchantments.MOB_LOOTING), 1))
                                        .build()).toVanilla())

                .unlockedBy(getHasName(SlashBladeRegister.SILVER), has(SlashBladeRegister.SILVER))
                .save(consumer);

        SlashBladeShapedRecipeBuilder.shaped(SlashBladeBuiltInRegistry.MURAMASA.location()).pattern("SSS")
                .pattern("SBS").pattern("SSS")
                .define('B',
                        SlashBladeIngredient
                                .of(RequestDefinition.Builder.newInstance().proudSoul(10000).refineCount(20).build()).toVanilla())
                .define('S', Ingredient.of(SlashBladeRegister.PROUDSOUL_SPHERE))
                .unlockedBy(getHasName(SlashBladeRegister.SLASHBLADE), has(SlashBladeRegister.SLASHBLADE)).save(consumer);

        SlashBladeShapedRecipeBuilder.shaped(SlashBladeBuiltInRegistry.TAGAYASAN.location()).pattern("SES")
                .pattern("DBD").pattern("SES")
                .define('B',
                        SlashBladeIngredient.of(SlashBladeRegister.WOOD, RequestDefinition.Builder.newInstance()
                                .addEnchantment(new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 1))
                                .proudSoul(1000).refineCount(10).build()).toVanilla())
                .define('S', Ingredient.of(SlashBladeRegister.PROUDSOUL_SPHERE)).define('E', Ingredient.of(Items.ENDER_EYE))
                .define('D', Ingredient.of(Items.ENDER_PEARL))
                .unlockedBy(getHasName(SlashBladeRegister.WOOD), has(SlashBladeRegister.WOOD)).save(consumer);

        SlashBladeShapedRecipeBuilder.shaped(SlashBladeBuiltInRegistry.AGITO.location()).pattern(" S ").pattern("SBS")
                .pattern(" S ")
                .define('B',
                        SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                                .name(SlashBladeBuiltInRegistry.AGITO_RUST.location()).killCount(100).build()).toVanilla())
                .define('S', Ingredient.of(SlashBladeRegister.PROUDSOUL))
                .unlockedBy(getHasName(SlashBladeRegister.PROUDSOUL), has(SlashBladeRegister.PROUDSOUL)).save(consumer);

        SlashBladeShapedRecipeBuilder.shaped(SlashBladeBuiltInRegistry.OROTIAGITO_SEALED.location()).pattern(" S ")
                .pattern("SBS").pattern(" S ")
                .define('B',
                        SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                                .name(SlashBladeBuiltInRegistry.OROTIAGITO_RUST.location()).killCount(100).build()).toVanilla())
                .define('S', Ingredient.of(SlashBladeRegister.PROUDSOUL))
                .unlockedBy(getHasName(SlashBladeRegister.PROUDSOUL), has(SlashBladeRegister.PROUDSOUL)).save(consumer);

        SlashBladeShapedRecipeBuilder.shaped(SlashBladeBuiltInRegistry.OROTIAGITO.location()).pattern("PSP")
                .pattern("SBS").pattern("PSP")
                .define('B',
                        SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                                .name(SlashBladeBuiltInRegistry.OROTIAGITO_SEALED.location()).killCount(1000)
                                .proudSoul(1000).refineCount(10).build()).toVanilla())
                .define('P', Ingredient.of(SlashBladeRegister.PROUDSOUL)).define('S', Ingredient.of(SlashBladeRegister.PROUDSOUL_SPHERE))
                .unlockedBy(getHasName(SlashBladeRegister.PROUDSOUL_SPHERE), has(SlashBladeRegister.PROUDSOUL_SPHERE)).save(consumer);

        SlashBladeShapedRecipeBuilder.shaped(SlashBladeBuiltInRegistry.DOUTANUKI.location()).pattern("  P")
                .pattern(" B ").pattern("P  ")
                .define('B',
                        SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                                .name(SlashBladeBuiltInRegistry.SABIGATANA.location()).killCount(100).proudSoul(1000)
                                .refineCount(10).build()).toVanilla())
                .define('P', Ingredient.of(SlashBladeRegister.PROUDSOUL_SPHERE))
                .unlockedBy(getHasName(SlashBladeRegister.PROUDSOUL_SPHERE), has(SlashBladeRegister.PROUDSOUL_SPHERE)).save(consumer);

        SlashBladeShapedRecipeBuilder.shaped(SlashBladeBuiltInRegistry.SABIGATANA.location()).pattern("  P")
                .pattern(" P ").pattern("B  ")
                .define('B',
                        SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                                .name(SlashBladeBuiltInRegistry.SABIGATANA.location()).addSwordType(SwordType.BROKEN)
                                .addSwordType(SwordType.SEALED).build()).toVanilla())
                .define('P', Ingredient.of(SlashBladeRegister.PROUDSOUL_INGOT))
                .unlockedBy(getHasName(SlashBladeRegister.PROUDSOUL_INGOT), has(SlashBladeRegister.PROUDSOUL_INGOT)).save(consumer);

        SlashBladeShapedRecipeBuilder.shaped(SlashBladeBuiltInRegistry.TUKUMO.location()).pattern("ESD").pattern("RBL")
                .pattern("ISG").define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('L', Tags.Items.STORAGE_BLOCKS_LAPIS).define('G', Tags.Items.STORAGE_BLOCKS_GOLD)
                .define('I', Tags.Items.STORAGE_BLOCKS_IRON).define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .define('E', Tags.Items.STORAGE_BLOCKS_EMERALD)
                .define('B',
                        SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                                .addEnchantment(
                                        new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_ASPECT), 1))
                                .build()).toVanilla())
                .define('S', Ingredient.of(SlashBladeRegister.PROUDSOUL_SPHERE))
                .unlockedBy(getHasName(SlashBladeRegister.SLASHBLADE), has(SlashBladeRegister.SLASHBLADE)).save(consumer);

        rodaiRecipe(SlashBladeBuiltInRegistry.RODAI_WOODEN.location(), Items.WOODEN_SWORD, consumer);
        rodaiRecipe(SlashBladeBuiltInRegistry.RODAI_STONE.location(), Items.STONE_SWORD, consumer);
        rodaiRecipe(SlashBladeBuiltInRegistry.RODAI_IRON.location(), Items.IRON_SWORD, consumer);
        rodaiRecipe(SlashBladeBuiltInRegistry.RODAI_GOLDEN.location(), Items.GOLDEN_SWORD, consumer);
        rodaiAdvRecipe(SlashBladeBuiltInRegistry.RODAI_DIAMOND.location(), Items.DIAMOND_SWORD, consumer);
        rodaiAdvRecipe(SlashBladeBuiltInRegistry.RODAI_NETHERITE.location(), Items.NETHERITE_SWORD, consumer);
    }

    private void rodaiRecipe(ResourceLocation rodai, ItemLike sword, Consumer<FinishedRecipe> consumer) {
        SlashBladeShapedRecipeBuilder.shaped(rodai).pattern("  P").pattern(" B ").pattern("WS ").define('B',
                        SlashBladeIngredient.of(SlashBladeRegister.SILVER,
                                RequestDefinition.Builder.newInstance().killCount(100).addSwordType(SwordType.BROKEN).build()).toVanilla())
                .define('W', Ingredient.of(sword)).define('S', Ingredient.of(Tags.Items.STRING))
                .define('P', Ingredient.of(SlashBladeRegister.PROUDSOUL_CRYSTAL))
                .unlockedBy(getHasName(SlashBladeRegister.SILVER), has(SlashBladeRegister.SILVER))
                .save(consumer);
    }

    private void rodaiAdvRecipe(ResourceLocation rodai, ItemLike sword, Consumer<FinishedRecipe> consumer) {
        SlashBladeShapedRecipeBuilder.shaped(rodai).pattern("  P").pattern(" B ").pattern("WS ").define('B',
                        SlashBladeIngredient.of(SlashBladeRegister.SILVER,
                                RequestDefinition.Builder.newInstance().killCount(100).addSwordType(SwordType.BROKEN).build()).toVanilla())
                .define('W', Ingredient.of(sword)).define('S', Ingredient.of(Tags.Items.STRING))
                .define('P', Ingredient.of(SlashBladeRegister.PROUDSOUL_TRAP))
                .unlockedBy(getHasName(SlashBladeRegister.SILVER), has(SlashBladeRegister.SILVER))
                .save(consumer);
    }

    private static ResourceLocation getEnchantmentID(Enchantment enchantment) {
        return BuiltInRegistries.ENCHANTMENT.getKey(enchantment);
    }
}
