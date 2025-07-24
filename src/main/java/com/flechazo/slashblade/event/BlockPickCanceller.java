package com.flechazo.slashblade.event;

/**
 * 此类已被弃用
 * <p>
 * 请参阅 {@link com.flechazo.slashblade.mixin.event.PickBlockCancellerMixin} 类以获得替代实现。
 *
 * @deprecated 使用 {@link com.flechazo.slashblade.mixin.event.PickBlockCancellerMixin} 替代。
 */
@Deprecated
public class BlockPickCanceller {
//    private static final class SingletonHolder {
//        private static final BlockPickCanceller instance = new BlockPickCanceller();
//    }
//
//    public static BlockPickCanceller getInstance() {
//        return BlockPickCanceller.SingletonHolder.instance;
//    }
//
//    private BlockPickCanceller() {
//    }
//
//    public void register() {
//        MinecraftForge.EVENT_BUS.register(this);
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent
//    public void onBlockPick(InputEvent.InteractionKeyMappingTriggered event) {
//        if (!event.isPickBlock())
//            return;
//
//        final Minecraft instance = Minecraft.getInstance();
//        LocalPlayer player = instance.player;
//        if (player == null)
//            return;
//        if (SlashBladeKeyMappings.KEY_SUMMON_BLADE.getKey() != SlashBladeKeyMappings.KEY_SUMMON_BLADE.getDefaultKey())
//            return;
//        if (player.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).isPresent()) {
//            event.setCanceled(true);
//        }
//    }
}
