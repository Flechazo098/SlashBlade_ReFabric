package com.flechazo.slashblade.client.renderer.entity;

import com.flechazo.slashblade.event.NameTagRenderCallback;
import com.flechazo.slashblade.registry.SlashBladeRegister;
import com.mojang.blaze3d.vertex.PoseStack;
import com.flechazo.slashblade.client.renderer.util.MSAutoCloser;
import com.flechazo.slashblade.entity.BladeStandEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Axis;

public class BladeStandEntityRenderer extends ItemFrameRenderer<BladeStandEntity> {
    private final net.minecraft.client.renderer.entity.ItemRenderer itemRenderer;

    public BladeStandEntityRenderer(EntityRendererProvider.Context context) {
        super(context);

        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(BladeStandEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn,
            MultiBufferSource bufferIn, int packedLightIn) {
        doRender(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public void doRender(BladeStandEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn,
            MultiBufferSource bufferIn, int packedLightIn) {

        if (entity.currentTypeStack.isEmpty()) {
            if (entity.currentType == null || entity.currentType == Items.AIR) {
                entity.currentTypeStack = new ItemStack(Items.ITEM_FRAME);
            } else {
                entity.currentTypeStack = new ItemStack(entity.currentType);
            }
            entity.currentTypeStack.setEntityRepresentation(entity);
        }

        try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStackIn)) {
            BlockPos blockpos = entity.getPos();
            Vec3 vec = Vec3.upFromBottomCenterOf(blockpos, 0.75).subtract(entity.position());
            matrixStackIn.translate(vec.x, vec.y, vec.z);
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - entity.getYRot()));

            try (MSAutoCloser msacB = MSAutoCloser.pushMatrix(matrixStackIn)) {
                int i = entity.getRotation();
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees((float) i * 360.0F / 8.0F));

                matrixStackIn.scale(2, 2, 2);
                Item type = entity.currentType;
                if (type == SBItems.bladestand_1) {
                    matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90f));
                } else if (type == SBItems.bladestand_2) {
                    matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90f));
                } else if (type == SBItems.bladestand_v) {
                    matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90f));
                } else if (type == SBItems.bladestand_s) {
                    matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90f));
                } else if (type == SBItems.bladestand_1w) {
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(180f));
                    matrixStackIn.translate(0, 0, -0.15f);
                } else if (type == SBItems.bladestand_2w) {
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(180f));
                    matrixStackIn.translate(0, 0, -0.15f);
                }

                // stand render
                matrixStackIn.pushPose();
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(90));
                matrixStackIn.scale(0.5f, 0.5f, 0.5f);
                matrixStackIn.translate(0, 0, 0.44);
                this.renderItem(entity, entity.currentTypeStack, matrixStackIn, bufferIn, packedLightIn);
                matrixStackIn.popPose();

                if (entity.currentType == SlashBladeRegister.bladestand_1w || type == SBItems.bladestand_2w) {
                    matrixStackIn.translate(0, 0, -0.19f);
                } else if (entity.currentType == SBItems.bladestand_1) {
                }
                // blade render
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(-180f));
                this.renderItem(entity, entity.getItem(), matrixStackIn, bufferIn, packedLightIn);

            }
        }

        if (NameTagRenderCallback.EVENT.invoker().shouldRenderNameTag(entity, entity.getDisplayName(), matrixStackIn, bufferIn, packedLightIn)) {
            this.renderNameTag(entity, entity.getDisplayName(), matrixStackIn, bufferIn, packedLightIn);
        }

    }

    private void renderItem(BladeStandEntity entity, ItemStack itemstack, PoseStack matrixStackIn,
            MultiBufferSource bufferIn, int packedLightIn) {
        if (!itemstack.isEmpty()) {
            BakedModel ibakedmodel = this.itemRenderer.getModel(itemstack, entity.level(), (LivingEntity) null, 0);
            this.itemRenderer.render(itemstack, ItemDisplayContext.FIXED, false, matrixStackIn, bufferIn, packedLightIn,
                    OverlayTexture.NO_OVERLAY, ibakedmodel);
        }
    }

}
