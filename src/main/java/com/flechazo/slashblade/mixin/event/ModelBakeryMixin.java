package com.flechazo.slashblade.mixin.event;

import com.flechazo.slashblade.client.renderer.model.BladeModel;
import com.flechazo.slashblade.registry.SlashBladeRegister;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ModelBakery.ModelBakerImpl.class)
@Environment(EnvType.CLIENT)
public class ModelBakeryMixin {
    @Shadow
    @Final
    ModelBakery field_40571;

    @Unique
    private static final Set<ResourceLocation> TARGET_MODELS = Set.of(
            SlashBladeRegister.SLASHBLADE_ID,
            SlashBladeRegister.WHITE_ID,
            SlashBladeRegister.WOOD_ID,
            SlashBladeRegister.SILVER_ID,
            SlashBladeRegister.BAMBOO_ID
    );
    @Inject(method = "bake", at = @At("RETURN"), cancellable = true)
    private void onBake(ResourceLocation id, ModelState state, CallbackInfoReturnable<BakedModel> cir) {
        if (id.getPath().endsWith("_inventory") && TARGET_MODELS.contains(
                new ResourceLocation(id.getNamespace(), id.getPath().replace("_inventory", ""))
        )) {
            BakedModel original = cir.getReturnValue();
            cir.setReturnValue(new BladeModel(original, field_40571));
        }
    }
}
