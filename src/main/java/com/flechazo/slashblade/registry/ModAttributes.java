package com.flechazo.slashblade.registry;

import com.flechazo.slashblade.SlashBladeRefabriced;
import net.minecraft.core.Registry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;

public class ModAttributes {
    // 创建自定义属性注册表的ResourceKey
    public static final ResourceKey<Registry<Attribute>> ATTRIBUTES_KEY = ResourceKey.createRegistryKey(
            new ResourceLocation(SlashBladeRefabriced.MODID, "attributes"));

    // 创建自定义属性注册表
    public static final Registry<Attribute> ATTRIBUTES = FabricRegistryBuilder
            .<Attribute>createSimple(ATTRIBUTES_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    // 拔刀剑伤害属性
    public static final Attribute SLASHBLADE_DAMAGE = Registry.register(ATTRIBUTES,
            new ResourceLocation(SlashBladeRefabriced.MODID, "slashblade_damage"),
            new RangedAttribute("attribute.name.generic.slashblade_damage", 1.0d, 0.0d, 512.0d).setSyncable(true));

    public static Attribute getSlashBladeDamage() {
        return SLASHBLADE_DAMAGE;
    }

    // 初始化方法，在主类中调用
    public static void init() {
        // 注册完成后的其他初始化逻辑
    }
}