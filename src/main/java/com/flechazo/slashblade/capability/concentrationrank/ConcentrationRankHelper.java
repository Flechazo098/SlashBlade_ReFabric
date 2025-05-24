package com.flechazo.slashblade.capability.concentrationrank;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class ConcentrationRankHelper {

    /**
     * 获取实体的集中度等级组件
     * @param entity 实体
     * @return 集中度等级组件，如果实体不是玩家则返回null
     */
    public static Optional<ConcentrationRankComponent> getConcentrationRank(Entity entity) {
        if (entity instanceof Player) {
            return ConcentrationRankComponentRegistry.CONCENTRATION_RANK.maybeGet(entity);
        }
        return Optional.empty();
    }
}