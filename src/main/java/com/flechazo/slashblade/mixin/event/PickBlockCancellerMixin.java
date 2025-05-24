package com.flechazo.slashblade.mixin.event;

import com.flechazo.slashblade.item.ItemSlashBlade;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MultiPlayerGameMode.class)
public class PickBlockCancellerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "handlePickItem(I)V", at = @At("HEAD"), cancellable = true)
    private void onHandlePickItem(int slot, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.getMainHandItem().getItem() instanceof ItemSlashBlade) {
            ci.cancel();
        }
    }
}