package com.flechazo.slashblade.mixin.client;

import com.flechazo.slashblade.client.renderer.model.BladeModelManager;
import com.flechazo.slashblade.client.renderer.model.BladeMotionManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Environment(EnvType.CLIENT)
@Mixin(TextureAtlas.class)
public class TextureAtlasMixin  {

    @Inject(method = "upload", at = @At("RETURN"))
    private void onResourceReload(SpriteLoader.Preparations preparations, CallbackInfo ci) {
            BladeModelManager.getInstance().reload();
            BladeMotionManager.getInstance().reload();
    }
}