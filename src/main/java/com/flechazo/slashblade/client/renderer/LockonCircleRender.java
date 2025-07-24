package com.flechazo.slashblade.client.renderer;

import net.minecraft.resources.ResourceLocation;

public class LockonCircleRender {
    private static final class SingletonHolder {
        private static final LockonCircleRender instance = new LockonCircleRender();
    }

    public static LockonCircleRender getInstance() {
        return SingletonHolder.instance;
    }

    private LockonCircleRender() {
    }


    public static final ResourceLocation modelLoc = new ResourceLocation("slashblade", "model/util/lockon.obj");
    public static final ResourceLocation textureLoc = new ResourceLocation("slashblade", "model/util/lockon.png");

    /**
     * 此方法已被弃用
     * <p>
     * 请参阅 {@link com.flechazo.slashblade.mixin.event.LivingEntityRendererMixin} 类以获得替代实现。
     *
     * @deprecated 使用 {@link com.flechazo.slashblade.mixin.event.LivingEntityRendererMixin} 替代。
     */
//    @SubscribeEvent
    @Deprecated
    public void onRenderLiving() {
//        final Minecraft minecraftInstance = Minecraft.getInstance();
//        Player player = minecraftInstance.player;
//        if (player == null)
//            return;
//        if (! InputStateHelper.getInputState(player)
//                .filter(input -> input.getCommands().contains(InputCommand.SNEAK)).isPresent())
//            return;
//
//        ItemStack stack = player.getMainHandItem();
//        Level level = player.level();
//        Optional<Color> effectColor = BladeStateHelper.getBladeState(stack)
//                .filter(s -> event.getEntity().equals(s.getTargetEntity(level))).map(s -> s.getEffectColor());
//
//        if (effectColor.isEmpty())
//            return;
//
//        LivingEntityRenderer<?, ?> renderer = event.getRenderer();
//        LivingEntity livingEntity = event.getEntity();
//
//        if (!livingEntity.isAlive())
//            return;
//
//        float health = livingEntity.getHealth() / livingEntity.getMaxHealth();
//
//        Color col = new Color(effectColor.get().getRGB() & 0xFFFFFF | 0xAA000000, true);
//
//        PoseStack poseStack = event.getPoseStack();
//
//        float f = livingEntity.getBbHeight() * 0.5f;
//        float partialTicks = event.getPartialTick();
//
//        poseStack.pushPose();
//        poseStack.translate(0.0D, (double) f, 0.0D);
//
//        Vec3 offset = renderer.entityRenderDispatcher.camera.getPosition()
//                .subtract(livingEntity.getPosition(partialTicks).add(0, f, 0));
//        offset = offset.scale(0.5f);
//        poseStack.translate(offset.x(), offset.y(), offset.z());
//
//        poseStack.mulPose(renderer.entityRenderDispatcher.cameraOrientation());
//        // poseStack.scale(-0.025F, -0.025F, 0.025F);
//
//        float scale = 0.0025f;
//        poseStack.scale(scale, -scale, scale);
//
//        WavefrontObject model = BladeModelManager.getInstance().getModel(modelLoc);
//        ResourceLocation resourceTexture = textureLoc;
//
//        MultiBufferSource buffer = event.getMultiBufferSource();
//
//        final String base = "lockonBase";
//        final String mask = "lockonHealthMask";
//        final String value = "lockonHealth";
//
//        BladeRenderState.setCol(col);
//        BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, base, resourceTexture, poseStack, buffer,
//                BladeRenderState.MAX_LIGHT);
//        {
//            poseStack.pushPose();
//            poseStack.translate(0, 0, health * 10.0f);
//            BladeRenderState.setCol(new Color(0x20000000, true));
//            BladeRenderState.renderOverridedLuminousDepthWrite(ItemStack.EMPTY, model, mask, resourceTexture, poseStack,
//                    buffer, BladeRenderState.MAX_LIGHT);
//            poseStack.popPose();
//        }
//        BladeRenderState.setCol(col);
//        BladeRenderState.renderOverridedLuminousDepthWrite(ItemStack.EMPTY, model, value, resourceTexture, poseStack,
//                buffer, BladeRenderState.MAX_LIGHT);
//
//        poseStack.popPose();
    }
}
