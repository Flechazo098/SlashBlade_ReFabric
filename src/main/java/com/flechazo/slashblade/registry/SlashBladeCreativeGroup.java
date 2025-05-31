package com.flechazo.slashblade.registry;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class SlashBladeCreativeGroup {

	private static void fillBlades(CreativeModeTab.ItemDisplayParameters features, CreativeModeTab.Output output) {
		SlashBladeRefabriced.getSlashBladeDefinitionRegistry(features.holders()).listElements()
				.sorted(SlashBladeDefinition.COMPARATOR).forEach(entry -> {
					ItemStack blade = entry.value().getBlade();
						output.accept(blade);
				});
	}

	private static void fillSASpheres(CreativeModeTab.Output output) {
		SlashArtsRegistry.REGISTRY.forEach(slashArts -> {
			ResourceLocation key = SlashArtsRegistry.REGISTRY.getKey(slashArts);
			if (slashArts.equals(SlashArtsRegistry.NONE) || key == null)
				return;
			ItemStack sphere = new ItemStack(SlashBladeRegister.PROUDSOUL_SPHERE);
			CompoundTag tag = new CompoundTag();
			tag.putString("SpecialAttackType", key.toString());
			sphere.setTag(tag);
			output.accept(sphere);
		});
	}

	public static CreativeModeTab SLASHBLADE;

	public static void init() {
		SLASHBLADE = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
				new ResourceLocation(SlashBladeRefabriced.MODID, "slashblade"),
				FabricItemGroup.builder()
						.title(Component.translatable("itemGroup.slashblade"))
						.icon(() -> {
							ItemStack stack = new ItemStack(SlashBladeRegister.SLASHBLADE);
							BladeStateHelper.getBladeState(stack).ifPresent(s -> {
								s.setModel(new ResourceLocation(SlashBladeRefabriced.MODID, "model/named/yamato.obj"));
								s.setTexture(new ResourceLocation(SlashBladeRefabriced.MODID, "model/named/yamato.png"));
							});
							return stack;
						})
						.displayItems((features, output) -> {
							output.accept(SlashBladeRegister.PROUDSOUL);
							output.accept(SlashBladeRegister.PROUDSOUL_TINY);
							output.accept(SlashBladeRegister.PROUDSOUL_INGOT);
							output.accept(SlashBladeRegister.PROUDSOUL_SPHERE);

							output.accept(SlashBladeRegister.PROUDSOUL_CRYSTAL);
							output.accept(SlashBladeRegister.PROUDSOUL_TRAP);
							fillSASpheres(output);

							output.accept(SlashBladeRegister.BLADESTAND_1);
							output.accept(SlashBladeRegister.BLADESTAND_1W);
							output.accept(SlashBladeRegister.BLADESTAND_2);
							output.accept(SlashBladeRegister.BLADESTAND_2W);
							output.accept(SlashBladeRegister.BLADESTAND_S);
							output.accept(SlashBladeRegister.BLADESTAND_V);

							output.accept(SlashBladeRegister.WOOD);
							output.accept(SlashBladeRegister.BAMBOO);
							output.accept(SlashBladeRegister.SILVER);
							output.accept(SlashBladeRegister.WHITE);
							output.accept(SlashBladeRegister.SLASHBLADE);

							fillBlades(features, output);
						})
						.build());
	}
}
