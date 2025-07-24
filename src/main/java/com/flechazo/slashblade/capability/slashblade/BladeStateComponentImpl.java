package com.flechazo.slashblade.capability.slashblade;

import com.flechazo.slashblade.client.renderer.CarryType;
import com.flechazo.slashblade.registry.ComboStateRegistry;
import com.flechazo.slashblade.registry.SlashArtsRegistry;
import com.flechazo.slashblade.registry.SpecialEffectsRegistry;
import com.flechazo.slashblade.util.NBTHelper;
import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BladeStateComponentImpl extends ItemComponent implements BladeStateComponent {

    // action state
    protected long lastActionTime; // lastActionTime
    protected int targetEntityId; // TargetEntity
    protected boolean _onClick; // _onClick
    protected float fallDecreaseRate;
    protected boolean isCharged; // isCharged
    protected float attackAmplifier; // AttackAmplifier
    protected ResourceLocation comboSeq; // comboSeq
    protected String lastPosHash; // lastPosHash
    protected boolean isBroken; // isBroken

    // passive state
    protected boolean isNoScabbard; // isNoScabbard
    protected boolean isSealed; // isSealed

    protected float baseAttackModifier = 4F; // BaseAttackModifier

    protected int killCount; // killCount
    protected int refine; // RepairCounter

    protected UUID owner; // Owner

    protected UUID uniqueId = UUID.randomUUID(); // Owner

    protected String translationKey = "";

    // performance setting
    protected ResourceLocation slashArtsKey; // SpecialAttackType
    protected boolean isDefaultBewitched = false; // isDefaultBewitched

    protected ResourceLocation comboRootName;

    // render info
    protected Optional<CarryType> carryType = Optional.empty(); // StandbyRenderType
    protected Optional<Color> effectColor = Optional.empty(); // SummonedSwordColor
    protected boolean effectColorInverse;// SummonedSwordColorInverse
    protected Optional<Vec3> adjust = Optional.empty();// adjustXYZ

    protected Optional<ResourceLocation> texture = Optional.empty(); // TextureName
    protected Optional<ResourceLocation> model = Optional.empty();// ModelName

    protected int maxDamage = 40;
    protected int damage = 0;

    protected int proudSoul = 0;

    protected boolean isEmpty = true;

    protected List<ResourceLocation> specialEffects = new ArrayList<>();

    protected boolean isChangedActiveState = false;

    private final ItemStack blade;

    public BladeStateComponentImpl(ItemStack blade) {
        super(blade);
        this.blade = blade;
        if (!blade.isEmpty()) {
            if (blade.getOrCreateTag().contains("bladeState"))
                this.readFromNbt(blade.getOrCreateTag().getCompound("bladeState"));
        }
    }

    @Override
    public long getLastActionTime() {
        return lastActionTime;
    }

    @Override
    public void setLastActionTime(long lastActionTime) {
        this.lastActionTime = lastActionTime;
        setHasChangedActiveState(true);
    }

    @Override
    public boolean onClick() {
        return _onClick;
    }

    @Override
    public void setOnClick(boolean onClick) {
        this._onClick = onClick;
        setHasChangedActiveState(true);
    }

    @Override
    public float getFallDecreaseRate() {
        return fallDecreaseRate;
    }

    @Override
    public void setFallDecreaseRate(float fallDecreaseRate) {
        this.fallDecreaseRate = fallDecreaseRate;
        setHasChangedActiveState(true);
    }

    @Override
    public float getAttackAmplifier() {
        return attackAmplifier;
    }

    @Override
    public void setAttackAmplifier(float attackAmplifier) {
        this.attackAmplifier = attackAmplifier;
        setHasChangedActiveState(true);
    }

    @Override
    @Nonnull
    public ResourceLocation getComboSeq() {
        return comboSeq != null ? comboSeq : ComboStateRegistry.NONE.getId();
    }

    @Override
    public void setComboSeq(ResourceLocation comboSeq) {
        this.comboSeq = comboSeq;
        setHasChangedActiveState(true);
    }

    @Override
    public boolean isBroken() {
        return isBroken;
    }

    @Override
    public void setBroken(boolean broken) {
        isBroken = broken;
        setHasChangedActiveState(true);
    }

    @Override
    public boolean isSealed() {
        return isSealed;
    }

    @Override
    public void setSealed(boolean sealed) {
        isSealed = sealed;
    }

    @Override
    public float getBaseAttackModifier() {
        return baseAttackModifier;
    }

    @Override
    public void setBaseAttackModifier(float baseAttackModifier) {
        this.baseAttackModifier = baseAttackModifier;
    }

    @Override
    public int getKillCount() {
        return killCount;
    }

    @Override
    public void setKillCount(int killCount) {
        this.killCount = killCount;
        setHasChangedActiveState(true);
    }

    @Override
    public int getRefine() {
        return refine;
    }

    @Override
    public void setRefine(int refine) {
        this.refine = refine;
        setHasChangedActiveState(true);
    }

    @Override
    public ResourceLocation getSlashArtsKey() {
        return this.slashArtsKey;
    }

    @Override
    public void setSlashArtsKey(ResourceLocation key) {
        this.slashArtsKey = key;
    }

    @Override
    public boolean isDefaultBewitched() {
        return isDefaultBewitched;
    }

    @Override
    public void setDefaultBewitched(boolean defaultBewitched) {
        isDefaultBewitched = defaultBewitched;
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    @Override
    public void setTranslationKey(String translationKey) {
        this.translationKey = Optional.ofNullable(translationKey).orElse("");
    }

    @Override
    @Nonnull
    public CarryType getCarryType() {
        return carryType.orElse(CarryType.NONE);
    }

    @Override
    public void setCarryType(CarryType carryType) {
        this.carryType = Optional.ofNullable(carryType);
    }

    @Override
    public Color getEffectColor() {
        return effectColor.orElseGet(() -> new Color(0x3333FF));
    }

    @Override
    public void setEffectColor(Color effectColor) {
        this.effectColor = Optional.ofNullable(effectColor);
    }

    @Override
    public boolean isEffectColorInverse() {
        return effectColorInverse;
    }

    @Override
    public void setEffectColorInverse(boolean effectColorInverse) {
        this.effectColorInverse = effectColorInverse;
    }

    @Override
    public Vec3 getAdjust() {
        return adjust.orElseGet(() -> Vec3.ZERO);
    }

    @Override
    public void setAdjust(Vec3 adjust) {
        this.adjust = Optional.ofNullable(adjust);
    }

    @Override
    public Optional<ResourceLocation> getTexture() {
        return texture;
    }

    @Override
    public void setTexture(ResourceLocation texture) {
        this.texture = Optional.ofNullable(texture);
    }

    @Override
    public Optional<ResourceLocation> getModel() {
        return model;
    }

    @Override
    public void setModel(ResourceLocation model) {
        this.model = Optional.ofNullable(model);
    }

    @Override
    public int getTargetEntityId() {
        return targetEntityId;
    }

    @Override
    public void setTargetEntityIdInt(int id) {
        targetEntityId = id;
        setHasChangedActiveState(true);
    }

    @Override
    public ResourceLocation getComboRoot() {
        if (this.comboRootName == null || !ComboStateRegistry.COMBO_STATE.containsKey(this.comboRootName))
            return ComboStateRegistry.STANDBY.getId();
        return this.comboRootName;
    }

    @Override
    public void setComboRoot(ResourceLocation rootLoc) {
        this.comboRootName = ComboStateRegistry.COMBO_STATE.containsKey(rootLoc) ? rootLoc
                : ComboStateRegistry.STANDBY.getId();
    }

    @Override
    public boolean hasChangedActiveState() {
        return this.isChangedActiveState;
    }

    @Override
    public void setHasChangedActiveState(boolean isChanged) {
        this.isChangedActiveState = isChanged;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public int getMaxDamage() {
        return this.maxDamage;
    }

    @Override
    public void setMaxDamage(int damage) {
        this.maxDamage = damage;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }

    @Override
    public void setDamage(int damage) {
        this.damage = Math.max(0, damage);
        setHasChangedActiveState(true);
    }

    @Override
    public int getProudSoulCount() {
        return this.proudSoul;
    }

    @Override
    public void setProudSoulCount(int psCount) {
        this.proudSoul = Math.max(0, psCount);
        setHasChangedActiveState(true);
    }

    @Override
    public List<ResourceLocation> getSpecialEffects() {
        return this.specialEffects;
    }

    @Override
    public void setSpecialEffects(ListTag list) {
        List<ResourceLocation> result = new ArrayList<>();
        list.forEach(tag -> {
            ResourceLocation se = ResourceLocation.tryParse(tag.getAsString());
            if (SpecialEffectsRegistry.REGISTRY.containsKey(se))
                result.add(se);
        });
        this.specialEffects = result;
    }

    @Override
    public boolean addSpecialEffect(ResourceLocation se) {
        if (SpecialEffectsRegistry.REGISTRY.containsKey(se)) {
            return this.specialEffects.add(se);
        }
        return false;
    }

    @Override
    public boolean removeSpecialEffect(ResourceLocation se) {
        return this.specialEffects.remove(se);
    }

    @Override
    public boolean hasSpecialEffect(ResourceLocation se) {
        if (SpecialEffectsRegistry.REGISTRY.containsKey(se)) {
            return this.specialEffects.contains(se);
        }
        this.specialEffects.remove(se);
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.isEmpty;
    }

    @Override
    public void setNonEmpty() {
        this.isEmpty = false;
    }

    @Override
    public CompoundTag getActiveState() {
        CompoundTag tag = new CompoundTag();

        this.setNonEmpty();
        // action state
        this.setLastActionTime(tag.getLong("lastActionTime"));
        this.setTargetEntityIdInt(tag.getInt("TargetEntity"));
        this.setOnClick(tag.getBoolean("_onClick"));
        this.setFallDecreaseRate(tag.getFloat("fallDecreaseRate"));
        this.setAttackAmplifier(tag.getFloat("AttackAmplifier"));
        this.setComboSeq(ResourceLocation.tryParse(tag.getString("currentCombo")));
        this.setDamage(tag.getInt("Damage"));
        this.setMaxDamage(tag.getInt("maxDamage"));
        this.setProudSoulCount(tag.getInt("proudSoul"));
        this.setBroken(tag.getBoolean("isBroken"));

        this.setHasChangedActiveState(true);

        // passive state
        this.setSealed(tag.getBoolean("isSealed"));

        this.setBaseAttackModifier(tag.getFloat("baseAttackModifier"));

        this.setKillCount(tag.getInt("killCount"));
        this.setRefine(tag.getInt("RepairCounter"));

        this.setUniqueId(tag.hasUUID("BladeUniqueId") ? tag.getUUID("BladeUniqueId") : UUID.randomUUID());

        // performance setting
        this.setSlashArtsKey(ResourceLocation.tryParse(tag.getString("SpecialAttackType")));
        this.setDefaultBewitched(tag.getBoolean("isDefaultBewitched"));
        this.setTranslationKey(tag.getString("translationKey"));

        // render info
        this.setCarryType(CarryType.values()[tag.getByte("StandbyRenderType") % CarryType.values().length]);
        this.setColorCode(tag.getInt("SummonedSwordColor"));
        this.setEffectColorInverse(tag.getBoolean("SummonedSwordColorInverse"));

        if (tag.contains("adjustXYZ")) {
            double[] adjustArray = new double[3];
            for (int i = 0; i < 3; i++) {
                adjustArray[i] = tag.getList("adjustXYZ", 6).getDouble(i);
            }
            this.setAdjust(new Vec3(adjustArray[0], adjustArray[1], adjustArray[2]));
        }

        if (tag.contains("TextureName")) {
            this.setTexture(ResourceLocation.tryParse(tag.getString("TextureName")));
        }

        if (tag.contains("ModelName")) {
            this.setModel(ResourceLocation.tryParse(tag.getString("ModelName")));
        }

        this.setComboRoot(ResourceLocation.tryParse(tag.getString("ComboRoot")));

        if (tag.contains("SpecialEffects")) {
            this.setSpecialEffects(tag.getList("SpecialEffects", 8));
        }
        return tag;
    }

    @Override
    public void setActiveState(CompoundTag tag) {
        // action state
        tag.putLong("lastActionTime", this.getLastActionTime());
        tag.putInt("TargetEntity", this.getTargetEntityId());
        tag.putBoolean("_onClick", this.onClick());
        tag.putFloat("fallDecreaseRate", this.getFallDecreaseRate());
        tag.putFloat("AttackAmplifier", this.getAttackAmplifier());
        tag.putString("currentCombo", this.getComboSeq().toString());
        tag.putInt("Damage", this.getDamage());
        tag.putInt("maxDamage", this.getMaxDamage());
        tag.putInt("proudSoul", this.getProudSoulCount());
        tag.putBoolean("isBroken", this.isBroken());

        // passive state
        tag.putBoolean("isSealed", this.isSealed());

        tag.putFloat("baseAttackModifier", this.getBaseAttackModifier());

        tag.putInt("killCount", this.getKillCount());
        tag.putInt("RepairCounter", this.getRefine());

        UUID bladeId = this.getUniqueId();
        tag.putUUID("BladeUniqueId", bladeId);

        // performance setting
        tag.putString("SpecialAttackType", Optional.ofNullable(this.getSlashArtsKey())
                .orElse(SlashArtsRegistry.JUDGEMENT_CUT.getId()).toString());
        tag.putBoolean("isDefaultBewitched", this.isDefaultBewitched());
        tag.putString("translationKey", this.getTranslationKey());

        // render info
        tag.putByte("StandbyRenderType", (byte) this.getCarryType().ordinal());
        tag.putInt("SummonedSwordColor", this.getColorCode());
        tag.putBoolean("SummonedSwordColorInverse", this.isEffectColorInverse());

        double[] adjustArray = new double[]{this.getAdjust().x, this.getAdjust().y, this.getAdjust().z};
        tag.put("adjustXYZ", NBTHelper.newDoubleNBTList(adjustArray));

        this.getTexture().ifPresent(loc -> tag.putString("TextureName", loc.toString()));
        this.getModel().ifPresent(loc -> tag.putString("ModelName", loc.toString()));

        tag.putString("ComboRoot",
                Optional.ofNullable(this.getComboRoot()).orElse(ComboStateRegistry.STANDBY.getId()).toString());

        if (this.getSpecialEffects() != null && !this.getSpecialEffects().isEmpty()) {
            ListTag seList = new ListTag();
            this.getSpecialEffects().forEach(se -> seList.add(StringTag.valueOf(se.toString())));
            tag.put("SpecialEffects", seList);
        }
    }
}