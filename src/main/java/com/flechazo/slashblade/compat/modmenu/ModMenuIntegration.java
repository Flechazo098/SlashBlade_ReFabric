package com.flechazo.slashblade.compat.modmenu;

import com.flechazo.slashblade.SlashBladeConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory () {
        return parent -> AutoConfig.getConfigScreen(SlashBladeConfig.class, parent).get();
    }
}
