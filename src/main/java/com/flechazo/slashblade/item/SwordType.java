package com.flechazo.slashblade.item;

import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.Optional;

import com.mojang.serialization.Codec;

import com.flechazo.slashblade.capability.slashblade.BladeStateComponent;

public enum SwordType {
    NONE, EDGEFRAGMENT, BROKEN, ENCHANTED, BEWITCHED, FIERCEREDGE, NOSCABBARD, SEALED,;

    public static final Codec<SwordType> CODEC = Codec.STRING.xmap(string -> SwordType.valueOf(string.toUpperCase()),
            instance -> instance.name().toLowerCase());

    public static EnumSet<SwordType> from(ItemStack itemStackIn) {
        EnumSet<SwordType> types = EnumSet.noneOf(SwordType.class);

        Optional<BladeStateComponent> state = BladeStateHelper.getBladeState(itemStackIn);

        if (state.isPresent()) {
            BladeStateHelper.getBladeState(itemStackIn).ifPresent(s -> {
                if (s.isBroken() || itemStackIn.getOrCreateTagElement("bladeState").getBoolean("isBroken"))
                    types.add(BROKEN);

                if (s.isSealed() || itemStackIn.getOrCreateTagElement("bladeState").getBoolean("isSealed"))
                    types.add(SEALED);

                if (!s.isSealed() && itemStackIn.isEnchanted()
                        && (itemStackIn.hasCustomHoverName() || s.isDefaultBewitched()))
                    types.add(BEWITCHED);

                if (s.getKillCount() >= 1000)
                    types.add(FIERCEREDGE);

            });
        } else {
            types.add(NOSCABBARD);
            types.add(EDGEFRAGMENT);
        }

        if (itemStackIn.isEnchanted())
            types.add(ENCHANTED);

        if (itemStackIn.getItem() instanceof ItemSlashBladeDetune) {
            types.remove(SwordType.ENCHANTED);
            types.remove(SwordType.BEWITCHED);
        }

        return types;
    }
}
