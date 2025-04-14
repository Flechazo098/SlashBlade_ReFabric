package com.flechazo.slashblade.event;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.capability.concentrationrank.ConcentrationRankCapabilityProvider;
import com.flechazo.slashblade.capability.inputstate.InputStateCapabilityProvider;
import com.flechazo.slashblade.capability.mobeffect.MobEffectHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityAttachHandler {

    static public final ResourceLocation MOBEFFECT_KEY = new ResourceLocation(SlashBladeRefabriced.MODID, "mobeffect");
    static public final ResourceLocation INPUTSTATE_KEY = new ResourceLocation(SlashBladeRefabriced.MODID, "inputstate");
    static public final ResourceLocation RANK_KEY = new ResourceLocation(SlashBladeRefabriced.MODID, "concentration");

    @SubscribeEvent
    public void AttachCapabilities_Entity(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof LivingEntity))
            return;

        event.addCapability(INPUTSTATE_KEY, new InputStateCapabilityProvider());
        event.addCapability(MOBEFFECT_KEY, new MobEffectHelper());
        event.addCapability(RANK_KEY, new ConcentrationRankCapabilityProvider());
    }

}
