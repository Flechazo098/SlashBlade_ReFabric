package com.flechazo.slashblade.capability.persistentdata;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.CompoundTag;

public interface PersistentDataComponent extends Component {
    CompoundTag getPersistentData();

    void setPersistentData(CompoundTag data);
}