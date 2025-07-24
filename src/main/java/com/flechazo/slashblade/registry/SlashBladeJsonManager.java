package com.flechazo.slashblade.registry;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.event.drop.EntityDropEntry;
import com.flechazo.slashblade.network.SlashBladeDataSyncMessage;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlashBladeJsonManager {
    private static final SlashBladeJsonManager INSTANCE = new SlashBladeJsonManager();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<ResourceLocation, SlashBladeDefinition> slashBladeDefinitions = new ConcurrentHashMap<>();
    private final Map<ResourceLocation, EntityDropEntry> entityDropEntries = new ConcurrentHashMap<>();

    public static SlashBladeJsonManager getInstance() {
        return INSTANCE;
    }

    public void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(this::onDataPackReload);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            syncToPlayer(handler.getPlayer());
        });
    }

    private void onServerStarted(MinecraftServer server) {
        loadJsonData(server.getResourceManager());
    }

    private void onDataPackReload(MinecraftServer server, net.minecraft.server.packs.resources.ResourceManager resourceManager, boolean success) {
        if (success) {
            loadJsonData(resourceManager);
            syncToAllPlayers(server);
        }
    }

    private void loadJsonData(ResourceManager resourceManager) {
        slashBladeDefinitions.clear();
        entityDropEntries.clear();

        // 加载SlashBlade定义
        loadSlashBladeDefinitions(resourceManager);

        // 加载实体掉落条目
        loadEntityDropEntries(resourceManager);

        SlashBladeRefabriced.LOGGER.info("Loaded {} SlashBlade definitions and {} entity drop entries",
                slashBladeDefinitions.size(), entityDropEntries.size());
    }

    private void loadSlashBladeDefinitions(ResourceManager resourceManager) {
        String directory = "slashblade/named_blades";

        for (Map.Entry<ResourceLocation, Resource> entry : resourceManager.listResources(directory,
                location -> location.getPath().endsWith(".json")).entrySet()) {

            ResourceLocation fullLocation = entry.getKey();
            String path = fullLocation.getPath();

            // 从路径中提取实际的资源名称
            String fileName = path.substring(directory.length() + 1);
            if (fileName.endsWith(".json")) {
                fileName = fileName.substring(0, fileName.length() - 5);
            }

            ResourceLocation definitionId = new ResourceLocation(fullLocation.getNamespace(), fileName);

            try (InputStreamReader reader = new InputStreamReader(entry.getValue().open(), StandardCharsets.UTF_8)) {
                JsonElement jsonElement = JsonParser.parseReader(reader);

                var result = SlashBladeDefinition.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                if (result.result().isPresent()) {
                    slashBladeDefinitions.put(definitionId, result.result().get());
                    SlashBladeRefabriced.LOGGER.debug("Loaded SlashBlade definition: {}", definitionId);
                } else {
                    SlashBladeRefabriced.LOGGER.error("Failed to parse SlashBlade definition {}: {}",
                            definitionId, result.error().orElse(null));
                }
            } catch (IOException e) {
                SlashBladeRefabriced.LOGGER.error("Failed to read SlashBlade definition file: {}", fullLocation, e);
            }
        }
    }

    private void loadEntityDropEntries(ResourceManager resourceManager) {
        String directory = "slashblade/entity_drop";

        for (Map.Entry<ResourceLocation, Resource> entry : resourceManager.listResources(directory,
                location -> location.getPath().endsWith(".json")).entrySet()) {

            ResourceLocation fullLocation = entry.getKey();
            String path = fullLocation.getPath();

            // 从路径中提取实际的资源名称
            String fileName = path.substring(directory.length() + 1);
            if (fileName.endsWith(".json")) {
                fileName = fileName.substring(0, fileName.length() - 5);
            }

            ResourceLocation entryId = new ResourceLocation(fullLocation.getNamespace(), fileName);

            try (InputStreamReader reader = new InputStreamReader(entry.getValue().open(), StandardCharsets.UTF_8)) {
                JsonElement jsonElement = JsonParser.parseReader(reader);

                var result = EntityDropEntry.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                if (result.result().isPresent()) {
                    entityDropEntries.put(entryId, result.result().get());
                    SlashBladeRefabriced.LOGGER.debug("Loaded entity drop entry: {}", entryId);
                } else {
                    SlashBladeRefabriced.LOGGER.error("Failed to parse entity drop entry {}: {}",
                            entryId, result.error().orElse(null));
                }
            } catch (IOException e) {
                SlashBladeRefabriced.LOGGER.error("Failed to read entity drop entry file: {}", fullLocation, e);
            }
        }
    }

    private void syncToAllPlayers(MinecraftServer server) {
        for (ServerPlayer player : PlayerLookup.all(server)) {
            syncToPlayer(player);
        }
    }

    private void syncToPlayer(ServerPlayer player) {
        SlashBladeDataSyncMessage.sendToClient(player,
                new HashMap<>(slashBladeDefinitions),
                new HashMap<>(entityDropEntries));
    }

    // Getter方法
    public Map<ResourceLocation, SlashBladeDefinition> getSlashBladeDefinitions() {
        return new HashMap<>(slashBladeDefinitions);
    }

    public Map<ResourceLocation, EntityDropEntry> getEntityDropEntries() {
        return new HashMap<>(entityDropEntries);
    }

    public SlashBladeDefinition getSlashBladeDefinition(ResourceLocation id) {
        return slashBladeDefinitions.get(id);
    }

    public EntityDropEntry getEntityDropEntry(ResourceLocation id) {
        return entityDropEntries.get(id);
    }
}