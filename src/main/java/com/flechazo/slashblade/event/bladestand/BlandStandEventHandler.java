package com.flechazo.slashblade.event.bladestand;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.data.builtin.SlashBladeBuiltInRegistry;
import com.flechazo.slashblade.data.tag.SlashBladeItemTags;
import com.flechazo.slashblade.entity.BladeStandEntity;
import com.flechazo.slashblade.event.SlashBladeEvent;
import com.flechazo.slashblade.registry.SlashArtsRegistry;
import com.flechazo.slashblade.recipe.RequestDefinition;
import com.flechazo.slashblade.recipe.SlashBladeIngredient;
import com.flechazo.slashblade.registry.SlashBladeRegister;
import com.flechazo.slashblade.registry.SpecialEffectsRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class BlandStandEventHandler {

	public static void init() {
		// 注册所有事件处理器
		SlashBladeEvent.BLADE_STAND_ATTACK.register(BlandStandEventHandler::eventKoseki);
		SlashBladeEvent.BLADE_STAND_ATTACK.register(BlandStandEventHandler::eventChangeSE);
		SlashBladeEvent.BLADE_STAND_ATTACK.register(BlandStandEventHandler::eventChangeSA);
		SlashBladeEvent.BLADE_STAND_ATTACK.register(BlandStandEventHandler::eventCopySE);
		SlashBladeEvent.BLADE_STAND_ATTACK.register(BlandStandEventHandler::eventCopySA);
		SlashBladeEvent.BLADE_STAND_ATTACK.register(BlandStandEventHandler::eventProudSoulEnchantment);
	}

	public static void eventKoseki(SlashBladeEvent.BladeStandAttackEvent event) {
		var slashBladeDefinitionRegistry = SlashBladeRefabriced.getSlashBladeDefinitionRegistry(event.getBladeStand().level());
		if(!slashBladeDefinitionRegistry.containsKey(SlashBladeBuiltInRegistry.KOSEKI.location()))
			return;
		if (!(event.getDamageSource().getEntity() instanceof WitherBoss))
			return;
		if(!event.getDamageSource().is(DamageTypeTags.IS_EXPLOSION))
			return;
		var in = SlashBladeIngredient.of(RequestDefinition.Builder.newInstance().build());
		if(!in.test(event.getBlade()))
			return;
		event.getBladeStand().setItem(slashBladeDefinitionRegistry.get(SlashBladeBuiltInRegistry.KOSEKI).getBlade());
		event.setCanceled(true);
	}

	public static void eventChangeSE(SlashBladeEvent.BladeStandAttackEvent event) {
		if (!(event.getDamageSource().getEntity() instanceof ServerPlayer))
			return;
		Player player = (Player) event.getDamageSource().getEntity();
		ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
		ItemStack blade = event.getBlade();
		if (blade.isEmpty())
			return;
		if (!stack.is(SlashBladeRegister.PROUDSOUL_CRYSTAL))
			return;
		var world = player.level();
		var state = event.getSlashBladeState();

		if (stack.getTag() == null)
			return;

		CompoundTag tag = stack.getTag();
		if (tag.contains("SpecialEffectType")) {
			var bladeStand = event.getBladeStand();
			ResourceLocation SEKey = new ResourceLocation(tag.getString("SpecialEffectType"));
			if (!(SpecialEffectsRegistry.REGISTRY.containsKey(SEKey)))
				return;
			if (state.hasSpecialEffect(SEKey))
				return;
			state.addSpecialEffect(SEKey);
			RandomSource random = player.getRandom();
			world.playSound(bladeStand, bladeStand.getPos(),
					SoundEvents.WITHER_SPAWN, SoundSource.BLOCKS, 1f, 1f);
			for(int i = 0; i < 32; ++i) {
				if(player.level().isClientSide())
					break;
				double xDist = (random.nextFloat() * 2.0F - 1.0F);
				double yDist = (random.nextFloat() * 2.0F - 1.0F);
				double zDist = (random.nextFloat() * 2.0F - 1.0F);
				if (!(xDist * xDist + yDist * yDist + zDist * zDist > 1.0D)) {
					double x = bladeStand.getX(xDist / 4.0D);
					double y = bladeStand.getY(0.5D + yDist / 4.0D);
					double z = bladeStand.getZ(zDist / 4.0D);
					((ServerLevel)world).sendParticles(ParticleTypes.PORTAL, x, y, z,0, xDist, yDist + 0.2D, zDist,1);
				}
			}
			if (!player.isCreative())
				stack.shrink(1);
			event.setCanceled(true);
		}
	}

	public static void eventChangeSA(SlashBladeEvent.BladeStandAttackEvent event) {
		if(!(event.getDamageSource().getEntity() instanceof ServerPlayer))
			return;
		Player player = (Player) event.getDamageSource().getEntity();
		ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
		CompoundTag tag = stack.getTag();

		if (!stack.is(SlashBladeRegister.PROUDSOUL_SPHERE) || tag == null || !tag.contains("SpecialAttackType"))
			return;

		ResourceLocation SAKey = new ResourceLocation(tag.getString("SpecialAttackType"));
		if (!SlashArtsRegistry.REGISTRY.containsKey(SAKey))
			return;

		ItemStack blade = event.getBlade();

		// 使用BladeStateHelper替代Capability
		BladeStateHelper.getBladeState(blade).ifPresent(state -> {
			if (!SAKey.equals(state.getSlashArtsKey())) {
				state.setSlashArtsKey(SAKey);

				RandomSource random = player.getRandom();
				BladeStandEntity bladeStand = event.getBladeStand();
				player.level().playSound(bladeStand, bladeStand.getPos(),
						SoundEvents.WITHER_SPAWN, SoundSource.BLOCKS, 1f, 1f);
				for(int i = 0; i < 32; ++i) {
					if(player.level().isClientSide())
						break;
					double xDist = (random.nextFloat() * 2.0F - 1.0F);
					double yDist = (random.nextFloat() * 2.0F - 1.0F);
					double zDist = (random.nextFloat() * 2.0F - 1.0F);
					if (!(xDist * xDist + yDist * yDist + zDist * zDist > 1.0D)) {
						double x = bladeStand.getX(xDist / 4.0D);
						double y = bladeStand.getY(0.5D + yDist / 4.0D);
						double z = bladeStand.getZ(zDist / 4.0D);
						((ServerLevel)player.level()).sendParticles(ParticleTypes.PORTAL, x, y, z,0, xDist, yDist + 0.2D, zDist,1);
					}
				}

				if (!player.isCreative()){
					stack.shrink(1);
				}
			}
		});
		event.setCanceled(true);//防止掉落拔刀
	}

	public static void eventCopySE(SlashBladeEvent.BladeStandAttackEvent event) {
		if(!(event.getDamageSource().getEntity() instanceof ServerPlayer))
			return;
		Player player = (Player) event.getDamageSource().getEntity();
		ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
		ItemStack blade = event.getBlade();
		if (blade.isEmpty())
			return;
		if (!stack.is(SlashBladeRegister.PROUDSOUL_CRYSTAL))
			return;
		var world = player.level();
		var state = event.getSlashBladeState();
		var bladeStand = event.getBladeStand();
		var specialEffects = state.getSpecialEffects();

		for (var se : specialEffects) {
			if (!SpecialEffectsRegistry.REGISTRY.containsKey(se))
				continue;
			if (!SpecialEffectsRegistry.REGISTRY.get(se).isCopiable())
				continue;
			ItemStack orb = new ItemStack(SlashBladeRegister.PROUDSOUL_CRYSTAL);
			CompoundTag tag = new CompoundTag();
			tag.putString("SpecialEffectType", se.toString());
			orb.setTag(tag);
			if (!player.isCreative())
				stack.shrink(1);
			RandomSource random = player.getRandom();
			world.playSound(bladeStand, bladeStand.getPos(),
					SoundEvents.WITHER_SPAWN, SoundSource.BLOCKS, 1f, 1f);
			for(int i = 0; i < 32; ++i) {
				if(world.isClientSide())
					break;
				double xDist = (random.nextFloat() * 2.0F - 1.0F);
				double yDist = (random.nextFloat() * 2.0F - 1.0F);
				double zDist = (random.nextFloat() * 2.0F - 1.0F);
				if (!(xDist * xDist + yDist * yDist + zDist * zDist > 1.0D)) {
					double x = bladeStand.getX(xDist / 4.0D);
					double y = bladeStand.getY(0.5D + yDist / 4.0D);
					double z = bladeStand.getZ(zDist / 4.0D);
					((ServerLevel)world).sendParticles(ParticleTypes.PORTAL, x, y, z,0, xDist, yDist + 0.2D, zDist,1);
				}
			}
			player.drop(orb, true);
			if(SpecialEffectsRegistry.REGISTRY.get(se).isRemovable())
				state.removeSpecialEffect(se);
			event.setCanceled(true);
			return;
		}
	}

	public static void eventCopySA(SlashBladeEvent.BladeStandAttackEvent event) {
		if (!(event.getDamageSource().getEntity() instanceof Player))
			return;
		Player player = (Player) event.getDamageSource().getEntity();
		ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
		ItemStack blade = event.getBlade();
		if (blade.isEmpty())
			return;
		if (!stack.is(SlashBladeRegister.PROUDSOUL_INGOT) || !stack.isEnchanted())
			return;
		var world = player.level();
		var state = event.getSlashBladeState();
		var bladeStand = event.getBladeStand();
		var enchantments = EnchantmentHelper.getEnchantments(stack).keySet();
		ResourceLocation SA = state.getSlashArtsKey();
		if (SA != null && !SA.equals(SlashArtsRegistry.NONE.getId())) {
			ItemStack orb = new ItemStack(SlashBladeRegister.PROUDSOUL_SPHERE);
			for (Enchantment e : enchantments)
			{
				if (EnchantmentHelper.getItemEnchantmentLevel(e, blade) < e.getMaxLevel())
					return;
			}
			CompoundTag tag = new CompoundTag();
			tag.putString("SpecialAttackType", state.getSlashArtsKey().toString());
			orb.setTag(tag);

			if (!player.isCreative())
				stack.shrink(1);
			world.playSound(bladeStand, bladeStand.getPos(), SoundEvents.WITHER_SPAWN, SoundSource.BLOCKS, 1f, 1f);
			RandomSource random = player.getRandom();
			for(int i = 0; i < 32; ++i) {
				if(world.isClientSide())
					break;
				double xDist = (random.nextFloat() * 2.0F - 1.0F);
				double yDist = (random.nextFloat() * 2.0F - 1.0F);
				double zDist = (random.nextFloat() * 2.0F - 1.0F);
				if (!(xDist * xDist + yDist * yDist + zDist * zDist > 1.0D)) {
					double x = bladeStand.getX(xDist / 4.0D);
					double y = bladeStand.getY(0.5D + yDist / 4.0D);
					double z = bladeStand.getZ(zDist / 4.0D);
					((ServerLevel)world).sendParticles(ParticleTypes.PORTAL, x, y, z,0, xDist, yDist + 0.2D, zDist,1);
				}
			}
			player.drop(orb, true);
			event.setCanceled(true);
		}
	}

	// 优先级在Fabric中通过注册顺序处理，这里保持为最后注册的事件
	public static void eventProudSoulEnchantment(SlashBladeEvent.BladeStandAttackEvent event) {
		if (!(event.getDamageSource().getEntity() instanceof Player))
			return;
		Player player = (Player) event.getDamageSource().getEntity();
		ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
		ItemStack blade = event.getBlade();

		if (blade.isEmpty())
			return;

		if (!stack.is(SlashBladeItemTags.PROUD_SOULS))
			return;

		if (!stack.isEnchanted())
			return;

		var world = player.level();
		var random = world.getRandom();
		var bladeStand = event.getBladeStand();
		Map<Enchantment, Integer> currentBladeEnchantments = EnchantmentHelper.getEnchantments(blade);
		Map<Enchantment, Integer> proudEnchants = EnchantmentHelper.getEnchantments(stack);
		for (var entry : proudEnchants.entrySet()) {
			if(event.isCanceled())
				return;

			Enchantment enchantment = entry.getKey();
			int proudLevel = entry.getValue();
			if (!enchantment.canEnchant(blade))
				return;

			var probability = 1.0F;
			if (stack.is(SlashBladeRegister.PROUDSOUL_TINY))
				probability = 0.25F;
			if (stack.is(SlashBladeRegister.PROUDSOUL))
				probability = 0.5F;
			if (stack.is(SlashBladeRegister.PROUDSOUL_INGOT))
				probability = 0.75F;
			if (random.nextFloat() <= probability) {
				int enchantLevel = Math.min(enchantment.getMaxLevel(), EnchantmentHelper.getItemEnchantmentLevel(enchantment, blade) + 1);
				currentBladeEnchantments.put(enchantment, enchantLevel);
				EnchantmentHelper.setEnchantments(currentBladeEnchantments, blade);
				world.playSound(bladeStand, bladeStand.getPos(), SoundEvents.WITHER_SPAWN, SoundSource.BLOCKS, 1f, 1f);
				for(int i = 0; i < 32; ++i) {
					if(player.level().isClientSide())
						break;
					double xDist = (random.nextFloat() * 2.0F - 1.0F);
					double yDist = (random.nextFloat() * 2.0F - 1.0F);
					double zDist = (random.nextFloat() * 2.0F - 1.0F);
					if (!(xDist * xDist + yDist * yDist + zDist * zDist > 1.0D)) {
						double x = bladeStand.getX(xDist / 4.0D);
						double y = bladeStand.getY(0.5D + yDist / 4.0D);
						double z = bladeStand.getZ(zDist / 4.0D);
						((ServerLevel)world).sendParticles(ParticleTypes.PORTAL, x, y, z,0, xDist, yDist + 0.2D, zDist,1);
					}
				}
			}
			if (!player.isCreative())
				stack.shrink(1);
			event.setCanceled(true);
		}
	}
}