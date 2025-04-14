package com.flechazo.slashblade.event;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.SlashBladeConfig;
import com.flechazo.slashblade.data.builtin.SlashBladeBuiltInRegistry;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.core.Registry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntitySpawnEventHandler {
    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
        LivingEntity entity = event.getEntity();
        boolean isZombie = isZombie(entity);
        if (!isZombie)
            return;
        if (!entity.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
            return;

        RandomSource random = event.getLevel().getRandom();
        float difficultyMultiplier = event.getDifficulty().getSpecialMultiplier();

        Registry<SlashBladeDefinition> bladeRegistry = SlashBladeRefabriced
                .getSlashBladeDefinitionRegistry(event.getEntity().level());
        if (!bladeRegistry.containsKey(SlashBladeBuiltInRegistry.SABIGATANA.location()))
            return;

        float rngResult = random.nextFloat();

        if (rngResult < SlashBladeConfig.BROKEN_SABIGATANA_SPAWN_CHANCE.get() * difficultyMultiplier) {
            if (rngResult < SlashBladeConfig.SABIGATANA_SPAWN_CHANCE.get() * difficultyMultiplier) {
                entity.setItemSlot(EquipmentSlot.MAINHAND,
                        bladeRegistry.get(SlashBladeBuiltInRegistry.SABIGATANA.location()).getBlade());
            } else {
                entity.setItemSlot(EquipmentSlot.MAINHAND,
                        bladeRegistry.get(SlashBladeBuiltInRegistry.SABIGATANA_BROKEN.location()).getBlade());
            }
        }
    }

    private static boolean isZombie(LivingEntity entity) {
        return entity instanceof Zombie && !(entity instanceof Drowned) && !(entity instanceof ZombifiedPiglin);
    }
}
