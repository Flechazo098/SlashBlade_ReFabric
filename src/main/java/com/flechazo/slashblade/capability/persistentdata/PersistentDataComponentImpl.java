package com.flechazo.slashblade.capability.persistentdata;

import net.minecraft.nbt.CompoundTag;

public class PersistentDataComponentImpl implements PersistentDataComponent {
    private CompoundTag persistentData = new CompoundTag();

    @Override
    public CompoundTag getPersistentData() {
        return this.persistentData;
    }

    @Override
    public void setPersistentData(CompoundTag data) {
        this.persistentData = data;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        if (tag.contains("SlashBladePersistentData")) {
            this.persistentData = tag.getCompound("SlashBladePersistentData");
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.put("SlashBladePersistentData", this.persistentData);
    }
}