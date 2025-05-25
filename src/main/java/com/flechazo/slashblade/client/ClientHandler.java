package com.flechazo.slashblade.client;

import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.client.renderer.SlashBladeTEISR;
import com.flechazo.slashblade.client.renderer.entity.*;
import com.flechazo.slashblade.client.renderer.gui.RankRenderer;
import com.flechazo.slashblade.client.renderer.model.BladeModel;
import com.flechazo.slashblade.compat.playerAnim.PlayerAnimationOverrider;
import com.flechazo.slashblade.event.bladestand.BlandStandEventHandler;
import com.flechazo.slashblade.event.client.AdvancementsRecipeRenderer;
import com.flechazo.slashblade.event.client.RenderOverrideEvent;
import com.flechazo.slashblade.event.client.UserPoseOverrider;
import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.network.NetworkManager;
import com.flechazo.slashblade.registry.EntityTypeRegister;
import com.flechazo.slashblade.registry.SlashBladeRegister;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.util.LoaderUtil;
import org.jetbrains.annotations.Nullable;

public class ClientHandler implements ClientModInitializer {
    @Override
    public void onInitializeClient () {
        // 注册客户端网络处理器
        NetworkManager.registerClientReceivers();

        if (LoaderUtil.isClassAvailable("dev.kosmx.playerAnim.api.layered.AnimationStack")) {
            PlayerAnimationOverrider.getInstance().register();
        } else {
            UserPoseOverrider.UsePoseOverrider = true;
        }

        BlandStandEventHandler.init();
        AdvancementsRecipeRenderer.getInstance().register();

        RankRenderer.getInstance().register();

        ItemProperties.register(SlashBladeRegister.SLASHBLADE, new ResourceLocation("slashblade:user"),
                (p_174564_, p_174565_, p_174566_, p_174567_) -> {
                    BladeModel.user = p_174566_;
                    return 0;
                });

        ItemProperties.register(SlashBladeRegister.BAMBOO, new ResourceLocation("slashblade:user"),
                (p_174564_, p_174565_, p_174566_, p_174567_) -> {
                    BladeModel.user = p_174566_;
                    return 0;
                });

        ItemProperties.register(SlashBladeRegister.SILVER, new ResourceLocation("slashblade:user"),
                (p_174564_, p_174565_, p_174566_, p_174567_) -> {
                    BladeModel.user = p_174566_;
                    return 0;
                });

        ItemProperties.register(SlashBladeRegister.WHITE, new ResourceLocation("slashblade:user"),
                (p_174564_, p_174565_, p_174566_, p_174567_) -> {
                    BladeModel.user = p_174566_;
                    return 0;
                });

        ItemProperties.register(SlashBladeRegister.WOOD, new ResourceLocation("slashblade:user"),
                (p_174564_, p_174565_, p_174566_, p_174567_) -> {
                    BladeModel.user = p_174566_;
                    return 0;
                });
        SlashBladeKeyMappings.init();
        registerEntityRenderers();
    }

    private void registerEntityRenderers() {
        EntityRendererRegistry.register(EntityTypeRegister.SummonedSword, SummonedSwordRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegister.StormSwords, SummonedSwordRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegister.SpiralSwords, SummonedSwordRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegister.BlisteringSwords, SummonedSwordRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegister.HeavyRainSwords, SummonedSwordRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegister.JudgementCut, JudgementCutRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegister.BladeItem, BladeItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegister.BladeStand, BladeStandEntityRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegister.SlashEffect, SlashEffectRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegister.Drive, DriveRenderer::new);
    }

}
