package com.flechazo.slashblade.event;

import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.registry.ComboStateRegistry;
import com.flechazo.slashblade.registry.combo.ComboState;
import com.flechazo.slashblade.util.AdvancementHelper;
import io.github.fabricators_of_create.porting_lib.attributes.PortingLibAttributes;
import io.github.fabricators_of_create.porting_lib.block.CustomLandingEffectsBlock;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;

/**
 * 摔落事件处理类
 * 具体实现请参阅 {@link com.flechazo.slashblade.mixin.event.LivingEntityFallDamageMixin}
 */
public class FallHandler {

    public static void resetState(LivingEntity user) {
        BladeStateHelper.getBladeState(user.getMainHandItem()).ifPresent((state) -> {
            state.setFallDecreaseRate(0);

            ComboStateRegistry.COMBO_STATE.get(state.getComboSeq());
            ComboState combo = ComboStateRegistry.COMBO_STATE.get(state.getComboSeq());
            if (combo.isAerial()) {
                state.setComboSeq(combo.getNextOfTimeout(user));
            }
        });

    }

    public static void spawnLandingParticle(LivingEntity user, float fallFactor) {
        if (!user.level().isClientSide()) {
            int x = Mth.floor(user.getX());
            int y = Mth.floor(user.getY() - (double) 0.5F);
            int z = Mth.floor(user.getZ());
            BlockPos pos = new BlockPos(x, y, z);
            BlockState state = user.level().getBlockState(pos);

            float f = (float) Mth.ceil(fallFactor);
            if (!state.isAir()) {
                double d0 = Math.min((double) (0.2F + f / 15.0F), 2.5D);
                int i = (int) (150.0D * d0);
                Block block = state.getBlock();

                if (block instanceof CustomLandingEffectsBlock custom) {
                    if (custom.addLandingEffects(state, (ServerLevel) user.level(), pos, state, user, i)) {
                        return;
                    }
                }

                ((ServerLevel) user.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), user.getX(), user.getY(), user.getZ(), i, 0.0D, 0.0D, 0.0D, 0.15D);
            }
        }
    }


    public static void spawnLandingParticle(Entity user, Vec3 targetPos, Vec3 normal, float fallFactor) {
        if (!user.level().isClientSide()) {

            Vec3 blockPos = targetPos.add(normal.normalize().scale(0.5f));

            int x = Mth.floor(blockPos.x());
            int y = Mth.floor(blockPos.y());
            int z = Mth.floor(blockPos.z());
            BlockPos pos = new BlockPos(x, y, z);
            BlockState state = user.level().getBlockState(pos);

            float f = (float) Mth.ceil(fallFactor);
            if (!state.isAir()) {
                double d0 = Math.min((double) (0.2F + f / 15.0F), 2.5D);
                int i = (int) (150.0D * d0);
                ((ServerLevel) user.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state),
                        targetPos.x(), targetPos.y(), targetPos.z(), i, 0.0D, 0.0D, 0.0D, (double) 0.15F);
            }
        }
    }

    public static void fallDecrease(LivingEntity user) {
        if (!user.isNoGravity() && !user.onGround()) {
            user.fallDistance = 1;

            float currentRatio = BladeStateHelper.getBladeState(user.getMainHandItem()).map((state) -> {
                float decRatio = state.getFallDecreaseRate();

                float newDecRatio = decRatio + 0.05f;
                newDecRatio = Math.min(1.0f, newDecRatio);
                state.setFallDecreaseRate(newDecRatio);

                return decRatio;
            }).orElseGet(() -> 1.0f);

            double gravityReductionFactor = 0.85f;

            int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.FALL_PROTECTION, user);
            if (0 < level) {
                gravityReductionFactor = Math.min(0.93, gravityReductionFactor + 0.2 * level);
                AdvancementHelper.grantedIf(Enchantments.FALL_PROTECTION, user);
            }

            AttributeInstance gravity = user.getAttribute(PortingLibAttributes.ENTITY_GRAVITY);
            double g = gravity.getValue() * gravityReductionFactor;

            Vec3 motion = user.getDeltaMovement();
            if (motion.y < 0)
                user.setDeltaMovement(motion.x, (motion.y + g) * currentRatio, motion.z);
        }
    }

    public static void fallResist(LivingEntity user) {
        if (!user.isNoGravity() && !user.onGround()) {
            user.fallDistance = 1;

            Vec3 motion = user.getDeltaMovement();
            AttributeInstance gravity = user.getAttribute(PortingLibAttributes.ENTITY_GRAVITY);
            double g = gravity.getValue();
            if (motion.y < 0)
                user.setDeltaMovement(motion.x, (motion.y + g + 0.002f), motion.z);
        }
    }
}
