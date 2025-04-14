package com.flechazo.slashblade.capability.concentrationrank;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;

public interface ConcentrationRankComponent extends Component {
    long getRawRankPoint();

    void setRawRankPoint(long point);

    long getLastUpdate();

    void setLastUpdte(long time);

    long getLastRankRise();

    void setLastRankRise(long time);

    long getUnitCapacity();

    float getRankPointModifier(DamageSource ds);

    float getRankPointModifier(ResourceLocation combo);

    // 添加获取等级方法
    ConcentrationRanks getRank(long time);

    // 添加获取等级进度方法
    float getRankLevel(long time);

    // 定义等级枚举
    enum ConcentrationRanks {
        NONE(0, Range.lessThan(1.0f)),
        D(1, Range.closedOpen(1.0f, 2.0f)),
        C(2, Range.closedOpen(2.0f, 3.0f)),
        B(3, Range.closedOpen(3.0f, 4.0f)),
        A(4, Range.closedOpen(4.0f, 5.0f)),
        S(5, Range.closedOpen(5.0f, 5.25f)),
        SS(6, Range.closedOpen(5.25f, 5.5f)),
        SSS(7, Range.atLeast(5.5f));

        static public float MAX_LEVEL = 6.0f;

        final Range<Float> pointRange;
        public final int level;

        ConcentrationRanks(int level, Range<Float> pointRange) {
            this.pointRange = pointRange;
            this.level = level;
        }

        public static ConcentrationRanks getRankFromLevel(float point) {
            return concentrationRanksMap.get(point);
        }

        private static RangeMap<Float, ConcentrationRanks> concentrationRanksMap = ImmutableRangeMap
                .<Float, ConcentrationRanks>builder()
                .put(ConcentrationRanks.NONE.pointRange, ConcentrationRanks.NONE)
                .put(ConcentrationRanks.D.pointRange, ConcentrationRanks.D)
                .put(ConcentrationRanks.C.pointRange, ConcentrationRanks.C)
                .put(ConcentrationRanks.B.pointRange, ConcentrationRanks.B)
                .put(ConcentrationRanks.A.pointRange, ConcentrationRanks.A)
                .put(ConcentrationRanks.S.pointRange, ConcentrationRanks.S)
                .put(ConcentrationRanks.SS.pointRange, ConcentrationRanks.SS)
                .put(ConcentrationRanks.SSS.pointRange, ConcentrationRanks.SSS).build();
    }
}