package com.flechazo.slashblade.slasharts;

import com.flechazo.slashblade.capability.concentrationrank.ConcentrationRankHelper;
import com.flechazo.slashblade.capability.slashblade.BladeStateComponent;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.entity.EntitySlashEffect;
import com.flechazo.slashblade.registry.EntityTypeRegister;
import com.flechazo.slashblade.util.KnockBacks;
import com.flechazo.slashblade.util.VectorHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class SakuraEnd {
    public static EntitySlashEffect doSlash(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute,
                                            boolean critical, double damage) {
        return doSlash(playerIn, roll, centerOffset, mute, critical, damage, KnockBacks.cancel);
    }

    public static EntitySlashEffect doSlash(LivingEntity playerIn, float roll, Vec3 centerOffset, boolean mute,
                                            boolean critical, double damage, KnockBacks knockback) {

        int colorCode = BladeStateHelper.getBladeState(playerIn.getMainHandItem())
                .map(BladeStateComponent::getColorCode).orElse(0xFFFFFF);

        return doSlash(playerIn, roll, colorCode, centerOffset, mute, critical, damage, knockback);
    }

    public static EntitySlashEffect doSlash(LivingEntity playerIn, float roll, int colorCode, Vec3 centerOffset,
                                            boolean mute, boolean critical, double damage, KnockBacks knockback) {

        if (playerIn.level().isClientSide())
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

        jc.setDamage(damage);

        jc.setKnockBack(knockback);

        if (playerIn != null)
            ConcentrationRankHelper.getConcentrationRank(playerIn)
                    .ifPresent(rank -> jc.setRank(rank.getRankLevel(playerIn.level().getGameTime())));

        playerIn.level().addFreshEntity(jc);

        return jc;
    }
}
