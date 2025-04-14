package com.flechazo.slashblade.capability.concentrationrank;

import com.flechazo.slashblade.SlashBladeRefabriced;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.resources.ResourceLocation;

public class ConcentrationRankComponentRegistry implements EntityComponentInitializer {
    public static final ComponentKey<ConcentrationRankComponent> CONCENTRATION_RANK =
            ComponentRegistry.getOrCreate(
                    new ResourceLocation(SlashBladeRefabriced.MODID, "concentration_rank"),
                    ConcentrationRankComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // 只给玩家添加集中度等级组件
        registry.registerForPlayers(CONCENTRATION_RANK, player -> new ConcentrationRankComponentImpl(),
                RespawnCopyStrategy.ALWAYS_COPY);
    }
}