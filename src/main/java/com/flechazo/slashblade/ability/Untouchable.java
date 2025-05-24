package com.flechazo.slashblade.ability;

import com.flechazo.slashblade.capability.mobeffect.MobEffectComponent;
import com.flechazo.slashblade.capability.mobeffect.MobEffectHelper;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import io.github.fabricators_of_create.porting_lib.entity.events.LivingAttackEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingDamageEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Optional;

public class Untouchable {
    private static final class SingletonHolder {
        private static final Untouchable instance = new Untouchable();
    }

    public static Untouchable getInstance() {
        return Untouchable.SingletonHolder.instance;
    }

    private Untouchable() {
    }

    public void register() {
        LivingHurtEvent.HURT.register(this::onLivingHurt);
        LivingDamageEvent.DAMAGE.register(this::onLivingDamage);
        LivingAttackEvent.ATTACK.register(this::onLivingAttack);
        ServerLivingEntityEvents.ALLOW_DEATH.register(this::onAllowDeath);
        LivingEntityEvents.LivingTickEvent.TICK.register(this::onLivingTicks);
        LivingEntityEvents.LivingJumpEvent.JUMP.register(this::onPlayerJump);
    }

    public static void setUntouchable(LivingEntity entity, int ticks) {
        MobEffectHelper.getMobEffect(entity).ifPresent(ef -> {
            ef.setManagedUntouchable(entity.level().getGameTime(), ticks);
            ef.storeEffects(entity.getActiveEffectsMap().keySet());
            ef.storeHealth(entity.getHealth());
        });
    }

    private boolean checkUntouchable(LivingEntity entity) {
        Optional<Boolean> isUntouchable = MobEffectHelper.getMobEffect(entity)
                .map(ef -> ef.isUntouchable(entity.getCommandSenderWorld().getGameTime()));

        return isUntouchable.orElse(false);
    }

    private void doWitchTime(Entity entity) {
        if (entity == null)
            return;

        if (!(entity instanceof LivingEntity))
            return;

        StunManager.setStun((LivingEntity) entity);
    }

    public void onLivingHurt(LivingHurtEvent event) {
        if (checkUntouchable(event.getEntity())) {
            event.setCanceled(true);
            doWitchTime(event.getSource().getEntity());
        }
    }

    public void onLivingDamage(LivingDamageEvent event) {
        if (checkUntouchable(event.getEntity())) {
            event.setCanceled(true);
            doWitchTime(event.getSource().getEntity());
        }
    }

    public void onLivingAttack(LivingAttackEvent event) {
        if (checkUntouchable(event.getEntity())) {
            event.setCanceled(true);
            doWitchTime(event.getSource().getEntity());
        }
    }

    private boolean onAllowDeath(LivingEntity entity, DamageSource source, float damageAmount) {
        if (checkUntouchable(entity)) {

            MobEffectComponent eff = MobEffectHelper.getMobEffect(entity).orElse(null);
            if (eff != null && eff.hasUntouchableWorked()) {
                List<MobEffect> toRemove = entity.getActiveEffectsMap().keySet().stream()
                        .filter(m -> !(eff.getEffectSet().contains(m) || m.isBeneficial()))
                        .toList();
                toRemove.forEach(entity::removeEffect);

                float stored = eff.getStoredHealth();
                if (entity.getHealth() < stored) {
                    entity.setHealth(stored);
                }
            }
            return false;
        }
        return true;
    }

    public void onLivingTicks(LivingEntityEvents.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.level().isClientSide())
            return;

        MobEffectHelper.getMobEffect(entity).ifPresent(ef -> {
            if (ef.hasUntouchableWorked()) {
                ef.setUntouchableWorked(false);
                List<MobEffect> filterd = entity.getActiveEffectsMap().keySet().stream()
                        .filter(p -> !(ef.getEffectSet().contains(p) || p.isBeneficial())).toList();

                filterd.forEach(entity::removeEffect);

                float storedHealth = ef.getStoredHealth();
                if (ef.getStoredHealth() < storedHealth)
                    entity.setHealth(ef.getStoredHealth());
            }
        });
    }

    final static int JUMP_TICKS = 10;

    public void onPlayerJump(LivingEntityEvents.LivingJumpEvent event) {
        if (BladeStateHelper.getBladeState(event.getEntity().getMainHandItem()).isEmpty())
            return;

        Untouchable.setUntouchable(event.getEntity(), JUMP_TICKS);
    }
}
