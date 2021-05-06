package austeretony.oxygen_core.client.gui.base;

import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.gui.base.block.Texture;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.util.MinecraftClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GUIUtils {

    private GUIUtils() {}

    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    public static int getGUIScale() {
        return mc().gameSettings.guiScale;
    }

    public static int getDisplayWidth() {
        return mc().displayWidth;
    }

    public static int getDisplayHeight() {
        return mc().displayHeight;
    }

    public static ScaledResolution getScaledResolution() {
        return new ScaledResolution(mc());
    }

    public static int getScaledDisplayWidth() {
        return getScaledResolution().getScaledWidth();
    }

    public static int getScaledDisplayHeight() {
        return getScaledResolution().getScaledHeight();
    }

    public static RenderItem getRenderItem() {
        return mc().getRenderItem();
    }

    public static ResourceLocation getMissingTextureLocation() {
        return TextureManager.RESOURCE_LOCATION_EMPTY;
    }

    public static String getKeyDisplayString(int keyCode) {
        return GameSettings.getKeyDisplayString(keyCode);
    }

    public static float getTextWidth(String text, float textScale) {
        return mc().fontRenderer.getStringWidth(text) * textScale;
    }

    public static float getTextHeight(float textScale) {
        return mc().fontRenderer.FONT_HEIGHT * textScale;
    }

    public static void pushMatrix() {
        GlStateManager.pushMatrix();
    }

    public static void translate(float x, float y) {
        GlStateManager.translate(x, y, 0F);
    }

    public static void scale(float x, float y) {
        GlStateManager.scale(x, y, 0F);
    }

    public static void color(float r, float g, float b) {
        GlStateManager.color(r, g, b);
    }

    public static void color(float r, float g, float b, float alpha) {
        GlStateManager.color(r, g, b, alpha);
    }

    public static void colorDef() {
       color(1F, 1F, 1F);
    }

    public static void enableBlend() {
        GlStateManager.enableBlend();
    }

    public static void tryBlendFuncSeparate(GlStateManager.SourceFactor srcFactor, GlStateManager.DestFactor dstFactor,
                                            GlStateManager.SourceFactor srcFactorAlpha, GlStateManager.DestFactor dstFactorAlpha) {
        GlStateManager.tryBlendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
    }

    public static void blendFunc(int srcFactor, int dstFactor) {
        GlStateManager.blendFunc(srcFactor, dstFactor);
    }

    public static void disableBlend() {
        GlStateManager.disableBlend();
    }

    public static void enableDepth() {
        GlStateManager.enableDepth();
    }

    public static void disableDepth() {
        GlStateManager.disableDepth();
    }

    public static void enableGUIStandardItemLighting() {
        RenderHelper.enableGUIStandardItemLighting();
    }

    public static void enableStandardItemLighting() {
        RenderHelper.enableStandardItemLighting();
    }

    public static void disableStandardItemLighting() {
        RenderHelper.disableStandardItemLighting();
    }

    public static void popMatrix() {
        GlStateManager.popMatrix();
    }

    public static void drawString(@Nonnull String text, float x, float y, float scale, int colorHex, boolean shadow) {
        pushMatrix();
        translate(x, y);
        scale(scale, scale);
        mc().fontRenderer.drawString(text, 0F, 0F, colorHex, shadow);
        popMatrix();
    }

    public static void drawString(@Nonnull Text text, float x, float y, int colorHex) {
        drawString(text.getText(), x, y, text.getScale(), colorHex, text.isShadowEnabled());
    }

    public static void drawString(@Nonnull String text, float x, float y, int colorHex, boolean shadow) {
        mc().fontRenderer.drawString(text, x, y, colorHex, shadow);
    }

    public static void drawRect(double xStart, double yStart, double xEnd, double yEnd, int colorHex) {
        double j1;
        if (xStart < xEnd) {
            j1 = xStart;
            xStart = xEnd;
            xEnd = j1;
        }
        if (yStart < yEnd) {
            j1 = yStart;
            yStart = yEnd;
            yEnd = j1;
        }

        float
                alpha = (float) (colorHex >> 24 & 255) / 255.0F,
                red = (float) (colorHex >> 16 & 255) / 255.0F,
                green = (float) (colorHex >> 8 & 255) / 255.0F,
                blue = (float) (colorHex & 255) / 255.0F;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(red, green, blue, alpha);

        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(xStart, yEnd, 0.0D).endVertex();
        bufferBuilder.pos(xEnd, yEnd, 0.0D).endVertex();
        bufferBuilder.pos(xEnd, yStart, 0.0D).endVertex();
        bufferBuilder.pos(xStart, yStart, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(double xStart, double yStart, double xEnd, double yEnd, int colorHex1,
                                        int colorHex2, Alignment alignment) {
        float
                alpha1 = (float) (colorHex1 >> 24 & 255) / 255.0F,
                red1 = (float) (colorHex1 >> 16 & 255) / 255.0F,
                green1 = (float) (colorHex1 >> 8 & 255) / 255.0F,
                blue1 = (float) (colorHex1 & 255) / 255.0F,
                alpha2 = (float) (colorHex2 >> 24 & 255) / 255.0F,
                red2 = (float) (colorHex2 >> 16 & 255) / 255.0F,
                green2 = (float) (colorHex2 >> 8 & 255) / 255.0F,
                blue2 = (float) (colorHex2 & 255) / 255.0F;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);

        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        switch (alignment) {
            case BOTTOM:
                bufferBuilder.pos(xStart, yEnd, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
                bufferBuilder.pos(xEnd, yEnd, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
                bufferBuilder.pos(xEnd, yStart, 0.0D).color(red1, green1, blue1, alpha1).endVertex();
                bufferBuilder.pos(xStart, yStart, 0.0D).color(red1, green1, blue1, alpha1).endVertex();
                break;
            case TOP:
                bufferBuilder.pos(xStart, yEnd, 0.0D).color(red1, green1, blue1, alpha1).endVertex();
                bufferBuilder.pos(xEnd, yEnd, 0.0D).color(red1, green1, blue1, alpha1).endVertex();
                bufferBuilder.pos(xEnd, yStart, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
                bufferBuilder.pos(xStart, yStart, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
                break;
            case RIGHT:
                bufferBuilder.pos(xStart, yEnd, 0.0D).color(red1, green1, blue1, alpha1).endVertex();
                bufferBuilder.pos(xEnd, yEnd, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
                bufferBuilder.pos(xEnd, yStart, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
                bufferBuilder.pos(xStart, yStart, 0.0D).color(red1, green1, blue1, alpha1).endVertex();
                break;
            default:
                bufferBuilder.pos(xStart, yEnd, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
                bufferBuilder.pos(xEnd, yEnd, 0.0D).color(red1, green1, blue1, alpha1).endVertex();
                bufferBuilder.pos(xEnd, yStart, 0.0D).color(red1, green1, blue1, alpha1).endVertex();
                bufferBuilder.pos(xStart, yStart, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
                break;
        }
        tessellator.draw();

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawFrame(double x, double y, double width, double height, double frameWidth, int color) {
        drawRect(x + -frameWidth, y, x, y + height, color);
        drawRect(x + width, y, x + width + frameWidth, y + height, color);
        drawRect(x + -frameWidth, y - frameWidth, x + width + frameWidth, y, color);
        drawRect(x + -frameWidth, y + height, x + width + frameWidth, y + height + frameWidth, color);
    }

    public static void drawFrame(double x, double y, double width, double height) {
        drawFrame(x, y, width, height, Widget.FRAME_WIDTH, CoreSettings.COLOR_BACKGROUND_ADDITIONAL.asInt());
    }

    public static void drawTexturedRect(double x, double y, Texture texture) {
        drawTexturedRect(x, y, texture.getU(), texture.getV(), texture);
    }

    public static void drawTexturedRect(double x, double y, int u, int v, Texture texture) {
        drawTexturedRect(x, y, texture.getWidth(), texture.getHeight(), texture.getTexture(), u, v, texture.getImageWidth(),
                texture.getImageHeight());
    }

    public static void drawTexturedRect(double x, double y, int width, int height, ResourceLocation texture, int u, int v,
                                        int textureWidth, int textureHeight) {
        mc().getTextureManager().bindTexture(texture);
        double
                wRatio = 1.0 / textureWidth,
                hRatio = 1.0 / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0.0).tex(u * wRatio, (v + height) * hRatio).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0).tex((u + width) * wRatio, (v + height) * hRatio).endVertex();
        bufferbuilder.pos(x + width, y, 0.0).tex((u + width) * wRatio, v * hRatio).endVertex();
        bufferbuilder.pos(x, y, 0.0).tex(u * wRatio, v * hRatio).endVertex();
        tessellator.draw();
    }

    public static void drawSelectionBox(int startX, int startY, int endX, int endY) {
        if (startX < endX) {
            int i = startX;
            startX = endX;
            endX = i;
        }

        if (startY < endY) {
            int j = startY;
            startY = endY;
            endY = j;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.color(.0F, .0F, 255F, 255F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(startX, endY, .0D).endVertex();
        bufferbuilder.pos(endX, endY, .0D).endVertex();
        bufferbuilder.pos(endX, startY, .0D).endVertex();
        bufferbuilder.pos(startX, startY, .0D).endVertex();
        tessellator.draw();

        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    public static void pushScissor() {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    public static void scissor(int x, int y, int width, int height) {
        int scale = getScaledResolution().getScaleFactor();

        int scissorWidth = width * scale;
        int scissorHeight = height * scale;
        int scissorX = x * scale;
        int scissorY = getDisplayHeight() - scissorHeight - (y * scale);

        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
    }

    public static void popScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static List<String> splitTextToLines(String text, float textScale, int maxWidth) {
        if (text.isEmpty()) return Collections.emptyList();
        if (!text.contains(" ") && !text.contains("\n")) return Collections.singletonList(text);

        List<String> lines = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        int index = 0;
        int wordStartIndex = 0;
        boolean wordProcessing = false;
        char prevSymbol = '0';

        for (char symbol : text.toCharArray()) {
            if (symbol != ' ') {
                wordProcessing = true;
                if (prevSymbol == ' ') {
                    wordStartIndex = index;
                }
            }

            if (symbol == '\n') {
                lines.add(builder.toString());
                builder.delete(0, builder.length());
                index = 0;
                continue;
            }

            if (getTextWidth(builder.toString() + symbol, textScale) <= maxWidth) {
                builder.append(symbol);
            } else {
                if (symbol == '.' || symbol == ',' || symbol == '!' || symbol == '?') {
                    builder.append(symbol);
                }
                if (wordProcessing) {
                    lines.add(builder.toString().substring(0, wordStartIndex));
                    builder.delete(0, wordStartIndex);
                } else {
                    lines.add(builder.toString());
                    builder.delete(0, builder.length());
                }
                if (symbol != ' ') {
                    builder.append(symbol);
                }
                index = builder.length() - 1;
            }

            wordProcessing = false;
            prevSymbol = symbol;
            index++;
        }

        if (builder.length() != 0) {
            lines.add(builder.toString());
        }
        return lines;
    }

    public static int scaleAlpha(int color, float alpha) {
        float a = ((color >> 24) & 0xFF) / 255F;
        int newAlpha = (int) (a * alpha * 255F) << 24;
        return (color & 0xFFFFFF) | newAlpha;
    }

    public static void renderItemStack(ItemStack itemStack, int x, int y, boolean showDurabilityBar) {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableDepth();
        getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);

        if (showDurabilityBar) {
            FontRenderer fontRenderer = itemStack.getItem().getFontRenderer(itemStack);
            if (fontRenderer == null) {
                fontRenderer = mc().fontRenderer;
            }
            getRenderItem().renderItemOverlayIntoGUI(fontRenderer, itemStack, x, y, null);
        }

        GlStateManager.disableDepth();
        RenderHelper.disableStandardItemLighting();
    }

    public static void renderItemStack(ItemStack itemStack, int x, int y) {
        renderItemStack(itemStack, x, y, false);
    }

    public static void renderItemStackWithoutGlint(ItemStack itemStack, int x, int y) {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableDepth();
        renderItemModelIntoGUI(itemStack, x, y, getItemModelWithOverrides(itemStack, null, null));
        GlStateManager.disableDepth();
        RenderHelper.disableStandardItemLighting();
    }

    private static IBakedModel getItemModelWithOverrides(ItemStack itemStack, @Nullable World world,
                                                        @Nullable EntityLivingBase livingBase) {
        IBakedModel bakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(itemStack);
        return bakedModel.getOverrides().handleItemState(bakedModel, itemStack, world, livingBase);
    }

    private static void renderItemModelIntoGUI(ItemStack itemStack, int x, int y, IBakedModel bakedModel) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, .1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1F, 1F, 1F, 1F);
        setupGuiTransform(x, y, bakedModel.isGui3d());
        renderItem(itemStack, bakedModel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }

    private static void setupGuiTransform(int x, int y, boolean isGui3d) {
        GlStateManager.translate(x, y, 100.0F + Minecraft.getMinecraft().getRenderItem().zLevel);
        GlStateManager.translate(8F, 8F, 0F);
        GlStateManager.scale(1F, -1F, 1F);
        GlStateManager.scale(16F, 16F, 16F);

        if (isGui3d) {
            GlStateManager.enableLighting();
        } else {
            GlStateManager.disableLighting();
        }
    }

    private static void renderItem(ItemStack itemStack, IBakedModel bakedModel) {
        if (!itemStack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-.5F, -.5F, -.5F);

            if (bakedModel.isBuiltInRenderer()) {
                GlStateManager.color(1F, 1F, 1F, 1F);
                GlStateManager.enableRescaleNormal();
                itemStack.getItem().getTileEntityItemStackRenderer().renderByItem(itemStack);
            } else {
                renderModel(bakedModel, itemStack);
            }

            GlStateManager.popMatrix();
        }
    }

    private static void renderModel(IBakedModel bakedModel, ItemStack itemStack) {
        renderModel(bakedModel, -1, itemStack);
    }

    private static void renderModel(IBakedModel bakedModel, int colorHex, ItemStack itemStack) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

        for (EnumFacing facing : EnumFacing.values()) {
            renderQuads(bufferbuilder, bakedModel.getQuads(null, facing, 0L), colorHex, itemStack);
        }

        renderQuads(bufferbuilder, bakedModel.getQuads(null, null, 0L), colorHex, itemStack);
        tessellator.draw();
    }

    private static void renderQuads(BufferBuilder bufferBuilder, List<BakedQuad> quadList, int colorHex, ItemStack itemStack) {
        boolean flag = colorHex == -1 && !itemStack.isEmpty();
        int i = 0;
        for (int j = quadList.size(); i < j; ++i) {
            BakedQuad bakedQuad = quadList.get(i);
            int k = colorHex;
            if (flag && bakedQuad.hasTintIndex()) {
                k = Minecraft.getMinecraft().getItemColors().colorMultiplier(itemStack, bakedQuad.getTintIndex());

                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k);
                }

                k = k | -16777216;
            }
            LightUtil.renderQuadColor(bufferBuilder, bakedQuad, k);
        }
    }

    public static List<String> getItemStackToolTip(ItemStack itemStack) {
        List<String> tooltipLines = itemStack.getTooltip(MinecraftClient.getPlayer(),
                MinecraftClient.getGameSettings().advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED
                        : ITooltipFlag.TooltipFlags.NORMAL);
        for (int i = 0; i < tooltipLines.size(); ++i) {
            if (i == 0) {
                tooltipLines.set(i, itemStack.getItem().getForgeRarity(itemStack).getColor() + tooltipLines.get(i));
            } else {
                tooltipLines.set(i, TextFormatting.GRAY + tooltipLines.get(i));
            }
        }
        return tooltipLines;
    }

    public static TextFormatting getItemStackRarityColor(ItemStack itemStack) {
        return itemStack.getRarity().rarityColor;
    }
}
