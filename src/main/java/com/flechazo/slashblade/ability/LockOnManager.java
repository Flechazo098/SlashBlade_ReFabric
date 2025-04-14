package com.flechazo.slashblade.ability;

import com.flechazo.slashblade.capability.inputstate.InputStateComponentRegistry;
import com.flechazo.slashblade.event.InputCommandEvent;
import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.util.InputCommand;
import com.flechazo.slashblade.util.RayTraceHelper;
import com.flechazo.slashblade.util.TargetSelector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
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

        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> {
            s.setTargetEntityId(targetEntity);
        });

    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onEntityUpdate(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;

        final Minecraft mcinstance = Minecraft.getInstance();
		if (mcinstance.player == null)
            return;

        LocalPlayer player = mcinstance.player;

        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty())
            return;
        if (!(stack.getItem() instanceof ItemSlashBlade))
            return;

        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> {

            Entity target = s.getTargetEntity(player.level());

            if (target == null)
                return;
            if (!target.isAlive())
                return;

            LivingEntity entity = player;

            if (!entity.level().isClientSide())
                return;
            if (!entity.getCapability(InputStateComponentRegistry.INPUT_STATE)
                    .filter(input -> input.getCommands().contains(InputCommand.SNEAK)).isPresent())
                return;

            float partialTicks = mcinstance.getFrameTime();

            float oldYawHead = entity.yHeadRot;
            float oldYawOffset = entity.yBodyRot;
            float oldPitch = entity.getXRot();
            float oldYaw = entity.getYRot();

            float prevYawHead = entity.yHeadRotO;
            float prevYawOffset = entity.yBodyRotO;
            float prevYaw = entity.yRotO;
            float prevPitch = entity.xRotO;

            entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.position().add(0, target.getEyeHeight() / 2.0, 0));

            float step = 0.125f * partialTicks;

            step *= Math.min(1.0f, Math.abs(Mth.wrapDegrees(oldYaw - entity.yHeadRot) * 0.5));

            entity.setXRot(Mth.rotLerp(step, oldPitch, entity.getXRot()));
            entity.setYRot(Mth.rotLerp(step, oldYaw, entity.getYRot()));
            entity.setYHeadRot(Mth.rotLerp(step, oldYawHead, entity.getYHeadRot()));

            entity.yBodyRot = oldYawOffset;

            entity.yBodyRotO = prevYawOffset;
            entity.yHeadRotO = prevYawHead;
            entity.yRotO = prevYaw;
            entity.xRotO = prevPitch;
        });
    }

}
