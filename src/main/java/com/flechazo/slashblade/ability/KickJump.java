package com.flechazo.slashblade.ability;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.capability.persistentdata.PersistentDataHelper;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.event.InputCommandEvent;
import com.flechazo.slashblade.registry.ComboStateRegistry;
import com.flechazo.slashblade.util.AdvancementHelper;
import com.flechazo.slashblade.util.InputCommand;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerTickEvents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumSet;

public class KickJump {
    private static final class SingletonHolder {
        private static final KickJump instance = new KickJump();
    }

    public static KickJump getInstance() {
        return KickJump.SingletonHolder.instance;
    }

    private KickJump() {
    }

    public void register() {
        InputCommandEvent.INPUT_COMMAND.register(this::onInputChange);

        PlayerTickEvents.START.register(this::onTick);
    }

    static final TargetingConditions tc = new TargetingConditions(false).ignoreLineOfSight()
            .ignoreInvisibilityTesting();

    static public final ResourceLocation ADVANCEMENT_KICK_JUMP = new ResourceLocation(SlashBladeRefabriced.MODID,
            "abilities/kick_jump");

    static public final String KEY_KICKJUMP = "sb.kickjump";

    public void onInputChange(InputCommandEvent event) {

        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();
        ServerPlayer sender = event.getEntity();
        Level worldIn = sender.level();

        if (sender.onGround())
            return;
        if (old.contains(InputCommand.JUMP))
            return;
        if (!current.contains(InputCommand.JUMP))
            return;

        if (PersistentDataHelper.getPersistentData(sender)
                .map(data -> data.getPersistentData().getInt(KEY_KICKJUMP))
                .orElse(0) != 0) {
            return;
        }


        Iterable<VoxelShape> list = worldIn.getBlockCollisions(sender, sender.getBoundingBox().inflate(0.5, 0, 1));
        if (!list.iterator().hasNext())
            return;

        // execute
        Untouchable.setUntouchable(sender, Untouchable.JUMP_TICKS);

        // set cooldown
        PersistentDataHelper.getPersistentData(sender).ifPresent(data ->
                data.getPersistentData().putInt(KEY_KICKJUMP, 2)
        );

        Vec3 delta = sender.getDeltaMovement();
        Vec3 motion = new Vec3(delta.x, +0.8, delta.z);

        sender.move(MoverType.SELF, motion);

        sender.connection.send(new ClientboundSetEntityMotionPacket(sender.getId(), motion.scale(0.75f)));

        AdvancementHelper.grantCriterion(sender, ADVANCEMENT_KICK_JUMP);
        sender.playNotifySound(SoundEvents.PLAYER_SMALL_FALL, SoundSource.PLAYERS, 0.5f, 1.2f);

        BladeStateHelper.getBladeState(sender.getMainHandItem()).ifPresent(s -> {
            s.updateComboSeq(sender, ComboStateRegistry.NONE.getId());
        });

        if (worldIn instanceof ServerLevel) {
            ((ServerLevel) worldIn).sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.GLASS.defaultBlockState()), sender.getX(),
                    sender.getY(), sender.getZ(), 20, 0.0D, 0.0D, 0.0D, 0.15F);
        }

    }

    public void onTick(Player player) {
        // 只在服务端执行
        if (player.level().isClientSide) return;

        PersistentDataHelper.getPersistentData(player).ifPresent(data -> {
            CompoundTag tag = data.getPersistentData();
            if (player.onGround() && tag.getInt(KEY_KICKJUMP) > 0) {
                int count = tag.getInt(KEY_KICKJUMP) - 1;

                if (count <= 0) {
                    tag.remove(KEY_KICKJUMP);
                } else {
                    tag.putInt(KEY_KICKJUMP, count);
                }
            }
        });
    }
}
