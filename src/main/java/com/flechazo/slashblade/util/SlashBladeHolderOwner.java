package com.flechazo.slashblade.util;

import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.core.HolderOwner;

public final class SlashBladeHolderOwner implements HolderOwner<SlashBladeDefinition> {
    public static final SlashBladeHolderOwner INSTANCE = new SlashBladeHolderOwner();
    private SlashBladeHolderOwner() {}
}
