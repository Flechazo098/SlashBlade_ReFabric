package com.flechazo.slashblade.capability.inputstate;

import com.flechazo.slashblade.event.Scheduler;
import com.flechazo.slashblade.util.InputCommand;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumMap;
import java.util.EnumSet;

public interface InputStateComponent extends Component {
    EnumSet<InputCommand> getCommands();

    Scheduler getScheduler();

    EnumMap<InputCommand, Long> getLastPressTimes();

    default long getLastPressTime(InputCommand command) {
        if (this.getLastPressTimes().containsKey(command)) {
            return this.getLastPressTimes().get(command);
        } else {
            return -1;
        }
    }

    default EnumSet<InputCommand> getCommands(LivingEntity owner) {
        EnumSet<InputCommand> commands = getCommands().clone();

        if (owner.onGround()) {
            commands.add(InputCommand.ON_GROUND);
        } else {
            commands.add(InputCommand.ON_AIR);
        }
        return commands;
    }
}