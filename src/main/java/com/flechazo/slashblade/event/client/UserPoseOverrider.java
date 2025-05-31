package com.flechazo.slashblade.event.client;

import com.flechazo.slashblade.util.accessor.PersistentDataAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;

@Environment(EnvType.CLIENT)
public class UserPoseOverrider {

    static public boolean UsePoseOverrider = false;

    private UserPoseOverrider() {
    }

    private static final String TAG_ROT = "sb_yrot";
    private static final String TAG_ROT_PREV = "sb_yrot_prev";


    static public void setRot(Entity target, float rotYaw, boolean isOffset) {
        CompoundTag tag = ((PersistentDataAccessor) target).slashbladerefabriced$getPersistentData();

        float prevRot = tag.getFloat(TAG_ROT);
        tag.putFloat(TAG_ROT_PREV, prevRot);

        if (isOffset)
            rotYaw += prevRot;

        tag.putFloat(TAG_ROT, rotYaw);
    }

    static public void resetRot(Entity target) {
        CompoundTag tag = ((PersistentDataAccessor) target).slashbladerefabriced$getPersistentData();
        tag.putFloat(TAG_ROT_PREV, 0);
        tag.putFloat(TAG_ROT, 0);
    }

    static public void invertRot(PoseStack matrixStack, Entity entity, float partialTicks) {
        float rot = ((PersistentDataAccessor) entity).slashbladerefabriced$getPersistentData().getFloat(TAG_ROT);
        float rotPrev =((PersistentDataAccessor) entity).slashbladerefabriced$getPersistentData().getFloat(TAG_ROT_PREV);
        matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp(partialTicks, rot, rotPrev)));
    }
}