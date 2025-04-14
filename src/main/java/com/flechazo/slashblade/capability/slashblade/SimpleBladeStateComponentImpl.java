package com.flechazo.slashblade.capability.slashblade;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class SimpleBladeStateComponentImpl extends BladeStateComponentImpl {

    private final ResourceLocation model;
    private final ResourceLocation texture;
    private final float attack;
    private int damage;

    public SimpleBladeStateComponentImpl(ItemStack blade, ResourceLocation model, ResourceLocation texture, float attack, int damage) {
        super(blade);
        this.model = model;
        this.attack = attack;
        this.damage = damage;
        this.texture = texture;
    }

    @Override
    public Optional<ResourceLocation> getModel() {
        return Optional.ofNullable(model);
    }

    @Override
    public void setModel(ResourceLocation model) {
        // 不允许修改
    }

    @Override
    public float getBaseAttackModifier() {
        return this.attack;
    }

    @Override
    public void setBaseAttackModifier(float baseAttackModifier) {
        // 不允许修改
    }

    @Override
    public ResourceLocation getSlashArtsKey() {
        return super.getSlashArtsKey();
    }

    @Override
    public void setSlashArtsKey(ResourceLocation key) {
        // 不允许修改
    }

    @Override
    public boolean isDefaultBewitched() {
        return false;
    }

    @Override
    public String getTranslationKey() {
        return super.getTranslationKey();
    }

    @Override
    public void setTranslationKey(String translationKey) {
        // 不允许修改
    }

    @Override
    public Optional<ResourceLocation> getTexture() {
        return Optional.ofNullable(texture);
    }

    @Override
    public void setTexture(ResourceLocation texture) {
        // 不允许修改
    }

    @Override
    public int getMaxDamage() {
        return this.damage;
    }

    @Override
    public void setMaxDamage(int damage) {
        this.damage = damage;
    }
}