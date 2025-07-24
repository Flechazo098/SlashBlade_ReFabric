package com.flechazo.slashblade.registry;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.registry.specialeffects.SpecialEffect;
import com.flechazo.slashblade.registry.specialeffects.WitherEdge;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class SpecialEffectsRegistry {
    public static final ResourceKey<Registry<SpecialEffect>> SPECIAL_EFFECTS_KEY = ResourceKey.createRegistryKey(
            new ResourceLocation(SlashBladeRefabriced.MODID, "special_effects"));

    public static final Registry<SpecialEffect> REGISTRY = FabricRegistryBuilder
            .createSimple(SPECIAL_EFFECTS_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    public static final SpecialEffect WITHER_EDGE = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "wither_edge"),
            new WitherEdge());

    public static void init() {
    }
}