package com.flechazo.slashblade.compat.jei;

import com.flechazo.slashblade.registry.SlashBladeRegister;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import com.flechazo.slashblade.SlashBladeRefabriced;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEICompat implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return SlashBladeRefabriced.prefix(SlashBladeRefabriced.MODID);
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(SlashBladeRegister.SLASHBLADE,
				(stack, context) -> stack.getOrCreateTagElement("bladeState").getString("translationKey"));
	}

}
