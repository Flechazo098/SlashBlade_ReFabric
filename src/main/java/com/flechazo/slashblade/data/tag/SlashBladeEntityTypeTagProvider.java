package com.flechazo.slashblade.data.tag;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import com.flechazo.slashblade.SlashBladeRefabriced;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;


public class SlashBladeEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {


    public SlashBladeEntityTypeTagProvider (FabricDataOutput output, CompletableFuture<Provider> completableFuture) {
        super(output, completableFuture);
    }
    public SlashBladeEntityTypeTagProvider (FabricDataOutput output, CompletableFuture<Provider> completableFuture, String id) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(Provider lookupProvider) {

        this.tag(EntityTypeTags.ATTACKABLE_BLACKLIST).add(EntityType.VILLAGER.builtInRegistryHolder().key());

        this.tag(EntityTypeTags.ATTACKABLE_BLACKLIST).addOptional(new ResourceLocation("touhou_little_maid", "maid"));
    }

    public static class EntityTypeTags {
        public static final TagKey<EntityType<?>> ATTACKABLE_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE,
                SlashBladeRefabriced.prefix("blacklist/attackable"));
    }
}
