package austeretony.oxygen_core.client.api;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.pipeline.LightUtil;

public class ItemRenderHelper {

    public static void renderItemWithoutEffectIntoGUI(ItemStack stack, int x, int y) {
        renderItemModelIntoGUI(stack, x, y, getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null));
    }

    public static IBakedModel getItemModelWithOverrides(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entitylivingbaseIn) {
        IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
        return ibakedmodel.getOverrides().handleItemState(ibakedmodel, stack, worldIn, entitylivingbaseIn);
    }

    protected static void renderItemModelIntoGUI(ItemStack stack, int x, int y, IBakedModel bakedmodel) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        setupGuiTransform(x, y, bakedmodel.isGui3d());
        renderItem(stack, bakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }

    private static void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d) {
        GlStateManager.translate((float)xPosition, (float)yPosition, 100.0F + Minecraft.getMinecraft().getRenderItem().zLevel);
        GlStateManager.translate(8.0F, 8.0F, 0.0F);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.scale(16.0F, 16.0F, 16.0F);

        if (isGui3d)
            GlStateManager.enableLighting();
        else
            GlStateManager.disableLighting();
    }

    public static void renderItem(ItemStack stack, IBakedModel model) {
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);

            if (model.isBuiltInRenderer()) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();
                stack.getItem().getTileEntityItemStackRenderer().renderByItem(stack);
            } else
                renderModel(model, stack);

            GlStateManager.popMatrix();
        }
    }

    private static void renderModel(IBakedModel model, ItemStack stack) {
        renderModel(model, -1, stack);
    }

    private static void renderModel(IBakedModel model, int color, ItemStack stack) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

        for (EnumFacing enumfacing : EnumFacing.values())
            renderQuads(bufferbuilder, model.getQuads((IBlockState) null, enumfacing, 0L), color, stack);

        renderQuads(bufferbuilder, model.getQuads((IBlockState) null, (EnumFacing) null, 0L), color, stack);
        tessellator.draw();
    }

    private static void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack) {
        boolean flag = color == -1 && !stack.isEmpty();
        int i = 0;

        for (int j = quads.size(); i < j; ++i) {
            BakedQuad bakedquad = quads.get(i);
            int k = color;

            if (flag && bakedquad.hasTintIndex()) {
                k = Minecraft.getMinecraft().getItemColors().colorMultiplier(stack, bakedquad.getTintIndex());

                if (EntityRenderer.anaglyphEnable)
                    k = TextureUtil.anaglyphColor(k);

                k = k | -16777216;
            }

            LightUtil.renderQuadColor(renderer, bakedquad, k);
        }
    }
}
