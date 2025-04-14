package com.flechazo.slashblade.registry;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.registry.specialeffects.SpecialEffect;
import com.flechazo.slashblade.registry.specialeffects.WitherEdge;
import net.minecraft.core.Registry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;

public class SpecialEffectsRegistry {
    // 创建特殊效果注册表的ResourceKey
    public static final ResourceKey<Registry<SpecialEffect>> SPECIAL_EFFECTS_KEY = ResourceKey.createRegistryKey(
            new ResourceLocation(SlashBladeRefabriced.MODID, "special_effects"));

    // 创建特殊效果注册表
    public static final Registry<SpecialEffect> REGISTRY = FabricRegistryBuilder
            .<SpecialEffect>createSimple(SPECIAL_EFFECTS_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    // 凋零之刃效果
    public static final SpecialEffect WITHER_EDGE = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "wither_edge"),
            new WitherEdge());

    // 初始化方法，在主类中调用
    public static void init() {
        // 注册完成后的其他初始化逻辑
    }
}