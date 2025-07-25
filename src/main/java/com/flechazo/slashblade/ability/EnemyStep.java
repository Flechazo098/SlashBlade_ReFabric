package com.flechazo.slashblade.ability;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.event.InputCommandEvent;
import com.flechazo.slashblade.registry.ComboStateRegistry;
import com.flechazo.slashblade.util.AdvancementHelper;
import com.flechazo.slashblade.util.InputCommand;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class EnemyStep {
    private static final class SingletonHolder {
        private static final EnemyStep instance = new EnemyStep();
    }

    public static EnemyStep getInstance() {
        return EnemyStep.SingletonHolder.instance;
    }

    private EnemyStep() {
    }

    public void register() {
        InputCommandEvent.INPUT_COMMAND.register(this::onInputChange);
    }

    static final TargetingConditions tc = new TargetingConditions(false).ignoreLineOfSight()
            .ignoreInvisibilityTesting();

    static public final ResourceLocation ADVANCEMENT_ENEMY_STEP = new ResourceLocation(SlashBladeRefabriced.MODID,
            "abilities/enemy_step");

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

        List<LivingEntity> list = worldIn.getNearbyEntities(LivingEntity.class, tc, sender,
                sender.getBoundingBox().inflate(1));
        if (list.isEmpty())
            return;

        Untouchable.setUntouchable(sender, Untouchable.JUMP_TICKS);

        Vec3 motion = new Vec3(0, +0.5, 0);

        sender.move(MoverType.SELF, motion);

        sender.connection.send(new ClientboundSetEntityMotionPacket(sender.getId(), motion.scale(0.75f)));

        AdvancementHelper.grantCriterion(sender, ADVANCEMENT_ENEMY_STEP);
        sender.playNotifySound(SoundEvents.PLAYER_SMALL_FALL, SoundSource.PLAYERS, 0.5f, 1.2f);

        BladeStateHelper.getBladeState(sender.getMainHandItem()).ifPresent(s -> {
            s.updateComboSeq(sender, ComboStateRegistry.NONE.getId());
        });

        if (worldIn instanceof ServerLevel) {
            ((ServerLevel) worldIn).sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ORANGE_STAINED_GLASS.defaultBlockState()),
                    sender.getX(), sender.getY(), sender.getZ(), 20, 0.0D, 0.0D, 0.0D, 0.15F);
        }
    }

}
