package com.flechazo.slashblade.data;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.data.builtin.SlashBladeBuiltInRegistry;
import com.flechazo.slashblade.data.builtin.SlashBladeEntityDropBuiltInRegistry;
import com.flechazo.slashblade.data.tag.SlashBladeEntityTypeTagProvider;
import com.flechazo.slashblade.event.drop.EntityDropEntry;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import io.github.fabricators_of_create.porting_lib.data.DatapackBuiltinEntriesProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;

import java.util.Set;

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
            public String getName() {
                return "SlashBlade Combined Registry";
            }
        });

        // 添加标签生成器
        pack.addProvider((output, registries) -> new SlashBladeEntityTypeTagProvider(output, registries, SlashBladeRefabriced.MODID));
    }
}