package com.flechazo.slashblade.capability.mobeffect;

import com.google.common.collect.Sets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;

import java.util.Optional;
import java.util.Set;

public class MobEffectComponentImpl implements MobEffectComponent {

    private long stunTimeout = -1;
    private int stunLimit = 200;
    private Optional<Long> untouchableTimeout = Optional.empty();
    private int untouchableLimit = 200;
    private Set<MobEffect> effectSet = Sets.newHashSet();
    private float storedHealth;
    private boolean hasWorked;
    private Optional<Long> avoidCooldown = Optional.empty();
    private int avoidCount = 0;

    @Override
    public void setStunTimeOut(long timeout) {
        stunTimeout = timeout;
    }

    @Override
    public long getStunTimeOut() {
        return stunTimeout;
    }

    @Override
    public int getStunLimit() {
        return stunLimit;
    }

    @Override
    public void setStunLimit(int limit) {
        this.stunLimit = limit;
    }

    @Override
    public int getUntouchableLimit() {
        return untouchableLimit;
    }

    @Override
    public void setUntouchableLimit(int limit) {
        this.untouchableLimit = limit;
    }

    @Override
    public void setUntouchableTimeOut(Optional<Long> timeout) {
        this.untouchableTimeout = timeout;
    }

    @Override
    public Optional<Long> getUntouchableTimeOut() {
        return this.untouchableTimeout;
    }

    @Override
    public Set<MobEffect> getEffectSet() {
        return effectSet;
    }

    @Override
    public boolean hasUntouchableWorked() {
        return this.hasWorked;
    }

    @Override
    public void setUntouchableWorked(boolean b) {
        this.hasWorked = b;
    }

    @Override
    public float getStoredHealth() {
        return storedHealth;
    }

    @Override
    public void storeHealth(float health) {
        this.storedHealth = health;
    }

    @Override
    public Optional<Long> getAvoidCooldown() {
        return avoidCooldown;
    }

    @Override
    public int getAvoidCount() {
        return avoidCount;
    }

    @Override
    public void setAvoidCooldown(Optional<Long> time) {
        this.avoidCooldown = time;
    }

    @Override
    public void setAvoidCount(int value) {
        avoidCount = value;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        if (tag.contains("StunTimeout")) {
            this.stunTimeout = tag.getLong("StunTimeout");
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putLong("StunTimeout", this.stunTimeout);
    }
}