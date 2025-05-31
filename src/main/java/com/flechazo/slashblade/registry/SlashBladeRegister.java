package com.flechazo.slashblade.registry;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.entity.BladeItemEntity;
import com.flechazo.slashblade.item.BladeStandItem;
import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.item.ItemSlashBladeDetune;
import com.flechazo.slashblade.item.ItemTierSlashBlade;
import com.flechazo.slashblade.registry.specialeffects.SpecialEffect;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import static com.flechazo.slashblade.SlashBladeConfig.getTrapezohedronMaxRefine;

public class SlashBladeRegister {
    public static final ResourceLocation WOOD_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "slashblade_wood");
    public static final ResourceLocation BAMBOO_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "slashblade_bamboo");
    public static final ResourceLocation SILVER_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "slashblade_silverbamboo");
    public static final ResourceLocation WHITE_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "slashblade_white");
    public static final ResourceLocation SLASHBLADE_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "slashblade");

    private static final ResourceLocation PROUDSOUL_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "proudsoul");
    private static final ResourceLocation PROUDSOUL_INGOT_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "proudsoul_ingot");
    private static final ResourceLocation PROUDSOUL_TINY_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "proudsoul_tiny");
    private static final ResourceLocation PROUDSOUL_SPHERE_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "proudsoul_sphere");
    private static final ResourceLocation PROUDSOUL_CRYSTAL_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "proudsoul_crystal");
    private static final ResourceLocation PROUDSOUL_TRAP_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "proudsoul_trapezohedron");

    private static final ResourceLocation BLADESTAND_1_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "bladestand_1");
    private static final ResourceLocation BLADESTAND_2_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "bladestand_2");
    private static final ResourceLocation BLADESTAND_V_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "bladestand_v");
    private static final ResourceLocation BLADESTAND_S_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "bladestand_s");
    private static final ResourceLocation BLADESTAND_1W_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "bladestand_1w");
    private static final ResourceLocation BLADESTAND_2W_ID = new ResourceLocation(SlashBladeRefabriced.MODID, "bladestand_2w");


    public static ItemSlashBladeDetune WOOD;
    public static ItemSlashBladeDetune BAMBOO;
    public static ItemSlashBladeDetune SILVER;
    public static ItemSlashBladeDetune WHITE;
    public static ItemSlashBlade SLASHBLADE;
    public static Item PROUDSOUL;
    public static Item PROUDSOUL_INGOT;
    public static Item PROUDSOUL_TINY;
    public static Item PROUDSOUL_SPHERE;
    public static Item PROUDSOUL_CRYSTAL;
    public static Item PROUDSOUL_TRAP;
    public static BladeStandItem BLADESTAND_1;
    public static BladeStandItem BLADESTAND_2;
    public static BladeStandItem BLADESTAND_V;
    public static BladeStandItem BLADESTAND_S;
    public static BladeStandItem BLADESTAND_1W;
    public static BladeStandItem BLADESTAND_2W;

    public static void registerAll () {
        WOOD = new ItemSlashBladeDetune(new ItemTierSlashBlade(60, 2F), 2, - 2.4F, new FabricItemSettings())
                .setDestructable().setTexture(new ResourceLocation(SlashBladeRefabriced.MODID, "model/wood.png"));
        Registry.register(BuiltInRegistries.ITEM, WOOD_ID, WOOD);

        BAMBOO = new ItemSlashBladeDetune(new ItemTierSlashBlade(70, 3F), 3, - 2.4F, new FabricItemSettings())
                .setDestructable().setTexture(new ResourceLocation(SlashBladeRefabriced.MODID, "model/bamboo.png"));
        Registry.register(BuiltInRegistries.ITEM, BAMBOO_ID, BAMBOO);

        SILVER = new ItemSlashBladeDetune(new ItemTierSlashBlade(40, 3F), 3, - 2.4F, new FabricItemSettings())
                .setDestructable().setTexture(new ResourceLocation(SlashBladeRefabriced.MODID, "model/silverbamboo.png"));
        Registry.register(BuiltInRegistries.ITEM, SILVER_ID, SILVER);

        WHITE = new ItemSlashBladeDetune(new ItemTierSlashBlade(70, 4F), 4, - 2.4F, new FabricItemSettings())
                .setDestructable().setTexture(new ResourceLocation(SlashBladeRefabriced.MODID, "model/white.png"));
        Registry.register(BuiltInRegistries.ITEM, WHITE_ID, WHITE);

        SLASHBLADE = new ItemSlashBlade(new ItemTierSlashBlade(40, 4F), 4, - 2.4F, new FabricItemSettings());
        Registry.register(BuiltInRegistries.ITEM, SLASHBLADE_ID, SLASHBLADE);

        PROUDSOUL = new Item(new FabricItemSettings()) {
            @Override
            public boolean onEntityItemUpdate (ItemStack stack, ItemEntity entity) {
                if (entity instanceof BladeItemEntity) return false;
                CompoundTag tag = stack.getOrCreateTag();
                tag.putInt("Health", 50);
                entity.load(tag);
                if (entity.isCurrentlyGlowing()) {
                    entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.8, 0, 0.8).add(0, 0.04, 0));
                } else if (entity.isOnFire()) {
                    entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.8, 0.5, 0.8).add(0, 0.04, 0));
                }
                return false;
            }

            @Override
            public boolean isFoil (ItemStack stack) {
                return true;
            }

            @Override
            public int getEnchantmentValue () {
                return 50;
            }
        };
        Registry.register(BuiltInRegistries.ITEM, PROUDSOUL_ID, PROUDSOUL);

        PROUDSOUL_INGOT = new Item(new FabricItemSettings()) {
            @Override
            public boolean isFoil (ItemStack stack) {
                return true;
            }

            @Override
            public int getEnchantmentValue () {
                return 100;
            }
        };
        Registry.register(BuiltInRegistries.ITEM, PROUDSOUL_INGOT_ID, PROUDSOUL_INGOT);

        PROUDSOUL_TINY = new Item(new FabricItemSettings()) {
            @Override
            public boolean isFoil (ItemStack stack) {
                return true;
            }

            @Override
            public int getEnchantmentValue () {
                return 10;
            }
        };
        Registry.register(BuiltInRegistries.ITEM, PROUDSOUL_TINY_ID, PROUDSOUL_TINY);

        PROUDSOUL_SPHERE = new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON)) {
            @Override
            public boolean isFoil (ItemStack stack) {
                return true;
            }

            @Override
            public int getEnchantmentValue () {
                return 150;
            }

            @Override
            public void appendHoverText (ItemStack stack, @Nullable Level lvl, List<Component> tips, TooltipFlag flag) {
                if (stack.hasTag() && stack.getTag().contains("SpecialAttackType")) {
                    ResourceLocation sa = new ResourceLocation(stack.getTag().getString("SpecialAttackType"));
                    var entry = SlashArtsRegistry.REGISTRY.get(sa);
                    if (entry != null && entry != SlashArtsRegistry.NONE) {
                        tips.add(Component.translatable("slashblade.tooltip.slash_art", entry.getDescription())
                                .withStyle(ChatFormatting.GRAY));
                    }
                }
                super.appendHoverText(stack, lvl, tips, flag);
            }
        };
        Registry.register(BuiltInRegistries.ITEM, PROUDSOUL_SPHERE_ID, PROUDSOUL_SPHERE);

        PROUDSOUL_CRYSTAL = new Item(new FabricItemSettings().rarity(Rarity.RARE)) {
            @Override
            public boolean isFoil (ItemStack stack) {
                return true;
            }

            @Override
            public int getEnchantmentValue () {
                return 200;
            }

            @Override
            public void appendHoverText (ItemStack stack, @Nullable Level lvl, List<Component> tips, TooltipFlag flag) {
                if (stack.hasTag() && stack.getTag().contains("SpecialEffectType")) {
                    ResourceLocation se = new ResourceLocation(stack.getTag().getString("SpecialEffectType"));
                    var effect = SpecialEffectsRegistry.REGISTRY.get(se);
                    if (effect != null) {
                        var mc = Minecraft.getInstance();
                        int lvlReq = effect.getRequestLevel();
                        boolean ok = SpecialEffect.isEffective(se, mc.player.experienceLevel);
                        tips.add(Component.translatable(
                                "slashblade.tooltip.special_effect",
                                effect.getDescription(),
                                Component.literal(String.valueOf(lvlReq))
                                        .withStyle(ok ? ChatFormatting.RED : ChatFormatting.DARK_GRAY)
                        ).withStyle(ChatFormatting.GRAY));
                    }
                }
                super.appendHoverText(stack, lvl, tips, flag);
            }
        };
        Registry.register(BuiltInRegistries.ITEM, PROUDSOUL_CRYSTAL_ID, PROUDSOUL_CRYSTAL);

        PROUDSOUL_TRAP = new Item(new FabricItemSettings().rarity(Rarity.EPIC)) {
            @Override
            public boolean isFoil (ItemStack stack) {
                return true;
            }

            @Override
            public int getEnchantmentValue () {
                return getTrapezohedronMaxRefine();
            }
        };
        Registry.register(BuiltInRegistries.ITEM, PROUDSOUL_TRAP_ID, PROUDSOUL_TRAP);

        BLADESTAND_1 = new BladeStandItem(new FabricItemSettings().rarity(Rarity.COMMON));
        Registry.register(BuiltInRegistries.ITEM, BLADESTAND_1_ID, BLADESTAND_1);

        BLADESTAND_2 = new BladeStandItem(new FabricItemSettings().rarity(Rarity.COMMON));
        Registry.register(BuiltInRegistries.ITEM, BLADESTAND_2_ID, BLADESTAND_2);

        BLADESTAND_V = new BladeStandItem(new FabricItemSettings().rarity(Rarity.COMMON));
        Registry.register(BuiltInRegistries.ITEM, BLADESTAND_V_ID, BLADESTAND_V);

        BLADESTAND_S = new BladeStandItem(new FabricItemSettings().rarity(Rarity.COMMON));
        Registry.register(BuiltInRegistries.ITEM, BLADESTAND_S_ID, BLADESTAND_S);

        BLADESTAND_1W = new BladeStandItem(new FabricItemSettings().rarity(Rarity.COMMON), true);
        Registry.register(BuiltInRegistries.ITEM, BLADESTAND_1W_ID, BLADESTAND_1W);

        BLADESTAND_2W = new BladeStandItem(new FabricItemSettings().rarity(Rarity.COMMON), true);
        Registry.register(BuiltInRegistries.ITEM, BLADESTAND_2W_ID, BLADESTAND_2W);
    }
}
