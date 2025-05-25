package com.flechazo.slashblade.capability.slashblade;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.item.ItemSlashBladeDetune;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.minecraft.resources.ResourceLocation;

public class BladeStateComponentRegistry implements ItemComponentInitializer {

    public static final ComponentKey<BladeStateComponent> BLADE_STATE =
            ComponentRegistry.getOrCreate(
                    new ResourceLocation(SlashBladeRefabriced.MODID, "blade_state"),
                    BladeStateComponent.class);

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        // 为所有SlashBlade物品注册BladeState组件
        registry.register(item -> item instanceof ItemSlashBlade, BLADE_STATE, BladeStateComponentImpl::new);
        registry.register(SlashBladeRefabriced.RegistryEvents, BLADE_STATE, stack -> ((ItemSlashBladeDetune) stack.getItem()).createComponent(stack)
        );
    }
}