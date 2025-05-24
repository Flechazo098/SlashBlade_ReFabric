package com.flechazo.slashblade.capability.inputstate;

import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class InputStateHelper {

    /**
     * 获取实体的输入状态组件
     * @param entity 实体
     * @return 输入状态组件
     */
    public static Optional<InputStateComponent> getInputState(LivingEntity entity) {
        return InputStateComponentRegistry.INPUT_STATE.maybeGet(entity);
    }
}