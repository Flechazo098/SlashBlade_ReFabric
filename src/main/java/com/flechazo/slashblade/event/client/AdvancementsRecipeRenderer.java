package com.flechazo.slashblade.event.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.recipebook.GhostRecipe;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;

import java.util.*;

import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;

@Environment(EnvType.CLIENT)
public class AdvancementsRecipeRenderer implements PlaceRecipe<Ingredient> {

    private static final Minecraft MCINSTANCE = Minecraft.getInstance();
    private static final ResourceLocation GUI_TEXTURE_CRAFTING_TABLE = new ResourceLocation(
            "textures/gui/container/crafting_table.png");
    private static final ResourceLocation GUI_TEXTURE_FURNACE = new ResourceLocation(
            "textures/gui/container/furnace.png");
    private static final ResourceLocation GUI_TEXTURE_BLAST_FURNACE = new ResourceLocation(
            "textures/gui/container/blast_furnace.png");
    private static final ResourceLocation GUI_TEXTURE_SMOKER = new ResourceLocation(
            "textures/gui/container/smoker.png");
    private static final ResourceLocation GUI_TEXTURE_SMITHING = new ResourceLocation(
            "textures/gui/container/smithing.png");
    private static final ResourceLocation GUI_TEXTURE_ANVIL = new ResourceLocation("textures/gui/container/anvil.png");

    private static final class SingletonHolder {
        private static final AdvancementsRecipeRenderer instance = new AdvancementsRecipeRenderer();
    }

    public static AdvancementsRecipeRenderer getInstance() {
        return SingletonHolder.instance;
    }

    private AdvancementsRecipeRenderer() {
    }

    public void register() {
        // 使用Fabric的事件系统注册屏幕事件
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof AdvancementsScreen) {
                ScreenEvents.afterRender(screen).register(this::onDrawScreenPost);
                ScreenMouseEvents.afterMouseClick(screen).register(this::onMouseClicked);
            }
        });
    }

    static public ItemStack target = null;

    static public GhostRecipe gr = new GhostRecipe();

    static public ResourceLocation currentRecipe = null;

    static final RecipeType<DummyAnvilRecipe> dummy_anvilType = RecipeType.register("sb_forgeing");

    static RecipeView currentView = null;
    static Map<RecipeType<?>, RecipeView> typeRecipeViewMap = createRecipeViewMap();

    static class DummyAnvilRecipe implements Recipe<Container> {
        protected SmithingTransformRecipe original;
        private final ItemStack result;
        private final ResourceLocation recipeId;

        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(2, Ingredient.EMPTY);

        public DummyAnvilRecipe(SmithingTransformRecipe recipe) {
            original = recipe;

            FriendlyByteBuf pb = new FriendlyByteBuf(Unpooled.buffer());
            SmithingTransformRecipe.Serializer ss = new SmithingTransformRecipe.Serializer();
            ss.toNetwork(pb, original);

            Ingredient.fromNetwork(pb);
            nonnulllist.set(0, Ingredient.fromNetwork(pb));
            nonnulllist.set(1, Ingredient.fromNetwork(pb));

            result = pb.readItem();

            this.recipeId = original.getId();
        }

        @Override
        public boolean matches(Container inv, Level worldIn) {
            return false;
        }

        @Override
        public ItemStack assemble(Container p_44001_, RegistryAccess p_267165_) {
            return result.copy();
        }

        @Override
        public boolean canCraftInDimensions(int width, int height) {
            return false;
        }

        @Override
        public ItemStack getResultItem(RegistryAccess p_267052_) {
            return result;
        }

        @Override
        public NonNullList<Ingredient> getIngredients() {
            return nonnulllist;
        }

        @Override
        public ItemStack getToastSymbol() {
            return new ItemStack(Blocks.ANVIL);
        }

        @Override
        public ResourceLocation getId() {
            return this.recipeId;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return null;
        }

        @Override
        public RecipeType<?> getType() {
            return dummy_anvilType;
        }
    }

    static class DummySmithingRecipe extends DummyAnvilRecipe {
        public DummySmithingRecipe(SmithingTransformRecipe recipe) {
            super(recipe);
        }

        @Override
        public ItemStack getToastSymbol() {
            return original.getToastSymbol();
        }

        @Override
        public RecipeType<?> getType() {
            return original.getType();
        }
    }

    static Recipe<?> overrideDummyRecipe(Recipe<?> original) {

        if (!(original instanceof SmithingTransformRecipe))
            return original;

        if (original.getId().getPath().startsWith("anvilcrafting")) {
            return new DummyAnvilRecipe((SmithingTransformRecipe) original);
        } else {
            return new DummySmithingRecipe((SmithingTransformRecipe) original);
        }
    }

    static public class RecipeView {
        final RecipeType<?> recipeType;
        final ResourceLocation background;
        List<Vec3i> slots = Lists.newArrayList();
        final boolean isWideOutputSlot;

        public RecipeView(RecipeType<?> recipeType, ResourceLocation background, List<Vec3i> slots) {
            this(recipeType, background, slots, true);
        }

        public RecipeView(RecipeType<?> recipeType, ResourceLocation background, List<Vec3i> slots,
                          boolean isWideOutputSlot) {
            this.recipeType = recipeType;
            this.background = background;
            this.slots = slots;
            this.isWideOutputSlot = isWideOutputSlot;
        }
    }

    static Map<RecipeType<?>, RecipeView> createRecipeViewMap() {
        Map<RecipeType<?>, RecipeView> map = Maps.newHashMap();

        {
            List<Vec3i> list = Lists.newArrayList();

            // output
            list.add(new Vec3i(124, 35, 0));

            // grid
            int SlotMargin = 18;
            int LeftMargin = 30;
            int TopMargin = 17;

            int RecipeGridX = 3;
            int RecipeGridY = 3;

            for (int i = 0; i < RecipeGridX; ++i) {
                for (int j = 0; j < RecipeGridY; ++j) {
                    list.add(new Vec3i(LeftMargin + j * SlotMargin, TopMargin + i * SlotMargin, 0));
                }
            }

            RecipeType<?> key = RecipeType.CRAFTING;
            map.put(key, new RecipeView(key, GUI_TEXTURE_CRAFTING_TABLE, list));
        }

        {
            List<Vec3i> list = Lists.newArrayList();

            // output
            list.add(new Vec3i(116, 35, 0));
            // input
            list.add(new Vec3i(56, 17, 0));
            // fuel
            list.add(new Vec3i(56, 53, 0));

            {
                RecipeType<?> key = RecipeType.SMELTING;
                map.put(key, new RecipeView(key, GUI_TEXTURE_FURNACE, list));
            }
            {
                RecipeType<?> key = RecipeType.BLASTING;
                map.put(key, new RecipeView(key, GUI_TEXTURE_BLAST_FURNACE, list));
            }
            {
                RecipeType<?> key = RecipeType.SMOKING;
                map.put(key, new RecipeView(key, GUI_TEXTURE_SMOKER, list));
            }
        }

        {
            List<Vec3i> list = Lists.newArrayList();

            // output
            list.add(new Vec3i(134, 47, 0));

            // input
            list.add(new Vec3i(27, 47, 0));
            // material
            list.add(new Vec3i(76, 47, 0));

            {
                RecipeType<?> key = RecipeType.SMITHING;
                map.put(key, new RecipeView(key, GUI_TEXTURE_SMITHING, list, false));
            }

            {
                RecipeType<?> key = dummy_anvilType;
                map.put(key, new RecipeView(key, GUI_TEXTURE_ANVIL, list, false));
            }
        }

        return map;
    }

    @Override
    public void addItemToSlot(Iterator<Ingredient> ingredients, int slotIn, int maxAmount, int y, int x) {
        Ingredient ingredient = ingredients.next();
        if (!ingredient.isEmpty()) {
            if (slotIn < currentView.slots.size()) {

                Vec3i slot = currentView.slots.get(slotIn);
                gr.addIngredient(ingredient, slot.getX(), slot.getY());
            }
        }
    }

    static void clearGhostRecipe() {
        target = null;
        gr.clear();
        currentRecipe = null;
        currentView = null;
    }

    static void setGhostRecipe(ItemStack icon) {
        if (icon != null) {
            if (icon.hasTag() && icon.getTag().contains("Crafting")) {
                getInstance().setGhostRecipe(new ResourceLocation(icon.getTag().getString("Crafting")));
            }
        }

        target = icon;
    }

    void setGhostRecipe(ResourceLocation loc) {

        if (!Objects.equals(loc, currentRecipe)) {
            currentRecipe = loc;

            Optional<? extends Recipe<?>> recipe = MCINSTANCE.level.getRecipeManager().byKey(loc);
            if (recipe.isPresent()) {
                gr.clear();

                Recipe<?> iRecipe = recipe.get();
                iRecipe = overrideDummyRecipe(iRecipe);

                gr.setRecipe(iRecipe);

                currentView = typeRecipeViewMap.get(iRecipe.getType());

                if (currentView != null && 0 < currentView.slots.size()) {
                    final int outputslotIndex = 0;
                    Vec3i outputSlot = currentView.slots.get(outputslotIndex);
                    gr.addIngredient(Ingredient.of(iRecipe.getResultItem(MCINSTANCE.level.registryAccess())), outputSlot.getX(), outputSlot.getY());

                    this.placeRecipe(3, 3, outputslotIndex, iRecipe, iRecipe.getIngredients().iterator(), 1);
                }
            } else
                gr.clear();
        }
    }

    void drawBackGround(GuiGraphics gg, int xCorner, int yCorner, int zOffset, int xSize, int ySize, int yClip) {
        int bPadding = 5;
        gg.blit(currentView.background, xCorner, yCorner, zOffset, 0, 0, xSize, yClip - bPadding, 256, 256);
        gg.blit(currentView.background, xCorner, yCorner + yClip - bPadding, zOffset, 0, ySize - bPadding, xSize,
                bPadding, 256, 256);
    }

    void drawGhostRecipe(GuiGraphics gg, int xCorner, int yCorner, int zOffset, float partialTicks) {
        try {
            gg.pose().pushPose();
            // matrixStack.translate(0,0,zOffset);

            /*
             * ItemRenderer ir = Minecraft.getInstance().getItemRenderer();
             *
             * float tmp = ir.blitOffset; ir.blitOffset = zOffset - 125;
             */
            int padding = 5;
            gg.renderFakeItem(gr.getRecipe().getToastSymbol(), xCorner + padding, yCorner + padding);

            boolean wideOutputSlot = currentView.isWideOutputSlot;

            gr.render(gg, MCINSTANCE, xCorner, yCorner, wideOutputSlot, partialTicks);

            // ir.blitOffset = tmp;

        } finally {
            gg.pose().popPose();
        }
    }

    void drawTooltip(GuiGraphics gg, int xCorner, int yCorner, int zOffset, int mouseX, int mouseY, Screen gui) {

        ItemStack itemStack = null;

        int slotSize = 16;

        for (int i = 0; i < gr.size(); ++i) {
            GhostRecipe.GhostIngredient ghostIngredient = gr.get(i);
            int j = ghostIngredient.getX() + xCorner;
            int k = ghostIngredient.getY() + yCorner;
            if (mouseX >= j && mouseY >= k && mouseX < j + slotSize && mouseY < k + slotSize) {
                itemStack = ghostIngredient.getItem();
            }
        }

        if (itemStack != null) {
            if (itemStack != null && MCINSTANCE.screen != null) {
                gg.renderTooltip(MCINSTANCE.font, itemStack, mouseX, mouseY);
            }
        }
    }

    // Fabric事件处理方法
    private void onDrawScreenPost(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (AdvancementsRecipeRenderer.currentRecipe == null)
            return;
        if (AdvancementsRecipeRenderer.currentView == null)
            return;

        AdvancementsScreen gui = (AdvancementsScreen) screen;

        try {
            guiGraphics.pose().pushPose();

            PoseStack matrixStack = guiGraphics.pose();

            int zOffset = 425;
            int zStep = 75;
            matrixStack.translate(0, 0, zOffset);

            int xSize = 176;
            int ySize = 166;
            int yClip = 85;

            int xCorner = (gui.width - xSize) / 2;
            int yCorner = (gui.height - yClip) / 2;

            drawBackGround(guiGraphics, xCorner, yCorner, zOffset, xSize, ySize, yClip);

            drawGhostRecipe(guiGraphics, xCorner, yCorner, zOffset, partialTick);

            matrixStack.translate(0, 0, zStep);
            drawTooltip(guiGraphics, xCorner, yCorner, zOffset, mouseX, mouseY, gui);

        } finally {
            guiGraphics.pose().popPose();
        }
    }

    // Fabric鼠标点击事件处理
    private boolean onMouseClicked(Screen screen, double mouseX, double mouseY, int button) {
        if (button == 1) {
            clearGhostRecipe();
            return false;
        }

        AdvancementsScreen advScreen = (AdvancementsScreen) screen;
        int offsetX = (advScreen.width - 252) / 2;
        int offsetY = (advScreen.height - 140) / 2;

        ItemStack found = null;

        AdvancementTab selectedTab = advScreen.selectedTab;

        if (selectedTab == null)
            return false;

        int mouseXX = (int) (mouseX - offsetX - 9);
        int mouseYY = (int) (mouseY - offsetY - 18);

        double scrollX = selectedTab.scrollX;
        double scrollY = selectedTab.scrollY;
        Map<Advancement, AdvancementWidget> guis = selectedTab.widgets;

        int i = Mth.floor(scrollX);
        int j = Mth.floor(scrollY);
        if (mouseXX > 0 && mouseXX < 234 && mouseYY > 0 && mouseYY < 113) {
            for (AdvancementWidget advancemententrygui : guis.values()) {
                if (advancemententrygui.isMouseOver(i, j, mouseXX, mouseYY)) {
                    DisplayInfo info = advancemententrygui.display;
                    found = info.getIcon();
                    break;
                }
            }
        }

        setGhostRecipe(found);
        return false;
    }
}