package com.flechazo.slashblade.registry.specialeffects;

import com.flechazo.slashblade.capability.slashblade.BladeStateComponent;
import com.flechazo.slashblade.event.SlashBladeEvent;
import com.flechazo.slashblade.registry.SpecialEffectsRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class WitherEdge extends SpecialEffect {

    public WitherEdge() {
        super(20, true, true);
    }

    public void register() {
        SlashBladeEvent.UPDATE_EVENT.register(this::onSlashBladeUpdate);
        SlashBladeEvent.HIT_EVENT.register(this::onSlashBladeHit);
    }


    public void onSlashBladeUpdate(SlashBladeEvent.UpdateEvent event) {
        BladeStateComponent state = event.getSlashBladeState();
        if (state.hasSpecialEffect(SpecialEffectsRegistry.WITHER_EDGE.getId())) {
            if (!(event.getEntity() instanceof Player player)) {
                return;
            }

            if (!event.isSelected())
                return;

            int level = player.experienceLevel;

            if (!SpecialEffect.isEffective(SpecialEffectsRegistry.WITHER_EDGE, level))
                player.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1));
        }
    }

    public void onSlashBladeHit(SlashBladeEvent.HitEvent event) {
        BladeStateComponent state = event.getSlashBladeState();
        if (state.hasSpecialEffect(SpecialEffectsRegistry.WITHER_EDGE.getId())) {
            if (!(event.getUser() instanceof Player player)) {
                return;
            }

            int level = player.experienceLevel;

            if (SpecialEffect.isEffective(SpecialEffectsRegistry.WITHER_EDGE, level))
                event.getTarget().addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1));
        }
    }
}
