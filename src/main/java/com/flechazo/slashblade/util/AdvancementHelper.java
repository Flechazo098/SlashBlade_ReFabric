package com.flechazo.slashblade.util;

import com.flechazo.slashblade.SlashBladeRefabriced;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class AdvancementHelper {

    static public final ResourceLocation ADVANCEMENT_COMBO_A = SlashBladeRefabriced.prefix("arts/combo_a");
    static public final ResourceLocation ADVANCEMENT_COMBO_A_EX = SlashBladeRefabriced.prefix("arts/combo_a_ex");
    static public final ResourceLocation ADVANCEMENT_COMBO_B = SlashBladeRefabriced.prefix("arts/combo_b");
    static public final ResourceLocation ADVANCEMENT_COMBO_B_MAX = SlashBladeRefabriced.prefix("arts/combo_b_max");
    static public final ResourceLocation ADVANCEMENT_COMBO_C = SlashBladeRefabriced.prefix("arts/combo_c");
    static public final ResourceLocation ADVANCEMENT_AERIAL_A = SlashBladeRefabriced.prefix("arts/aerial_a");
    static public final ResourceLocation ADVANCEMENT_AERIAL_B = SlashBladeRefabriced.prefix("arts/aerial_b");
    static public final ResourceLocation ADVANCEMENT_UPPERSLASH = SlashBladeRefabriced.prefix("arts/upperslash");
    static public final ResourceLocation ADVANCEMENT_UPPERSLASH_JUMP = SlashBladeRefabriced.prefix("arts/upperslash_jump");
    static public final ResourceLocation ADVANCEMENT_AERIAL_CLEAVE = SlashBladeRefabriced.prefix("arts/aerial_cleave");
    static public final ResourceLocation ADVANCEMENT_RISING_STAR = SlashBladeRefabriced.prefix("arts/rising_star");
    static public final ResourceLocation ADVANCEMENT_RAPID_SLASH = SlashBladeRefabriced.prefix("arts/rapid_slash");
    static public final ResourceLocation ADVANCEMENT_JUDGEMENT_CUT = SlashBladeRefabriced.prefix("arts/judgement_cut");
    static public final ResourceLocation ADVANCEMENT_JUDGEMENT_CUT_JUST = SlashBladeRefabriced.prefix("arts/judgement_cut_just");
    static public final ResourceLocation ADVANCEMENT_QUICK_CHARGE = SlashBladeRefabriced.prefix("arts/quick_charge");

    public static void grantCriterion(LivingEntity entity, ResourceLocation resourcelocation) {
        if (entity instanceof ServerPlayer)
            grantCriterion((ServerPlayer) entity, resourcelocation);
    }

    public static void grantCriterion(ServerPlayer player, ResourceLocation resourcelocation) {
        Advancement adv = player.getServer().getAdvancements().getAdvancement(resourcelocation);
        if (adv == null)
            return;

        AdvancementProgress advancementprogress = player.getAdvancements().getOrStartProgress(adv);
        if (advancementprogress.isDone())
            return;

        for (String s : advancementprogress.getRemainingCriteria()) {
            player.getAdvancements().award(adv, s);
        }
    }

    static final ResourceLocation EXEFFECT_ENCHANTMENT = SlashBladeRefabriced.prefix("enchantment/");

    static public void grantedIf(Enchantment enchantment, LivingEntity owner) {
        int level = EnchantmentHelper.getEnchantmentLevel(enchantment, owner);
        if (0 < level) {
            grantCriterion(owner, EXEFFECT_ENCHANTMENT.withSuffix("root"));
            grantCriterion(owner,
                    EXEFFECT_ENCHANTMENT.withSuffix(BuiltInRegistries.ENCHANTMENT.getKey(enchantment).getPath()));
        }
    }
}
