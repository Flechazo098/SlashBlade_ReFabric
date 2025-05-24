package com.flechazo.slashblade.event;

import com.flechazo.slashblade.capability.slashblade.BladeStateComponent;
import com.flechazo.slashblade.entity.BladeStandEntity;
import com.flechazo.slashblade.util.KnockBacks;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * SlashBlade事件基类
 * 自定义事件系统
 */
public abstract class SlashBladeEvent {
	private final ItemStack blade;
	private final BladeStateComponent state;

	// 定义事件总线实例
	public static final EventBus<BladeStandAttackEvent> BLADE_STAND_ATTACK = new EventBus<>();

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

	/**
	 * 表示是否可以取消的事件
	 */
	public interface Cancelable {
		boolean isCanceled();
		void setCanceled(boolean canceled);
	}

	/**
	 * 刀剑充能事件
	 */
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

	/**
	 * 更新攻击事件
	 */
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

	/**
	 * 刀架攻击事件
	 */
	public static class BladeStandAttackEvent extends SlashBladeEvent implements Cancelable {
		private final BladeStandEntity bladeStand;
		private final DamageSource damageSource;
		private boolean canceled = false;

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

		@Override
		public boolean isCanceled() {
			return canceled;
		}

		@Override
		public void setCanceled(boolean canceled) {
			this.canceled = canceled;
		}
	}

	/**
	 * 命中事件
	 */
	public static class HitEvent extends SlashBladeEvent implements Cancelable {
		private final LivingEntity target;
		private final LivingEntity user;
		private boolean canceled = false;

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

		@Override
		public boolean isCanceled() {
			return canceled;
		}

		@Override
		public void setCanceled(boolean canceled) {
			this.canceled = canceled;
		}
	}

	/**
	 * 更新事件
	 */
	public static class UpdateEvent extends SlashBladeEvent implements Cancelable {
		private final Level level;
		private final Entity entity;
		private final int itemSlot;
		private final boolean isSelected;
		private boolean canceled = false;

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

		@Override
		public boolean isCanceled() {
			return canceled;
		}

		@Override
		public void setCanceled(boolean canceled) {
			this.canceled = canceled;
		}
	}

	/**
	 * 执行斩击事件
	 */
	public static class DoSlashEvent extends SlashBladeEvent implements Cancelable {
		private final LivingEntity user;
		private float roll;
		private boolean critical;
		private double damage;
		private KnockBacks knockback;
		private boolean canceled = false;

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

		@Override
		public boolean isCanceled() {
			return canceled;
		}

		@Override
		public void setCanceled(boolean canceled) {
			this.canceled = canceled;
		}
	}

	/**
	 * 事件总线，用于注册和触发事件
	 * @param <T> 事件类型
	 */
	public static class EventBus<T extends SlashBladeEvent> {
		private final List<Consumer<T>> listeners = new ArrayList<>();

		/**
		 * 注册事件监听器
		 * @param listener 事件处理函数
		 */
		public void register(Consumer<T> listener) {
			listeners.add(listener);
		}

		/**
		 * 触发事件
		 * @param event 要触发的事件
		 */
		public void post(T event) {
			for (Consumer<T> listener : listeners) {
				listener.accept(event);
				// 如果事件可取消且已被取消，则停止处理
				if (event instanceof Cancelable && ((Cancelable) event).isCanceled()) {
					break;
				}
			}
		}
	}
}