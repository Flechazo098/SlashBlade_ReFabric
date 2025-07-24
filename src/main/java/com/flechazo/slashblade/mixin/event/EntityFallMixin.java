package com.flechazo.slashblade.mixin.event;

import com.flechazo.slashblade.event.FallHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityFallMixin {
    @Inject(method = "checkFallDamage( DZLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", at = @At("HEAD"))

    private void onEntityFall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPos, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity living) {
            FallHandler.resetState(living);
        }
    }

    @Inject(method = "checkFallDamage( DZLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", at = @At("TAIL"))

    private void onEntityLand(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPos, CallbackInfo ci
    ) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity living) {
            // 传入 fallFactor = 落差，用于粒子强度
            float fallFactor = (float) Math.abs(heightDifference);
            FallHandler.spawnLandingParticle(living, fallFactor);
        }
    }
}
