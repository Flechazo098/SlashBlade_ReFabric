package com.flechazo.slashblade.event;

import com.flechazo.slashblade.capability.concentrationrank.ConcentrationRankHelper;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class RankPointHandler {
    private static final class SingletonHolder {
        private static final RankPointHandler instance = new RankPointHandler();
    }

    public static RankPointHandler getInstance() {
        return SingletonHolder.instance;
    }

    private RankPointHandler() {
    }

    public void register() {
        LivingHurtEvent.HURT.register(this::onLivingDeathEvent);
    }

    /**
     * Not reached if canceled.
     *
     * @param event
     */
    public void onLivingDeathEvent(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim != null)
            ConcentrationRankHelper.getConcentrationRank(victim)
                    .ifPresent(cr -> cr.addRankPoint(victim, -cr.getUnitCapacity()));

        Entity trueSource = event.getSource().getEntity();
        if (!(trueSource instanceof LivingEntity sourceEntity))
            return;

        if (BladeStateHelper.getBladeState(sourceEntity.getMainHandItem()).isEmpty())
            return;

        ConcentrationRankHelper.getConcentrationRank(sourceEntity)
                .ifPresent(cr -> cr.addRankPoint(event.getSource()));
    }
}
