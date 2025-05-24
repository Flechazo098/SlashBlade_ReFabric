package com.flechazo.slashblade.event;

import com.flechazo.slashblade.entity.BladeStandEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

@FunctionalInterface
public interface NameTagRenderCallback {
    boolean shouldRenderNameTag(BladeStandEntity entity, Component displayName, PoseStack matrices, MultiBufferSource vertexConsumers, int light);

    Event<NameTagRenderCallback> EVENT = EventFactory.createArrayBacked(
            NameTagRenderCallback.class,
            callbacks -> (entity, name, matrices, vcp, light) -> {
                for (NameTagRenderCallback cb : callbacks) {
                    if (!cb.shouldRenderNameTag(entity, name, matrices, vcp, light)) {
                        return false;
                    }
                }
                return true;
            }
    );
}
