package com.flechazo.slashblade;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;

@Config(name = SlashBladeRefabriced.MODID)
public class SlashBladeConfig implements ConfigData {

    private static SlashBladeConfig INSTANCE;

    public static void init() {
        AutoConfig.register(SlashBladeConfig.class, JanksonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(SlashBladeConfig.class).getConfig();
    }

    public static SlashBladeConfig getInstance() {
        return INSTANCE;
    }

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Category("general")
    public General general = new General();

    public static class General {
        @Comment("Determining the spawn chance of sabigatana.")
        public double sabigatanaSpawnChance = 0.05D;

        @Comment("Determining the spawn chance of a broken sabigatana.")
        public double brokenSabigatanaSpawnChance = 0.15D;

        @Comment("Determines whether to make hunger effect repair slashblade.\nIf enable, if player has hunger effect, your slashblade in hotbar will be repaired, cost player's hunger.")
        public boolean hungerCanRepair = true;

        @Comment("Determines whether to enable slashblade's PVP.\nIf enable, player can attack player with SlashBlade.")
        public boolean pvpEnable = false;

        @Comment("Determines whether to enable slashblade's friendly fire.\nIf enable, player can attack friendly entity with SlashBlade.")
        public boolean friendlyEnable = false;

        @Comment("Determining the level cost for refine a slashblade.")
        @ConfigEntry.BoundedDiscrete(min = 1, max = Integer.MAX_VALUE)
        public int refineLevelCost = 1;

        @Comment("Determining the proud soul cost for single summon mirage blade.")
        @ConfigEntry.BoundedDiscrete(min = 1, max = Integer.MAX_VALUE)
        public int summonSwordCost = 2;

        @Comment("Determining the proud soul cost for summon blade arts.")
        @ConfigEntry.BoundedDiscrete(min = 1, max = Integer.MAX_VALUE)
        public int summonBladeArtCost = 20;

        @Comment("Determining the max proud soul count for single mobs kill.")
        @ConfigEntry.BoundedDiscrete(min = 1, max = Integer.MAX_VALUE)
        public int maxProudSoulGot = 100;

        @Comment("Determining the base exhaustion for slashblade's self-repair.")
        @ConfigEntry.BoundedDiscrete(min = 1L, max = Long.MAX_VALUE)
        public long bewitchedHungerExhaustion = 50L;

        @Comment("Blade Damage: Base Damage × Multiplier.[Default: 1.0D]")
        @ConfigEntry.BoundedDiscrete(min = 0, max = 1024)
        public double slashbladeDamageMultiplier = 1.0D;

        @Comment("S-Rank Refine Bonus: Each Refine × Multiplier'value Damage.[Default: 1.D]")
        @ConfigEntry.BoundedDiscrete(min = 0, max = 1024)
        public double refineDamageMultiplier = 1.0D;

        @Comment("The maximum number of refine of Trapezohedron.[Default: 2147483647(infinity)]")
        @ConfigEntry.BoundedDiscrete(min = 200, max = Integer.MAX_VALUE)
        public int trapezohedronMaxRefine = Integer.MAX_VALUE;

        @Comment("Example: 'minecraft:sharpness', That will prevent the enchantment from dropping the corresponding proudsoul tiny.")
        public List<String> nonDroppableEnchantments = new ArrayList<>();
    }

    public static double getSabigatanaSpawnChance() {
        return getInstance().general.sabigatanaSpawnChance;
    }

    public static double getBrokenSabigatanaSpawnChance() {
        return getInstance().general.brokenSabigatanaSpawnChance;
    }

    public static boolean isHungerCanRepair() {
        return getInstance().general.hungerCanRepair;
    }

    public static boolean isPvpEnable() {
        return getInstance().general.pvpEnable;
    }

    public static boolean isFriendlyEnable() {
        return getInstance().general.friendlyEnable;
    }

    public static int getRefineLevelCost() {
        return getInstance().general.refineLevelCost;
    }

    public static int getSummonSwordCost() {
        return getInstance().general.summonSwordCost;
    }

    public static int getSummonBladeArtCost() {
        return getInstance().general.summonBladeArtCost;
    }

    public static int getMaxProudSoulGot() {
        return getInstance().general.maxProudSoulGot;
    }

    public static long getBewitchedHungerExhaustion() {
        return getInstance().general.bewitchedHungerExhaustion;
    }

    public static double getSlashbladeDamageMultiplier() {
        return getInstance().general.slashbladeDamageMultiplier;
    }

    public static double getRefineDamageMultiplier() {
        return getInstance().general.refineDamageMultiplier;
    }

    public static int getTrapezohedronMaxRefine() {
        return getInstance().general.trapezohedronMaxRefine;
    }

    public static List<String> getNonDroppableEnchantments() {
        return getInstance().general.nonDroppableEnchantments;
    }
}