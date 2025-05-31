package com.flechazo.slashblade.event.client;

/**
 * 此类已被弃用
 *<p>
 * 请参阅 {@link com.flechazo.slashblade.mixin.event.SneakingMotionCancellerMixin} 类以获得替代实现。
 * @deprecated 使用 {@link com.flechazo.slashblade.mixin.event.SneakingMotionCancellerMixin} 替代。
 */
@Deprecated
public class SneakingMotionCanceller {
//    private static final class SingletonHolder {
//        private static final SneakingMotionCanceller instance = new SneakingMotionCanceller();
//    }
//
//    public static SneakingMotionCanceller getInstance() {
//        return SingletonHolder.instance;
//    }
//
//    private SneakingMotionCanceller() {
//    }
//
//    public void register() {
//        LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register((entity, pose, buffer, partialTick, light, overlay, renderer) -> {
//            if (!(entity instanceof Player player)) {
//                return true;
//            }
//
//            ItemStack stack = player.getMainHandItem();
//            if (stack.isEmpty() || !(stack.getItem() instanceof ItemSlashBlade)) {
//                return true;
//            }
//
//            if (renderer instanceof PlayerRenderer playerRenderer) {
//                PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();
//
//                if (!model.crouching) {
//                    return true;
//                }
//
//                final Minecraft instance = Minecraft.getInstance();
//                if (instance.options.getCameraType() == CameraType.FIRST_PERSON && instance.player == player) {
//                    return true;
//                }
//
//                model.crouching = false;
//
//                Vec3 offset = playerRenderer.getRenderOffset((AbstractClientPlayer) player, partialTick).scale(-1);
//                pose.translate(offset.x, offset.y, offset.z);
//            }
//
//            return true;
//        });
//    }
}