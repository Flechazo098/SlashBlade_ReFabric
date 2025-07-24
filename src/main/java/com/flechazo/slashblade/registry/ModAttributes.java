package com.flechazo.slashblade.registry;

import com.flechazo.slashblade.SlashBladeRefabriced;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;

public class ModAttributes {
    public static final ResourceKey<Registry<Attribute>> ATTRIBUTES_KEY = ResourceKey.createRegistryKey(
            new ResourceLocation(SlashBladeRefabriced.MODID, "attributes"));

    public static final Registry<Attribute> ATTRIBUTES = FabricRegistryBuilder
            .createSimple(ATTRIBUTES_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    public static final Attribute SLASHBLADE_DAMAGE = Registry.register(ATTRIBUTES,
            new ResourceLocation(SlashBladeRefabriced.MODID, "slashblade_damage"),
            new RangedAttribute("attribute.name.generic.slashblade_damage", 1.0d, 0.0d, 512.0d).setSyncable(true));

    public static Attribute getSlashBladeDamage() {
        return SLASHBLADE_DAMAGE;
    }

    public static void addAttribute() {
        FabricDefaultAttributeRegistry.register(
                EntityType.PLAYER,
                Player.createAttributes()
                        .add(ModAttributes.SLASHBLADE_DAMAGE)
        );
    }
}