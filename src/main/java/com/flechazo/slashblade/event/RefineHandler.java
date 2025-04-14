package com.flechazo.slashblade.event;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.SlashBladeConfig;
import com.flechazo.slashblade.item.ItemSlashBlade;
import com.flechazo.slashblade.util.AdvancementHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RefineHandler {
    private static final class SingletonHolder {
        private static final RefineHandler instance = new RefineHandler();
    }

    public static RefineHandler getInstance() {
        return SingletonHolder.instance;
    }

    private RefineHandler() {
    }

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onAnvilUpdateEvent(AnvilUpdateEvent event) {
        if (!event.getOutput().isEmpty())
            return;

        ItemStack base = event.getLeft();
        ItemStack material = event.getRight();

        if (base.isEmpty())
            return;
        if (!(base.getCapability(ItemSlashBlade.BLADESTATE).isPresent()))
            return;
        
        if (material.isEmpty())
            return;

        boolean isRepairable = base.getItem().isValidRepairItem(base, material);
        if (!isRepairable)
            return;

        int level = material.getEnchantmentValue();

        if (level < 0)
            return;

        ItemStack result = base.copy();

        int refineLimit = Math.max(10, level);

        int cost = 0;
        int levelCostBase = SlashBladeConfig.REFINE_LEVEL_COST.get();
        int costResult = levelCostBase * cost;
        while (cost < material.getCount()) {
            cost++;
            costResult = levelCostBase * cost;
            
            result.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> {
                s.setProudSoulCount(s.getProudSoulCount() + Math.min(5000, level * 10));
                if (s.getRefine() < refineLimit) {
                    s.setRefine(s.getRefine() + 1);
                    if(s.getRefine() < 200)
                    	s.setMaxDamage(s.getMaxDamage() + 1);
                }
                
                result.setDamageValue(result.getDamageValue() - Math.max(1, level / 2));
                result.getOrCreateTag().put("bladeState", s.serializeNBT());
            });

            boolean refineable = !event.getPlayer().getAbilities().instabuild && event.getPlayer().experienceLevel <= costResult;
			if (refineable)
                break;
        }

        event.setMaterialCost(cost);
		event.setCost(costResult);
        event.setOutput(result);
    }

    static private final ResourceLocation REFINE = new ResourceLocation(SlashBladeRefabriced.MODID, "tips/refine");

    @SubscribeEvent
    public void onAnvilRepairEvent(AnvilRepairEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer))
            return;

        ItemStack material = event.getRight();// .getIngredientInput();
        ItemStack base = event.getLeft();// .getItemInput();
        ItemStack output = event.getOutput();

        if (base.isEmpty())
            return;
        if (!(base.getItem() instanceof ItemSlashBlade))
            return;
        if (material.isEmpty())
            return;

        boolean isRepairable = base.getItem().isValidRepairItem(base, material);

        if (!isRepairable)
            return;

        int before = base.getCapability(ItemSlashBlade.BLADESTATE).map(s -> s.getRefine()).orElse(0);
        int after = output.getCapability(ItemSlashBlade.BLADESTATE).map(s -> s.getRefine()).orElse(0);

        if (before < after)
            AdvancementHelper.grantCriterion((ServerPlayer) event.getEntity(), REFINE);

    }

}
