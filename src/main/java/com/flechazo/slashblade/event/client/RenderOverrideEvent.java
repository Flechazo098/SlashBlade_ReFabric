package com.flechazo.slashblade.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.flechazo.slashblade.client.renderer.model.obj.WavefrontObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class RenderOverrideEvent {
    public static final Event<RenderOverrideCallback> EVENT = EventFactory.createArrayBacked(RenderOverrideCallback.class,
            (listeners) -> (stack, model, target, texture, matrixStack, buffer) -> {
                RenderOverrideResult result = new RenderOverrideResult(model, target, texture, false);
                for (RenderOverrideCallback listener : listeners) {
                    result = listener.onRenderOverride(stack, model, target, texture, matrixStack, buffer);
                    if (result.cancelled()) {
                        break;
                    }
                }
                return result;
            });

    @Environment(EnvType.CLIENT)
    public interface RenderOverrideCallback {
        RenderOverrideResult onRenderOverride(ItemStack stack, WavefrontObject model, String target,
                                              ResourceLocation texture, PoseStack matrixStack, MultiBufferSource buffer);
    }

    @Environment(EnvType.CLIENT)
        public record RenderOverrideResult(WavefrontObject model, String target, ResourceLocation texture,
                                           boolean cancelled) {
    }

    public static RenderOverrideResult onRenderOverride(ItemStack stack, WavefrontObject model, String target,
                                                        ResourceLocation texture, PoseStack matrixStack, MultiBufferSource buffer) {
        return EVENT.invoker().onRenderOverride(stack, model, target, texture, matrixStack, buffer);
    }
}