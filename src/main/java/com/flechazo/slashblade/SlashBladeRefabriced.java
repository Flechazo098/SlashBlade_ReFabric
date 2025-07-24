package com.flechazo.slashblade;

import com.flechazo.slashblade.ability.*;
import com.flechazo.slashblade.client.registry.ClientSlashBladeRegistry;
import com.flechazo.slashblade.client.renderer.model.BladeModelManager;
import com.flechazo.slashblade.event.*;
import com.flechazo.slashblade.network.NetworkManager;
import com.flechazo.slashblade.recipe.RecipeSerializerRegistry;
import com.flechazo.slashblade.registry.*;
import com.flechazo.slashblade.registry.combo.ComboCommands;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import com.flechazo.slashblade.util.SlashBladeHolderOwner;
import com.flechazo.slashblade.util.TargetSelector;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;


public class SlashBladeRefabriced implements ModInitializer {
    public static final String MODID = "slashblade";

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(SlashBladeRefabriced.MODID, path);
    }

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();


    @Override
    public void onInitialize() {
        SlashBladeConfig.init();
        RegistryHandler.initJsonManager();
        NetworkManager.registerServerReceivers();

        EntityTypeRegister.registerEntityTypes();
        RegistryHandler.registerIngredientSerializer();
        ModAttributes.addAttribute();
        RecipeSerializerRegistry.register();

        SlashBladeRegister.registerAll();

        eventHandlerInit();

        SlashArtsRegistry.init();

        ComboStateRegistry.init();
        SpecialEffectsRegistry.init();
        SlashBladeCreativeGroup.init();
    }

    private void eventHandlerInit() {
        LockOnManager.getInstance().register();
        Guard.getInstance().register();
        StunManager.getInstance().register();
        KillCounter.getInstance().register();
        RankPointHandler.getInstance().register();
        AllowFlightOverrwrite.getInstance().register();
        BladeMotionEventBroadcaster.getInstance().register();
        TargetSelector.getInstance().register();
        SummonedSwordArts.getInstance().register();
        SlayerStyleArts.getInstance().register();
        Untouchable.getInstance().register();
        EnemyStep.getInstance().register();
        KickJump.getInstance().register();
        SuperSlashArts.getInstance().register();
        ComboCommands.initDefaultStandByCommands();
    }

    /**
     * /scoreboard objectives add stat minecraft.custom:slashblade.sword_summoned
     * /scoreboard objectives setdisplay sidebar stat
     */

    public static Registry<SlashBladeDefinition> getSlashBladeDefinitionRegistry(Level level) {
        if (level.isClientSide()) {
            // 在客户端，从客户端注册表获取数据
            return new ClientSlashBladeRegistryWrapper();
        }
        // 在服务端，从JSON管理器获取数据
        return new ServerSlashBladeRegistryWrapper();
    }

    public static Registry<SlashBladeDefinition> getSlashBladeDefinitionRegistryForCreativeTab() {
        // 根据环境判断使用哪个注册表
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            // 客户端环境，返回客户端注册表
            return new ClientSlashBladeRegistryWrapper();
        } else {
            // 服务端环境，返回服务端注册表
            return new ServerSlashBladeRegistryWrapper();
        }
    }
    
    // 内部包装类，用于适配现有的Registry接口
    private static class ClientSlashBladeRegistryWrapper implements Registry<SlashBladeDefinition> {
        
        @Override
        public ResourceKey<? extends Registry<SlashBladeDefinition>> key() {
            return SlashBladeDefinition.NAMED_BLADES_KEY;
        }

        @Override
        public @Nullable ResourceLocation getKey(SlashBladeDefinition object) {
            // 在客户端注册表中查找对象对应的键
            for (Map.Entry<ResourceLocation, SlashBladeDefinition> entry : ClientSlashBladeRegistry.getSlashBladeDefinitions().entrySet()) {
                if (entry.getValue().equals(object)) {
                    return entry.getKey();
                }
            }
            return null;
        }

        @Override
        public Optional<ResourceKey<SlashBladeDefinition>> getResourceKey(SlashBladeDefinition object) {
            ResourceLocation key = getKey(object);
            return key != null ? Optional.of(ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, key)) : Optional.empty();
        }

        @Override
        public int getId(@Nullable SlashBladeDefinition object) {
            if (object == null) return -1;
            ResourceLocation key = getKey(object);
            return key != null ? key.hashCode() : -1;
        }

        @Override
        public @Nullable SlashBladeDefinition byId(int i) {
            // 根据ID查找，这里简化实现
            for (SlashBladeDefinition definition : ClientSlashBladeRegistry.getSlashBladeDefinitions().values()) {
                if (getId(definition) == i) {
                    return definition;
                }
            }
            return null;
        }

        @Override
        public int size() {
            return ClientSlashBladeRegistry.getSlashBladeDefinitions().size();
        }

        @Override
        public @Nullable SlashBladeDefinition get(@Nullable ResourceKey<SlashBladeDefinition> resourceKey) {
            return resourceKey != null ? ClientSlashBladeRegistry.getSlashBladeDefinition(resourceKey.location()) : null;
        }

        @Override
        public SlashBladeDefinition get(ResourceLocation id) {
            return ClientSlashBladeRegistry.getSlashBladeDefinition(id);
        }

        @Override
        public Lifecycle lifecycle(SlashBladeDefinition object) {
            return Lifecycle.stable();
        }

        @Override
        public Lifecycle registryLifecycle() {
            return Lifecycle.stable();
        }

        @Override
        public Set<ResourceLocation> keySet() {
            return ClientSlashBladeRegistry.getSlashBladeDefinitions().keySet();
        }

        @Override
        public Set<Map.Entry<ResourceKey<SlashBladeDefinition>, SlashBladeDefinition>> entrySet() {
            return ClientSlashBladeRegistry.getSlashBladeDefinitions().entrySet().stream()
                    .map(entry -> Map.entry(
                            ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, entry.getKey()),
                            entry.getValue()
                    ))
                    .collect(java.util.stream.Collectors.toSet());
        }

        @Override
        public Set<ResourceKey<SlashBladeDefinition>> registryKeySet() {
            return ClientSlashBladeRegistry.getSlashBladeDefinitions().keySet().stream()
                    .map(key -> ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, key))
                    .collect(java.util.stream.Collectors.toSet());
        }

        @Override
        public Optional<Holder.Reference<SlashBladeDefinition>> getRandom(RandomSource randomSource) {
            var definitions = ClientSlashBladeRegistry.getSlashBladeDefinitions();
            if (definitions.isEmpty()) return Optional.empty();
            
            var keys = new ArrayList<>(definitions.keySet());
            ResourceLocation randomKey = keys.get(randomSource.nextInt(keys.size()));
            return getHolder(ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, randomKey));
        }

        @Override
        public boolean containsKey(ResourceLocation resourceLocation) {
            return ClientSlashBladeRegistry.hasSlashBladeDefinition(resourceLocation);
        }

        @Override
        public boolean containsKey(ResourceKey<SlashBladeDefinition> resourceKey) {
            return ClientSlashBladeRegistry.hasSlashBladeDefinition(resourceKey.location());
        }

        @Override
        public Registry<SlashBladeDefinition> freeze() {
            return this; // 客户端注册表已经是不可变的
        }

        @Override
        public Holder.Reference<SlashBladeDefinition> createIntrusiveHolder(SlashBladeDefinition object) {
            ResourceLocation key = getKey(object);
            if (key != null) {
                return Holder.Reference.createStandAlone(this.holderOwner(), ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, key));
            }
            throw new IllegalArgumentException("Object not found in registry");
        }

        @Override
        public Optional<Holder.Reference<SlashBladeDefinition>> getHolder(int i) {
            SlashBladeDefinition definition = byId(i);
            if (definition != null) {
                ResourceLocation key = getKey(definition);
                if (key != null) {
                    return Optional.of(Holder.Reference.createStandAlone(this.holderOwner(), ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, key)));
                }
            }
            return Optional.empty();
        }

        @Override
        public Optional<Holder.Reference<SlashBladeDefinition>> getHolder(ResourceKey<SlashBladeDefinition> resourceKey) {
            if (containsKey(resourceKey)) {
                return Optional.of(Holder.Reference.createStandAlone(this.holderOwner(), resourceKey));
            }
            return Optional.empty();
        }

        @Override
        public Holder<SlashBladeDefinition> wrapAsHolder(SlashBladeDefinition object) {
            ResourceLocation key = getKey(object);
            if (key != null) {
                return Holder.Reference.createStandAlone(this.holderOwner(), ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, key));
            }
            return Holder.direct(object);
        }

        @Override
        public Stream<Holder.Reference<SlashBladeDefinition>> holders() {
            return ClientSlashBladeRegistry.getSlashBladeDefinitions().keySet().stream()
                    .map(key -> ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, key))
                    .map(resourceKey -> Holder.Reference.createStandAlone(this.holderOwner(), resourceKey));
        }

        @Override
        public Optional<HolderSet.Named<SlashBladeDefinition>> getTag(TagKey<SlashBladeDefinition> tagKey) {
            return Optional.empty(); // 客户端不支持标签
        }

        @Override
        public HolderSet.Named<SlashBladeDefinition> getOrCreateTag(TagKey<SlashBladeDefinition> tagKey) {
            throw new UnsupportedOperationException("Tags not supported in client registry");
        }

        @Override
        public Stream<Pair<TagKey<SlashBladeDefinition>, HolderSet.Named<SlashBladeDefinition>>> getTags() {
            return Stream.empty();
        }

        @Override
        public Stream<TagKey<SlashBladeDefinition>> getTagNames() {
            return Stream.empty();
        }

        @Override
        public void resetTags() {
            // 客户端不支持标签，无需操作
        }

        @Override
        public void bindTags(Map<TagKey<SlashBladeDefinition>, List<Holder<SlashBladeDefinition>>> map) {
            // 客户端不支持标签，无需操作
        }

        @Override
        public HolderOwner<SlashBladeDefinition> holderOwner() {
            return SlashBladeHolderOwner.INSTANCE;
        }

        @Override
        public HolderLookup.RegistryLookup<SlashBladeDefinition> asLookup() {
            return new HolderLookup.RegistryLookup<>() {
                @Override
                public ResourceKey<? extends Registry<? extends SlashBladeDefinition>> key() {
                    return SlashBladeDefinition.NAMED_BLADES_KEY;
                }

                @Override
                public Lifecycle registryLifecycle() {
                    return Lifecycle.stable();
                }

                @Override
                public Optional<Holder.Reference<SlashBladeDefinition>> get(ResourceKey<SlashBladeDefinition> resourceKey) {
                    return getHolder(resourceKey);
                }

                @Override
                public Stream<Holder.Reference<SlashBladeDefinition>> listElements() {
                    return holders();
                }

                @Override
                public Optional<HolderSet.Named<SlashBladeDefinition>> get(TagKey<SlashBladeDefinition> tagKey) {
                    return Optional.empty();
                }

                @Override
                public Stream<HolderSet.Named<SlashBladeDefinition>> listTags() {
                    return Stream.empty();
                }
            };
        }

        @Override
        public @NotNull Iterator<SlashBladeDefinition> iterator() {
            return ClientSlashBladeRegistry.getSlashBladeDefinitions().values().iterator();
        }
    }
    
    private static class ServerSlashBladeRegistryWrapper implements Registry<SlashBladeDefinition> {
        
        @Override
        public ResourceKey<? extends Registry<SlashBladeDefinition>> key() {
            return SlashBladeDefinition.NAMED_BLADES_KEY;
        }

        @Override
        public @Nullable ResourceLocation getKey(SlashBladeDefinition object) {
            // 在服务端注册表中查找对象对应的键
            for (Map.Entry<ResourceLocation, SlashBladeDefinition> entry : SlashBladeJsonManager.getInstance().getSlashBladeDefinitions().entrySet()) {
                if (entry.getValue().equals(object)) {
                    return entry.getKey();
                }
            }
            return null;
        }

        @Override
        public Optional<ResourceKey<SlashBladeDefinition>> getResourceKey(SlashBladeDefinition object) {
            ResourceLocation key = getKey(object);
            return key != null ? Optional.of(ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, key)) : Optional.empty();
        }

        @Override
        public int getId(@Nullable SlashBladeDefinition object) {
            if (object == null) return -1;
            ResourceLocation key = getKey(object);
            return key != null ? key.hashCode() : -1;
        }

        @Override
        public @Nullable SlashBladeDefinition byId(int i) {
            // 根据ID查找，这里简化实现
            for (SlashBladeDefinition definition : SlashBladeJsonManager.getInstance().getSlashBladeDefinitions().values()) {
                if (getId(definition) == i) {
                    return definition;
                }
            }
            return null;
        }

        @Override
        public int size() {
            return SlashBladeJsonManager.getInstance().getSlashBladeDefinitions().size();
        }

        @Override
        public @Nullable SlashBladeDefinition get(@Nullable ResourceKey<SlashBladeDefinition> resourceKey) {
            return resourceKey != null ? SlashBladeJsonManager.getInstance().getSlashBladeDefinition(resourceKey.location()) : null;
        }

        @Override
        public SlashBladeDefinition get(ResourceLocation id) {
            return SlashBladeJsonManager.getInstance().getSlashBladeDefinition(id);
        }

        @Override
        public Lifecycle lifecycle(SlashBladeDefinition object) {
            return Lifecycle.stable();
        }

        @Override
        public Lifecycle registryLifecycle() {
            return Lifecycle.stable();
        }

        @Override
        public Set<ResourceLocation> keySet() {
            return SlashBladeJsonManager.getInstance().getSlashBladeDefinitions().keySet();
        }

        @Override
        public Set<Map.Entry<ResourceKey<SlashBladeDefinition>, SlashBladeDefinition>> entrySet() {
            return SlashBladeJsonManager.getInstance().getSlashBladeDefinitions().entrySet().stream()
                    .map(entry -> Map.entry(
                            ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, entry.getKey()),
                            entry.getValue()
                    ))
                    .collect(java.util.stream.Collectors.toSet());
        }

        @Override
        public Set<ResourceKey<SlashBladeDefinition>> registryKeySet() {
            return SlashBladeJsonManager.getInstance().getSlashBladeDefinitions().keySet().stream()
                    .map(key -> ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, key))
                    .collect(java.util.stream.Collectors.toSet());
        }

        @Override
        public Optional<Holder.Reference<SlashBladeDefinition>> getRandom(RandomSource randomSource) {
            var definitions = SlashBladeJsonManager.getInstance().getSlashBladeDefinitions();
            if (definitions.isEmpty()) return Optional.empty();
            
            var keys = new ArrayList<>(definitions.keySet());
            ResourceLocation randomKey = keys.get(randomSource.nextInt(keys.size()));
            return getHolder(ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, randomKey));
        }

        @Override
        public boolean containsKey(ResourceLocation resourceLocation) {
            return SlashBladeJsonManager.getInstance().getSlashBladeDefinition(resourceLocation) != null;
        }

        @Override
        public boolean containsKey(ResourceKey<SlashBladeDefinition> resourceKey) {
            return SlashBladeJsonManager.getInstance().getSlashBladeDefinition(resourceKey.location()) != null;
        }

        @Override
        public Registry<SlashBladeDefinition> freeze() {
            return this; // 服务端注册表已经是不可变的
        }

        @Override
        public Holder.Reference<SlashBladeDefinition> createIntrusiveHolder(SlashBladeDefinition object) {
            ResourceLocation key = getKey(object);
            if (key != null) {
                return Holder.Reference.createStandAlone(this.holderOwner(), ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, key));
            }
            throw new IllegalArgumentException("Object not found in registry");
        }

        @Override
        public Optional<Holder.Reference<SlashBladeDefinition>> getHolder(int i) {
            SlashBladeDefinition definition = byId(i);
            if (definition != null) {
                ResourceLocation key = getKey(definition);
                if (key != null) {
                    return Optional.of(Holder.Reference.createStandAlone(this.holderOwner(), ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, key)));
                }
            }
            return Optional.empty();
        }

        @Override
        public Optional<Holder.Reference<SlashBladeDefinition>> getHolder(ResourceKey<SlashBladeDefinition> resourceKey) {
            if (containsKey(resourceKey)) {
                return Optional.of(Holder.Reference.createStandAlone(this.holderOwner(), resourceKey));
            }
            return Optional.empty();
        }

        @Override
        public Holder<SlashBladeDefinition> wrapAsHolder(SlashBladeDefinition object) {
            ResourceLocation key = getKey(object);
            if (key != null) {
                return Holder.Reference.createStandAlone(this.holderOwner(), ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, key));
            }
            return Holder.direct(object);
        }

        @Override
        public Stream<Holder.Reference<SlashBladeDefinition>> holders() {
            return SlashBladeJsonManager.getInstance().getSlashBladeDefinitions().keySet().stream()
                    .map(key -> ResourceKey.create(SlashBladeDefinition.NAMED_BLADES_KEY, key))
                    .map(resourceKey -> Holder.Reference.createStandAlone(this.holderOwner(), resourceKey));
        }


        @Override
        public Optional<HolderSet.Named<SlashBladeDefinition>> getTag(TagKey<SlashBladeDefinition> tagKey) {
            return Optional.empty(); // 服务端暂不支持标签
        }

        @Override
        public HolderSet.Named<SlashBladeDefinition> getOrCreateTag(TagKey<SlashBladeDefinition> tagKey) {
            throw new UnsupportedOperationException("Tags not supported in server registry");
        }

        @Override
        public Stream<Pair<TagKey<SlashBladeDefinition>, HolderSet.Named<SlashBladeDefinition>>> getTags() {
            return Stream.empty();
        }

        @Override
        public Stream<TagKey<SlashBladeDefinition>> getTagNames() {
            return Stream.empty();
        }

        @Override
        public void resetTags() {
            // 服务端不支持标签，无需操作
        }

        @Override
        public void bindTags(Map<TagKey<SlashBladeDefinition>, List<Holder<SlashBladeDefinition>>> map) {
            // 服务端不支持标签，无需操作
        }

        @Override
        public HolderOwner<SlashBladeDefinition> holderOwner() {
            return SlashBladeHolderOwner.INSTANCE;
        }

        @Override
        public HolderLookup.RegistryLookup<SlashBladeDefinition> asLookup() {
            return new HolderLookup.RegistryLookup<>() {
                @Override
                public ResourceKey<? extends Registry<? extends SlashBladeDefinition>> key() {
                    return SlashBladeDefinition.NAMED_BLADES_KEY;
                }

                @Override
                public Lifecycle registryLifecycle() {
                    return Lifecycle.stable();
                }

                @Override
                public Optional<Holder.Reference<SlashBladeDefinition>> get(ResourceKey<SlashBladeDefinition> resourceKey) {
                    return getHolder(resourceKey);
                }

                @Override
                public Stream<Holder.Reference<SlashBladeDefinition>> listElements() {
                    return holders();
                }

                @Override
                public Optional<HolderSet.Named<SlashBladeDefinition>> get(TagKey<SlashBladeDefinition> tagKey) {
                    return Optional.empty();
                }

                @Override
                public Stream<HolderSet.Named<SlashBladeDefinition>> listTags() {
                    return Stream.empty();
                }
            };
        }

        @Override
        public @NotNull Iterator<SlashBladeDefinition> iterator() {
            return SlashBladeJsonManager.getInstance().getSlashBladeDefinitions().values().iterator();
        }
    }
}



