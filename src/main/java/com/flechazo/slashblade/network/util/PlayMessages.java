package com.flechazo.slashblade.network.util;

import io.github.fabricators_of_create.porting_lib.entity.IEntityAdditionalSpawnData;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class PlayMessages {
    /**
     * Used to spawn a custom entity without the same restrictions as
     * {@link ClientboundAddEntityPacket}
     * <p>
     * To customize how your entity is created clientside (instead of using the default factory provided to the
     * {@link EntityType})
     * see {@link EntityType.Builder#setCustomClientFactory}.
     */
    public static class SpawnEntity {
        private final Entity entity;
        private final int typeId;
        private final int entityId;
        private final UUID uuid;
        private final double posX, posY, posZ;
        private final byte pitch, yaw, headYaw;
        private final int velX, velY, velZ;
        private final FriendlyByteBuf buf;

        public SpawnEntity(Entity e) {
            this.entity = e;
            this.typeId = BuiltInRegistries.ENTITY_TYPE.getId(e.getType());
            this.entityId = e.getId();
            this.uuid = e.getUUID();
            this.posX = e.getX();
            this.posY = e.getY();
            this.posZ = e.getZ();
            this.pitch = (byte) Mth.floor(e.getXRot() * 256.0F / 360.0F);
            this.yaw = (byte) Mth.floor(e.getYRot() * 256.0F / 360.0F);
            this.headYaw = (byte) (e.getYHeadRot() * 256.0F / 360.0F);
            Vec3 vec3d = e.getDeltaMovement();
            double d1 = Mth.clamp(vec3d.x, -3.9D, 3.9D);
            double d2 = Mth.clamp(vec3d.y, -3.9D, 3.9D);
            double d3 = Mth.clamp(vec3d.z, -3.9D, 3.9D);
            this.velX = (int) (d1 * 8000.0D);
            this.velY = (int) (d2 * 8000.0D);
            this.velZ = (int) (d3 * 8000.0D);
            this.buf = null;
        }

        private SpawnEntity(int typeId, int entityId, UUID uuid, double posX, double posY, double posZ, byte pitch, byte yaw, byte headYaw, int velX, int velY, int velZ, FriendlyByteBuf buf) {
            this.entity = null;
            this.typeId = typeId;
            this.entityId = entityId;
            this.uuid = uuid;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.pitch = pitch;
            this.yaw = yaw;
            this.headYaw = headYaw;
            this.velX = velX;
            this.velY = velY;
            this.velZ = velZ;
            this.buf = buf;
        }

        public static void encode(SpawnEntity msg, FriendlyByteBuf buf) {
            buf.writeVarInt(msg.typeId);
            buf.writeInt(msg.entityId);
            buf.writeLong(msg.uuid.getMostSignificantBits());
            buf.writeLong(msg.uuid.getLeastSignificantBits());
            buf.writeDouble(msg.posX);
            buf.writeDouble(msg.posY);
            buf.writeDouble(msg.posZ);
            buf.writeByte(msg.pitch);
            buf.writeByte(msg.yaw);
            buf.writeByte(msg.headYaw);
            buf.writeShort(msg.velX);
            buf.writeShort(msg.velY);
            buf.writeShort(msg.velZ);
            if (msg.entity instanceof IEntityAdditionalSpawnData entityAdditionalSpawnData) {
                final FriendlyByteBuf spawnDataBuffer = new FriendlyByteBuf(Unpooled.buffer());

                entityAdditionalSpawnData.writeSpawnData(spawnDataBuffer);

                buf.writeVarInt(spawnDataBuffer.readableBytes());
                buf.writeBytes(spawnDataBuffer);

                spawnDataBuffer.release();
            } else {
                buf.writeVarInt(0);
            }
        }

        public static SpawnEntity decode(FriendlyByteBuf buf) {
            return new SpawnEntity(buf.readVarInt(), buf.readInt(), new UUID(buf.readLong(), buf.readLong()), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readShort(), buf.readShort(), buf.readShort(), readSpawnDataPacket(buf));
        }

        private static FriendlyByteBuf readSpawnDataPacket(FriendlyByteBuf buf) {
            final int count = buf.readVarInt();
            if (count > 0) {
                final FriendlyByteBuf spawnDataBuffer = new FriendlyByteBuf(Unpooled.buffer());
                spawnDataBuffer.writeBytes(buf, count);
                return spawnDataBuffer;
            }

            return new FriendlyByteBuf(Unpooled.buffer());
        }

        public static void handleClient(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
            SpawnEntity msg = decode(buf);
            client.execute(() -> {
                try {
                    EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.byId(msg.typeId);
                    ClientLevel world = client.level;
                    if (world == null) {
                        return;
                    }

                    Entity e = type.create(world);
                    if (e == null) {
                        return;
                    }

                    /*
                     * Sets the position on the client, Mirrors what
                     * Entity#recreateFromPacket and LivingEntity#recreateFromPacket does.
                     */
                    e.syncPacketPositionCodec(msg.posX, msg.posY, msg.posZ);
                    e.absMoveTo(msg.posX, msg.posY, msg.posZ, (msg.yaw * 360) / 256.0F, (msg.pitch * 360) / 256.0F);
                    e.setYHeadRot((msg.headYaw * 360) / 256.0F);
                    e.setYBodyRot((msg.headYaw * 360) / 256.0F);

                    e.setId(msg.entityId);
                    e.setUUID(msg.uuid);
                    world.putNonPlayerEntity(msg.entityId, e);
                    e.lerpMotion(msg.velX / 8000.0, msg.velY / 8000.0, msg.velZ / 8000.0);
                    if (e instanceof IEntityAdditionalSpawnData entityAdditionalSpawnData) {
                        entityAdditionalSpawnData.readSpawnData(msg.buf);
                    }
                } finally {
                    if (msg.buf != null) {
                        msg.buf.release();
                    }
                }
            });

        }

        public Entity getEntity() {
            return entity;
        }

        public int getTypeId() {
            return typeId;
        }

        public int getEntityId() {
            return entityId;
        }

        public UUID getUuid() {
            return uuid;
        }

        public double getPosX() {
            return posX;
        }

        public double getPosY() {
            return posY;
        }

        public double getPosZ() {
            return posZ;
        }

        public byte getPitch() {
            return pitch;
        }

        public byte getYaw() {
            return yaw;
        }

        public byte getHeadYaw() {
            return headYaw;
        }

        public int getVelX() {
            return velX;
        }

        public int getVelY() {
            return velY;
        }

        public int getVelZ() {
            return velZ;
        }

        public FriendlyByteBuf getAdditionalData() {
            return buf;
        }
    }
}