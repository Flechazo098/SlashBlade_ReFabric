package com.flechazo.slashblade.mixin.event;

import com.flechazo.slashblade.SlashBladeConfig;
import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.capability.slashblade.BladeStateComponent;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.mixin.accessor.ItemCombinerMenuAccessor;
import com.flechazo.slashblade.util.AdvancementHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {


    @Inject(method = "createResult", at = @At("TAIL"))
    private void onCreateResult(CallbackInfo ci) {
        AnvilMenu self = (AnvilMenu) (Object) this;
        ItemCombinerMenuAccessor accessor = (ItemCombinerMenuAccessor) self;
        Container inputSlots = accessor.getInputSlots();
        ResultContainer resultSlots = accessor.getResultSlots();
        Player player = accessor.getPlayer();

        ItemStack base = inputSlots.getItem(0);
        ItemStack material = inputSlots.getItem(1);
        ItemStack output = ItemStack.EMPTY;

        if (base.isEmpty() || material.isEmpty()) return;

        if (BladeStateHelper.getBladeState(base).isEmpty())
            return;

        boolean isRepairable = base.getItem().isValidRepairItem(base, material);
        if (!isRepairable) return;

        int level = material.getItem().getEnchantmentValue();
        if (level < 0) return;

        ItemStack result = base.copy();
        int refineLimit = Math.max(10, level);

        int cost = 0;
        int levelCostBase = SlashBladeConfig.getRefineLevelCost();
        int costResult = levelCostBase * cost;
        while (cost < material.getCount()) {
            cost++;
            costResult = levelCostBase * cost;

            BladeStateHelper.getBladeState(result).ifPresent(s -> {
                s.setProudSoulCount(s.getProudSoulCount() + Math.min(5000, level * 10));
                if (s.getRefine() < refineLimit) {
                    s.setRefine(s.getRefine() + 1);
                    if (s.getRefine() < 200)
                        s.setMaxDamage(s.getMaxDamage() + 1);
                }

                result.setDamageValue(result.getDamageValue() - Math.max(1, level / 2));
                result.getOrCreateTag().put("bladeState", s.getActiveState());
            });

            boolean refineable = !player.getAbilities().instabuild && player.experienceLevel <= costResult;
            if (refineable)
                break;
        }

        if (!result.equals(base)) {
            resultSlots.setItem(0, result);

            self.cost.set(costResult);
            self.repairItemCountCost = material.getCount();
        }
    }

    @Inject(method = "onTake", at = @At("TAIL"))
    private void onTakeOutput(Player player, ItemStack output, CallbackInfo ci) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        AnvilMenu self = (AnvilMenu) (Object) this;
        ItemCombinerMenuAccessor accessor = (ItemCombinerMenuAccessor) self;
        Container inputSlots = accessor.getInputSlots();

        ItemStack base = inputSlots.getItem(0);
        ItemStack material = inputSlots.getItem(1);

        if (base.isEmpty() || material.isEmpty()) return;
        if (!(base.getItem() instanceof ItemSlashBlade)) return;

        boolean isRepairable = base.getItem().isValidRepairItem(base, material);
        if (!isRepairable) return;

        int before = BladeStateHelper.getBladeState(base).map(BladeStateComponent::getRefine).orElse(0);
        int after = BladeStateHelper.getBladeState(output).map(BladeStateComponent::getRefine).orElse(0);

        if (before < after) {
            AdvancementHelper.grantCriterion(serverPlayer, new ResourceLocation(SlashBladeRefabriced.MODID, "tips/refine"));
        }
    }
}
