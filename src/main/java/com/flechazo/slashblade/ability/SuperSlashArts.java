package com.flechazo.slashblade.ability;

import com.flechazo.slashblade.capability.inputstate.InputStateHelper;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.event.InputCommandEvent;
import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.item.SwordType;
import com.flechazo.slashblade.registry.ComboStateRegistry;
import com.flechazo.slashblade.registry.combo.ComboState;
import com.flechazo.slashblade.slasharts.SlashArts;
import com.flechazo.slashblade.util.InputCommand;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.timers.TimerCallback;
import net.minecraft.world.level.timers.TimerQueue;

import java.util.EnumSet;
import java.util.Map;

public class SuperSlashArts {
    private static final class SingletonHolder {
        private static final SuperSlashArts instance = new SuperSlashArts();
    }

    public static SuperSlashArts getInstance() {
        return SuperSlashArts.SingletonHolder.instance;
    }

    private SuperSlashArts() {
    }

    public void register() {
        InputCommandEvent.INPUT_COMMAND.register(this::onInputChange);
    }

    public void onInputChange(InputCommandEvent event) {

        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();
        ServerPlayer sender = event.getEntity();

        InputCommand targetCommnad = InputCommand.SPRINT;

        boolean onDown = !old.contains(targetCommnad) && current.contains(targetCommnad);
        RandomSource random = sender.getRandom();
        final Long pressTime = event.getState().getLastPressTime(targetCommnad);
        if (onDown) {

            InputStateHelper.getInputState(sender).ifPresent(input -> {
                input.getScheduler().schedule("sendPartical", pressTime + 5, new TimerCallback<LivingEntity>() {
                    @Override
                    public void handle(LivingEntity rawEntity, TimerQueue<LivingEntity> queue, long now) {

                        if (!(rawEntity instanceof ServerPlayer entity))
                            return;

                        InputCommand targetCommnad = InputCommand.SPRINT;
                        boolean inputSucceed = InputStateHelper.getInputState(entity)
                                .filter(input -> input.getCommands().contains(targetCommnad)
                                        && (!InputCommand.anyMatch(input.getCommands(), InputCommand.move)
                                        || !input.getCommands().contains(InputCommand.SNEAK))
                                        && input.getLastPressTime(targetCommnad) == pressTime)
                                .isPresent();
                        if (!inputSucceed)
                            return;
                        ItemStack mainHandItem = entity.getMainHandItem();
                        BladeStateHelper.getBladeState(mainHandItem).ifPresent((state) -> {
                            if (state.isBroken() || state.getDamage() > 0 || state.isSealed()
                                    || !SwordType.from(mainHandItem).contains(SwordType.BEWITCHED)
                                    || !SwordType.from(mainHandItem).contains(SwordType.FIERCEREDGE))
                                return;

                            if (!entity.onGround())
                                return;
                            for (int i = 0; i < 32; ++i) {
                                double xDist = (random.nextFloat() * 2.0F - 1.0F);
                                double yDist = (random.nextFloat() * 2.0F - 1.0F);
                                double zDist = (random.nextFloat() * 2.0F - 1.0F);
                                if (!(xDist * xDist + yDist * yDist + zDist * zDist > 1.0D)) {
                                    double x = sender.getX(xDist / 4.0D);
                                    double y = sender.getY(0.5D + yDist / 4.0D);
                                    double z = sender.getZ(zDist / 4.0D);
                                    ((ServerLevel) event.getEntity().level()).sendParticles(
                                            ParticleTypes.REVERSE_PORTAL, x, y, z, 0, xDist, yDist + 0.2D, zDist, 1);
                                }
                            }
                        });
                    }
                });
                input.getScheduler().schedule("chargeSuperSA", pressTime + 20, new TimerCallback<LivingEntity>() {

                    @Override
                    public void handle(LivingEntity rawEntity, TimerQueue<LivingEntity> queue, long now) {
                        if (!(rawEntity instanceof ServerPlayer entity))
                            return;

                        InputCommand targetCommnad = InputCommand.SPRINT;
                        boolean inputSucceed = InputStateHelper.getInputState(entity)
                                .filter(input -> input.getCommands().contains(targetCommnad)
                                        && (!InputCommand.anyMatch(input.getCommands(), InputCommand.move)
                                        || !input.getCommands().contains(InputCommand.SNEAK))
                                        && input.getLastPressTime(targetCommnad) == pressTime)
                                .isPresent();
                        if (!inputSucceed)
                            return;

                        releaseSSA(entity);
                    }

                });
            });
        }
    }

    public static void releaseSSA(ServerPlayer entity) {
        ItemStack mainHandItem = entity.getMainHandItem();
        BladeStateHelper.getBladeState(mainHandItem).ifPresent((state) -> {
            if (state.isBroken() || state.getDamage() > 0 || state.isSealed()
                    || !SwordType.from(mainHandItem).contains(SwordType.BEWITCHED)
                    || !SwordType.from(mainHandItem).contains(SwordType.FIERCEREDGE))
                return;

            if (!entity.onGround())
                return;

            mainHandItem.hurtAndBreak(mainHandItem.getMaxDamage() / 2, entity,
                    ItemSlashBlade.getOnBroken(mainHandItem));

            Map.Entry<Integer, ResourceLocation> currentloc = state.resolvCurrentComboStateTicks(entity);

            ComboState currentCS = ComboStateRegistry.COMBO_STATE.get(currentloc.getValue());

            ResourceLocation csloc = state.getSlashArts().doArts(SlashArts.ArtsType.Super, entity);
            ComboState cs = ComboStateRegistry.COMBO_STATE.get(csloc);
            if (csloc != ComboStateRegistry.NONE.getId() && !currentloc.getValue().equals(csloc)) {

                if (currentCS.getPriority() > cs.getPriority()) {
                    state.updateComboSeq(entity, csloc);
                }
            }
        });
    }
}
