package com.flechazo.slashblade.event;

import com.flechazo.slashblade.SlashBladeConfig;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.item.ItemSlashBlade;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

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
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(this::onKill);
    }

    private void onKill(ServerLevel level, Entity attacker, LivingEntity killed) {
        if (attacker instanceof LivingEntity livingAttacker) {
            ItemStack stack = livingAttacker.getMainHandItem();
            if (!stack.isEmpty() && stack.getItem() instanceof ItemSlashBlade) {
                BladeStateHelper.getBladeState(stack)
                        .ifPresent(state -> state.setKillCount(state.getKillCount() + 1));
            }
        }
        if (attacker instanceof ServerPlayer player) {
            ItemStack stack = player.getMainHandItem();
            if (!stack.isEmpty() && stack.getItem() instanceof ItemSlashBlade) {
                int baseXp = killed.getExperienceReward();
                int looting = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING, stack);

                int souls = Math.floorDiv(Math.round(baseXp * (1.0F + looting * 0.1F)), 1);
                souls = Math.min(SlashBladeConfig.getMaxProudSoulGot(), souls);
                int finalSouls = souls;
                BladeStateHelper.getBladeState(stack).ifPresent(state -> state.setProudSoulCount(state.getProudSoulCount() + finalSouls));
            }
        }
    }
}
