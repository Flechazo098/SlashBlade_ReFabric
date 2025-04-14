package com.flechazo.slashblade.event;

import com.flechazo.slashblade.SlashBladeConfig;
import com.flechazo.slashblade.capability.concentrationrank.ConcentrationRankCapabilityProvider;
import com.flechazo.slashblade.capability.concentrationrank.IConcentrationRank;
import com.flechazo.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KillCounter {
    private static final class SingletonHolder {
        private static final KillCounter instance = new KillCounter();
    }

    public static KillCounter getInstance() {
        return SingletonHolder.instance;
    }

    private KillCounter() {
    }

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDeathEvent(LivingDeathEvent event) {
        Entity trueSource = event.getSource().getEntity();

        if (!(trueSource instanceof LivingEntity))
            return;

        ItemStack stack = ((LivingEntity) trueSource).getMainHandItem();
        if (stack.isEmpty())
            return;
        if (!(stack.getItem() instanceof ItemSlashBlade))
            return;

        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {

            state.setKillCount(state.getKillCount() + 1);
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onXPDropping(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer();
        if (player == null)
            return;
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty())
            return;
        if (!(stack.getItem() instanceof ItemSlashBlade))
            return;

        IConcentrationRank.ConcentrationRanks rankBonus = player
                .getCapability(ConcentrationRankCapabilityProvider.RANK_POINT)
                .map(rp -> rp.getRank(player.getCommandSenderWorld().getGameTime()))
                .orElse(IConcentrationRank.ConcentrationRanks.NONE);
        int souls = (int) Math.floor(event.getDroppedExperience() * (1.0F + (rankBonus.level * 0.1F)));

        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            state.setProudSoulCount(
                    state.getProudSoulCount() + Math.min(SlashBladeConfig.MAX_PROUD_SOUL_GOT.get(), souls));
        });
        
    }
}
