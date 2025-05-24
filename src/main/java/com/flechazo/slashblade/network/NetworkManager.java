package com.flechazo.slashblade.network;

import com.flechazo.slashblade.SlashBladeRefabriced;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
    /**
     * 向指定世界中以 (x,y,z) 为中心，半径 radius 范围内所有玩家发送包
     */
    public static void sendToNear(ServerLevel world, int x, int y, int z, double radius, ResourceLocation channelId, FriendlyByteBuf buf) {
        // 将浮点坐标转为整数方块位置
        BlockPos center = new BlockPos(x, y, z);
        // PlayerLookup.around 返回所有在 view-distance 内、并且距离 center <= radius 的 ServerPlayer
        for (ServerPlayer player : PlayerLookup.around(world, center, radius)) {
            ServerPlayNetworking.send(player, channelId, buf);  // 向单个玩家发送包
        }
    }
    /**
     * 通用的send方法，用于兼容Forge风格的PacketDistributor调用
     * 这个方法处理PacketDistributor.PLAYER.with(() -> player)的调用模式
     */
    public static void send(PacketDistributor distributor, Object message) {
        if (distributor.getType() == PacketDistributor.Type.PLAYER) {
            ServerPlayer player = distributor.getPlayerSupplier().get();

            if (message instanceof RankSyncMessage msg) {
                FriendlyByteBuf buf = PacketByteBufs.create();
                buf.writeLong(msg.rawPoint);
                ServerPlayNetworking.send(player, RANK_SYNC_ID, buf);
            }
        }
    }
    /**
     * PacketDistributor类，用于模拟Forge的网络分发机制
     */
    public static class PacketDistributor {
        private final Type type;
        private java.util.function.Supplier<ServerPlayer> playerSupplier;

        private PacketDistributor(Type type) {
            this.type = type;
        }

        public Type getType() {
            return type;
        }

        public java.util.function.Supplier<ServerPlayer> getPlayerSupplier() {
            return playerSupplier;
        }

        public PacketDistributor with(java.util.function.Supplier<ServerPlayer> supplier) {
            this.playerSupplier = supplier;
            return this;
        }

        public static final PacketDistributor PLAYER = new PacketDistributor(Type.PLAYER);

        public enum Type {
            PLAYER
        }
    }

}