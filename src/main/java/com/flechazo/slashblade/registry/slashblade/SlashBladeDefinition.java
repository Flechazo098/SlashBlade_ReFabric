package com.flechazo.slashblade.registry.slashblade;

import java.util.Comparator;
import java.util.List;

import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.capability.slashblade.BladeStateComponentImpl;
import net.minecraft.Util;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SlashBladeDefinition {

	public static final Codec<SlashBladeDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.optionalFieldOf("item", SlashBladeRefabriced.prefix("slashblade"))
					.forGetter(SlashBladeDefinition::getItemName),
			ResourceLocation.CODEC.fieldOf("name").forGetter(SlashBladeDefinition::getName),
			RenderDefinition.CODEC.fieldOf("render").forGetter(SlashBladeDefinition::getRenderDefinition),
			PropertiesDefinition.CODEC.fieldOf("properties").forGetter(SlashBladeDefinition::getStateDefinition),
			EnchantmentDefinition.CODEC.listOf().optionalFieldOf("enchantments", Lists.newArrayList())
					.forGetter(SlashBladeDefinition::getEnchantments))
			.apply(instance, SlashBladeDefinition::new));

	public static final ResourceKey<Registry<SlashBladeDefinition>> REGISTRY_KEY = ResourceKey
			.createRegistryKey(SlashBladeRefabriced.prefix("named_blades"));

	private final ResourceLocation item;
	private final ResourceLocation name;
	private final RenderDefinition renderDefinition;
	private final PropertiesDefinition stateDefinition;
	private final List<EnchantmentDefinition> enchantments;

	public SlashBladeDefinition(ResourceLocation name, RenderDefinition renderDefinition,
			PropertiesDefinition stateDefinition, List<EnchantmentDefinition> enchantments) {
		this(SlashBladeRefabriced.prefix("slashblade"), name, renderDefinition, stateDefinition, enchantments);
	}

	public SlashBladeDefinition(ResourceLocation item, ResourceLocation name, RenderDefinition renderDefinition,
			PropertiesDefinition stateDefinition, List<EnchantmentDefinition> enchantments) {
		this.item = item;
		this.name = name;
		this.renderDefinition = renderDefinition;
		this.stateDefinition = stateDefinition;
		this.enchantments = enchantments;
	}

	public ResourceLocation getItemName() {
		return item;
	}
	
	public ResourceLocation getName() {
		return name;
	}

	public String getTranslationKey() {
		return Util.makeDescriptionId("item", this.getName());
	}

	public RenderDefinition getRenderDefinition() {
		return renderDefinition;
	}

	public PropertiesDefinition getStateDefinition() {
		return stateDefinition;
	}

	public List<EnchantmentDefinition> getEnchantments() {
		return enchantments;
	}

	public ItemStack getBlade() {
		return getBlade(getItem());
	}

	public ItemStack getBlade(Item bladeItem) {
		ItemStack result = new ItemStack(bladeItem);
		var state = BladeStateHelper.getBladeState(result).orElse(new BladeStateComponentImpl(result));
		state.setNonEmpty();
		state.setBaseAttackModifier(this.stateDefinition.getBaseAttackModifier());
		state.setMaxDamage(this.stateDefinition.getMaxDamage());
		state.setComboRoot(this.stateDefinition.getComboRoot());
		state.setSlashArtsKey(this.stateDefinition.getSpecialAttackType());

		this.stateDefinition.getSpecialEffects().forEach(state::addSpecialEffect);

		this.stateDefinition.getDefaultType().forEach(type -> {
			switch (type) {
			case BEWITCHED -> state.setDefaultBewitched(true);
			case BROKEN -> {
				result.setDamageValue(result.getMaxDamage() - 1);
				state.setBroken(true);
			}
			case SEALED -> state.setSealed(true);
			default -> {
			}
			}
		});

		state.setModel(this.renderDefinition.getModelName());
		state.setTexture(this.renderDefinition.getTextureName());
		state.setColorCode(this.renderDefinition.getSummonedSwordColor());
		state.setEffectColorInverse(this.renderDefinition.isSummonedSwordColorInverse());
		state.setCarryType(this.renderDefinition.getStandbyRenderType());
		if (!this.getName().equals(SlashBladeRefabriced.prefix("none")))
			state.setTranslationKey(this.getTranslationKey());

		result.getOrCreateTag().put("bladeState", state.getActiveState());

		for (var instance : this.enchantments) {
			var enchantment = BuiltInRegistries.ENCHANTMENT.get(instance.getEnchantmentID());
			result.enchant(enchantment, instance.getEnchantmentLevel());

		}
		return result;
	}

	public Item getItem() {
		return BuiltInRegistries.ITEM.get(this.item);
	}
	
	

	public static final BladeComparator COMPARATOR = new BladeComparator();

	private static class BladeComparator implements Comparator<Reference<SlashBladeDefinition>> {
		@Override
		public int compare(Reference<SlashBladeDefinition> left, Reference<SlashBladeDefinition> right) {

			ResourceLocation leftKey = left.key().location();
			ResourceLocation rightKey = right.key().location();
			boolean checkSame = leftKey.getNamespace().equalsIgnoreCase(rightKey.getNamespace());
			if (!checkSame) {
				if (leftKey.getNamespace().equalsIgnoreCase(SlashBladeRefabriced.MODID))
					return -1;

				if (rightKey.getNamespace().equalsIgnoreCase(SlashBladeRefabriced.MODID))
					return 1;
			}
			String leftName = leftKey.toString();
			String rightName = rightKey.toString();

			return leftName.compareToIgnoreCase(rightName);
		}
	}
}
