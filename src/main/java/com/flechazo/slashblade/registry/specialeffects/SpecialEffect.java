package com.flechazo.slashblade.registry.specialeffects;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.registry.SpecialEffectsRegistry;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class SpecialEffect {
    private final int requestLevel;
    private final boolean isCopiable;
    private final boolean isRemovable;

    public SpecialEffect(int requestLevel) {
        this(requestLevel, false, false);
    }

    public SpecialEffect(int requestLevel, boolean isCopiable, boolean isRemovable) {
        this.requestLevel = requestLevel;
        this.isCopiable = isCopiable;
        this.isRemovable = isRemovable;
    }

    public ResourceLocation getId() {
        return SpecialEffectsRegistry.REGISTRY.getKey(this);
    }


    public int getRequestLevel() {
        return requestLevel;
    }

    public boolean isCopiable() {
        return isCopiable;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

    public static boolean isEffective(SpecialEffect se, int level) {
        return se.requestLevel <= level;
    }

    public static boolean isEffective(ResourceLocation id, int level) {
        return SpecialEffectsRegistry.REGISTRY.get(id).getRequestLevel() <= level;
    }

    public static Component getDescription(ResourceLocation id) {
        return SpecialEffectsRegistry.REGISTRY.get(id).getDescription();
    }

    public static int getRequestLevel(ResourceLocation id) {
        return SpecialEffectsRegistry.REGISTRY.get(id).getRequestLevel();
    }

    public Component getDescription() {
        return Component.translatable(this.getDescriptionId());
    }

    public String toString() {
        return SpecialEffectsRegistry.REGISTRY.getKey(this).toString();
    }

    private String descriptionId;

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("se", SpecialEffectsRegistry.REGISTRY.getKey(this));
        }
        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }
}
