package com.flechazo.slashblade.capability.slashblade;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class BladeStateHelper {

    /**
     * 获取物品的BladeState组件
     *
     * @param stack 物品堆
     * @return BladeState组件
     */
    public static Optional<BladeStateComponent> getBladeState(ItemStack stack) {
        return BladeStateComponentRegistry.BLADE_STATE.maybeGet(stack);
    }

    /**
     * 创建一个简单的BladeState组件
     *
     * @param stack   物品堆
     * @param model   模型
     * @param texture 纹理
     * @param attack  攻击力
     * @param damage  耐久
     * @return 简单的BladeState组件
     */
    public static BladeStateComponent createSimpleBladeState(ItemStack stack, ResourceLocation model, ResourceLocation texture, float attack, int damage) {
        return new SimpleBladeStateComponentImpl(stack, model, texture, attack, damage);
    }
}