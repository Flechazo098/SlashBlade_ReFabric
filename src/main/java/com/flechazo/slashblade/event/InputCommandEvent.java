package com.flechazo.slashblade.event;

import com.flechazo.slashblade.capability.inputstate.InputStateComponent;
import com.flechazo.slashblade.capability.slashblade.BladeStateComponent;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.util.InputCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class InputCommandEvent extends SlashBladeEvent {


    public InputCommandEvent(ItemStack blade, BladeStateComponent bState, ServerPlayer player, InputStateComponent iSate, EnumSet<InputCommand> old, EnumSet<InputCommand> current) {
        super(blade, bState);
        this.player = player;
        this.state = iSate;
        this.old = old;
        this.current = current;
    }

    public InputCommandEvent(ServerPlayer player, InputStateComponent state, EnumSet<InputCommand> old, EnumSet<InputCommand> current) {
        super(player.getMainHandItem(), BladeStateHelper.getBladeState(player.getMainHandItem()).orElse(null));
        this.player = player;
        this.state = state;
        this.old = old;
        this.current = current;
    }

    public ServerPlayer getEntity() {
        return player;
    }

    public InputStateComponent getState() {
        return state;
    }

    public EnumSet<InputCommand> getOld() {
        return old;
    }

    public EnumSet<InputCommand> getCurrent() {
        return current;
    }

    ServerPlayer player;
    InputStateComponent state;
    EnumSet<InputCommand> old;
    EnumSet<InputCommand> current;

    public static InputCommandEvent onInputChange (ServerPlayer player, InputStateComponent state, EnumSet<InputCommand> old, EnumSet<InputCommand> current) {
        InputCommandEvent event = new InputCommandEvent(player, state, old, current);
        INPUT_COMMAND.post(event);
        return event;
    }
}
