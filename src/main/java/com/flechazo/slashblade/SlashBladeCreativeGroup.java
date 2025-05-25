package com.flechazo.slashblade;

import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.registry.SlashArtsRegistry;
import com.flechazo.slashblade.registry.SlashBladeRegister;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class SlashBladeCreativeGroup {


	private static final CreativeModeTab SLASHBLADE = registerCreativeModeTab( "slashblade",
			FabricItemGroup.builder()
			.title(Component.translatable("itemGroup.slashblade")).icon(() -> {
				ItemStack stack = new ItemStack(SlashBladeRegister.SLASHBLADE);
				BladeStateHelper.getBladeState(stack).ifPresent(s -> {
					s.setModel(new ResourceLocation(SlashBladeRefabriced.MODID, "model/named/yamato.obj"));
					s.setTexture(new ResourceLocation(SlashBladeRefabriced.MODID, "model/named/yamato.png"));
				});
				return stack;
			}).displayItems((features, output) -> {

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
			}).build());

	private static void fillBlades(CreativeModeTab.ItemDisplayParameters features, CreativeModeTab.Output output) {
		SlashBladeRefabriced.getSlashBladeDefinitionRegistry(features.holders()).listElements()
				.sorted(SlashBladeDefinition.COMPARATOR).forEach(entry -> {
					output.accept(entry.value().getBlade());
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
	private static CreativeModeTab registerCreativeModeTab(String name, CreativeModeTab tab) {
		return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(SlashBladeRefabriced.MODID, name), tab);
	}

	public static void init() {}
}
