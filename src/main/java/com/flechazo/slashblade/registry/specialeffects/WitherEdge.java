package com.flechazo.slashblade.registry.specialeffects;

import com.flechazo.slashblade.capability.slashblade.BladeStateComponent;
import com.flechazo.slashblade.event.SlashBladeEvent;
import com.flechazo.slashblade.registry.SpecialEffectsRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class WitherEdge extends SpecialEffect{
	
	public WitherEdge() {
		super(20, true, true);
	}
	
	@SubscribeEvent
	public static void onSlashBladeUpdate(SlashBladeEvent.UpdateEvent event) {
		BladeStateComponent state = event.getSlashBladeState();
		if(state.hasSpecialEffect(SpecialEffectsRegistry.WITHER_EDGE.getId())) {
			if (!(event.getEntity() instanceof Player)) {
				return;
			}
			
			if(!event.isSelected())
				return;
			
			Player player = (Player) event.getEntity();
			
			int level = player.experienceLevel;
			
			if(!SpecialEffect.isEffective(SpecialEffectsRegistry.WITHER_EDGE.get(),level))
				player.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1));
		}
	}
	
	@SubscribeEvent
	public static void onSlashBladeHit(SlashBladeEvent.HitEvent event) {
		BladeStateComponent state = event.getSlashBladeState();
		if(state.hasSpecialEffect(SpecialEffectsRegistry.WITHER_EDGE.getId())) {
			if (!(event.getUser() instanceof Player)) {
				return;
			}
			
			Player player = (Player) event.getUser();
			
			int level = player.experienceLevel;
			
			if(SpecialEffect.isEffective(SpecialEffectsRegistry.WITHER_EDGE.get(),level))
				event.getTarget().addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1));
		}
	}
}
