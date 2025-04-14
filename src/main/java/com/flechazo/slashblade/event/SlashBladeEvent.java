package com.flechazo.slashblade.event;

import com.flechazo.slashblade.capability.slashblade.BladeStateComponent;
import com.flechazo.slashblade.entity.BladeStandEntity;
import com.flechazo.slashblade.util.KnockBacks;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public abstract class SlashBladeEvent extends Event {
	private final ItemStack blade;
	private final BladeStateComponent state;
	public SlashBladeEvent(ItemStack blade, BladeStateComponent state) {
		this.blade = blade;
		this.state = state;
	}
	
	public ItemStack getBlade() {
		return blade;
	}

	public BladeStateComponent getSlashBladeState() {
		return state;
	}
	
	public static class PowerBladeEvent extends SlashBladeEvent {
		private final LivingEntity user;
		private boolean isPowered;
		public PowerBladeEvent(ItemStack blade, BladeStateComponent state, LivingEntity user, boolean isPowered) {
			super(blade, state);
			this.user = user;
			this.setPowered(isPowered);
		}
		
		public boolean isPowered() {
			return isPowered;
		}
		
		public void setPowered(boolean isPowered) {
			this.isPowered = isPowered;
		}
		
		public LivingEntity getUser() {
			return user;
		}
		
	}
	
	public static class UpdateAttackEvent extends SlashBladeEvent {
		private final double originDamage;
		private double newDamage;
		public UpdateAttackEvent(ItemStack blade, BladeStateComponent state, double damage) {
			super(blade, state);
			this.originDamage = damage;
			this.newDamage = damage;
		}
		public double getNewDamage() {
			return newDamage;
		}
		
		public void setNewDamage(double newDamage) {
			this.newDamage = newDamage;
		}
		
		public double getOriginDamage() {
			return originDamage;
		}		
	}
	
	@Cancelable
	public static class BladeStandAttackEvent extends SlashBladeEvent {
		private final BladeStandEntity bladeStand;
		private final DamageSource damageSource;
		public BladeStandAttackEvent(ItemStack blade, BladeStateComponent state, BladeStandEntity bladeStand, DamageSource damageSource) {
			super(blade, state);
			this.bladeStand = bladeStand;
			this.damageSource = damageSource;
		}
		
		public BladeStandEntity getBladeStand() {
			return bladeStand;
		}
		
		public DamageSource getDamageSource() {
			return damageSource;
		}
		
	}
	
	@Cancelable
	public static class HitEvent extends SlashBladeEvent {
		private final LivingEntity target;
		private final LivingEntity user;
		public HitEvent(ItemStack blade, BladeStateComponent state, LivingEntity target, LivingEntity user) {
			super(blade, state);
			this.target = target;
			this.user = user;
		}
		
		public LivingEntity getUser() {
			return user;
		}
		
		public LivingEntity getTarget() {
			return target;
		}
		
	}
	
	@Cancelable
	public static class UpdateEvent extends SlashBladeEvent {
		private final Level level;
		private final Entity entity;
		private final int itemSlot;
		private final boolean isSelected;
		
		public UpdateEvent(ItemStack blade, BladeStateComponent state,
				Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
			super(blade, state);
			this.level = worldIn;
			this.entity = entityIn;
			this.itemSlot = itemSlot;
			this.isSelected = isSelected;
		}
		
		public Level getLevel() {
			return level;
		}
		
		public Entity getEntity() {
			return entity;
		}
		
		public int getItemSlot() {
			return itemSlot;
		}
		
		public boolean isSelected() {
			return isSelected;
		}

	}
	
	@Cancelable
	public static class DoSlashEvent extends SlashBladeEvent {
		private final LivingEntity user;
		private float roll;
		private boolean critical;
		private double damage;
		private KnockBacks knockback;
		public DoSlashEvent(ItemStack blade, BladeStateComponent state, LivingEntity user,
							float roll, boolean critical, double damage, KnockBacks knockback) {
			super(blade, state);
			this.user = user;
			this.roll = roll;
			this.critical = critical;
			this.knockback = knockback;
			this.damage = damage;
		}
		
		public LivingEntity getUser() {
			return user;
		}

		public float getRoll() {
			return roll;
		}

		public void setRoll(float roll) {
			this.roll = roll;
		}

		public boolean isCritical() {
			return critical;
		}

		public void setCritical(boolean critical) {
			this.critical = critical;
		}

		public double getDamage() {
			return damage;
		}

		public void setDamage(double damage) {
			this.damage = damage;
		}

		public KnockBacks getKnockback() {
			return knockback;
		}

		public void setKnockback(KnockBacks knockback) {
			this.knockback = knockback;
		}
		
	}
}
