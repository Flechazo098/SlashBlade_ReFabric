package com.flechazo.slashblade.event;

import com.flechazo.slashblade.network.MotionBroadcastMessage;
import com.flechazo.slashblade.network.NetworkManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class BladeMotionEventBroadcaster {

    private static final class SingletonHolder {
        private static final BladeMotionEventBroadcaster instance = new BladeMotionEventBroadcaster();
    }

    public static BladeMotionEventBroadcaster getInstance() {
        return BladeMotionEventBroadcaster.SingletonHolder.instance;
    }

    private BladeMotionEventBroadcaster() {
    }

    public void register() {
        BladeMotionEvent.BLADE_MOTION.register(event -> {
            if (!(event.getEntity() instanceof ServerPlayer sp))
                return;

            MotionBroadcastMessage msg = new MotionBroadcastMessage();
            msg.playerId = sp.getUUID();
            msg.combo = event.getCombo().toString();

            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeUUID(msg.playerId);
            buf.writeUtf(msg.combo);

            NetworkManager.sendToNear(
                    sp.serverLevel(),
                    (int) sp.getX(),
                    (int) sp.getY(),
                    (int) sp.getZ(),
                    20.0,
                    NetworkManager.MOTION_BROADCAST_ID,
                    buf
            );
        });
    }
}
