package com.flechazo.slashblade.network;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.client.registry.ClientSlashBladeRegistry;
import com.flechazo.slashblade.event.drop.EntityDropEntry;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class SlashBladeDataSyncMessage {
    private static final Gson GSON = new GsonBuilder().create();

    private final Map<ResourceLocation, SlashBladeDefinition> slashBladeDefinitions;
    private final Map<ResourceLocation, EntityDropEntry> entityDropEntries;

    public SlashBladeDataSyncMessage(Map<ResourceLocation, SlashBladeDefinition> slashBladeDefinitions,
                                     Map<ResourceLocation, EntityDropEntry> entityDropEntries) {
        this.slashBladeDefinitions = slashBladeDefinitions;
        this.entityDropEntries = entityDropEntries;
    }

    // 发送到客户端
    public static void sendToClient(ServerPlayer player, Map<ResourceLocation, SlashBladeDefinition> slashBladeDefinitions,
                                    Map<ResourceLocation, EntityDropEntry> entityDropEntries) {
        FriendlyByteBuf buf = PacketByteBufs.create();

        // 写入SlashBlade定义
        buf.writeVarInt(slashBladeDefinitions.size());
        for (Map.Entry<ResourceLocation, SlashBladeDefinition> entry : slashBladeDefinitions.entrySet()) {
            buf.writeResourceLocation(entry.getKey());

            var result = SlashBladeDefinition.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue());
            if (result.result().isPresent()) {
                String jsonString = GSON.toJson(result.result().get());
                buf.writeUtf(jsonString);
            } else {
                buf.writeUtf("{}"); // 空JSON作为fallback
            }
        }

        // 写入实体掉落条目
        buf.writeVarInt(entityDropEntries.size());
        for (Map.Entry<ResourceLocation, EntityDropEntry> entry : entityDropEntries.entrySet()) {
            buf.writeResourceLocation(entry.getKey());

            var result = EntityDropEntry.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue());
            if (result.result().isPresent()) {
                String jsonString = GSON.toJson(result.result().get());
                buf.writeUtf(jsonString);
            } else {
                buf.writeUtf("{}"); // 空JSON作为fallback
            }
        }

        NetworkManager.sendToPlayer(player, NetworkManager.SLASHBLADE_DATA_SYNC_ID, buf);
    }

    // 客户端处理
    public static void handleClient(Minecraft client, ClientPacketListener handler,
                                    FriendlyByteBuf buf, PacketSender responseSender) {
        Map<ResourceLocation, SlashBladeDefinition> slashBladeDefinitions = new HashMap<>();
        Map<ResourceLocation, EntityDropEntry> entityDropEntries = new HashMap<>();

        // 读取SlashBlade定义
        int slashBladeCount = buf.readVarInt();
        for (int i = 0; i < slashBladeCount; i++) {
            ResourceLocation id = buf.readResourceLocation();
            String jsonString = buf.readUtf();

            try {
                JsonElement jsonElement = GSON.fromJson(jsonString, JsonElement.class);
                var result = SlashBladeDefinition.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                if (result.result().isPresent()) {
                    slashBladeDefinitions.put(id, result.result().get());
                }
            } catch (Exception e) {
                SlashBladeRefabriced.LOGGER.warn("Failed to parse SlashBlade definition: {} - {}", id, e.getMessage());
            }
        }

        // 读取实体掉落条目
        int dropCount = buf.readVarInt();
        for (int i = 0; i < dropCount; i++) {
            ResourceLocation id = buf.readResourceLocation();
            String jsonString = buf.readUtf();

            try {
                JsonElement jsonElement = GSON.fromJson(jsonString, JsonElement.class);
                var result = EntityDropEntry.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                if (result.result().isPresent()) {
                    entityDropEntries.put(id, result.result().get());
                }
            } catch (Exception e) {
                SlashBladeRefabriced.LOGGER.warn("Failed to parse entity drop entry: {} - {}", id, e.getMessage());
            }
        }

        client.execute(() -> {
            ClientSlashBladeRegistry.updateFromServer(slashBladeDefinitions, entityDropEntries);
        });
    }

    public Map<ResourceLocation, SlashBladeDefinition> getSlashBladeDefinitions() {
        return slashBladeDefinitions;
    }

    public Map<ResourceLocation, EntityDropEntry> getEntityDropEntries() {
        return entityDropEntries;
    }
}