package com.flechazo.slashblade.registry;

import com.flechazo.slashblade.SlashBladeRefabriced;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class SlashBladeStats {
    public static ResourceLocation SWORD_SUMMONED;

    public static void register() {
        SWORD_SUMMONED = registerCustomStat("sword_summoned");
    }

    private static ResourceLocation registerCustomStat(String name) {
        ResourceLocation id = new ResourceLocation(SlashBladeRefabriced.MODID, name);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, id, id);
        Stats.CUSTOM.get(id, StatFormatter.DEFAULT);
        return id;
    }
}
