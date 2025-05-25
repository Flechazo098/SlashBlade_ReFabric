package com.flechazo.slashblade.mixin.event;

import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.util.accessor.PersistentDataAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public abstract class UserPoseOverriderMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))

    private void onRenderPre(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight, CallbackInfo ci) {
        // 只针对手持你自定义刀具的实体
        ItemStack stack = entity.getMainHandItem();
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSlashBlade)) return;


        // 读取自定义NBT
        CompoundTag tag = ((PersistentDataAccessor) entity).slashbladerefabriced$getPersistentData();
        float rot = tag.getFloat("sb_yrot");
        float rotPrev = tag.getFloat("sb_yrot_prev");

        // 1）绕Y轴先旋转到身体朝向的反向
        float bodyYaw = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));

        // 2）执行"正向"那套飞行／游泳姿态旋转
        applyFlightSwimRot(poseStack, entity, partialTicks, true);

        // 3）再绕Y轴旋转用户自定义角度 sb_yrot
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp(partialTicks, rot, rotPrev)));

        // 4）执行"反向"那套飞行／游泳姿态旋转
        applyFlightSwimRot(poseStack, entity, partialTicks, false);

        // 5）恢复身体原向（180° - bodyYaw）
        poseStack.mulPose(Axis.YN.rotationDegrees(180.0F - bodyYaw));
    }

    private static <T extends LivingEntity> void applyFlightSwimRot(PoseStack matrices, T entity, float partialTicks, boolean isPositive) {

        float np = isPositive ? 1 : -1;
        float swim = entity.getSwimAmount(partialTicks);

        if (entity.isFallFlying()) {
            float t = (entity.getFallFlyingTicks() + partialTicks);
            float f2 = Mth.clamp(t * t / 100.0F, 0.0F, 1.0F);
            if (!entity.isAutoSpinAttack()) {
                matrices.mulPose(Axis.XP.rotationDegrees(np * f2 * (-90.0F - entity.getXRot())));
            }
            Vec3 view = entity.getViewVector(partialTicks);
            Vec3 motion = entity.getDeltaMovement();
            double d0 = motion.horizontalDistanceSqr(), d1 = view.horizontalDistanceSqr();
            if (d0 > 0 && d1 > 0) {
                double dot = (motion.x * view.x + motion.z * view.z) / Math.sqrt(d0 * d1);
                double cross = motion.x * view.z - motion.z * view.x;
                matrices.mulPose(Axis.YP.rotation((float)(np * Math.signum(cross) * Math.acos(dot))));
            }
        } else if (swim > 0.0F) {
            float f3 = entity.isInWater() ? -90.0F - entity.getXRot() : -90.0F;
            float angle = Mth.lerp(swim, 0.0F, f3);
            matrices.mulPose(Axis.XP.rotationDegrees(np * angle));
            if (entity.isVisuallySwimming()) {
                matrices.translate(0.0D, np * -1.0D, np * 0.3F);
            }
        }
    }
}