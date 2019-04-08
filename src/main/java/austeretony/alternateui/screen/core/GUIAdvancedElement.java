package austeretony.alternateui.screen.core;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Класс-основа сложных (текстурированных) графических элементов ГПИ.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIAdvancedElement<T extends GUISimpleElement> extends GUISimpleElement<T> {

    private ResourceLocation texture;

    private int textureWidth, textureHeight, textureU, textureV, imageWidth, imageHeight, textureOffsetX, textureOffsetY;

    private boolean isTextureEnabled;

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        if (this.isVisible()) {        	
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);           
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);    
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            if (this.isTextureEnabled()) {  
                int u = this.getTextureU();        		
                this.mc.getTextureManager().bindTexture(this.getTexture());
                if (!this.isEnabled())                  
                    u = this.getTextureWidth();   
                else if (this.isHovered() || this.isToggled())              	
                    u += this.getTextureWidth() * 2;      
                GlStateManager.enableBlend(); 
                this.drawCustomSizedTexturedRect((this.getWidth() - this.getTextureWidth()) / 2, 
                        (this.getHeight() - this.getTextureHeight()) / 2, u, this.getTextureV(), this.getTextureWidth(), 
                        this.getTextureHeight(), this.getImageWidth(), this.getImageHeight());        	
                GlStateManager.disableBlend(); 
            }       	             
            GlStateManager.popMatrix();
        }
    }

    public ResourceLocation getTexture() {   	
        return this.texture;
    }

    /**
     * Установка текстуры.
     * 
     * @param textureLoctaion путь к текстуре
     * @param textureWidth ширина
     * @param textureHeight высота
     * @param u координата текстуры по x
     * @param v координата текстуры по y
     * @param imageWidth ширина изображения
     * @param imageHeight высота изображения
     * 
     * @return вызывающий объект
     */
    public T setTexture(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int imageWidth, int imageHeight) {   	  	
        this.setTexture(texture, textureWidth, textureHeight, imageWidth, imageHeight);
        this.textureU = u;
        this.textureV = v;    	 	
        return (T) this;
    }

    /**
     * Установка текстуры.
     * 
     * @param textureLoctaion путь к текстуре
     * @param textureWidth ширина
     * @param textureHeight высота
     * 
     * @return вызывающий объект
     */
    public T setTexture(ResourceLocation texture, int textureWidth, int textureHeight, int imageWidth, int imageHeight) {   
        this.setTexture(texture, textureWidth, textureHeight);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;   	
        return (T) this;
    }

    /**
     * Установка текстуры.
     * 
     * @param textureLoctaion путь к текстуре
     * @param textureWidth ширина
     * @param textureHeight высота
     * 
     * @return вызывающий объект
     */
    public T setTexture(ResourceLocation texture, int textureWidth, int textureHeight) {   	
        this.setTexture(texture);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;   	
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;    	
        this.enableTexture();   	
        return (T) this;
    }

    /**
     * Установка текстуры.
     * 
     * @param textureLoctaion путь к текстуре
     * 
     * @return вызывающий объект
     */
    public T setTexture(ResourceLocation texture) {    	
        this.texture = texture;   	
        this.enableTexture();   	
        return (T) this;
    }  

    public int getTextureWidth() {  	
        return this.textureWidth;
    }

    public int getTextureHeight() {  	
        return this.textureHeight;
    }

    /**
     * Установка размера текстуры.
     * 
     * @param textureWidth
     * @param textureHeight
     * 
     * @return вызывающий объект
     */
    public T setTextureSize(int textureWidth, int textureHeight) {   	
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;

        return (T) this;
    }

    public int getTextureU() {   	
        return this.textureU;
    }

    public int getTextureV() {    	
        return this.textureV;
    }

    /**
     * Установка UV координат для текстуры.
     * 
     * @param u
     * @param v
     * 
     * @return вызывающий объект
     */
    public T setTextureUV(int u, int v) {   	
        this.textureU = u;
        this.textureV = v;

        return (T) this;
    }

    public int getImageWidth() {  	
        return this.imageWidth;
    }

    public int getImageHeight() {   	
        return this.imageHeight;
    }

    /**
     * Установка размера изображения с текстурой.
     * 
     * @param width
     * @param height
     * 
     * @return вызывающий объект
     */
    public T setImageSize(int width, int height) {   	
        this.imageWidth = width;
        this.imageHeight = height;

        return (T) this;
    }

    public int getTextureOffsetX() {    	
        return this.textureOffsetX;
    }

    public int getTextureOffsetY() {   	
        return this.textureOffsetY;
    }

    /**
     * Устанавлиает отступ для текстуры (от левого верхнего угла элемента).
     * 
     * @param xOffset отступ по горизонтали
     * @param yOffset отступ по вертикали
     * 
     * @return вызывающий объект
     */
    public T setTextureOffset(int offsetX, int offsetY) {		
        this.textureOffsetX = offsetX;
        this.textureOffsetY = offsetY;		
        return (T) this;
    }

    public boolean isTextureEnabled() {   	
        return this.isTextureEnabled;
    }

    /**
     * Использовать текстуру для элемента, требует установку текстуры {@link GUIAdvancedElement#setTexture(ResourceLocation)} 
     * (используйте один из перегруженных методов).
     * 
     * @return вызывающий объект
     */
    public T enableTexture() {   	
        this.isTextureEnabled = true;   	
        return (T) this;
    }

    public static void drawCustomSizedTexturedRect(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight) {  	
        float f = 1.0F / (float) textureWidth;
        float f1 = 1.0F / (float) textureHeight;        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);    
        bufferbuilder.pos((double) x, (double) (y + height), 0.0D).tex((double) (u * f), (double) ((v + (float) height) * f1)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + height), 0.0D).tex((double) ((u + (float) width) * f), (double) ((v + (float) height) * f1)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) y, 0.0D).tex((double) ((u + (float) width) * f), (double) (v * f1)).endVertex();
        bufferbuilder.pos((double) x, (double) y, 0.0D).tex((double) (u * f), (double) (v * f1)).endVertex();    
        tessellator.draw();
    }
}

