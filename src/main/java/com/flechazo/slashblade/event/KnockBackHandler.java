package com.flechazo.slashblade.event;

import com.flechazo.slashblade.capability.persistentdata.PersistentDataHelper;
import com.flechazo.slashblade.util.NBTHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * 击退事件处理类
 * 具体实现请查阅 {@link com.flechazo.slashblade.mixin.event.KnockBackHandlerMixin}
 */
public class KnockBackHandler {

    public static final String NBT_KEY = "knockback_factor";

    static public void setCancel(LivingEntity target) {
        setFactor(target, 0, 0, 0);
    }

    static public void setSmash(LivingEntity target, double horizontalFactor) {
        setFactor(target, horizontalFactor, 0, 0);
    }

    static public void setVertical(LivingEntity target, double verticalFactor) {
        setFactor(target, 0, verticalFactor, -verticalFactor);
    }

    static public void setFactor(LivingEntity target, double horizontalFactor, double verticalFactor,
                                 double addFallDistance) {
        PersistentDataHelper.getPersistentData(target).ifPresent(persistentData -> {
            NBTHelper.putVector3d(persistentData.getPersistentData(), NBT_KEY,
                    new Vec3(horizontalFactor, verticalFactor, addFallDistance));
        });
    }

}
