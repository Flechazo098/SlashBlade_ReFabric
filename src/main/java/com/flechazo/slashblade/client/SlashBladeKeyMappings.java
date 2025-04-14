package com.flechazo.slashblade.client;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

public class SlashBladeKeyMappings {
    public static final KeyMapping KEY_SPECIAL_MOVE = new KeyMapping("key.slashblade.special_move",
            KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V,
            "key.category.slashblade");

    public static final KeyMapping KEY_SUMMON_BLADE = new KeyMapping("key.slashblade.summon_blade",
            KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_MIDDLE,
            "key.category.slashblade");
}
