package com.flechazo.slashblade.client.renderer.gui;

import com.flechazo.slashblade.SlashBladeRefabriced;
import com.flechazo.slashblade.capability.concentrationrank.ConcentrationRankComponent;
import com.flechazo.slashblade.capability.concentrationrank.ConcentrationRankHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class RankRenderer {
    private static final class SingletonHolder {
        private static final RankRenderer instance = new RankRenderer();
    }

    public static RankRenderer getInstance() {
        return SingletonHolder.instance;
    }

    private RankRenderer() {
    }

    public void register() {
        HudRenderCallback.EVENT.register(this::onHudRender);
    }

    static ResourceLocation RankImg = new ResourceLocation(SlashBladeRefabriced.MODID, "textures/gui/rank.png");

    @Environment(EnvType.CLIENT)
    private void onHudRender(GuiGraphics guiGraphics, float tickDelta) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        if (!Minecraft.renderNames()) return;
        if (mc.screen != null && !(mc.screen instanceof ChatScreen)) return;

        long time = System.currentTimeMillis();
        renderRankHud(tickDelta, player, time);
    }

    private void renderRankHud(Float partialTicks, LocalPlayer player, long time) {
        Minecraft mc = Minecraft.getInstance();

        ConcentrationRankHelper.getConcentrationRank(player).ifPresent(cr -> {
            long now = player.level().getGameTime();

            ConcentrationRankComponent.ConcentrationRanks rank = cr.getRank(now);

            /*
             * debug rank = IConcentrationRank.ConcentrationRanks.C; now =
             * cr.getLastUpdate();
             */

            if (rank == ConcentrationRankComponent.ConcentrationRanks.NONE)
                return;

            // todo : korenani loadGUIRenderMatrix
            // mc.getMainWindow().loadGUIRenderMatrix(Minecraft.IS_RUNNING_ON_MAC);

            int k = mc.getWindow().getGuiScaledWidth();
            int l = mc.getWindow().getGuiScaledHeight();

            PoseStack poseStack = new PoseStack();
            // position
            poseStack.translate(k * 2 / 3, l / 5, 0);

            // RenderSystem.enableTexture();
            RenderSystem.disableDepthTest();
            TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
            texturemanager.getTexture(RankImg).setFilter(false, false);
            RenderSystem.setShaderTexture(0, RankImg);

            boolean showTextRank = false;

            long textTimeout = cr.getLastRankRise() + 20;
            long visibleTimeout = cr.getLastUpdate() + 120;

            if (now < textTimeout)
                showTextRank = true;

            if (now < visibleTimeout) {
                int rankOffset = 32 * (rank.level - 1);
                int textOffset = showTextRank ? 128 : 0;

                int progress = (int) (33 * cr.getRankProgress(now));

                int progressIcon = (int) (18 * cr.getRankProgress(now));
                int progressIconInv = 17 - progressIcon;

                // GL11.glScalef(3,3,3);
                // iconFrame
                drawTexturedQuad(poseStack, 0, 0, textOffset + 64, rankOffset, 64, 32, -95f);
                // icon
                drawTexturedQuad(poseStack, 0, progressIconInv + 7, textOffset, rankOffset + progressIconInv + 7,
                        64, progressIcon, -90f);

                // gauge frame
                drawTexturedQuad(poseStack, 0, 32, 0, 256 - 16, 64, 16, -90f);
                // gause fill
                drawTexturedQuad(poseStack, 16, 32, 16, 256 - 32, progress, 16, -95f);
            }

        });

    }

    public static void drawTexturedQuad(PoseStack poseStack, int x, int y, int u, int v, int width, int height,
                                        float zLevel) {
        float var7 = 0.00390625F; // 1/256 texturesize
        float var8 = 0.00390625F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder wr = tessellator.getBuilder();
        wr.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        Matrix4f m = poseStack.last().pose();

        wr.vertex(m, x, y + height, zLevel).uv((u + 0.0f) * var7, (v + height) * var8).endVertex();
        wr.vertex(m, x + width, y + height, zLevel).uv((u + width) * var7, (v + height) * var8).endVertex();
        wr.vertex(m, x + width, y, zLevel).uv((u + width) * var7, (v) * var8).endVertex();
        wr.vertex(m, x, y, zLevel).uv((u) * var7, (v) * var8).endVertex();

        // tessellator.end();
        BufferUploader.drawWithShader(wr.end());
    }
}
