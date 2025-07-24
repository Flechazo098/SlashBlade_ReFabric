package com.flechazo.slashblade.network;

import com.flechazo.slashblade.capability.inputstate.InputStateHelper;
import com.flechazo.slashblade.event.InputCommandEvent;
import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.util.EnumSetConverter;
import com.flechazo.slashblade.util.InputCommand;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class MoveCommandMessage {
    public int command;

    public MoveCommandMessage(int command) {
        this.command = command;
    }

    public MoveCommandMessage() {
    }

    // 从客户端发送到服务器
    public static void sendToServer(int command) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(command);
        NetworkManager.sendToServer(NetworkManager.MOVE_COMMAND_ID, buf);
    }

    // 服务器端处理
    public static void handleServer(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int command = buf.readInt();

        server.execute(() -> {
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (stack.isEmpty())
                return;
            if (!(stack.getItem() instanceof ItemSlashBlade))
                return;

            InputStateHelper.getInputState(player).ifPresent((state) -> {
                EnumSet<InputCommand> old = state.getCommands().clone();

                state.getCommands().clear();
                state.getCommands().addAll(EnumSetConverter.convertToEnumSet(InputCommand.class, command));

                EnumSet<InputCommand> current = state.getCommands().clone();

                long currentTime = player.level().getGameTime();
                current.forEach(c -> {
                    if (!old.contains(c))
                        state.getLastPressTimes().put(c, currentTime);
                });

                InputCommandEvent.onInputChange(player, state, old, current);
            });
        });
    }
}