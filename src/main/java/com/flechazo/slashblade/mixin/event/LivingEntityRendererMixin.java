package com.flechazo.slashblade.mixin.event;

import com.flechazo.slashblade.capability.inputstate.InputStateHelper;
import com.flechazo.slashblade.capability.slashblade.BladeStateComponent;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.client.renderer.LockonCircleRender;
import com.flechazo.slashblade.client.renderer.model.BladeModelManager;
import com.flechazo.slashblade.client.renderer.model.obj.WavefrontObject;
import com.flechazo.slashblade.client.renderer.util.BladeRenderState;
import com.flechazo.slashblade.util.InputCommand;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Optional;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    // Shadow 渲染分发器以获取摄像机信息
    @Unique
    private final EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

    @Inject(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("TAIL")
    )
    private void onRenderLiving(
            T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci
    ) {
        // 只在客户端执行
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (InputStateHelper.getInputState(mc.player)
                .filter(input -> input.getCommands().contains(InputCommand.SNEAK)).isEmpty())
            return;

        ItemStack stack = mc.player.getMainHandItem();
        Level level = mc.player.level();
        Optional<Color> effectColor = BladeStateHelper.getBladeState(stack)
                .filter(s -> entity.equals(s.getTargetEntity(level))).map(BladeStateComponent::getEffectColor);

        if (effectColor.isEmpty())
            return;


        if (!entity.isAlive())
            return;

        float health = entity.getHealth() / entity.getMaxHealth();

        Color col = new Color(effectColor.get().getRGB() & 0xFFFFFF | 0xAA000000, true);

        float f = entity.getBbHeight() * 0.5f;

        poseStack.pushPose();
        poseStack.translate(0.0D, f, 0.0D);

        Vec3 offset = dispatcher.camera.getPosition()
                .subtract(entity.getPosition(partialTicks).add(0, f, 0));
        offset = offset.scale(0.5f);
        poseStack.translate(offset.x(), offset.y(), offset.z());

        poseStack.mulPose(dispatcher.cameraOrientation());
        // poseStack.scale(-0.025F, -0.025F, 0.025F);

        float scale = 0.0025f;
        poseStack.scale(scale, -scale, scale);

        WavefrontObject model = BladeModelManager.getInstance().getModel(LockonCircleRender.modelLoc);
        ResourceLocation resourceTexture = LockonCircleRender.textureLoc;

        final String base = "lockonBase";
        final String mask = "lockonHealthMask";
        final String value = "lockonHealth";

        BladeRenderState.setCol(col);
        BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, base, resourceTexture, poseStack, bufferSource,
                BladeRenderState.MAX_LIGHT);
        {
            poseStack.pushPose();
            poseStack.translate(0, 0, health * 10.0f);
            BladeRenderState.setCol(new Color(0x20000000, true));
            BladeRenderState.renderOverridedLuminousDepthWrite(ItemStack.EMPTY, model, mask, resourceTexture, poseStack,
                    bufferSource, BladeRenderState.MAX_LIGHT);
            poseStack.popPose();
        }
        BladeRenderState.setCol(col);
        BladeRenderState.renderOverridedLuminousDepthWrite(ItemStack.EMPTY, model, value, resourceTexture, poseStack,
                bufferSource, BladeRenderState.MAX_LIGHT);

        poseStack.popPose();
    }
}