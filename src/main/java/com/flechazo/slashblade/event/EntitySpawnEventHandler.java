package com.flechazo.slashblade.event;

/**
 * 此类已被弃用
 *<p>
 * 请参阅 {@link com.flechazo.slashblade.mixin.event.MobSpawnMixin} 类以获得替代实现。
 * @deprecated 使用 {@link com.flechazo.slashblade.mixin.event.MobSpawnMixin} 替代。
 */
@Deprecated
public class EntitySpawnEventHandler {
//    @SubscribeEvent
//    public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
//        LivingEntity entity = event.getEntity();
//        boolean isZombie = isZombie(entity);
//        if (!isZombie)
//            return;
//        if (!entity.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
//            return;
//
//        RandomSource random = event.getLevel().getRandom();
//        float difficultyMultiplier = event.getDifficulty().getSpecialMultiplier();
//
//        Registry<SlashBladeDefinition> bladeRegistry = SlashBladeRefabriced
//                .getSlashBladeDefinitionRegistry(event.getEntity().level());
//        if (!bladeRegistry.containsKey(SlashBladeBuiltInRegistry.SABIGATANA.location()))
//            return;
//
//        float rngResult = random.nextFloat();
//
//        if (rngResult < SlashBladeConfig.BROKEN_SABIGATANA_SPAWN_CHANCE.get() * difficultyMultiplier) {
//            if (rngResult < SlashBladeConfig.SABIGATANA_SPAWN_CHANCE.get() * difficultyMultiplier) {
//                entity.setItemSlot(EquipmentSlot.MAINHAND,
//                        bladeRegistry.get(SlashBladeBuiltInRegistry.SABIGATANA.location()).getBlade());
//            } else {
//                entity.setItemSlot(EquipmentSlot.MAINHAND,
//                        bladeRegistry.get(SlashBladeBuiltInRegistry.SABIGATANA_BROKEN.location()).getBlade());
//            }
//        }
//    }
//
//    private static boolean isZombie(LivingEntity entity) {
//        return entity instanceof Zombie && !(entity instanceof Drowned) && !(entity instanceof ZombifiedPiglin);
//    }
}
