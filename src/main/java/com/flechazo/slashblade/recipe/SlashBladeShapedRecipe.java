package com.flechazo.slashblade.recipe;

import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.registry.SlashBladeRegister;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class SlashBladeShapedRecipe extends ShapedRecipe {

    public static final RecipeSerializer<SlashBladeShapedRecipe> SERIALIZER = new SlashBladeShapedRecipeSerializer<>(
            RecipeSerializer.SHAPED_RECIPE, SlashBladeShapedRecipe::new);

    private final ResourceLocation outputBlade;

    public SlashBladeShapedRecipe(ShapedRecipe compose, ResourceLocation outputBlade) {
        super(compose.getId(), compose.getGroup(), compose.category(), compose.getWidth(), compose.getHeight(),
                compose.getIngredients(), getResultBlade(outputBlade));
        this.outputBlade = outputBlade;
    }

    private static ItemStack getResultBlade(ResourceLocation outputBlade) {
        Item bladeItem = BuiltInRegistries.ITEM.containsKey(outputBlade) ? BuiltInRegistries.ITEM.get(outputBlade)
                : SlashBladeRegister.SLASHBLADE;

        return bladeItem.getDefaultInstance();
    }

    public ResourceLocation getOutputBlade() {
        return outputBlade;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        ItemStack result = SlashBladeShapedRecipe.getResultBlade(this.getOutputBlade());

        if (!BuiltInRegistries.ITEM.getKey(result.getItem()).equals(getOutputBlade())) {
            // 添加安全检查
            var registry = access.registry(SlashBladeDefinition.NAMED_BLADES_KEY);
            if (registry.isPresent()) {
                var definition = registry.get().get(getOutputBlade());
                if (definition != null) {
                    result = definition.getBlade(result.getItem());
                }
            }
        }

        return result;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
        var result = this.getResultItem(access);
        if (!(result.getItem() instanceof ItemSlashBlade)) {
        	result = new ItemStack(SlashBladeRegister.SLASHBLADE);
        }
        
        var resultState = BladeStateHelper.getBladeState(result).orElseThrow(NullPointerException::new);
        for (var stack : container.getItems()) {
            if (!(stack.getItem() instanceof ItemSlashBlade))
                continue;
            var ingredientState = BladeStateHelper.getBladeState(stack).orElseThrow(NullPointerException::new);

            resultState.setProudSoulCount(resultState.getProudSoulCount() + ingredientState.getProudSoulCount());
            resultState.setKillCount(resultState.getKillCount() + ingredientState.getKillCount());
            resultState.setRefine(resultState.getRefine() + ingredientState.getRefine());
            updateEnchantment(result, stack);
        }
        
        return result;
    }

    private void updateEnchantment(ItemStack result, ItemStack ingredient) {
        var newItemEnchants = EnchantmentHelper.getEnchantments(result);
        var oldItemEnchants = EnchantmentHelper.getEnchantments(ingredient);

        for (Map.Entry<Enchantment, Integer> entry : oldItemEnchants.entrySet()) {
            Enchantment enchant = entry.getKey();
            int srcLevel = entry.getValue();
            int destLevel = newItemEnchants.getOrDefault(enchant, 0);

            int finalLevel = Math.min(enchant.getMaxLevel(), Math.max(srcLevel, destLevel));

            if (enchant.canEnchant(result)) {
                boolean compatible = true;
                for (Enchantment existing : newItemEnchants.keySet()) {
                    if (existing != enchant && !enchant.isCompatibleWith(existing)) {
                        compatible = false;
                        break;
                    }
                }
                if (compatible) {
                    newItemEnchants.put(enchant, finalLevel);
                }
            }
        }

        EnchantmentHelper.setEnchantments(newItemEnchants, result);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

}
