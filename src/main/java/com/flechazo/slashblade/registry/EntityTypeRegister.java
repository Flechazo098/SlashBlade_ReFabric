package com.flechazo.slashblade.registry;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.entity.*;
import com.flechazo.slashblade.network.util.PlayMessages;
import com.flechazo.slashblade.util.EntitySpawnFactoryRegistryHelper;
import com.google.common.base.CaseFormat;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;

import java.util.function.BiFunction;

public class EntityTypeRegister {
    public static final ResourceLocation BladeItemEntityLoc = new ResourceLocation(SlashBladeRefabriced.MODID,
            classToString(BladeItemEntity.class));
    public static EntityType<BladeItemEntity> BladeItem;

    public static final ResourceLocation BladeStandEntityLoc = new ResourceLocation(SlashBladeRefabriced.MODID,
            classToString(BladeStandEntity.class));
    public static EntityType<BladeStandEntity> BladeStand;

    public static final ResourceLocation SummonedSwordLoc = new ResourceLocation(SlashBladeRefabriced.MODID,
            classToString(EntityAbstractSummonedSword.class));
    public static EntityType<EntityAbstractSummonedSword> SummonedSword;
    public static final ResourceLocation SpiralSwordsLoc = new ResourceLocation(SlashBladeRefabriced.MODID,
            classToString(EntitySpiralSwords.class));
    public static EntityType<EntitySpiralSwords> SpiralSwords;

    public static final ResourceLocation StormSwordsLoc = new ResourceLocation(SlashBladeRefabriced.MODID,
            classToString(EntityStormSwords.class));
    public static EntityType<EntityStormSwords> StormSwords;
    public static final ResourceLocation BlisteringSwordsLoc = new ResourceLocation(SlashBladeRefabriced.MODID,
            classToString(EntityBlisteringSwords.class));
    public static EntityType<EntityBlisteringSwords> BlisteringSwords;
    public static final ResourceLocation HeavyRainSwordsLoc = new ResourceLocation(SlashBladeRefabriced.MODID,
            classToString(EntityHeavyRainSwords.class));
    public static EntityType<EntityHeavyRainSwords> HeavyRainSwords;

    public static final ResourceLocation JudgementCutLoc = new ResourceLocation(SlashBladeRefabriced.MODID,
            classToString(EntityJudgementCut.class));
    public static EntityType<EntityJudgementCut> JudgementCut;

    public static final ResourceLocation SlashEffectLoc = new ResourceLocation(SlashBladeRefabriced.MODID,
            classToString(EntitySlashEffect.class));
    public static EntityType<EntitySlashEffect> SlashEffect;

    public static final ResourceLocation DriveLoc = new ResourceLocation(SlashBladeRefabriced.MODID,
            classToString(EntityDrive.class));
    public static EntityType<EntityDrive> Drive;


    private static String classToString (Class<? extends Entity> entityClass) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityClass.getSimpleName())
                .replace("entity_", "");
    }

    public static final ResourceLocation SUMMONED_SWORD_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "summoned_sword");
    public static final ResourceLocation STORM_SWORDS_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "storm_swords");
    public static final ResourceLocation SPIRAL_SWORDS_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "spiral_swords");
    public static final ResourceLocation BLISTERING_SWORDS_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "blistering_swords");
    public static final ResourceLocation HEAVY_RAIN_SWORDS_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "heavy_rain_swords");
    public static final ResourceLocation JUDGEMENT_CUT_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "judgement_cut");
    public static final ResourceLocation BLADE_ITEM_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "blade_item");
    public static final ResourceLocation BLADE_STAND_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "blade_stand");
    public static final ResourceLocation SLASH_EFFECT_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "slash_effect");
    public static final ResourceLocation DRIVE_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "drive");

    public static void registerEntityTypes() {
        BladeItem = registerEntity(BLADE_ITEM_ID, BladeItemEntityLoc, BladeItemEntity::new, 0.25f, 0.25f, 4, BladeItemEntity::createInstanceFromPacket);
        BladeStand = registerEntity(BLADE_STAND_ID, BladeStandEntityLoc, BladeStandEntity::new, 0.5f, 0.5f, 10, BladeStandEntity::createInstance);

        SummonedSword = registerEntity(SUMMONED_SWORD_ID, SummonedSwordLoc, EntityAbstractSummonedSword::new, 0.5f, 0.5f, 4, EntityDrive::createInstance);
        StormSwords = registerEntity(STORM_SWORDS_ID, StormSwordsLoc, EntityStormSwords::new, 0.5f, 0.5f, 4, EntityStormSwords::createInstance);
        SpiralSwords = registerEntity(SPIRAL_SWORDS_ID, SpiralSwordsLoc, EntitySpiralSwords::new, 0.5f, 0.5f, 4, EntitySpiralSwords::createInstance);
        BlisteringSwords = registerEntity(BLISTERING_SWORDS_ID, BlisteringSwordsLoc, EntityBlisteringSwords::new, 0.5f, 0.5f, 4, EntityBlisteringSwords::createInstance);
        HeavyRainSwords = registerEntity(HEAVY_RAIN_SWORDS_ID, HeavyRainSwordsLoc, EntityHeavyRainSwords::new, 0.5f, 0.5f, 4, EntityHeavyRainSwords::createInstance);

        JudgementCut = registerEntity(JUDGEMENT_CUT_ID, JudgementCutLoc, EntityJudgementCut::new, 2.5f, 2.5f, 4, EntityJudgementCut::createInstance);
        SlashEffect = registerEntity(SLASH_EFFECT_ID, SlashEffectLoc, EntitySlashEffect::new, 3.0f, 3.0f, 4, EntitySlashEffect::createInstance);
        Drive = registerEntity(DRIVE_ID, DriveLoc, EntityDrive::new, 3.0f, 3.0f, 4, EntityDrive::createInstance);
    }


    private static <T extends Entity> EntityType<T> registerEntity (
            ResourceLocation id,
            ResourceLocation loc,
            EntityType.EntityFactory<T> constructor,
            float width, float height,
            int trackingRange,
            BiFunction<PlayMessages.SpawnEntity, Level, T> factory
    ) {
        EntityType<T> type = EntityType.Builder
                .of(constructor, MobCategory.MISC)
                .sized(width, height)
                .clientTrackingRange(trackingRange)
                .updateInterval(20)
                .build(loc.toString());
        Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type);
        EntitySpawnFactoryRegistryHelper.register(type, factory);
        return type;
    }

}
