package com.flechazo.slashblade.network;

import com.flechazo.slashblade.event.BladeMotionEvent;
import com.flechazo.slashblade.registry.ComboStateRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class MotionBroadcastMessage {
    public UUID playerId;
    public String combo;

    public MotionBroadcastMessage(UUID playerId, String combo) {
        this.playerId = playerId;
        this.combo = combo;
    }

    public MotionBroadcastMessage () {

    }

    // 发送到所有客户端
    public static void broadcastToAll(UUID playerId, String combo) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeUUID(playerId);
        buf.writeUtf(combo);
        NetworkManager.sendToAll(NetworkManager.MOTION_BROADCAST_ID, buf);
    }

    // 客户端处理
    public static void handleClient(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        UUID playerId = buf.readUUID();
        String combo = buf.readUtf();

        client.execute(() -> {
            Player target = client.level.getPlayerByUUID(playerId);

            if (target == null)
                return;
            if (!(target instanceof AbstractClientPlayer))
                return;

            ResourceLocation state = ResourceLocation.tryParse(combo);
            if (state == null || !ComboStateRegistry.REGISTRY.get().containsKey(state))
                return;

            BladeMotionEvent.BLADE_MOTION.post(new BladeMotionEvent(target, state));
        });
    }
}