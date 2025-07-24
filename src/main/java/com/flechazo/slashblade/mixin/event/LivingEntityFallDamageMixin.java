package com.flechazo.slashblade.mixin.event;

import com.flechazo.slashblade.event.FallHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityFallDamageMixin {
    @Inject(method = "causeFallDamage(FFLnet/minecraft/world/damagesource/DamageSource;)Z", at = @At("HEAD"))

    private void onHandleFallDamage(float fallDistance, float damageMultiplier, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity living = (LivingEntity) (Object) this;

        if (living instanceof Player player && player.getAbilities().flying) {
            FallHandler.fallResist(living);
        } else {
            FallHandler.fallDecrease(living);
        }
    }
}
