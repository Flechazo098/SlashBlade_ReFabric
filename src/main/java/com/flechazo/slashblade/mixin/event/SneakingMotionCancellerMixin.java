package com.flechazo.slashblade.mixin.event;

import com.flechazo.slashblade.item.ItemSlashBlade;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerRenderer.class)
public abstract class SneakingMotionCancellerMixin {

    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void onRenderPre(AbstractClientPlayer player, float yaw, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {

        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSlashBlade)) return;

        if (player.isCrouching()) {
            PlayerModel<AbstractClientPlayer> model = ((PlayerRenderer) (Object) this).getModel();
            model.crouching = false;

            Vec3 offset = ((PlayerRenderer) (Object) this).getRenderOffset(player, tickDelta).scale(-1);
            poseStack.translate(offset.x, offset.y, offset.z);
        }
    }
}
