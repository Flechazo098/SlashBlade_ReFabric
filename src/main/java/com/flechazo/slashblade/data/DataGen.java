package com.flechazo.slashblade.data;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.data.builtin.SlashBladeBuiltInRegistry;
import com.flechazo.slashblade.data.builtin.SlashBladeEntityDropBuiltInRegistry;
import com.flechazo.slashblade.data.tag.SlashBladeEntityTypeTagProvider;
import com.flechazo.slashblade.event.drop.EntityDropEntry;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import io.github.fabricators_of_create.porting_lib.data.DatapackBuiltinEntriesProvider;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;

public class DataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {

        FabricDataGenerator.Pack pack = generator.createPack();

        // 添加配方生成器（RecipeProvider 子类）
        pack.addProvider(SlashBladeRecipeProvider::new);

        final RegistrySetBuilder builder = new RegistrySetBuilder()
                .add(SlashBladeDefinition.NAMED_BLADES_KEY, SlashBladeBuiltInRegistry::registerAll)
                .add(EntityDropEntry.REGISTRY_KEY, SlashBladeEntityDropBuiltInRegistry::registerAll);

        pack.addProvider((dataOutput, registries) -> new DatapackBuiltinEntriesProvider(dataOutput, registries, builder, Set.of(SlashBladeRefabriced.MODID)) {
            @Override
            public String getName () {
                return "SlashBlade Combined Registry";
            }
        });

        // 添加标签生成器
        pack.addProvider((output, registries) -> new SlashBladeEntityTypeTagProvider(output, registries, SlashBladeRefabriced.MODID));
    }
}