package com.flechazo.slashblade.entity;

import com.flechazo.slashblade.ability.StunManager;
import com.flechazo.slashblade.capability.persistentdata.PersistentDataHelper;
import com.flechazo.slashblade.network.util.PlayMessages;
import com.flechazo.slashblade.registry.EntityTypeRegister;
import com.flechazo.slashblade.util.KnockBacks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityHeavyRainSwords extends EntityAbstractSummonedSword {
    private static final EntityDataAccessor<Boolean> IT_FIRED = SynchedEntityData.defineId(EntityHeavyRainSwords.class,
            EntityDataSerializers.BOOLEAN);

    public EntityHeavyRainSwords(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);

        this.setPierce((byte) 5);

        PersistentDataHelper.getPersistentData(this).ifPresent(persistentData -> {
            CompoundTag compoundtag = persistentData.getPersistentData();
            ListTag listtag = compoundtag.getList("CustomPotionEffects", 9);
            MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 10);
            listtag.add(mobeffectinstance.save(new CompoundTag()));
            compoundtag.put("CustomPotionEffects", listtag);
        });
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        this.entityData.define(IT_FIRED, false);
    }

    public void doFire() {
        this.getEntityData().set(IT_FIRED, true);
    }

    public boolean itFired() {
        return this.getEntityData().get(IT_FIRED);
    }

    public static EntityHeavyRainSwords createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new EntityHeavyRainSwords(EntityTypeRegister.HeavyRainSwords, worldIn);
    }

    long fireTime = -1;

    @Override
    public void tick() {
        if (!itFired()) {
            if (level().isClientSide()) {
                if (getVehicle() == null) {
                    startRiding(this.getOwner(), true);
                }
            }
        }

        super.tick();
    }

    @Override
    public void rideTick() {
        if (itFired() && fireTime <= tickCount) {
            faceEntityStandby();

            this.stopRiding();

            Vec3 dir = new Vec3(0, -1, 0);
            this.shoot(dir.x, dir.y, dir.z, 4.0f, 2.0f);

            this.tickCount = 0;

            return;
        }

        // this.startRiding()
        this.setDeltaMovement(Vec3.ZERO);
        this.baseTick();

        faceEntityStandby();
        // this.getVehicle().positionRider(this);

        // lifetime check
        if (!itFired()) {
            int basedelay = 10;
            fireTime = tickCount + basedelay + getDelay();
            doFire();
        }

        /*
         * if(!level().isClientSide()) hitCheck();
         */
    }

    private void faceEntityStandby() {
        setPos(this.position());

        setRot(this.getYRot(), -90);

    }

    public void setSpread(Vec3 basePos) {
        double areaSize = 2.5;

        double offsetX = (this.random.nextDouble() * 2.0 - 1.0) * areaSize;
        double offsetZ = (this.random.nextDouble() * 2.0 - 1.0) * areaSize;

        setPos(basePos.x + offsetX, basePos.y, basePos.z + offsetZ);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {

        Entity targetEntity = entityHitResult.getEntity();
        if (targetEntity instanceof LivingEntity) {
            KnockBacks.cancel.action.accept((LivingEntity) targetEntity);
            StunManager.setStun((LivingEntity) targetEntity);
        }

        super.onHitEntity(entityHitResult);
    }

    int ON_GROUND_LIFE_TIME = 20;
    int ticksInGround = 0;

    protected void tryDespawn() {
        ++this.ticksInGround;
        if (ON_GROUND_LIFE_TIME <= this.ticksInGround) {
            this.burst();
        }

    }
}
