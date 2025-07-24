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
 */
public abstract class SlashBladeEvent {
    private final ItemStack blade;
    private final BladeStateComponent state;

    public static final EventBus<BladeStandAttackEvent> BLADE_STAND_ATTACK = new EventBus<>();
    public static final EventBus<UpdateEvent> UPDATE_EVENT = new EventBus<>();
    public static final EventBus<HitEvent> HIT_EVENT = new EventBus<>();
    public static final EventBus<PowerBladeEvent> POWER_BLADE_EVENT = new EventBus<>();
    public static final EventBus<DoSlashEvent> DO_SLASH_EVENT = new EventBus<>();
    public static final EventBus<UpdateAttackEvent> UPDATE_ATTACK_EVENT = new EventBus<>();
    public static final EventBus<InputCommandEvent> INPUT_COMMAND = new EventBus<>();

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

    public interface Cancelable {
        boolean isCanceled();

        void setCanceled(boolean canceled);
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
     * An event bus that registers and triggers events
     *
     * @param <T> The type of event
     */
    public static class EventBus<T extends SlashBladeEvent> {
        private final List<Consumer<T>> listeners = new ArrayList<>();

        /**
         * register event listener
         *
         * @param listener Event listener
         */
        public void register(Consumer<T> listener) {
            listeners.add(listener);
        }

        /**
         * Trigger event
         *
         * @param event The event to be triggered
         * @return true if event was canceled (i.e. someone called event.setCanceled(true))
         */
        public boolean post(T event) {
            for (Consumer<T> listener : listeners) {
                listener.accept(event);
                // 如果事件可取消且已被取消，则停止处理
                if (event instanceof Cancelable && ((Cancelable) event).isCanceled()) {
                    break;
                }
            }
            return false;
        }
    }
}