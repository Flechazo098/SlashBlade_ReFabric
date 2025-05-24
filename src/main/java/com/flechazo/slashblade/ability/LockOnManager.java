package com.flechazo.slashblade.ability;

import com.flechazo.slashblade.capability.inputstate.InputStateComponentRegistry;
import com.flechazo.slashblade.capability.inputstate.InputStateHelper;
import com.flechazo.slashblade.capability.slashblade.BladeStateHelper;
import com.flechazo.slashblade.event.InputCommandEvent;
import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.util.InputCommand;
import com.flechazo.slashblade.util.RayTraceHelper;
import com.flechazo.slashblade.util.TargetSelector;
import com.flechazo.slashblade.util.accessor.PersistentDataAccessor;
import io.github.fabricators_of_create.porting_lib.entity.PartEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LockOnManager {
    private static final class SingletonHolder {
        private static final LockOnManager instance = new LockOnManager();
    }

    public static LockOnManager getInstance() {
        return SingletonHolder.instance;
    }

    private LockOnManager() {
    }

    public void register() {
        InputCommandEvent.INPUT_COMMAND.register(this::onInputChange);
        ClientTickEvents.START_CLIENT_TICK.register(this::onEntityUpdate);
    }

    public void onInputChange(InputCommandEvent event) {


        ServerPlayer player = event.getEntity();
        // set target
        ItemStack stack = event.getEntity().getMainHandItem();
        if (stack.isEmpty())
            return;
        if (!(stack.getItem() instanceof ItemSlashBlade))
            return;

        Entity targetEntity;
        
        if (event.getOld().contains(InputCommand.SNEAK) == event.getCurrent().contains(InputCommand.SNEAK))
            return;
        
        if ((event.getOld().contains(InputCommand.SNEAK) && !event.getCurrent().contains(InputCommand.SNEAK))) {
            // remove target
            targetEntity = null;
        } else {
            // search target

            Optional<HitResult> result = RayTraceHelper.rayTrace(player.level(), player, player.getEyePosition(1.0f),
                    player.getLookAngle(), 40, 40, (e) -> true);
            Optional<Entity> foundEntity = result.filter(r -> r.getType() == HitResult.Type.ENTITY).filter(r -> {
                EntityHitResult er = (EntityHitResult) r;
                Entity target = er.getEntity();

                if (target instanceof PartEntity) {
                    target = ((PartEntity<?>) target).getParent();
                }

                boolean isMatch = false;

                if (target instanceof LivingEntity)
                    isMatch = TargetSelector.lockon.test(player, (LivingEntity) target);

                return isMatch;
            }).map(r -> ((EntityHitResult) r).getEntity());

            if (!foundEntity.isPresent()) {
                List<LivingEntity> entities = player.level().getNearbyEntities(LivingEntity.class,
                        TargetSelector.lockon, player, player.getBoundingBox().inflate(12.0D, 6.0D, 12.0D));

                foundEntity = entities.stream().map(s -> (Entity) s)
                        .min(Comparator.comparingDouble(e -> e.distanceToSqr(player)));
            }

            targetEntity = foundEntity.map(e -> (e instanceof PartEntity) ? ((PartEntity<?>) e).getParent() : e)
                    .orElse(null);

        }

        BladeStateHelper.getBladeState(stack).ifPresent(s -> {
            s.setTargetEntityId(targetEntity);
        });

    }

    @Environment(EnvType.CLIENT)
    public void onEntityUpdate(Minecraft client) {
        // 确保玩家存在且在客户端
        if (client.player == null || client.level == null || !client.isSameThread()) return;

        LocalPlayer player = client.player;
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemSlashBlade)) return;

        // 必须蹲下才激活
        CompoundTag data = ((PersistentDataAccessor)(Object)player).slashbladerefabriced$getPersistentData();
        if (!InputStateHelper.getInputState(player)
                .filter(st -> st.getCommands().contains(InputCommand.SNEAK))
                .isPresent()) return;

        // 获取当前目标实体
        BladeStateHelper.getBladeState(stack).ifPresent(state -> {
            Entity tmp = state.getTargetEntity(player.level());
            if (!(tmp instanceof LivingEntity target) || !target.isAlive()) return;

            // 部分旋转插值恢复
            float partialTicks = client.getFrameTime();
            float oldHead   = player.yHeadRot,   oldBody = player.yBodyRot,
                    oldPitch  = player.getXRot(),  oldYaw  = player.getYRot();
            float prevHead = player.yHeadRotO,   prevBody = player.yBodyRotO,
                    prevYaw  = player.yRotO,       prevPitch = player.xRotO;

            // 旋转朝向目标
            player.lookAt(EntityAnchorArgument.Anchor.EYES,
                    target.getEyeY() == 0
                            ? target.position().add(0, target.getEyeHeight()/2, 0)
                            : target.position().add(0, target.getEyeHeight(), 0)
            );

            // 插值平滑
            float lerpStep = 0.125f * partialTicks;
            float diff     = Math.abs(Mth.wrapDegrees(oldYaw - player.yHeadRot)) * 0.5f;
            lerpStep *= Math.min(1.0f, diff);

            player.setXRot (Mth.rotLerp(lerpStep, oldPitch, player.getXRot()));
            player.setYRot (Mth.rotLerp(lerpStep, oldYaw,   player.getYRot()));
            player.setYHeadRot(Mth.rotLerp(lerpStep, oldHead, player.getYHeadRot()));

            // 恢复身体朝向
            player.yBodyRot = oldBody;
            player.yBodyRotO = prevBody;
            player.yHeadRotO = prevHead;
            player.yRotO      = prevYaw;
            player.xRotO      = prevPitch;
        });
    }
}

