package com.flechazo.slashblade.thetwilightforest.mixin;

import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.util.EnchantmentsHelper;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.inventory.UncraftingMenu;

@Mixin(UncraftingMenu.class)
public class UncraftingMenuMixin {
    @Inject(method = "matches", at = @At("RETURN"), cancellable = true, remap = false)
    private static void onMatches(ItemStack input, ItemStack output, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        // 提前排除非拔刀剑类的情况
        if (!(input.getItem() instanceof ItemSlashBlade) || !(output.getItem() instanceof ItemSlashBlade)) {
            return;
        }

        // 获取刀状态（若缺失能力则直接抛出异常）
        var inputState = BladeStateHelper.getBladeState(input).orElseThrow(NullPointerException::new);
        var outputState = BladeStateHelper.getBladeState(output).orElseThrow(NullPointerException::new);

        // 判断是否为同一类刀
        if (!inputState.getTranslationKey().equals(outputState.getTranslationKey())) {
            cir.setReturnValue(false);
            return;
        }

        // 判断断刀状态是否一致
        if (inputState.isBroken() != outputState.isBroken()) {
            cir.setReturnValue(false);
            return;
        }

        // 判断附魔是否一致
        if (!EnchantmentsHelper.hasEnchantmentsMatch(input, output)) {
            cir.setReturnValue(false);
            return;
        }

        // 所有条件满足时不做更改保持原true返回值
    }
}
