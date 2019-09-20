package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.util.EnumGUIAlignment;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class CustomRectUtils {

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
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
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

    public static void drawGradientRect(double xStart, double yStart, double xEnd, double yEnd, int colorHex1, int colorHex2, EnumGUIAlignment alignment) {         
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
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);  

        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        switch (alignment) {
        case BOTTOM:
            break;
        case TOP:
            break;
        case LEFT:
            bufferBuilder.pos(xStart, yEnd, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
            bufferBuilder.pos(xEnd, yEnd, 0.0D).color(red1, green1, blue1, alpha1).endVertex();
            bufferBuilder.pos(xEnd, yStart, 0.0D).color(red1, green1, blue1, alpha1).endVertex();
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
}