package com.flechazo.slashblade.capability;

import com.flechazo.slashblade.capability.concentrationrank.ConcentrationRankComponentImpl;
import com.flechazo.slashblade.capability.concentrationrank.ConcentrationRankComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.world.entity.LivingEntity;

public class LivingEntityComponentRegister implements EntityComponentInitializer {
    @Override
    public void registerEntityComponentFactories (EntityComponentFactoryRegistry registry) {
        registry.registerFor(LivingEntity.class, ConcentrationRankComponentRegistry.CONCENTRATION_RANK, entity -> new ConcentrationRankComponentImpl());
    }
}
