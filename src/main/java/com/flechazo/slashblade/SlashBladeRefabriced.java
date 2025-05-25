package com.flechazo.slashblade;

import com.flechazo.slashblade.ability.*;
import com.flechazo.slashblade.event.*;
import com.flechazo.slashblade.client.renderer.model.BladeModelManager;
import com.flechazo.slashblade.network.NetworkManager;
import com.flechazo.slashblade.recipe.RecipeSerializerRegistry;
import com.flechazo.slashblade.registry.ComboStateRegistry;
import com.flechazo.slashblade.registry.ModAttributes;
import com.flechazo.slashblade.registry.SlashArtsRegistry;
import com.flechazo.slashblade.registry.SpecialEffectsRegistry;
import com.flechazo.slashblade.registry.combo.ComboCommands;
import com.flechazo.slashblade.registry.slashblade.SlashBladeDefinition;
import com.flechazo.slashblade.util.TargetSelector;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SlashBladeRefabriced implements ModInitializer {
    public static final String MODID = "slashblade";

    public static ResourceLocation prefix (String path) {
        return new ResourceLocation(SlashBladeRefabriced.MODID, path);
    }

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();


    @Override
    public void onInitialize () {
        SlashBladeConfig.init();
        eventHandlerInit();

        ModAttributes.addAttribute();
        NetworkManager.registerServerReceivers();

        ComboStateRegistry.init();
        SlashArtsRegistry.init();
        SlashBladeCreativeGroup.init();
        RecipeSerializerRegistry.register();
        SpecialEffectsRegistry.init();

    }

    private void eventHandlerInit() {
        LockOnManager.getInstance().register();
        Guard.getInstance().register();
        StunManager.getInstance().register();
        KillCounter.getInstance().register();
        RankPointHandler.getInstance().register();
        AllowFlightOverrwrite.getInstance().register();
        BladeMotionEventBroadcaster.getInstance().register();
        TargetSelector.getInstance().register();
        SummonedSwordArts.getInstance().register();
        SlayerStyleArts.getInstance().register();
        Untouchable.getInstance().register();
        EnemyStep.getInstance().register();
        KickJump.getInstance().register();
        SuperSlashArts.getInstance().register();
        ComboCommands.initDefaultStandByCommands();
    }






        /**
         * /scoreboard objectives add stat minecraft.custom:slashblade.sword_summoned
         * /scoreboard objectives setdisplay sidebar stat
         */


    public static Registry<SlashBladeDefinition> getSlashBladeDefinitionRegistry (Level level) {
        if (level.isClientSide())
            return BladeModelManager.getClientSlashBladeRegistry();
        return level.registryAccess().registryOrThrow(SlashBladeDefinition.REGISTRY_KEY);
    }

    public static HolderLookup.RegistryLookup<SlashBladeDefinition> getSlashBladeDefinitionRegistry (HolderLookup.Provider access) {
        return access.lookupOrThrow(SlashBladeDefinition.REGISTRY_KEY);
    }
    }



