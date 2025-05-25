package com.flechazo.slashblade.util;

import com.flechazo.slashblade.SlashBladeConfig;
import com.flechazo.slashblade.capability.concentrationrank.ConcentrationRankComponent;
import com.flechazo.slashblade.capability.concentrationrank.ConcentrationRankHelper;
import com.flechazo.slashblade.capability.slashblade.BladeStateComponent;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.registry.EntityTypeRegister;
import com.google.common.collect.Lists;
import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.ability.ArrowReflector;
import com.flechazo.slashblade.ability.TNTExtinguisher;
import com.flechazo.slashblade.entity.EntityAbstractSummonedSword;
import com.flechazo.slashblade.entity.EntitySlashEffect;
import com.flechazo.slashblade.entity.IShootable;
import com.flechazo.slashblade.event.SlashBladeEvent;
import com.flechazo.slashblade.registry.ModAttributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;

public class AttackManager {
	
	public static boolean isPowered(LivingEntity entity) {
		ItemStack blade = entity.getMainHandItem();
		boolean result = entity.hasEffect(MobEffects.DAMAGE_BOOST) || entity.hasEffect(MobEffects.HUNGER);
		if(BladeStateHelper.getBladeState(blade).isPresent()) {
			var state = BladeStateHelper.getBladeState(blade).orElseThrow(NullPointerException::new);
			var event = new SlashBladeEvent.PowerBladeEvent(blade, state, entity, result);
			SlashBladeEvent.POWER_BLADE_EVENT.post(event);
			result = event.isPowered();
		}
		
		return result;
	}
	
    static public void areaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit) {
        areaAttack(playerIn, beforeHit, 1.0f, true, true, false);
    }

    static public EntitySlashEffect doSlash(LivingEntity playerIn, float roll) {
        return doSlash(playerIn, roll, false);
    }

    static public EntitySlashEffect doSlash(LivingEntity playerIn, float roll, boolean mute) {
        return doSlash(playerIn, roll, mute, false);
    }

    static public EntitySlashEffect doSlash(LivingEntity playerIn, float roll, boolean mute, boolean critical) {
        return doSlash(playerIn, roll, mute, critical, 1.0);
    }

    static public EntitySlashEffect doSlash(LivingEntity playerIn, float roll, boolean mute, boolean critical,
                                            double comboRatio) {
        return doSlash(playerIn, roll, Vec3.ZERO, mute, critical, comboRatio);
    }

    static public EntitySlashEffect doSlash(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute,
                                            boolean critical, double comboRatio) {
        return doSlash(playerIn, roll, centerOffset, mute, critical, comboRatio, KnockBacks.cancel);
    }

    static public EntitySlashEffect doSlash(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute,
                                            boolean critical, double comboRatio, KnockBacks knockback) {

        int colorCode = BladeStateHelper.getBladeState(playerIn.getMainHandItem())
                .map(BladeStateComponent::getColorCode).orElse(0xFFFFFF);

        return doSlash(playerIn, roll, colorCode, centerOffset, mute, critical, comboRatio, knockback);
    }

    static public EntitySlashEffect doSlash(LivingEntity playerIn, float roll, int colorCode, Vec3 centerOffset,
                                            boolean mute, boolean critical, double comboRatio, KnockBacks knockback) {

        if (playerIn.level().isClientSide())
            return null;
        ItemStack blade = playerIn.getMainHandItem();
        if(!BladeStateHelper.getBladeState(blade).isPresent())
            return null;
        if (SlashBladeEvent.DO_SLASH_EVENT.post(new SlashBladeEvent.DoSlashEvent(blade,
                BladeStateHelper.getBladeState(blade).orElseThrow(NullPointerException::new),
                playerIn, roll, critical, comboRatio, knockback)))
            return null;
        Vec3 pos = playerIn.position().add(0.0D, (double) playerIn.getEyeHeight() * 0.75D, 0.0D)
                .add(playerIn.getLookAngle().scale(0.3f));

        pos = pos.add(VectorHelper.getVectorForRotation(-90.0F, playerIn.getViewYRot(0)).scale(centerOffset.y))
                .add(VectorHelper.getVectorForRotation(0, playerIn.getViewYRot(0) + 90).scale(centerOffset.z))
                .add(playerIn.getLookAngle().scale(centerOffset.z));

        EntitySlashEffect jc = new EntitySlashEffect(EntityTypeRegister.SlashEffect, playerIn.level());
        jc.setPos(pos.x, pos.y, pos.z);
        jc.setOwner(playerIn);
        jc.setRotationRoll(roll);
        jc.setYRot(playerIn.getYRot());
        jc.setXRot(0);

        jc.setColor(colorCode);

        jc.setMute(mute);
        jc.setIsCritical(critical);

        jc.setDamage(comboRatio);

        jc.setKnockBack(knockback);

        if (playerIn != null)
            ConcentrationRankHelper.getConcentrationRank(playerIn)
                    .ifPresent(rank -> jc.setRank(rank.getRankLevel(playerIn.level().getGameTime())));

        playerIn.level().addFreshEntity(jc);

        return jc;
    }

    public static void doVoidSlashAttack(LivingEntity living) {
        if (living.level().isClientSide())
            return;

        Vec3 pos = living.position().add(0.0D, (double) living.getEyeHeight() * 0.75D, 0.0D)
                .add(living.getLookAngle().scale(0.3f));

        pos = pos.add(VectorHelper.getVectorForRotation(-90.0F, living.getViewYRot(0)).scale(Vec3.ZERO.y))
                .add(VectorHelper.getVectorForRotation(0, living.getViewYRot(0) + 90).scale(Vec3.ZERO.z))
                .add(living.getLookAngle().scale(Vec3.ZERO.z));

        EntitySlashEffect jc = new EntitySlashEffect(EntityTypeRegister.SlashEffect, living.level()) {

            @Override
            public double getDamage() {
                return 0;
            }

            @Override
            public SoundEvent getSlashSound() {
                return SoundEvents.BLAZE_HURT;
            }

            @Override
            protected void tryDespawn() {
                if (!this.level().isClientSide()) {
                    if (this.getLifetime() < this.tickCount) {
                        this.level().playSound((Player) null, this.getX(), this.getY(), this.getZ(),
                                SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F,
                                0.625F + 0.1f * this.random.nextFloat());
                        ((ServerLevel) this.level()).sendParticles(ParticleTypes.ENCHANTED_HIT, this.getX(),
                                this.getY(), this.getZ(), 16, 0.7, 0.7, 0.7, 0.02);
                        this.getAlreadyHits().forEach(entity -> {

                            if (entity.isAlive()) {
                                float yRot = this.getOwner() != null ? this.getOwner().getYRot() : 0;
                                entity.addDeltaMovement(new Vec3(
                                        (double) (-Math.sin(yRot * (float) Math.PI / 180.0F) * 0.5),
                                        0.05D,
                                        (double) (Math.cos(yRot * (float) Math.PI / 180.0F) * 0.5)));
                                double baseAmount = living.getAttributeValue(Attributes.ATTACK_DAMAGE);
                                int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, living.getMainHandItem());
                                baseAmount *= (1 + (float) powerLevel * 0.1F);
                                //评分等级加成
                                if (living instanceof Player player){
                                    ConcentrationRankComponent.ConcentrationRanks rankBonus = ConcentrationRankHelper.getConcentrationRank(player)
                                            .map(rp -> rp.getRank(player.getCommandSenderWorld().getGameTime()))
                                            .orElse(ConcentrationRankComponent.ConcentrationRanks.NONE);
                                    float rankDamageBonus = rankBonus.level / 2.0f;
                                    if (ConcentrationRankComponent.ConcentrationRanks.S.level <= rankBonus.level) {
                                        int refine = BladeStateHelper.getBladeState(player.getMainHandItem()).map(BladeStateComponent::getRefine).orElse(0);
                                        int level = player.experienceLevel;
                                        rankDamageBonus = (float) Math.max(rankDamageBonus, Math.min(level, refine) * SlashBladeConfig.getRefineDamageMultiplier());
                                    }
                                    baseAmount += rankDamageBonus;
                                }
                                if (this.getShooter() instanceof LivingEntity shooter){
                                    baseAmount *= getSlashBladeDamageScale(shooter) * SlashBladeConfig.getSlashbladeDamageMultiplier();
                                }
                                doAttackWith(this.damageSources().indirectMagic(this, this.getShooter()),
                                        ((float) (baseAmount) * 5.1f), entity, true,
                                        true);
                            }
                        });
                        this.remove(RemovalReason.DISCARDED);
                    }
                }
            }
        };
        jc.setPos(pos.x, pos.y, pos.z);
        jc.setOwner(living);

        jc.setRotationRoll(180);
        jc.setYRot(living.getYRot() - 22.5F);
        jc.setXRot(0);

        int colorCode = BladeStateHelper.getBladeState(living.getMainHandItem())
                .map(BladeStateComponent::getColorCode).orElseGet(() -> 0xFFFFFF);
        jc.setColor(colorCode);

        jc.setMute(false);
        jc.setIsCritical(false);

        jc.setDamage(0D);

        jc.setKnockBack(KnockBacks.cancel);

        if (living != null)
            ConcentrationRankHelper.getConcentrationRank(living)
                    .ifPresent(rank -> jc.setRank(rank.getRankLevel(living.level().getGameTime())));

        jc.setLifetime(36);

        living.level().addFreshEntity(jc);
    }

    static public List<Entity> areaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio,
                                          boolean forceHit, boolean resetHit, boolean mute) {
        return areaAttack(playerIn, beforeHit, comboRatio, forceHit, resetHit, mute, null);
    }

    static public List<Entity> areaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float comboRatio,
                                          boolean forceHit, boolean resetHit, boolean mute, List<Entity> exclude) {
        List<Entity> founds = Lists.newArrayList();

        if (!playerIn.level().isClientSide()) {
            founds = TargetSelector.getTargettableEntitiesWithinAABB(playerIn.level(), playerIn);

            if (exclude != null)
                founds.removeAll(exclude);

            for (Entity entity : founds) {
                if (entity instanceof LivingEntity living)
                    beforeHit.accept(living);
                doMeleeAttack(playerIn, entity, forceHit, resetHit, comboRatio);
            }
        }

        if (!mute)
            playerIn.level().playSound((Player) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
                    SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 0.5F,
                    0.4F / (playerIn.getRandom().nextFloat() * 0.4F + 0.8F));

        return founds;
    }

    static public <E extends Entity & IShootable> List<Entity> areaAttack(E owner, Consumer<LivingEntity> beforeHit,
                                                                          double reach, boolean forceHit, boolean resetHit) {
        return areaAttack(owner, beforeHit, reach, forceHit, resetHit, null);
    }

    static public <E extends Entity & IShootable> List<Entity> areaAttack(E owner, Consumer<LivingEntity> beforeHit,
                                                                          double reach, boolean forceHit, boolean resetHit, List<Entity> exclude) {

        return areaAttack(owner, beforeHit, reach, forceHit, resetHit, 1.0F, exclude);
    }

    static public <E extends Entity & IShootable> List<Entity> areaAttack(E owner, Consumer<LivingEntity> beforeHit,
                                                                          double reach, boolean forceHit, boolean resetHit, float comboRatio, List<Entity> exclude) {
        List<Entity> founds = Lists.newArrayList();

        // AABB bb = owner.getBoundingBox();
        // bb = bb.grow(3.0D, 3D, 3.0D);

        if (!owner.level().isClientSide()) {
            founds = TargetSelector.getTargettableEntitiesWithinAABB(owner.level(), reach, owner);

            if (exclude != null)
                founds.removeAll(exclude);

            for (Entity entity : founds) {

                if (entity instanceof LivingEntity living)
                    beforeHit.accept(living);

                float baseAmount = (float) owner.getDamage();
                if(owner.getShooter() instanceof LivingEntity living) {
                    if(!(owner instanceof EntitySlashEffect)) {
                        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, living.getMainHandItem());
                        baseAmount += ((float) powerLevel * 0.1F);
                    }
                    baseAmount *= living.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    //评分等级加成
                    if (owner instanceof Player player){
                        ConcentrationRankComponent.ConcentrationRanks rankBonus = ConcentrationRankHelper.getConcentrationRank(player)
                                .map(rp -> rp.getRank(player.getCommandSenderWorld().getGameTime()))
                                .orElse(ConcentrationRankComponent.ConcentrationRanks.NONE);
                        float rankDamageBonus = rankBonus.level / 2.0f;
                        if (ConcentrationRankComponent.ConcentrationRanks.S.level <= rankBonus.level) {
                            int refine = BladeStateHelper.getBladeState(player.getMainHandItem()).map(BladeStateComponent::getRefine).orElse(0);
                            int level = player.experienceLevel;
                            rankDamageBonus = (float) Math.max(rankDamageBonus, Math.min(level, refine) * SlashBladeConfig.getRefineDamageMultiplier());
                        }
                        baseAmount += rankDamageBonus;
                    }

                    baseAmount *= comboRatio * getSlashBladeDamageScale(living) * SlashBladeConfig.getSlashbladeDamageMultiplier();

                }

                doAttackWith(owner.damageSources().indirectMagic(owner, owner.getShooter()), baseAmount, entity,
                        forceHit, resetHit);
            }
        }

        return founds;
    }

    static public void doManagedAttack(Consumer<Entity> attack, Entity target, boolean forceHit, boolean resetHit) {
        if (forceHit)
            target.invulnerableTime = 0;

        attack.accept(target);

        if (resetHit)
            target.invulnerableTime = 0;
    }

    static public void doAttackWith(DamageSource src, float amount, Entity target, boolean forceHit, boolean resetHit) {
        if (target instanceof EntityAbstractSummonedSword)
            return;

        doManagedAttack((t) -> {
            t.hurt(src, amount);
        }, target, forceHit, resetHit);
    }

    static public void doMeleeAttack(LivingEntity attacker, Entity target, boolean forceHit, boolean resetHit) {
        doMeleeAttack(attacker, target, forceHit, resetHit,1.0f);
    }

    static public void doMeleeAttack(LivingEntity attacker, Entity target, boolean forceHit, boolean resetHit, float comboRatio) {
        if (attacker instanceof Player) {
            doManagedAttack((t) -> {
                BladeStateHelper.getBladeState(attacker.getMainHandItem()).ifPresent((state) -> {
                    try {
                        state.setOnClick(true);
                        PlayerAttackHelper.attack(((Player) attacker),t,comboRatio);

                    } finally {
                        state.setOnClick(false);
                    }
                });
            }, target, forceHit, resetHit);
        } else {
            float baseAmount = (float) (attacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * getSlashBladeDamageScale(attacker) * SlashBladeConfig.getSlashbladeDamageMultiplier());
            doAttackWith(attacker.damageSources().mobAttack(attacker), baseAmount, target, forceHit, resetHit);
        }

        ArrowReflector.doReflect(target, attacker);
        TNTExtinguisher.doExtinguishing(target, attacker);
    }

    public static void playQuickSheathSoundAction(LivingEntity entity) {
        if(entity.level().isClientSide())
            return ;
        entity.level().playSound((Player)null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.CHAIN_HIT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    public static void playPiercingSoundAction(LivingEntity entity) {
        if(entity.level().isClientSide())
            return ;
        entity.level().playSound((Player)null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    public static Vec3 genRushOffset(LivingEntity entityIn) {
        return new Vec3(entityIn.getRandom().nextFloat() - 0.5f, entityIn.getRandom().nextFloat() - 0.5f, 0).scale(2.0);
    }

    public static float getSlashBladeDamageScale(LivingEntity entity) {
        if (entity.getAttribute(ModAttributes.getSlashBladeDamage()) != null){
            return (float) entity.getAttribute(ModAttributes.getSlashBladeDamage()).getValue();
        }
        return 1.0f;
    }

}