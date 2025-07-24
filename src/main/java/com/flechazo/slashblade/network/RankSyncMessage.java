package com.flechazo.slashblade.network;

import com.flechazo.slashblade.capability.concentrationrank.ConcentrationRankComponent;
import com.flechazo.slashblade.capability.concentrationrank.ConcentrationRankHelper;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class RankSyncMessage {
    public long rawPoint;

    public RankSyncMessage() {
        this.rawPoint = 0;
    }

    public RankSyncMessage(long rawPoint) {
        this.rawPoint = rawPoint;
    }

    // 发送到客户端
    public static void sendToClient(ServerPlayer player, long rawPoint) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeLong(rawPoint);
        NetworkManager.sendToPlayer(player, NetworkManager.RANK_SYNC_ID, buf);
    }

    // 客户端处理
    public static void handleClient(Minecraft client, ClientPacketListener handler,
                                    FriendlyByteBuf buf, PacketSender responseSender) {
        long point = buf.readLong();

        client.execute(() -> {
            Player player = client.player;
            if (player == null) return;

            ConcentrationRankHelper.getConcentrationRank(player).ifPresent(cr -> {

                long time = player.level().getGameTime();

                ConcentrationRankComponent.ConcentrationRanks oldRank = cr.getRank(time);

                cr.setRawRankPoint(point);
                cr.setLastUpdte(time);

                if (oldRank.level < cr.getRank(time).level)
                    cr.setLastRankRise(time);
            });
        });
    }
}