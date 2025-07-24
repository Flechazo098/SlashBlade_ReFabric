package com.flechazo.slashblade.data.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SlashBladeItemTags {
    public static final TagKey<Item> PROUD_SOULS = TagKey.create(Registries.ITEM, new ResourceLocation("slashblade", "proudsouls"));
    public static final TagKey<Item> BAMBOO = TagKey.create(Registries.ITEM, new ResourceLocation("c", "bamboo"));
}
