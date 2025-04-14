package com.flechazo.slashblade.data.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SlashBladeItemTags {
    public static final TagKey<Item> PROUD_SOULS = ItemTags.create(new ResourceLocation("slashblade", "proudsouls"));
    public static final TagKey<Item> BAMBOO = ItemTags.create(new ResourceLocation("forge", "bamboo"));
}
