package com.flechazo.slashblade.client;

import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.event.bladestand.BlandStandEventHandler;
import com.flechazo.slashblade.event.client.AdvancementsRecipeRenderer;
import com.flechazo.slashblade.event.client.RenderOverrideEvent;
import com.flechazo.slashblade.event.client.SneakingMotionCanceller;
import com.flechazo.slashblade.event.client.UserPoseOverrider;
import com.flechazo.slashblade.network.NetworkManager;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.ResourceLocation;

public class ClientHandler implements ClientModInitializer {
    @Override
    public void onInitializeClient () {
        // 注册客户端网络处理器
        NetworkManager.registerClientReceivers();

        BlandStandEventHandler.init();
        AdvancementsRecipeRenderer.getInstance().register();

        // 注册渲染事件处理器
        RenderOverrideEvent.EVENT.register((stack, model, target, texture, matrixStack, buffer) -> {
            // 获取刀的状态
            var bladeStateOpt = BladeStateHelper.getBladeState(stack);
            if (bladeStateOpt.isEmpty()) {
                return new RenderOverrideEvent.RenderOverrideResult(model, target, texture, false);
            }

            var bladeState = bladeStateOpt.get();

            // 检查是否有自定义模型和材质
            ResourceLocation customTexture = texture;
            if (bladeState.getTexture().isPresent()) {
                customTexture = bladeState.getTexture().get();
            }

            // 这里可以添加更多自定义渲染逻辑

            // 返回可能修改过的结果
            return new RenderOverrideEvent.RenderOverrideResult(model, target, customTexture, false);
        });

        SneakingMotionCanceller.getInstance().register();
        UserPoseOverrider.getInstance().register();
    }
}
