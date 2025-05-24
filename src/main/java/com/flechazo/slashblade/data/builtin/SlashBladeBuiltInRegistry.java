package com.flechazo.slashblade.data.builtin;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import org.apache.commons.compress.utils.Lists;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.client.renderer.CarryType;
import com.flechazo.slashblade.item.SwordType;
import com.flechazo.slashblade.registry.SlashArtsRegistry;
import com.flechazo.slashblade.registry.SpecialEffectsRegistry;
import com.flechazo.slashblade.registry.slashblade.EnchantmentDefinition;
import com.flechazo.slashblade.registry.slashblade.PropertiesDefinition;
import com.flechazo.slashblade.registry.slashblade.RenderDefinition;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class SlashBladeBuiltInRegistry {
    public static final ResourceKey<SlashBladeDefinition> YAMATO = register("yamato");
    public static final ResourceKey<SlashBladeDefinition> YAMATO_BROKEN = register("yamato_broken");

    public static final ResourceKey<SlashBladeDefinition> TUKUMO = register("yuzukitukumo");
    public static final ResourceKey<SlashBladeDefinition> MURAMASA = register("muramasa");
    public static final ResourceKey<SlashBladeDefinition> RUBY = register("ruby");
    public static final ResourceKey<SlashBladeDefinition> SANGE = register("sange");
    public static final ResourceKey<SlashBladeDefinition> FOX_BLACK = register("fox_black");
    public static final ResourceKey<SlashBladeDefinition> FOX_WHITE = register("fox_white");

    public static final ResourceKey<SlashBladeDefinition> RODAI_WOODEN = register("rodai_wooden");
    public static final ResourceKey<SlashBladeDefinition> RODAI_STONE = register("rodai_stone");
    public static final ResourceKey<SlashBladeDefinition> RODAI_IRON = register("rodai_iron");
    public static final ResourceKey<SlashBladeDefinition> RODAI_GOLDEN = register("rodai_golden");
    public static final ResourceKey<SlashBladeDefinition> RODAI_DIAMOND = register("rodai_diamond");
    public static final ResourceKey<SlashBladeDefinition> RODAI_NETHERITE = register("rodai_netherite");

    public static final ResourceKey<SlashBladeDefinition> TAGAYASAN = register("tagayasan");
    public static final ResourceKey<SlashBladeDefinition> AGITO = register("agito");
    public static final ResourceKey<SlashBladeDefinition> AGITO_RUST = register("agito_rust");
    public static final ResourceKey<SlashBladeDefinition> OROTIAGITO = register("orotiagito");
    public static final ResourceKey<SlashBladeDefinition> OROTIAGITO_SEALED = register("orotiagito_sealed");
    public static final ResourceKey<SlashBladeDefinition> OROTIAGITO_RUST = register("orotiagito_rust");

    public static final ResourceKey<SlashBladeDefinition> YASHA = register("yasha");
    public static final ResourceKey<SlashBladeDefinition> YASHA_TRUE = register("yasha_true");

    public static final ResourceKey<SlashBladeDefinition> SABIGATANA = register("sabigatana");
    public static final ResourceKey<SlashBladeDefinition> SABIGATANA_BROKEN = register("sabigatana_broken");
    public static final ResourceKey<SlashBladeDefinition> DOUTANUKI = register("doutanuki");
    public static final ResourceKey<SlashBladeDefinition> KOSEKI = register("koseki");

    public static void registerAll(BootstapContext<SlashBladeDefinition> bootstrap) {
    	bootstrap.register(KOSEKI,
                new SlashBladeDefinition(SlashBladeRefabriced.prefix("koseki"),
                        RenderDefinition.Builder
                                .newInstance()
                                .effectColor(0x303030)
                                .textureName(SlashBladeRefabriced.prefix("model/named/dios/koseki.png"))
                                .modelName(SlashBladeRefabriced.prefix("model/named/dios/dios.obj"))
                                .standbyRenderType(CarryType.NINJA)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                        .baseAttackModifier(5.0F).maxDamage(70)
                        
                        .defaultSwordType(List.of(SwordType.BEWITCHED))
                        .slashArtsType(SlashArtsRegistry.DRIVE_VERTICAL.getId())
                        .addSpecialEffect(SpecialEffectsRegistry.WITHER_EDGE.getId())
                        .build(),
                        List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.POWER_ARROWS), 2))));
    	
        bootstrap.register(SABIGATANA,
                new SlashBladeDefinition(SlashBladeRefabriced.prefix("sabigatana"),
                        RenderDefinition.Builder
                                .newInstance().textureName(SlashBladeRefabriced.prefix("model/named/muramasa/sabigatana.png"))
                                .modelName(SlashBladeRefabriced.prefix("model/named/muramasa/muramasa.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(3.0F).maxDamage(40).build(),
                        Lists.newArrayList()));

        bootstrap.register(SABIGATANA_BROKEN, new SlashBladeDefinition(SlashBladeRefabriced.prefix("sabigatana"),
                RenderDefinition.Builder
                        .newInstance().textureName(SlashBladeRefabriced.prefix("model/named/muramasa/sabigatana.png"))
                        .modelName(SlashBladeRefabriced.prefix("model/named/muramasa/muramasa.obj"))
                        .standbyRenderType(CarryType.PSO2)
                        .build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(3.0F).maxDamage(40)
                        .defaultSwordType(List.of(SwordType.BROKEN, SwordType.SEALED)).build(),
                Lists.newArrayList()));

        bootstrap.register(DOUTANUKI,
                new SlashBladeDefinition(SlashBladeRefabriced.prefix("doutanuki"),
                        RenderDefinition.Builder.newInstance()
                                .textureName(SlashBladeRefabriced.prefix("model/named/muramasa/doutanuki.png"))
                                .modelName(SlashBladeRefabriced.prefix("model/named/muramasa/muramasa.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(5.0F).maxDamage(60)
                                .slashArtsType(SlashArtsRegistry.CIRCLE_SLASH.getId())
                                .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                        Lists.newArrayList()));

        bootstrap.register(TAGAYASAN,
                new SlashBladeDefinition(SlashBladeRefabriced.prefix("tagayasan"),
                        RenderDefinition.Builder.newInstance()
                                .textureName(SlashBladeRefabriced.prefix("model/named/tagayasan.png"))
                                .standbyRenderType(CarryType.KATANA)
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(5.0F).maxDamage(70)
                                .slashArtsType(SlashArtsRegistry.DRIVE_VERTICAL.getId())
                                .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                        List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.SMITE), 3),
                                new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 3))));

        bootstrap.register(YASHA, new SlashBladeDefinition(SlashBladeRefabriced.prefix("yasha"),
                RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/named/yasha/yasha.png"))
                        .modelName(SlashBladeRefabriced.prefix("model/named/yasha/yasha.obj"))
                        .standbyRenderType(CarryType.PSO2)
                        .build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(6.0F)
                        .slashArtsType(SlashArtsRegistry.SAKURA_END.getId()).maxDamage(70).build(),
                Lists.newArrayList()));

        bootstrap.register(YASHA_TRUE, new SlashBladeDefinition(SlashBladeRefabriced.prefix("yasha_true"),
                RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/named/yasha/yasha.png"))
                        .modelName(SlashBladeRefabriced.prefix("model/named/yasha/yasha_true.obj"))
                        .standbyRenderType(CarryType.PSO2)
                        .build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(6.0F)
                        .slashArtsType(SlashArtsRegistry.CIRCLE_SLASH.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).maxDamage(70).build(),
                Lists.newArrayList()));

        bootstrap.register(AGITO_RUST, new SlashBladeDefinition(SlashBladeRefabriced.prefix("agito_rust"),
                RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/named/agito_rust.png"))
                        .modelName(SlashBladeRefabriced.prefix("model/named/agito.obj"))
                        .standbyRenderType(CarryType.PSO2)
                        .build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(3.0F).maxDamage(60)
                        .defaultSwordType(List.of(SwordType.SEALED)).build(),
                Lists.newArrayList()));

        bootstrap.register(AGITO, new SlashBladeDefinition(SlashBladeRefabriced.prefix("agito"),
                RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/named/agito_false.png"))
                        .modelName(SlashBladeRefabriced.prefix("model/named/agito.obj"))
                        .standbyRenderType(CarryType.PSO2)
                        .build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(5.0F)
                        .slashArtsType(SlashArtsRegistry.WAVE_EDGE.getId()).maxDamage(60).build(),
                Lists.newArrayList()));

        bootstrap.register(OROTIAGITO_RUST, new SlashBladeDefinition(SlashBladeRefabriced.prefix("orotiagito_rust"),
                RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/named/agito_rust_true.png"))
                        .modelName(SlashBladeRefabriced.prefix("model/named/agito.obj"))
                        .standbyRenderType(CarryType.PSO2)
                        .build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(3.0F).maxDamage(60)
                        .defaultSwordType(List.of(SwordType.SEALED)).build(),
                Lists.newArrayList()));

        bootstrap.register(OROTIAGITO_SEALED, new SlashBladeDefinition(SlashBladeRefabriced.prefix("orotiagito_sealed"),
                RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/named/agito_true.png"))
                        .modelName(SlashBladeRefabriced.prefix("model/named/agito.obj"))
                        .standbyRenderType(CarryType.PSO2)
                        .build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(5.0F)
                        .slashArtsType(SlashArtsRegistry.WAVE_EDGE.getId()).maxDamage(60).build(),
                Lists.newArrayList()));

        bootstrap.register(OROTIAGITO, new SlashBladeDefinition(SlashBladeRefabriced.prefix("orotiagito"),
                RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/named/orotiagito.png"))
                        .modelName(SlashBladeRefabriced.prefix("model/named/agito.obj"))
                        .standbyRenderType(CarryType.PSO2)
                        .build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(7.0F)
                        .slashArtsType(SlashArtsRegistry.WAVE_EDGE.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).maxDamage(60).build(),
                Lists.newArrayList()));

        bootstrap.register(RODAI_WOODEN,
                new SlashBladeDefinition(SlashBladeRefabriced.prefix("rodai_wooden"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/rodai_wooden.png"))
                        .standbyRenderType(CarryType.DEFAULT)
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(2.0F).maxDamage(60).build(),
                        Lists.newArrayList()));

        bootstrap.register(RODAI_STONE,
                new SlashBladeDefinition(SlashBladeRefabriced.prefix("rodai_stone"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/rodai_stone.png"))
                        .standbyRenderType(CarryType.DEFAULT)
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(3.0F).maxDamage(132).build(),
                        Lists.newArrayList()));

        bootstrap.register(RODAI_IRON,
                new SlashBladeDefinition(SlashBladeRefabriced.prefix("rodai_iron"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/rodai_iron.png"))
                        .standbyRenderType(CarryType.DEFAULT)
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(4.0F).maxDamage(250).build(),
                        Lists.newArrayList()));

        bootstrap.register(RODAI_GOLDEN,
                new SlashBladeDefinition(SlashBladeRefabriced.prefix("rodai_golden"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/rodai_golden.png"))
                        .standbyRenderType(CarryType.DEFAULT)
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(2.0F).maxDamage(33).build(),
                        Lists.newArrayList()));

        bootstrap.register(RODAI_DIAMOND,
                new SlashBladeDefinition(SlashBladeRefabriced.prefix("rodai_diamond"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/rodai_diamond.png"))
                        .standbyRenderType(CarryType.DEFAULT)
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(7.0F).maxDamage(1561).build(),
                        Lists.newArrayList()));

        bootstrap.register(RODAI_NETHERITE,
                new SlashBladeDefinition(SlashBladeRefabriced.prefix("rodai_netherite"),
                        RenderDefinition.Builder.newInstance()
                                .textureName(SlashBladeRefabriced.prefix("model/rodai_netherite.png"))
                                .standbyRenderType(CarryType.DEFAULT).build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(8.0F).maxDamage(2031).build(),
                        Lists.newArrayList()));

        bootstrap.register(RUBY,
                new SlashBladeDefinition(SlashBladeRefabriced.prefix("ruby"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/ruby.png"))
                        .standbyRenderType(CarryType.DEFAULT).build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(5.0F).maxDamage(45).build(),
                        Lists.newArrayList()));

        bootstrap.register(FOX_BLACK, new SlashBladeDefinition(SlashBladeRefabriced.prefix("fox_black"),
                RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/named/sange/black.png"))
                        .modelName(SlashBladeRefabriced.prefix("model/named/sange/sange.obj"))
                        .standbyRenderType(CarryType.PSO2)
                        .build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(5.0F).maxDamage(70)
                        .slashArtsType(SlashArtsRegistry.PIERCING.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.SMITE), 4),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.KNOCKBACK), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_ASPECT), 2))));

        bootstrap.register(FOX_WHITE, new SlashBladeDefinition(SlashBladeRefabriced.prefix("fox_white"),
                RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/named/sange/white.png"))
                        .modelName(SlashBladeRefabriced.prefix("model/named/sange/sange.obj"))
                        .standbyRenderType(CarryType.PSO2)
                        .build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(5.0F).maxDamage(70)
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.KNOCKBACK), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.BANE_OF_ARTHROPODS), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 3),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_ASPECT), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.MOB_LOOTING), 3))));

        bootstrap.register(YAMATO,
                new SlashBladeDefinition(SlashBladeRefabriced.prefix("yamato"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/named/yamato.png"))
                                .modelName(SlashBladeRefabriced.prefix("model/named/yamato.obj"))
                                .standbyRenderType(CarryType.DEFAULT)
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(7.0F)
                                .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                        List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.SOUL_SPEED), 2),
                                new EnchantmentDefinition(getEnchantmentID(Enchantments.POWER_ARROWS), 5),
                                new EnchantmentDefinition(getEnchantmentID(Enchantments.FALL_PROTECTION), 4))));

        bootstrap.register(YAMATO_BROKEN,
                new SlashBladeDefinition(SlashBladeRefabriced.prefix("yamato"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/named/yamato.png"))
                                .modelName(SlashBladeRefabriced.prefix("model/named/yamato.obj"))
                                .standbyRenderType(CarryType.DEFAULT)
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(7.0F)
                                .defaultSwordType(List.of(SwordType.BROKEN, SwordType.SEALED)).build(),
                        List.of()));

        bootstrap.register(TUKUMO, new SlashBladeDefinition(SlashBladeRefabriced.prefix("yuzukitukumo"),
                RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/named/a_tukumo.png"))
                        .modelName(SlashBladeRefabriced.prefix("model/named/agito.obj"))
                        .standbyRenderType(CarryType.PSO2)
                        .build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(6.0F)
                        .slashArtsType(SlashArtsRegistry.DRIVE_HORIZONTAL.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_ASPECT), 1),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.SHARPNESS), 4),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 3))));

        bootstrap.register(MURAMASA,
                new SlashBladeDefinition(SlashBladeRefabriced.prefix("muramasa"),
                        RenderDefinition.Builder
                                .newInstance().textureName(SlashBladeRefabriced.prefix("model/named/muramasa/muramasa.png"))
                                .modelName(SlashBladeRefabriced.prefix("model/named/muramasa/muramasa.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(7.0F).maxDamage(50)
                        .slashArtsType(SlashArtsRegistry.DRIVE_VERTICAL.getId()).build(),
                        Lists.newArrayList()));

        bootstrap.register(SANGE, new SlashBladeDefinition(SlashBladeRefabriced.prefix("sange"),
                RenderDefinition.Builder.newInstance().textureName(SlashBladeRefabriced.prefix("model/named/sange/sange.png"))
                        .modelName(SlashBladeRefabriced.prefix("model/named/sange/sange.obj"))
                        .standbyRenderType(CarryType.PSO2)
                        .build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(6.0F).maxDamage(70)
                        .slashArtsType(SlashArtsRegistry.VOID_SLASH.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                Lists.newArrayList()));

    }

    private static ResourceLocation getEnchantmentID(Enchantment enchantment) {
        return BuiltInRegistries.ENCHANTMENT.getKey(enchantment);
    }

    private static ResourceKey<SlashBladeDefinition> register(String id) {
        return ResourceKey.create(SlashBladeDefinition.REGISTRY_KEY,
                SlashBladeRefabriced.prefix(id));
    }
}
