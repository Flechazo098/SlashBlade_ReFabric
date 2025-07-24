package com.flechazo.slashblade.event.client;

import com.flechazo.slashblade.capability.persistentdata.PersistentDataHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

@Environment(EnvType.CLIENT)
public class UserPoseOverrider {

    static public boolean UsePoseOverrider = false;

    private UserPoseOverrider() {
    }

    private static final String TAG_ROT = "sb_yrot";
    private static final String TAG_ROT_PREV = "sb_yrot_prev";


    static public void setRot(Entity target, float rotYaw, boolean isOffset) {
        PersistentDataHelper.getPersistentData(target).ifPresent(persistentData -> {
            CompoundTag tag = persistentData.getPersistentData();

            float prevRot = tag.getFloat(TAG_ROT);
            tag.putFloat(TAG_ROT_PREV, prevRot);

            float finalRotYaw = isOffset ? rotYaw + prevRot : rotYaw;

            tag.putFloat(TAG_ROT, finalRotYaw);
        });
    }


    static public void resetRot(Entity target) {
        PersistentDataHelper.getPersistentData(target).ifPresent(persistentData -> {
            CompoundTag tag = persistentData.getPersistentData();
            tag.putFloat(TAG_ROT_PREV, 0);
            tag.putFloat(TAG_ROT, 0);
        });
    }

    static public void invertRot(PoseStack matrixStack, Entity entity, float partialTicks) {
        PersistentDataHelper.getPersistentData(entity).ifPresent(persistentData -> {
            CompoundTag tag = persistentData.getPersistentData();
            float rot = tag.getFloat(TAG_ROT);
            float rotPrev = tag.getFloat(TAG_ROT_PREV);
            matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp(partialTicks, rot, rotPrev)));
        });
    }

}