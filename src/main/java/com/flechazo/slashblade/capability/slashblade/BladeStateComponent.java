package com.flechazo.slashblade.capability.slashblade;

import com.flechazo.slashblade.client.renderer.CarryType;
import com.flechazo.slashblade.event.BladeMotionEvent;
import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.item.SwordType;
import com.flechazo.slashblade.network.ActiveStateSyncMessage;
import com.flechazo.slashblade.network.NetworkManager;
import com.flechazo.slashblade.registry.ComboStateRegistry;
import com.flechazo.slashblade.registry.SlashArtsRegistry;
import com.flechazo.slashblade.registry.combo.ComboState;
import com.flechazo.slashblade.slasharts.SlashArts;
import com.flechazo.slashblade.util.AdvancementHelper;
import com.flechazo.slashblade.util.EnumSetConverter;
import com.flechazo.slashblade.util.NBTHelper;
import com.flechazo.slashblade.util.TimeValueHelper;
import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface BladeStateComponent extends Component {
    // 获取最后一次动作时间
    long getLastActionTime();

    // 设置最后一次动作时间
    void setLastActionTime(long lastActionTime);

    // 获取经过的时间
    default long getElapsedTime(LivingEntity user) {
        long ticks = (Math.max(0, user.level().getGameTime() - this.getLastActionTime()));

        if (user.level().isClientSide())
            ticks = Math.max(0, ticks + 1);

        return ticks;
    }

    // 是否点击
    boolean onClick();

    // 设置点击状态
    void setOnClick(boolean onClick);

    // 获取下落减速率
    float getFallDecreaseRate();

    // 设置下落减速率
    void setFallDecreaseRate(float fallDecreaseRate);

    // 获取攻击增幅
    float getAttackAmplifier();

    // 设置攻击增幅
    void setAttackAmplifier(float attackAmplifier);

    // 获取连击序列
    @Nonnull
    ResourceLocation getComboSeq();

    // 设置连击序列
    void setComboSeq(ResourceLocation comboSeq);

    // 是否损坏
    boolean isBroken();

    // 设置损坏状态
    void setBroken(boolean broken);

    // 是否封印
    boolean isSealed();

    // 设置封印状态
    void setSealed(boolean sealed);

    // 获取基础攻击修饰符
    float getBaseAttackModifier();

    // 设置基础攻击修饰符
    void setBaseAttackModifier(float baseAttackModifier);

    // 获取傲魂数量
    int getProudSoulCount();

    // 设置傲魂数量
    void setProudSoulCount(int psCount);

    // 获取击杀数
    int getKillCount();

    // 设置击杀数
    void setKillCount(int killCount);

    // 获取精炼值
    int getRefine();

    // 设置精炼值
    void setRefine(int refine);

    // 获取唯一ID
    UUID getUniqueId();

    // 设置唯一ID
    void setUniqueId(UUID id);

    // 获取斩击技
    @Nonnull
    default SlashArts getSlashArts() {
        ResourceLocation key = getSlashArtsKey();
        SlashArts result = null;
        if (key != null)
            result = SlashArtsRegistry.REGISTRY.containsKey(key) ? SlashArtsRegistry.REGISTRY.get(key)
                    : SlashArtsRegistry.JUDGEMENT_CUT;

        if (key == SlashArtsRegistry.NONE.getId())
            result = null;

        return result != null ? result : SlashArtsRegistry.JUDGEMENT_CUT;
    }

    // 设置斩击技键
    void setSlashArtsKey(ResourceLocation slashArts);

    // 获取斩击技键
    ResourceLocation getSlashArtsKey();

    // 是否默认魔化
    boolean isDefaultBewitched();

    // 设置默认魔化
    void setDefaultBewitched(boolean defaultBewitched);

    // 获取翻译键
    @Nonnull
    String getTranslationKey();

    // 设置翻译键
    void setTranslationKey(String translationKey);

    // 获取携带类型
    @Nonnull
    CarryType getCarryType();

    // 设置携带类型
    void setCarryType(CarryType carryType);

    // 获取效果颜色
    @Nonnull
    Color getEffectColor();

    // 设置效果颜色
    void setEffectColor(Color effectColor);

    // 是否效果颜色反转
    boolean isEffectColorInverse();

    // 设置效果颜色反转
    void setEffectColorInverse(boolean effectColorInverse);

    // 设置颜色代码
    default void setColorCode(int colorCode) {
        setEffectColor(new Color(colorCode));
    }

    // 获取颜色代码
    default int getColorCode() {
        return getEffectColor().getRGB();
    }

    // 获取调整值
    @Nonnull
    Vec3 getAdjust();

    // 设置调整值
    void setAdjust(Vec3 adjust);

    // 获取纹理
    @Nonnull
    Optional<ResourceLocation> getTexture();

    // 设置纹理
    void setTexture(ResourceLocation texture);

    // 获取模型
    @Nonnull
    Optional<ResourceLocation> getModel();

    // 设置模型
    void setModel(ResourceLocation model);

    // 获取目标实体ID
    int getTargetEntityId();

    // 设置目标实体ID
    void setTargetEntityIdInt(int id);

    // 获取目标实体
    @Nullable
    default Entity getTargetEntity(Level world) {
        int id = getTargetEntityId();
        if (id < 0)
            return null;
        else
            return world.getEntity(id);
    }

    // 设置目标实体ID
    default void setTargetEntityId(Entity target) {
        if (target != null)
            this.setTargetEntityIdInt(target.getId());
        else
            this.setTargetEntityIdInt(-1);
    }

    // 获取完全充能刻
    default int getFullChargeTicks(LivingEntity user) {
        return SlashArts.ChargeTicks;
    }

    // 是否已充能
    default boolean isCharged(LivingEntity user) {
        if (!(SwordType.from(user.getMainHandItem()).contains(SwordType.ENCHANTED)))
            return false;
        if (this.isBroken() || this.isSealed())
            return false;
        int elapsed = user.getTicksUsingItem();
        return getFullChargeTicks(user) < elapsed;
    }

    // 进行连击
    default ResourceLocation progressCombo(LivingEntity user, boolean isVirtual) {
        ResourceLocation currentloc = resolvCurrentComboState(user);
        ComboState current = ComboStateRegistry.REGISTRY.get().getValue(currentloc);

        if(current == null)
            return ComboStateRegistry.NONE.getId();

        ResourceLocation next = current.getNext(user);
        if (!next.equals(ComboStateRegistry.NONE.getId()) && next.equals(currentloc))
            return ComboStateRegistry.NONE.getId();

        ResourceLocation rootNext = ComboStateRegistry.REGISTRY.get().getValue(getComboRoot()).getNext(user);
        ComboState nextCS = ComboStateRegistry.REGISTRY.get().getValue(next);
        ComboState rootNextCS = ComboStateRegistry.REGISTRY.get().getValue(rootNext);
        ResourceLocation resolved = nextCS.getPriority() <= rootNextCS.getPriority() ? next : rootNext;

        if (!isVirtual) {
            this.updateComboSeq(user, resolved);
        }

        return resolved;
    }

    // 进行连击
    default ResourceLocation progressCombo(LivingEntity user) {
        return progressCombo(user, false);
    }

    // 执行充能动作
    default ResourceLocation doChargeAction(LivingEntity user, int elapsed) {
        if (elapsed <= 2)
            return ComboStateRegistry.NONE.getId();

        if (this.isBroken() || this.isSealed())
            return ComboStateRegistry.NONE.getId();

        Map.Entry<Integer, ResourceLocation> currentloc = resolvCurrentComboStateTicks(user);

        ComboState current = ComboStateRegistry.REGISTRY.get().getValue(currentloc.getValue());
        if(current == null)
            return ComboStateRegistry.NONE.getId();

        // Uninterrupted
        if (currentloc.getValue() != ComboStateRegistry.NONE.getId() && current.getNext(user) == currentloc.getValue())
            return ComboStateRegistry.NONE.getId();

        int fullChargeTicks = getFullChargeTicks(user);
        int justReceptionSpan = SlashArts.getJustReceptionSpan(user);
        int justChargePeriod = fullChargeTicks + justReceptionSpan;

        RangeMap<Integer, SlashArts.ArtsType> charge_accept = ImmutableRangeMap.<Integer, SlashArts.ArtsType>builder()
                .put(Range.lessThan(fullChargeTicks), SlashArts.ArtsType.Fail)
                .put(Range.closedOpen(fullChargeTicks, justChargePeriod), SlashArts.ArtsType.Jackpot)
                .put(Range.atLeast(justChargePeriod), SlashArts.ArtsType.Success).build();

        SlashArts.ArtsType type = charge_accept.get(elapsed);

        if (type != SlashArts.ArtsType.Jackpot) {
            // quick charge
            SlashArts.ArtsType result = current.releaseAction(user, currentloc.getKey());

            if (result != SlashArts.ArtsType.Fail)
                type = result;
        }

        ResourceLocation csloc = this.getSlashArts().doArts(type, user);
        ComboState cs = ComboStateRegistry.REGISTRY.get().getValue(csloc);
        if (csloc != ComboStateRegistry.NONE.getId() && !currentloc.getValue().equals(csloc)) {

            if (current.getPriority() > cs.getPriority()) {
                if (type == SlashArts.ArtsType.Jackpot)
                    AdvancementHelper.grantedIf(Enchantments.SOUL_SPEED, user);
                this.updateComboSeq(user, csloc);
            }
        }
        return csloc;
    }

    // 更新连击序列
    default void updateComboSeq(LivingEntity entity, ResourceLocation loc) {

        BladeMotionEvent.BLADE_MOTION.post(new BladeMotionEvent(entity, loc));
        this.setComboSeq(loc);
        this.setLastActionTime(entity.level().getGameTime());
        ComboState cs = ComboStateRegistry.REGISTRY.get().getValue(loc);
        cs.clickAction(entity);
    }

    // 解析当前连击状态
    default ResourceLocation resolvCurrentComboState(LivingEntity user) {
        if(!(user.getMainHandItem().getItem() instanceof ItemSlashBlade))
            return ComboStateRegistry.NONE.getId();
        return resolvCurrentComboStateTicks(user).getValue();
    }

    // 解析当前连击状态刻
    default Map.Entry<Integer, ResourceLocation> resolvCurrentComboStateTicks(LivingEntity user) {
        ResourceLocation current = ComboStateRegistry.COMBO_STATE.containsKey(getComboSeq()) ? getComboSeq()
                : ComboStateRegistry.NONE.getId();
        ComboState currentCS = ComboStateRegistry.REGISTRY.get().getValue(current) != null
                ? ComboStateRegistry.REGISTRY.get().getValue(current)
                : ComboStateRegistry.NONE.get();
        int time = (int) TimeValueHelper.getMSecFromTicks(getElapsedTime(user));

        while (!current.equals(ComboStateRegistry.NONE.getId()) && currentCS.getTimeoutMS() < time) {
            time -= currentCS.getTimeoutMS();

            current = currentCS.getNextOfTimeout(user);
            this.updateComboSeq(user, current);
        }

        int ticks = (int) TimeValueHelper.getTicksFromMSec(time);
        return new AbstractMap.SimpleImmutableEntry<>(ticks, current);
    }

    // 获取连击根
    ResourceLocation getComboRoot();

    // 设置连击根
    void setComboRoot(ResourceLocation resourceLocation);

    // 获取损坏值
    int getDamage();

    // 设置损坏值
    void setDamage(int damage);

    // 获取最大损坏值
    int getMaxDamage();

    // 设置最大损坏值
    void setMaxDamage(int damage);

    // 获取特殊效果
    List<ResourceLocation> getSpecialEffects();

    // 设置特殊效果
    void setSpecialEffects(ListTag list);

    // 添加特殊效果
    boolean addSpecialEffect(ResourceLocation se);

    // 移除特殊效果
    boolean removeSpecialEffect(ResourceLocation se);

    // 是否有特殊效果
    boolean hasSpecialEffect(ResourceLocation se);

    // 是否有活动状态变化
    boolean hasChangedActiveState();

    // 设置活动状态变化
    void setHasChangedActiveState(boolean isChanged);

    // 发送变化
    default void sendChanges(Entity entityIn) {
        if (!entityIn.level().isClientSide() && this.hasChangedActiveState()) {
            ActiveStateSyncMessage msg = new ActiveStateSyncMessage();
            msg.activeTag = this.getActiveState();
            msg.id = entityIn.getId();
            NetworkManager.sendToTrackingAndSelf(entityIn, msg);

            this.setHasChangedActiveState(false);
        }
    }

    // 获取活动状态
    default CompoundTag getActiveState() {
        CompoundTag tag = new CompoundTag();

        NBTHelper.getNBTCoupler(tag)
                // Action State
                .put("lastActionTime", this.getLastActionTime())
                .put("TargetEntity", this.getTargetEntityId())
                .put("_onClick", this.onClick())
                .put("fallDecreaseRate", this.getFallDecreaseRate())
                .put("AttackAmplifier", this.getAttackAmplifier())
                .put("currentCombo", this.getComboSeq().toString())
                .put("Damage", this.getDamage())
                .put("maxDamage", this.getMaxDamage())
                .put("proudSoul", this.getProudSoulCount())
                .put("isBroken", this.isBroken())

                // Passive State
                .put("isSealed", this.isSealed())
                .put("baseAttackModifier", this.getBaseAttackModifier())
                .put("killCount", this.getKillCount())
                .put("RepairCounter", this.getRefine())

                // UUID
                .put("BladeUniqueId", this.getUniqueId())

                // Performance Setting
                .put("SpecialAttackType",
                        Optional.ofNullable(this.getSlashArtsKey())
                                .orElse(SlashArtsRegistry.JUDGEMENT_CUT.getId()).toString()
                )
                .put("isDefaultBewitched", this.isDefaultBewitched())
                .put("translationKey", this.getTranslationKey())

                // Render Info
                .put("StandbyRenderType", (byte) this.getCarryType().ordinal())
                .put("SummonedSwordColor", this.getColorCode())
                .put("SummonedSwordColorInverse", this.isEffectColorInverse())
                .put("adjustXYZ", NBTHelper.newDoubleNBTList(this.getAdjust()))

                // Texture & Model
                .put("TextureName", this.getTexture().map(ResourceLocation::toString).orElse(null))
                .put("ModelName", this.getModel().map(ResourceLocation::toString).orElse(null))

                // Combo Root
                .put("ComboRoot",
                        Optional.ofNullable(this.getComboRoot())
                                .orElse(ComboStateRegistry.STANDBY.getId()).toString()
                );

        if (this.getSpecialEffects() != null && ! this.getSpecialEffects().isEmpty()) {
            ListTag seList = new ListTag();
            this.getSpecialEffects().forEach(se -> seList.add(StringTag.valueOf(se.toString())));
            tag.put("SpecialEffects", seList);

        }
        return tag;
    }


    // 设置活动状态
    default void setActiveState(CompoundTag tag) {
        if (tag == null) return;
        this.setNonEmpty();

        NBTHelper.getNBTCoupler(tag)
                // Action State
                .get("BladeUniqueId", this::setUniqueId)
                .get("lastActionTime", this::setLastActionTime)
                .get("TargetEntity", this::setTargetEntityIdInt)
                .get("_onClick", this::setOnClick)
                .get("fallDecreaseRate", this::setFallDecreaseRate)
                .get("AttackAmplifier", this::setAttackAmplifier)
                .get("currentCombo", (String s) -> this.setComboSeq(ResourceLocation.tryParse(s)))
                .get("proudSoul", this::setProudSoulCount)
                .get("Damage", this::setDamage)
                .get("isBroken", this::setBroken)

                // Passive State
                .get("isSealed", this::setSealed)
                .get("baseAttackModifier", this::setBaseAttackModifier)
                .get("killCount", this::setKillCount)
                .get("RepairCounter", this::setRefine)

                // Performance Setting
                .get("SpecialAttackType", (String s) -> this.setSlashArtsKey(ResourceLocation.tryParse(s)))
                .get("isDefaultBewitched", this::setDefaultBewitched)
                .get("translationKey", this::setTranslationKey)

                // Render Info
                .get("StandbyRenderType", (Byte b) -> this.setCarryType(EnumSetConverter.fromOrdinal(CarryType.values(), b, CarryType.PSO2)))
                .get("SummonedSwordColor", this::setColorCode)
                .get("SummonedSwordColorInverse", this::setEffectColorInverse)
                .get("adjustXYZ", (ListTag list) -> this.setAdjust(NBTHelper.getVector3d(tag, "adjustXYZ")))

                // Texture & Model
                .get("TextureName", (String s) -> this.setTexture(new ResourceLocation(s)))
                .get("ModelName", (String s) -> this.setModel(new ResourceLocation(s)))

                // Combo Root
                .get("ComboRoot", (String s) -> this.setComboRoot(ResourceLocation.tryParse(s)));

                if (tag.contains("SpecialEffects")) {
                 ListTag list = tag.getList("SpecialEffects", Tag.TAG_STRING);
                 this.setSpecialEffects(list);
               }

        this.setHasChangedActiveState(false);
    }

    // 是否为空
    boolean isEmpty();

    // 设置非空
    void setNonEmpty();
}