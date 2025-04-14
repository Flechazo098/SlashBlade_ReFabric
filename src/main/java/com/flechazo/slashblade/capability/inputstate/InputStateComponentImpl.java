package com.flechazo.slashblade.capability.inputstate;

import com.flechazo.slashblade.event.Scheduler;
import com.flechazo.slashblade.util.EnumSetConverter;
import com.flechazo.slashblade.util.InputCommand;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;

import java.util.EnumMap;
import java.util.EnumSet;

public class InputStateComponentImpl implements InputStateComponent {

    private EnumSet<InputCommand> commands = EnumSet.noneOf(InputCommand.class);
    private Scheduler scheduler = new Scheduler();
    private EnumMap<InputCommand, Long> lastPressTimes = Maps.newEnumMap(InputCommand.class);

    @Override
    public EnumSet<InputCommand> getCommands() {
        return commands;
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public EnumMap<InputCommand, Long> getLastPressTimes() {
        return lastPressTimes;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        if (tag.contains("Command")) {
            commands.clear();
            commands.addAll(EnumSetConverter.convertToEnumSet(InputCommand.class, tag.getInt("Command")));
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putInt("Command", EnumSetConverter.convertToInt(commands));
    }
}