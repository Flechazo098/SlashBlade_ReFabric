package com.flechazo.slashblade.client.renderer.model;

import com.flechazo.slashblade.client.renderer.model.obj.WavefrontObject;
import com.flechazo.slashblade.init.DefaultResources;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.Executors;

/**
 * Created by Furia on 2016/02/06.
 */
@Environment(EnvType.CLIENT)
public class BladeModelManager {

    private static final class SingletonHolder {
        private static final BladeModelManager instance = new BladeModelManager();
    }

    public static BladeModelManager getInstance() {
        return SingletonHolder.instance;
    }

    public static Registry<SlashBladeDefinition> getClientSlashBladeRegistry() {
        return Minecraft.getInstance().getConnection().registryAccess()
                .registryOrThrow(SlashBladeDefinition.NAMED_BLADES_KEY);
    }

    WavefrontObject defaultModel;

    LoadingCache<ResourceLocation, WavefrontObject> cache;

    private BladeModelManager() {
        defaultModel = new WavefrontObject(DefaultResources.resourceDefaultModel);

        cache = CacheBuilder.newBuilder()
                .build(CacheLoader.asyncReloading(new CacheLoader<ResourceLocation, WavefrontObject>() {
                    @Override
                    public WavefrontObject load(ResourceLocation key) throws Exception {
                        try {
                            return new WavefrontObject(key);
                        } catch (Exception e) {
                            return defaultModel;
                        }
                    }

                }, Executors.newCachedThreadPool()));
    }

    public void reload() {
        cache.invalidateAll();
        defaultModel = new WavefrontObject(DefaultResources.resourceDefaultModel);
    }

    public WavefrontObject getModel(ResourceLocation loc) {
        if (loc != null) {
            try {
                return cache.get(loc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultModel;
    }
}
