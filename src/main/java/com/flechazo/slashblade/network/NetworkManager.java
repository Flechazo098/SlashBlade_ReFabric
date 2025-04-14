package com.flechazo.slashblade.network;

import com.flechazo.slashblade.SlashBladeRefabriced;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class NetworkManager {

    // 定义所有网络消息的标识符
    public static final ResourceLocation MOVE_COMMAND_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "move_command");
    public static final ResourceLocation ACTIVE_STATE_SYNC_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "active_state_sync");
    public static final ResourceLocation RANK_SYNC_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "rank_sync");
    public static final ResourceLocation MOTION_BROADCAST_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "motion_broadcast");

    public static void registerServerReceivers() {
        // 注册服务器接收器
        ServerPlayNetworking.registerGlobalReceiver(MOVE_COMMAND_ID, MoveCommandMessage::handleServer);
    }

    public static void registerClientReceivers() {
        // 注册客户端接收器
        ClientPlayNetworking.registerGlobalReceiver(ACTIVE_STATE_SYNC_ID, ActiveStateSyncMessage::handleClient);
        ClientPlayNetworking.registerGlobalReceiver(RANK_SYNC_ID, RankSyncMessage::handleClient);
        ClientPlayNetworking.registerGlobalReceiver(MOTION_BROADCAST_ID, MotionBroadcastMessage::handleClient);
    }

    // 发送到服务器的方法
    public static void sendToServer(ResourceLocation id, FriendlyByteBuf buf) {
        ClientPlayNetworking.send(id, buf);
    }

    // 发送到特定玩家的方法
    public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf buf) {
        ServerPlayNetworking.send(player, id, buf);
    }

    // 发送到所有玩家的方法
    public static void sendToAll(ResourceLocation id, FriendlyByteBuf buf) {
        for (ServerPlayer player : PlayerLookup.all(ServerPlayNetworking.getServer())) {
            ServerPlayNetworking.send(player, id, buf);
        }
    }

    // 发送到追踪实体的玩家和自己的方法
    public static void sendToTrackingAndSelf(Entity entity, Object message) {
        if (message instanceof ActiveStateSyncMessage msg) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeInt(msg.id);
            buf.writeNbt(msg.activeTag);

            // 发送给所有能看到这个实体的玩家
            for (ServerPlayer player : PlayerLookup.tracking(entity)) {
                ServerPlayNetworking.send(player, ACTIVE_STATE_SYNC_ID, buf);
            }

            // 如果实体是玩家，也发送给自己
            if (entity instanceof ServerPlayer serverPlayer) {
                ServerPlayNetworking.send(serverPlayer, ACTIVE_STATE_SYNC_ID, buf);
            }
        } else if (message instanceof MotionBroadcastMessage msg) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeUUID(msg.playerId);
            buf.writeUtf(msg.combo);

            for (ServerPlayer player : PlayerLookup.tracking(entity)) {
                ServerPlayNetworking.send(player, MOTION_BROADCAST_ID, buf);
            }

            if (entity instanceof ServerPlayer serverPlayer) {
                ServerPlayNetworking.send(serverPlayer, MOTION_BROADCAST_ID, buf);
            }
        }
    }
}