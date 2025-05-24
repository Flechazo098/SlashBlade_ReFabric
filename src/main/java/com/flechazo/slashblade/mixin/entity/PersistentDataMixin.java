package com.flechazo.slashblade.mixin.entity;

import com.flechazo.slashblade.util.accessor.PersistentDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class PersistentDataMixin implements PersistentDataAccessor {
    @Unique
    private CompoundTag persistentData = new CompoundTag();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        this.persistentData = new CompoundTag();
    }

    @Inject(method = "saveWithoutId", at = @At("TAIL"))
    private void onSave(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        tag.put("SlashBladePersistentData", this.persistentData);
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void onLoad(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("SlashBladePersistentData", Tag.TAG_COMPOUND)) {
            this.persistentData = tag.getCompound("SlashBladePersistentData");
        }
    }

    @Unique
    public CompoundTag slashbladerefabriced$getPersistentData () {
        return this.persistentData;
    }
}
