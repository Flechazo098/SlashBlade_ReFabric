package com.flechazo.slashblade.client;

import com.flechazo.slashblade.client.renderer.entity.*;
import com.flechazo.slashblade.client.renderer.gui.RankRenderer;
import com.flechazo.slashblade.client.renderer.layers.LayerMainBlade;
import com.flechazo.slashblade.client.renderer.model.BladeModel;
import com.flechazo.slashblade.compat.playerAnim.PlayerAnimationOverrider;
import com.flechazo.slashblade.event.bladestand.BlandStandEventHandler;
import com.flechazo.slashblade.event.client.AdvancementsRecipeRenderer;
import com.flechazo.slashblade.event.client.UserPoseOverrider;
import com.flechazo.slashblade.network.NetworkManager;
import com.flechazo.slashblade.registry.EntityTypeRegister;
import com.flechazo.slashblade.registry.SlashBladeRegister;
import io.github.fabricators_of_create.porting_lib.event.client.EntityAddedLayerCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.util.LoaderUtil;

import java.util.Map;

public class ClientHandler implements ClientModInitializer {
    @Override
    public void onInitializeClient () {
        NetworkManager.registerClientReceivers();

        if (LoaderUtil.isClassAvailable("dev.kosmx.playerAnim.api.layered.AnimationStack")) {
            PlayerAnimationOverrider.getInstance().register();
        } else {
            UserPoseOverrider.UsePoseOverrider = true;
        }

        BlandStandEventHandler.init();
        AdvancementsRecipeRenderer.getInstance().register();

        RankRenderer.getInstance().register();

        ItemProperties.register(SlashBladeRegister.SLASHBLADE, new ResourceLocation("slashblade", "user"),
                (itemStack, level, entity, seed) -> {
                    BladeModel.user = entity;
                    return 0;
                });

        ItemProperties.register(SlashBladeRegister.BAMBOO, new ResourceLocation("slashblade", "user"),
                (itemStack, level, entity, seed) -> {
                    BladeModel.user = entity;
                    return 0;
                });

        ItemProperties.register(SlashBladeRegister.SILVER, new ResourceLocation("slashblade", "user"),
                (itemStack, level, entity, seed) -> {
                    BladeModel.user = entity;
                    return 0;
                });

        ItemProperties.register(SlashBladeRegister.WHITE, new ResourceLocation("slashblade", "user"),
                (itemStack, level, entity, seed) -> {
                    BladeModel.user = entity;
                    return 0;
                });

        ItemProperties.register(SlashBladeRegister.WOOD, new ResourceLocation("slashblade", "user"),
                (itemStack, level, entity, seed) -> {
                    BladeModel.user = entity;
                    return 0;
                });

        SlashBladeKeyMappings.init();
        registerEntityRenderers();
        addLayers();
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

    private static void addLayers() {
        EntityAddedLayerCallback.EVENT.register((renderers, skinMap) -> {
            addPlayerLayer(skinMap, "default");
            addPlayerLayer(skinMap, "slim");

            addEntityLayer(renderers, EntityType.ZOMBIE);
            addEntityLayer(renderers, EntityType.HUSK);
            addEntityLayer(renderers, EntityType.ZOMBIE_VILLAGER);

            addEntityLayer(renderers, EntityType.WITHER_SKELETON);
            addEntityLayer(renderers, EntityType.SKELETON);
            addEntityLayer(renderers, EntityType.STRAY);

            addEntityLayer(renderers, EntityType.PIGLIN);
            addEntityLayer(renderers, EntityType.PIGLIN_BRUTE);
            addEntityLayer(renderers, EntityType.ZOMBIFIED_PIGLIN);
        });
    }

    private static <T extends LivingEntity, M extends EntityModel<T>> void addLayer(LivingEntityRenderer<T, M> renderer) {
        renderer.addLayer(new LayerMainBlade<>(renderer));
    }

    private static void addPlayerLayer(Map<String, EntityRenderer<? extends Player>> skinMap, String skin) {
        EntityRenderer<? extends Player> renderer = skinMap.get(skin);
        if (renderer instanceof LivingEntityRenderer<?, ?> livingRenderer) {
            addLayer((LivingEntityRenderer<?, ?>) livingRenderer);
        }
    }

    private static void addEntityLayer(Map<EntityType<?>, EntityRenderer<?>> renderers, EntityType<?> type) {
        EntityRenderer<?> renderer = renderers.get(type);
        if (renderer instanceof LivingEntityRenderer<?, ?> livingRenderer) {
            addLayer((LivingEntityRenderer<?, ?>) livingRenderer);
        }
    }
}
