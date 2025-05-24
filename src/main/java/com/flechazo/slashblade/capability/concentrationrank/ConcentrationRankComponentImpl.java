package com.flechazo.slashblade.capability.concentrationrank;

import com.flechazo.slashblade.capability.slashblade.BladeStateComponentRegistry;
import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.network.NetworkManager;
import com.flechazo.slashblade.network.RankSyncMessage;
import com.google.common.collect.Range;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ConcentrationRankComponentImpl implements ConcentrationRankComponent {
    private long rankpoint;
    private long lastupdate;
    private long lastrankrise;

    static public long UnitCapacity = 300;

    public ConcentrationRankComponentImpl() {
        rankpoint = 0;
        lastupdate = 0;
    }

    @Override
    public long getRawRankPoint() {
        return rankpoint;
    }

    @Override
    public void setRawRankPoint(long point) {
        this.rankpoint = point;
    }

    @Override
    public long getLastUpdate() {
        return lastupdate;
    }

    @Override
    public void setLastUpdte(long time) {
        this.lastupdate = time;
    }

    @Override
    public long getLastRankRise() {
        return this.lastrankrise;
    }

    @Override
    public void setLastRankRise(long time) {
        this.lastrankrise = time;
    }

    @Override
    public long getUnitCapacity() {
        return UnitCapacity;
    }

    @Override
    public float getRankPointModifier(DamageSource ds) {
        return 0.1f;
    }

    @Override
    public float getRankPointModifier(ResourceLocation combo) {
        return 0.1f;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.rankpoint = tag.getLong("rawPoint");
        this.lastupdate = tag.getLong("lastupdate");
        this.lastrankrise = tag.getLong("lastrankrise");
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putLong("rawPoint", this.rankpoint);
        tag.putLong("lastupdate", this.lastupdate);
        tag.putLong("lastrankrise", this.lastrankrise);
    }

    public long getMaxCapacity() {
        return (long) (ConcentrationRanks.MAX_LEVEL * getUnitCapacity()) - 1;
    }

    @Override
    public ConcentrationRanks getRank(long time) {
        return ConcentrationRanks.getRankFromLevel(getRankLevel(time));
    }

    public long reductionLimitter(long reduction) {
        long limit = getRawRankPoint() % getUnitCapacity();
        return Math.min(reduction, limit);
    }

    @Override
    public float getRankLevel(long currentTime) {
        return getRankPoint(currentTime) / (float) getUnitCapacity();
    }

    public float getRankProgress(long currentTime) {
        float level = getRankLevel(currentTime);
        Range<Float> range = getRank(currentTime).pointRange;
        double bottom = range.hasLowerBound() ? range.lowerEndpoint() : 0;
        double top = range.hasUpperBound() ? range.upperEndpoint() : Math.floor(bottom + 1.0f);
        double len = top - bottom;
        return (float) ((level - bottom) / len);
    }

    public long getRankPoint(long time) {
        long reduction = time - getLastUpdate();
        return getRawRankPoint() - reductionLimitter(reduction);
    }

    public void addRankPoint(LivingEntity user, long point) {
        long time = user.level().getGameTime();
        ConcentrationRanks oldRank = getRank(time);
        this.setRawRankPoint(Math.min(Math.max(0, point + getRankPoint(time)), getMaxCapacity()));
        this.setLastUpdte(time);

        if (oldRank.level < getRank(time).level)
            this.setLastRankRise(time);

        if (user instanceof ServerPlayer && !user.level().isClientSide()) {
            if (((ServerPlayer) user).connection == null)
                return;

            RankSyncMessage.sendToClient((ServerPlayer) user, this.getRawRankPoint());
        }
    }

    public void addRankPoint(DamageSource src) {
        if (!(src.getEntity() instanceof LivingEntity))
            return;

        LivingEntity user = (LivingEntity) src.getEntity();
        ItemStack stack = user.getMainHandItem();

        Optional<ResourceLocation> combo = Optional.empty();
        if (stack.getItem() instanceof ItemSlashBlade) {
            combo = BladeStateComponentRegistry.BLADE_STATE.maybeGet(stack)
                    .map(state -> state.resolvCurrentComboState(user));
        }

        float modifier = combo.map(this::getRankPointModifier).orElse(getRankPointModifier(src));
        addRankPoint(user, (long) (modifier * getUnitCapacity()));
    }
}