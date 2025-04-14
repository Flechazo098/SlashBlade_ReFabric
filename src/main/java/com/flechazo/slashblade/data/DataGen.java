package com.flechazo.slashblade.data;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.data.builtin.SlashBladeBuiltInRegistry;
import com.flechazo.slashblade.data.builtin.SlashBladeEntityDropBuiltInRegistry;
import com.flechazo.slashblade.data.tag.SlashBladeEntityTypeTagProvider;
import com.flechazo.slashblade.event.drop.EntityDropEntry;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void dataGen(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        CompletableFuture<Provider> lookupProvider = event.getLookupProvider();
        PackOutput packOutput = dataGenerator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        final RegistrySetBuilder bladeBuilder = new RegistrySetBuilder().add(SlashBladeDefinition.REGISTRY_KEY,
                SlashBladeBuiltInRegistry::registerAll);

        final RegistrySetBuilder bladeDropBuilder = new RegistrySetBuilder().add(EntityDropEntry.REGISTRY_KEY,
                SlashBladeEntityDropBuiltInRegistry::registerAll);

        dataGenerator.addProvider(event.includeServer(), new SlashBladeRecipeProvider(packOutput));
        dataGenerator.addProvider(event.includeServer(),
                new DatapackBuiltinEntriesProvider(packOutput, lookupProvider, bladeBuilder, Set.of(SlashBladeRefabriced.MODID)) {

                    @Override
                    public String getName() {
                        return "SlashBlade Definition Registry";
                    }

                });
        dataGenerator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(packOutput, lookupProvider,
                bladeDropBuilder, Set.of(SlashBladeRefabriced.MODID)) {

            @Override
            public String getName() {
                return "SlashBlade Entity Drop Entry Registry";
            }

        });
        dataGenerator.addProvider(event.includeServer(),
                new SlashBladeEntityTypeTagProvider(packOutput, lookupProvider, SlashBladeRefabriced.MODID, existingFileHelper));
    }

}
