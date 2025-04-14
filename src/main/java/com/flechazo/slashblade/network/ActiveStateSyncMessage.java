package com.flechazo.slashblade.network;

import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.item.ItemSlashBlade;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ActiveStateSyncMessage {
    public CompoundTag activeTag;
    public int id;

    public ActiveStateSyncMessage() {
    }

    public ActiveStateSyncMessage(CompoundTag tag, int entityId) {
        this.activeTag = tag;
        this.id = entityId;
    }

    // 客户端处理
    public static void handleClient(Minecraft client, ClientPacketListener handler,
                                    FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        CompoundTag activeTag = buf.readNbt();

        client.execute(() -> {
            if (!activeTag.hasUUID("BladeUniqueId"))
                return;

            Entity target = client.level.getEntity(id);

            if (target instanceof LivingEntity livingEntity) {
                ItemStack stack = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);
                if (stack.isEmpty())
                    return;
                if (!(stack.getItem() instanceof ItemSlashBlade))
                    return;

                BladeStateHelper.getBladeState(stack).ifPresent(state -> {
                    if (state.getUniqueId().equals(activeTag.getUUID("BladeUniqueId"))) {
                        state.setActiveState(activeTag);
                    }
                });
            }
        });
    }
}