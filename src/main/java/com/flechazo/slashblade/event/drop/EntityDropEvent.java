package com.flechazo.slashblade.event.drop;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.entity.BladeItemEntity;
import com.flechazo.slashblade.item.ItemSlashBlade;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class EntityDropEvent {

    public static void handleBladeDrop(ServerLevel level, LivingEntity entity, DamageSource source) {
        var bladeRegistry = SlashBladeRefabriced.getSlashBladeDefinitionRegistry(level);

        // Traverse through custom drop entry
        level.registryAccess()
                .registryOrThrow(EntityDropEntry.REGISTRY_KEY)
                .forEach(entry -> {
                    ResourceLocation entityId = entry.getEntityType();
                    EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(entityId);
                    if (type == null) return;

                    ResourceLocation bladeName = entry.getBladeName();
                    if (!bladeRegistry.containsKey(bladeName)) return;

                    if (!(source.getEntity() instanceof LivingEntity attacker)) return;

                    if (entry.isRequestSlashBladeKill()
                            && !(attacker.getMainHandItem().getItem() instanceof ItemSlashBlade)) return;

                    // drop rate
                    float rate = Math.min(1F, entry.getDropRate() + EnchantmentHelper.getMobLooting(attacker) * 0.1F);

                    // Calculate the drop coordinates
                    double x = entry.isDropFixedPoint() ? entry.getDropPoint().x : entity.getX();
                    double y = entry.isDropFixedPoint() ? entry.getDropPoint().y : entity.getY();
                    double z = entry.isDropFixedPoint() ? entry.getDropPoint().z : entity.getZ();

                    dropBlade(entity, type, bladeRegistry.get(bladeName).getBlade(), rate, x, y, z);
                });
    }


    public static void dropBlade(LivingEntity entity, EntityType<?> type, ItemStack blade, float percent, double x,
            double y, double z) {
        if (entity.getType().equals(type)) {
            var rand = entity.level().getRandom();

            if (rand.nextFloat() > percent)
                return;
            ItemEntity itementity = new ItemEntity(entity.level(), x, y, z, blade);
            BladeItemEntity e = new BladeItemEntity(SlashBladeRefabriced.RegistryEvents.BladeItem, entity.level());

            e.restoreFrom(itementity);
            e.init();
            e.push(0, 0.4, 0);

            e.setPickUpDelay(20 * 2);
            e.setGlowingTag(true);

            e.setAirSupply(-1);

            e.setThrower(entity.getUUID());

            entity.level().addFreshEntity(e);
        }
    }
}
