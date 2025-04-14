package com.flechazo.slashblade.data.builtin;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.event.drop.EntityDropEntry;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class SlashBladeEntityDropBuiltInRegistry {
    public static final ResourceKey<EntityDropEntry> ENDER_DRAGON_YAMATO = register("ender_dragon_yamato");
    public static final ResourceKey<EntityDropEntry> WITHER_SANGE = register("wither_sange");

    public static final ResourceKey<EntityDropEntry> MINOTAUR_YASHA = register("minotaur_yasha");
    public static final ResourceKey<EntityDropEntry> MINOSHROOM_YASHA_TRUE = register("minoshroom_yasha_true");

    public static final ResourceKey<EntityDropEntry> NAGA_AGITO = register("naga_agito");
    public static final ResourceKey<EntityDropEntry> HYDRA_OROTIAGITO = register("hydra_orotiagito");

    public static void registerAll(BootstapContext<EntityDropEntry> bootstrap) {
        bootstrap.register(ENDER_DRAGON_YAMATO, new EntityDropEntry(new ResourceLocation("minecraft", "ender_dragon"),
                SlashBladeRefabriced.prefix("yamato_broken"), 1.0F, false, true, new Vec3(0F, 60F, 0F)));

        bootstrap.register(WITHER_SANGE, new EntityDropEntry(new ResourceLocation("minecraft", "wither"),
                SlashBladeRefabriced.prefix("sange"), 0.3F, true));

        bootstrap.register(MINOTAUR_YASHA, new EntityDropEntry(new ResourceLocation("twilightforest", "minotaur"),
                SlashBladeRefabriced.prefix("yasha"), 0.05F, true));

        bootstrap.register(MINOSHROOM_YASHA_TRUE, new EntityDropEntry(
                new ResourceLocation("twilightforest", "minoshroom"), SlashBladeRefabriced.prefix("yasha_true"), 0.2F, true));

        bootstrap.register(NAGA_AGITO, new EntityDropEntry(new ResourceLocation("twilightforest", "naga"),
                SlashBladeRefabriced.prefix("agito_rust"), 0.3F, false));

        bootstrap.register(HYDRA_OROTIAGITO, new EntityDropEntry(new ResourceLocation("twilightforest", "hydra"),
                SlashBladeRefabriced.prefix("orotiagito_rust"), 0.3F, false));
    }

    private static ResourceKey<EntityDropEntry> register(String id) {
        ResourceKey<EntityDropEntry> loc = ResourceKey.create(EntityDropEntry.REGISTRY_KEY, SlashBladeRefabriced.prefix(id));
        return loc;
    }
}
