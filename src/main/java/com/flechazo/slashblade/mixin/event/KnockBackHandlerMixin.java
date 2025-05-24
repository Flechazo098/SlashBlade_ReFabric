package com.flechazo.slashblade.mixin.event;

import com.flechazo.slashblade.event.KnockBackHandler;
import com.flechazo.slashblade.util.NBTHelper;
import com.flechazo.slashblade.util.accessor.PersistentDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class KnockBackHandlerMixin {

    @Inject(method = "knockback(DDD)V", at = @At("HEAD"), cancellable = true)

    private void onKnockBack(double strength, double ratioX, double ratioZ, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        CompoundTag nbt = ((PersistentDataAccessor) self).slashbladerefabriced$getPersistentData();
        if (!nbt.contains(KnockBackHandler.NBT_KEY)) {
            return;
        }

        Vec3 factor = NBTHelper.getVector3d(nbt, KnockBackHandler.NBT_KEY);
        nbt.remove(KnockBackHandler.NBT_KEY);

        if (self.fallDistance < 0) self.fallDistance = 0;
        self.fallDistance += factor.z;

        double resist = self.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        if (self.getRandom().nextDouble() < resist) {
            ci.cancel();
            return;
        }

        self.hasImpulse = true;
        Vec3 motion = self.getDeltaMovement();

        if (factor.y > 0) {
            self.setDeltaMovement(motion.x, Math.max(motion.y, factor.y), motion.z);
        } else if (factor.y < 0) {
            self.setDeltaMovement(
                    motion.x,
                    Math.min(motion.y, factor.y),
                    motion.z
            );
        }

        self.knockback(strength, ratioX, ratioZ);
    }
}
