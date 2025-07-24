package com.flechazo.slashblade.event;

/**
 * 此类已被弃用
 * <p>
 * 请参阅 {@link com.flechazo.slashblade.mixin.event.AnvilMenuMixin} 类以获得替代实现。
 *
 * @deprecated 使用 {@link com.flechazo.slashblade.mixin.event.AnvilMenuMixin} 替代。
 */
@Deprecated
public class RefineHandler {
//    private static final class SingletonHolder {
//        private static final RefineHandler instance = new RefineHandler();
//    }
//
//    public static RefineHandler getInstance() {
//        return SingletonHolder.instance;
//    }
//
//    private RefineHandler() {
//    }
//
//    public void register() {
//        // 当 anvil 输出结果刷新时触发
//        AnvilScreenHandlerEvents.UPDATE_RESULT.register(RefineHandler::onAnvilUpdate);
//
//        // 当玩家从铁砧取走结果时触发
//        AnvilScreenHandlerEvents.ON_TAKE_RESULT.register(RefineHandler::onAnvilTake);
//    }
//
//    @SubscribeEvent(priority = EventPriority.LOW)
//    public void onAnvilUpdateEvent(AnvilUpdateEvent event) {
//        if (!event.getOutput().isEmpty())
//            return;
//
//        ItemStack base = event.getLeft();
//        ItemStack material = event.getRight();
//
//        if (base.isEmpty())
//            return;
//        if (!(base.getCapability(ItemSlashBlade.BLADESTATE).isPresent()))
//            return;
//
//        if (material.isEmpty())
//            return;
//
//        boolean isRepairable = base.getItem().isValidRepairItem(base, material);
//        if (!isRepairable)
//            return;
//
//        int level = material.getEnchantmentValue();
//
//        if (level < 0)
//            return;
//
//        ItemStack result = base.copy();
//
//        int refineLimit = Math.max(10, level);
//
//        int cost = 0;
//        int levelCostBase = SlashBladeConfig.REFINE_LEVEL_COST.get();
//        int costResult = levelCostBase * cost;
//        while (cost < material.getCount()) {
//            cost++;
//            costResult = levelCostBase * cost;
//
//            result.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> {
//                s.setProudSoulCount(s.getProudSoulCount() + Math.min(5000, level * 10));
//                if (s.getRefine() < refineLimit) {
//                    s.setRefine(s.getRefine() + 1);
//                    if(s.getRefine() < 200)
//                    	s.setMaxDamage(s.getMaxDamage() + 1);
//                }
//
//                result.setDamageValue(result.getDamageValue() - Math.max(1, level / 2));
//                result.getOrCreateTag().put("bladeState", s.serializeNBT());
//            });
//
//            boolean refineable = !event.getPlayer().getAbilities().instabuild && event.getPlayer().experienceLevel <= costResult;
//			if (refineable)
//                break;
//        }
//
//        event.setMaterialCost(cost);
//		event.setCost(costResult);
//        event.setOutput(result);
//    }
//
//    static private final ResourceLocation REFINE = new ResourceLocation(SlashBladeRefabriced.MODID, "tips/refine");
//
//    @SubscribeEvent
//    public void onAnvilRepairEvent(AnvilRepairEvent event) {
//
//        if (!(event.getEntity() instanceof ServerPlayer))
//            return;
//
//        ItemStack material = event.getRight();// .getIngredientInput();
//        ItemStack base = event.getLeft();// .getItemInput();
//        ItemStack output = event.getOutput();
//
//        if (base.isEmpty())
//            return;
//        if (!(base.getItem() instanceof ItemSlashBlade))
//            return;
//        if (material.isEmpty())
//            return;
//
//        boolean isRepairable = base.getItem().isValidRepairItem(base, material);
//
//        if (!isRepairable)
//            return;
//
//        int before = base.getCapability(ItemSlashBlade.BLADESTATE).map(s -> s.getRefine()).orElse(0);
//        int after = output.getCapability(ItemSlashBlade.BLADESTATE).map(s -> s.getRefine()).orElse(0);
//
//        if (before < after)
//            AdvancementHelper.grantCriterion((ServerPlayer) event.getEntity(), REFINE);
//
//    }

}
