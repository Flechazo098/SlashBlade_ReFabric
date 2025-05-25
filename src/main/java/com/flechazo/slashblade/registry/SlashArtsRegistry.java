package com.flechazo.slashblade.registry;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.slasharts.SlashArts;
import net.minecraft.core.Registry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;

public class SlashArtsRegistry {
    public static final ResourceKey<Registry<SlashArts>> SLASH_ARTS_KEY = ResourceKey.createRegistryKey(
            new ResourceLocation(SlashBladeRefabriced.MODID, "slash_arts"));

    public static final Registry<SlashArts> REGISTRY = FabricRegistryBuilder
            .<SlashArts>createSimple(SLASH_ARTS_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    public static final SlashArts NONE = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "none"),
            new SlashArts((e) -> ComboStateRegistry.NONE.getId()));

    public static final SlashArts JUDGEMENT_CUT = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "judgement_cut"),
            new SlashArts((e) -> e.onGround() ? ComboStateRegistry.JUDGEMENT_CUT.getId()
                    : ComboStateRegistry.JUDGEMENT_CUT_SLASH_AIR.getId())
                    .setComboStateJust((e) -> ComboStateRegistry.JUDGEMENT_CUT_SLASH_JUST.getId())
                    .setComboStateSuper(e -> ComboStateRegistry.JUDGEMENT_CUT_END.getId()));

    public static final SlashArts SAKURA_END = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "sakura_end"),
            new SlashArts((e) -> e.onGround() ? ComboStateRegistry.SAKURA_END_LEFT.getId()
                    : ComboStateRegistry.SAKURA_END_LEFT_AIR.getId()));

    public static final SlashArts VOID_SLASH = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "void_slash"),
            new SlashArts((e) -> ComboStateRegistry.VOID_SLASH.getId()));

    public static final SlashArts CIRCLE_SLASH = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "circle_slash"),
            new SlashArts((e) -> ComboStateRegistry.CIRCLE_SLASH.getId()));

    public static final SlashArts DRIVE_VERTICAL = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "drive_vertical"),
            new SlashArts((e) -> ComboStateRegistry.DRIVE_VERTICAL.getId()));

    public static final SlashArts DRIVE_HORIZONTAL = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "drive_horizontal"),
            new SlashArts((e) -> ComboStateRegistry.DRIVE_HORIZONTAL.getId()));

    public static final SlashArts WAVE_EDGE = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "wave_edge"),
            new SlashArts((e) -> ComboStateRegistry.WAVE_EDGE_VERTICAL.getId()));

    public static final SlashArts PIERCING = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "piercing"),
            new SlashArts((e) -> ComboStateRegistry.PIERCING.getId())
                    .setComboStateJust((e) -> ComboStateRegistry.PIERCING_JUST.getId()));

    public static void init() {
    }
}