package com.flechazo.slashblade.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class SlashBladeKeyMappings {
    public static final KeyMapping KEY_SPECIAL_MOVE = new KeyMapping(
            "key.slashblade.special_move",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "key.category.slashblade"
    );

    public static final KeyMapping KEY_SUMMON_BLADE = new KeyMapping(
            "key.slashblade.summon_blade",
            InputConstants.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_MIDDLE,
            "key.category.slashblade"
    );

    public static void init() {
    }
}
