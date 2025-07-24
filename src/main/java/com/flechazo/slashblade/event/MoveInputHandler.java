package com.flechazo.slashblade.event;

import com.flechazo.slashblade.capability.inputstate.InputStateComponent;
import com.flechazo.slashblade.capability.inputstate.InputStateHelper;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.client.SlashBladeKeyMappings;
import com.flechazo.slashblade.network.MoveCommandMessage;
import com.flechazo.slashblade.network.NetworkManager;
import com.flechazo.slashblade.util.EnumSetConverter;
import com.flechazo.slashblade.util.InputCommand;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

import java.util.EnumSet;


public class MoveInputHandler {

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(MoveInputHandler::onClientTick);
    }

    public static final String LAST_CHANGE_TIME = "SB_LAST_CHANGE_TIME";

    public static boolean checkFlag(int data, int flags) {
        return (data & flags) == flags;
    }

    private static void onClientTick(Minecraft client) {

        if (client.player == null || client.level == null) return;

        LocalPlayer player = client.player;

        if (BladeStateHelper.getBladeState(player.getMainHandItem()).isEmpty())
            return;

        EnumSet<InputCommand> commands = EnumSet.noneOf(InputCommand.class);

        if (player.input.up)
            commands.add(InputCommand.FORWARD);
        if (player.input.down)
            commands.add(InputCommand.BACK);
        if (player.input.left)
            commands.add(InputCommand.LEFT);
        if (player.input.right)
            commands.add(InputCommand.RIGHT);

        if (player.input.shiftKeyDown)
            commands.add(InputCommand.SNEAK);

        if (player.input.jumping) {
            commands.add(InputCommand.JUMP);
        }

        final Minecraft minecraftInstance = Minecraft.getInstance();

        if (SlashBladeKeyMappings.KEY_SPECIAL_MOVE.isDown())
            commands.add(InputCommand.SPRINT);

        if (minecraftInstance.options.keyUse.isDown())
            commands.add(InputCommand.R_DOWN);
        if (minecraftInstance.options.keyAttack.isDown())
            commands.add(InputCommand.L_DOWN);

        if (SlashBladeKeyMappings.KEY_SUMMON_BLADE.isDown())
            commands.add(InputCommand.M_DOWN);

        EnumSet<InputCommand> old = InputStateHelper.getInputState(player).map(InputStateComponent::getCommands)
                .orElseGet(() -> EnumSet.noneOf(InputCommand.class));

        Level worldIn = player.getCommandSenderWorld();
        long currentTime = worldIn.getGameTime();
        boolean doSend = !old.equals(commands);

        if (doSend) {
            InputStateHelper.getInputState(player).ifPresent((state) -> {
                commands.forEach(c -> {
                    if (!old.contains(c))
                        state.getLastPressTimes().put(c, currentTime);
                });

                state.getCommands().clear();
                state.getCommands().addAll(commands);
            });

            MoveCommandMessage msg = new MoveCommandMessage();
            msg.command = EnumSetConverter.convertToInt(commands);
            FriendlyByteBuf buf = PacketByteBufs.create();
            NetworkManager.sendToServer(NetworkManager.MOVE_COMMAND_ID, buf);
        }
    }
}