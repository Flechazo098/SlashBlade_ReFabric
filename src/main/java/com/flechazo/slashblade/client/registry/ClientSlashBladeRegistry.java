package com.flechazo.slashblade.client.registry;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.event.drop.EntityDropEntry;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientSlashBladeRegistry {
    private static final Map<ResourceLocation, SlashBladeDefinition> slashBladeDefinitions = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, EntityDropEntry> entityDropEntries = new ConcurrentHashMap<>();
    
    public static void updateFromServer(Map<ResourceLocation, SlashBladeDefinition> newSlashBladeDefinitions,
                                      Map<ResourceLocation, EntityDropEntry> newEntityDropEntries) {
        slashBladeDefinitions.clear();
        entityDropEntries.clear();
        
        slashBladeDefinitions.putAll(newSlashBladeDefinitions);
        entityDropEntries.putAll(newEntityDropEntries);
        
        SlashBladeRefabriced.LOGGER.info("Updated client registry with {} SlashBlade definitions and {} entity drop entries",
                slashBladeDefinitions.size(), entityDropEntries.size());
    }
    
    public static Map<ResourceLocation, SlashBladeDefinition> getSlashBladeDefinitions() {
        return new HashMap<>(slashBladeDefinitions);
    }
    
    public static Map<ResourceLocation, EntityDropEntry> getEntityDropEntries() {
        return new HashMap<>(entityDropEntries);
    }
    
    public static SlashBladeDefinition getSlashBladeDefinition(ResourceLocation id) {
        return slashBladeDefinitions.get(id);
    }
    
    public static EntityDropEntry getEntityDropEntry(ResourceLocation id) {
        return entityDropEntries.get(id);
    }
    
    public static boolean hasSlashBladeDefinition(ResourceLocation id) {
        return slashBladeDefinitions.containsKey(id);
    }
    
    public static boolean hasEntityDropEntry(ResourceLocation id) {
        return entityDropEntries.containsKey(id);
    }
}