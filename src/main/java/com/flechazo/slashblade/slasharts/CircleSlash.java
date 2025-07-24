package com.flechazo.slashblade.slasharts;

import com.flechazo.slashblade.capability.concentrationrank.ConcentrationRankHelper;
import com.flechazo.slashblade.capability.slashblade.BladeStateComponent;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.entity.EntitySlashEffect;
import com.flechazo.slashblade.registry.EntityTypeRegister;
import com.flechazo.slashblade.util.KnockBacks;
import com.flechazo.slashblade.util.VectorHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class CircleSlash {
    public static void doCircleSlashAttack(LivingEntity living, float yRot) {
        if (living.level().isClientSide())
            return;

        Vec3 pos = living.position().add(0.0D, (double) living.getEyeHeight() * 0.75D, 0.0D)
                .add(living.getLookAngle().scale(0.3f));

        pos = pos.add(VectorHelper.getVectorForRotation(-90.0F, living.getViewYRot(0)).scale(Vec3.ZERO.y))
                .add(VectorHelper.getVectorForRotation(0, living.getViewYRot(0) + 90).scale(Vec3.ZERO.z))
                .add(living.getLookAngle().scale(Vec3.ZERO.z));

        EntitySlashEffect jc = new EntitySlashEffect(EntityTypeRegister.SlashEffect, living.level()) {

            @Override
            public SoundEvent getSlashSound() {
                return SoundEvents.EMPTY;
            }
        };
        jc.setPos(pos.x, pos.y, pos.z);
        jc.setOwner(living);

        jc.setRotationRoll(0);
        jc.setYRot(living.getYRot() - 22.5F + yRot);
        jc.setXRot(0);

        int colorCode = BladeStateHelper.getBladeState(living.getMainHandItem())
                .map(BladeStateComponent::getColorCode).orElseGet(() -> 0xFFFFFF);
        jc.setColor(colorCode);

        jc.setMute(false);
        jc.setIsCritical(true);

        jc.setDamage(0.325D);

        jc.setKnockBack(KnockBacks.cancel);

        if (living != null)
            ConcentrationRankHelper.getConcentrationRank(living)
                    .ifPresent(rank -> jc.setRank(rank.getRankLevel(living.level().getGameTime())));

        living.level().addFreshEntity(jc);
    }

}
