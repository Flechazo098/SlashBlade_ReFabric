package com.flechazo.slashblade.event.client;

import com.flechazo.slashblade.item.ItemSlashBlade;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class SneakingMotionCanceller {
    private static final class SingletonHolder {
        private static final SneakingMotionCanceller instance = new SneakingMotionCanceller();
    }

    public static SneakingMotionCanceller getInstance() {
        return SingletonHolder.instance;
    }

    private SneakingMotionCanceller() {
    }

    public void register() {
        // 使用Fabric的实体渲染事件
        LivingEntityFeatureRenderEvents.BEFORE_ALL.register((entity, pose, buffer, partialTick, light, overlay, renderer) -> {
            if (!(entity instanceof Player player)) {
                return true;
            }

            ItemStack stack = player.getMainHandItem();
            if (stack.isEmpty() || !(stack.getItem() instanceof ItemSlashBlade)) {
                return true;
            }

            if (renderer instanceof PlayerRenderer playerRenderer) {
                PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();

                if (!model.crouching) {
                    return true;
                }

                final Minecraft instance = Minecraft.getInstance();
                if (instance.options.getCameraType() == CameraType.FIRST_PERSON && instance.player == player) {
                    return true;
                }

                model.crouching = false;

                Vec3 offset = playerRenderer.getRenderOffset((AbstractClientPlayer) player, partialTick).scale(-1);
                pose.translate(offset.x, offset.y, offset.z);
            }

            return true;
        });
    }
}