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
    // 创建斩击技注册表的ResourceKey
    public static final ResourceKey<Registry<SlashArts>> SLASH_ARTS_KEY = ResourceKey.createRegistryKey(
            new ResourceLocation(SlashBladeRefabriced.MODID, "slash_arts"));

    // 创建斩击技注册表
    public static final Registry<SlashArts> REGISTRY = FabricRegistryBuilder
            .<SlashArts>createSimple(SLASH_ARTS_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    // 空斩击技
    public static final SlashArts NONE = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "none"),
            new SlashArts((e) -> ComboStateRegistry.NONE.getId()));

    // 次元斩
    public static final SlashArts JUDGEMENT_CUT = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "judgement_cut"),
            new SlashArts((e) -> e.onGround() ? ComboStateRegistry.JUDGEMENT_CUT.getId()
                    : ComboStateRegistry.JUDGEMENT_CUT_SLASH_AIR.getId())
                    .setComboStateJust((e) -> ComboStateRegistry.JUDGEMENT_CUT_SLASH_JUST.getId())
                    .setComboStateSuper(e -> ComboStateRegistry.JUDGEMENT_CUT_END.getId()));

    // 樱花终结
    public static final SlashArts SAKURA_END = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "sakura_end"),
            new SlashArts((e) -> e.onGround() ? ComboStateRegistry.SAKURA_END_LEFT.getId()
                    : ComboStateRegistry.SAKURA_END_LEFT_AIR.getId()));

    // 虚空斩
    public static final SlashArts VOID_SLASH = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "void_slash"),
            new SlashArts((e) -> ComboStateRegistry.VOID_SLASH.getId()));

    // 环形斩
    public static final SlashArts CIRCLE_SLASH = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "circle_slash"),
            new SlashArts((e) -> ComboStateRegistry.CIRCLE_SLASH.getId()));

    // 纵向驱动斩
    public static final SlashArts DRIVE_VERTICAL = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "drive_vertical"),
            new SlashArts((e) -> ComboStateRegistry.DRIVE_VERTICAL.getId()));

    // 横向驱动斩
    public static final SlashArts DRIVE_HORIZONTAL = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "drive_horizontal"),
            new SlashArts((e) -> ComboStateRegistry.DRIVE_HORIZONTAL.getId()));

    // 波刃
    public static final SlashArts WAVE_EDGE = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "wave_edge"),
            new SlashArts((e) -> ComboStateRegistry.WAVE_EDGE_VERTICAL.getId()));

    // 穿刺
    public static final SlashArts PIERCING = Registry.register(REGISTRY,
            new ResourceLocation(SlashBladeRefabriced.MODID, "piercing"),
            new SlashArts((e) -> ComboStateRegistry.PIERCING.getId())
                    .setComboStateJust((e) -> ComboStateRegistry.PIERCING_JUST.getId()));

    // 初始化方法，在主类中调用
    public static void init() {
        // 注册完成后的其他初始化逻辑
    }
}