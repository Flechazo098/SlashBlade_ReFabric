package com.flechazo.slashblade.mixin.event;

import com.flechazo.slashblade.SlashBladeConfig;
import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.data.builtin.SlashBladeBuiltInRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.SERVER)
@Mixin(Mob.class)
public class MobSpawnMixin {
    @Inject(method = "finalizeSpawn", at = @At("TAIL"))

    private void onFinalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnData, CompoundTag entityData, CallbackInfoReturnable<SpawnGroupData> cir) {
        Mob self = (Mob) (Object) this;
        if (!(self instanceof Zombie zombie) || zombie instanceof Drowned || zombie instanceof ZombifiedPiglin) return;

        LivingEntity entity = (LivingEntity) (Object) this;
        ItemStack held = entity.getItemBySlot(EquipmentSlot.MAINHAND);
        if(!held.isEmpty()) return;

        RandomSource random = world.getRandom();
        float multiplier = difficulty.getSpecialMultiplier();

        var bladeRegistry = SlashBladeRefabriced.getSlashBladeDefinitionRegistry(world.getLevel());
        var sabigatanaId = SlashBladeBuiltInRegistry.SABIGATANA.location();
        if (!bladeRegistry.containsKey(sabigatanaId)) return;

        float roll = random.nextFloat();

        if (roll < SlashBladeConfig.getSabigatanaSpawnChance() * multiplier) {
            ItemStack blade = bladeRegistry.get(sabigatanaId).getBlade();
            entity.setItemSlot(EquipmentSlot.MAINHAND, blade);
        }else if (roll < SlashBladeConfig.getBrokenSabigatanaSpawnChance() * multiplier) {
            var brokenId = SlashBladeBuiltInRegistry.SABIGATANA_BROKEN.location();
            ItemStack blade = bladeRegistry.get(brokenId).getBlade();
            entity.setItemSlot(EquipmentSlot.MAINHAND, blade);
        }
    }
}
