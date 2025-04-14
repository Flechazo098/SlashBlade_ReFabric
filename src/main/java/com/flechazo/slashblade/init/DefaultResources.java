package com.flechazo.slashblade.init;

import com.flechazo.slashblade.SlashBladeRefabriced;
import net.minecraft.resources.ResourceLocation;

public interface DefaultResources {
    ResourceLocation BaseMotionLocation = SlashBladeRefabriced.prefix("combostate/old_motion.vmd");
    ResourceLocation ExMotionLocation = SlashBladeRefabriced.prefix("combostate/motion.vmd");
    
    ResourceLocation testLocation = SlashBladeRefabriced.prefix("combostate/piercing.vmd");
    
    ResourceLocation testPLLocation = SlashBladeRefabriced.prefix("combostate/piercing_pl.vmd");

    public static final ResourceLocation resourceDefaultModel = new ResourceLocation("slashblade", "model/blade.obj");
    public static final ResourceLocation resourceDefaultTexture = new ResourceLocation("slashblade", "model/blade.png");

    public static final ResourceLocation resourceDurabilityModel = new ResourceLocation("slashblade",
            "model/util/durability.obj");
    public static final ResourceLocation resourceDurabilityTexture = new ResourceLocation("slashblade",
            "model/util/durability.png");
}
