package com.flechazo.slashblade;

import com.flechazo.slashblade.init.SBItems;
import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.registry.SlashArtsRegistry;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SlashBladeCreativeGroup {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
			.create(Registries.CREATIVE_MODE_TAB, SlashBladeRefabriced.MODID);

	private static final CreativeModeTab SLASHBLADE = CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.slashblade")).icon(() -> {
				ItemStack stack = new ItemStack(SBItems.slashblade);
				stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> {
					s.setModel(new ResourceLocation(SlashBladeRefabriced.MODID, "model/named/yamato.obj"));
					s.setTexture(new ResourceLocation(SlashBladeRefabriced.MODID, "model/named/yamato.png"));
				});
				return stack;
			}).displayItems((features, output) -> {

				output.accept(SBItems.proudsoul);
				output.accept(SBItems.proudsoul_tiny);
				output.accept(SBItems.proudsoul_ingot);
				output.accept(SBItems.proudsoul_sphere);

				output.accept(SBItems.proudsoul_crystal);
				output.accept(SBItems.proudsoul_trapezohedron);
				fillSASpheres(output);
				output.accept(SBItems.bladestand_1);
				output.accept(SBItems.bladestand_1w);
				output.accept(SBItems.bladestand_2);
				output.accept(SBItems.bladestand_2w);
				output.accept(SBItems.bladestand_s);
				output.accept(SBItems.bladestand_v);

				output.accept(SBItems.slashblade_wood);
				output.accept(SBItems.slashblade_bamboo);
				output.accept(SBItems.slashblade_silverbamboo);
				output.accept(SBItems.slashblade_white);
				output.accept(SBItems.slashblade);

				fillBlades(features, output);
			}).build();

	public static final RegistryObject<CreativeModeTab> SLASHBLADE_GROUP = CREATIVE_MODE_TABS.register("slashblade",
			() -> SLASHBLADE);

	private static void fillBlades(CreativeModeTab.ItemDisplayParameters features, CreativeModeTab.Output output) {
		SlashBladeRefabriced.getSlashBladeDefinitionRegistry(features.holders()).listElements()
				.sorted(SlashBladeDefinition.COMPARATOR).forEach(entry -> {
					output.accept(entry.value().getBlade());
				});
	}

	private static void fillSASpheres(CreativeModeTab.Output output) {
		SlashArtsRegistry.REGISTRY.get().forEach(slashArts -> {
			ResourceLocation key = SlashArtsRegistry.REGISTRY.get().getKey(slashArts);
			if (slashArts.equals(SlashArtsRegistry.NONE.get()) || key == null)
				return;
			ItemStack sphere = new ItemStack(SBItems.proudsoul_sphere);
			CompoundTag tag = new CompoundTag();
			tag.putString("SpecialAttackType", key.toString());
			sphere.setTag(tag);
			output.accept(sphere);
		});
	}
}
