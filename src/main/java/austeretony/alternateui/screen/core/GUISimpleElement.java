package austeretony.alternateui.screen.core;

import austeretony.alternateui.screen.tooltip.AbstractGUITooltip;
import austeretony.alternateui.util.EnumGUIAlignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Класс-основа простых (без текстур) графических элементов ГПИ.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUISimpleElement<T extends GUIBaseElement> extends GUIBaseElement<T> {

    public final static int 
    ZERO = 0,
    FONT_HEIGHT = 9;

    private boolean isDebugMode, isVisible, isStatBackgroundEnabled, isDynBackgroundEnabled, isTextShadowEnabled, hasSimpleTooltip, hasAdvancedTooltip, hasDisplayText;

    private String displayText, tooltipText;

    private EnumGUIAlignment textAlignment = EnumGUIAlignment.CENTER;

    private int textOffset, tooltipWidth;

    private int 
    debugColor = 0x64FF00DC,
    statBackgroundColor = 0xFF404040,
    enabledColor = 0xFFA5A5A5,
    disabledColor = 0xFF666666,
    hoveredColor = 0xFFBFBFBF,
    enabledBackgroundColor = 0xFF555555,
    disabledBackgroundColor = 0xFF404040,
    hoveredBackgroundColor = 0xFF505050,
    enabledTextColor = 0xFFD1D1D1, 
    disabledTextColor = 0xFF707070,
    hoveredTextColor = 0xFFF2F2F2,
    tooltipBackgroundColor = 0xFF404040,
    tooltipTextColor = 0xFFD1D1D1;

    private float 
    mainScaleFactor = 1.0F,
    textScaleFactor = 1.0F,
    tooltipScaleFactor = 1.0F;

    private AbstractGUITooltip advancedTooltip;

    protected RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) { 
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            if (this.isDebugMode())  		
                this.drawRect(ZERO, ZERO, this.getWidth(), this.getHeight(), this.getDebugColor());	     	
            if (this.isStaticBackgroundEnabled())     		                
                this.drawRect(ZERO, ZERO, this.getWidth(), this.getHeight(), this.getStaticBackgroundColor());
            if (this.isDynamicBackgroundEnabled()) {    
                int color;        		
                if (!this.isEnabled())              	
                    color = this.getDisabledBackgroundColor();
                else if (this.isHovered() || this.isToggled())               	
                    color = this.getHoveredBackgroundColor();
                else               	
                    color = this.getEnabledBackgroundColor();   		                
                this.drawRect(ZERO, ZERO, this.getWidth(), this.getHeight(), color);
            } 
            if (this.hasDisplayText()) {   
                int textOffset = this.getTextAlignment() == EnumGUIAlignment.CENTER ? 
                        (this.getWidth() - (int) ((float) this.width(this.getDisplayText()) * this.getTextScale())) / 2 + this.getTextOffset() : (this.getTextAlignment() == EnumGUIAlignment.LEFT ? 
                                0 + this.getTextOffset() : this.getWidth() - (int) ((float) this.width(this.getDisplayText()) * this.getTextScale()) - this.getTextOffset()); 
                        GlStateManager.pushMatrix();           
                        GlStateManager.translate(textOffset, ((float) this.getHeight() - ((float) FONT_HEIGHT * this.getTextScale())) / 2, 0.0F);            
                        GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  
                        int color;       		
                        if (!this.isEnabled())               	
                            color = this.getDisabledTextColor();           
                        else if (this.isHovered() || this.isToggled())               	                	
                            color = this.getHoveredTextColor();
                        else               	
                            color = this.getEnabledTextColor();                                           
                        this.mc.fontRenderer.drawString(this.getDisplayText(), ZERO, ZERO, color, this.isTextShadowEnabled());
                        GlStateManager.popMatrix();
            }    
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {  	
        if (this.isHovered()) {  
            if (this.hasSimpleTooltip()) {      
                GlStateManager.pushMatrix();           
                GlStateManager.translate(mouseX, mouseY - 11, 0.0F);           
                GlStateManager.scale(this.tooltipScaleFactor, this.tooltipScaleFactor, 0.0F);    
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.drawRect(ZERO, ZERO, this.tooltipWidth, 11, this.tooltipBackgroundColor);               
                this.mc.fontRenderer.drawString(this.tooltipText, 2, 2, this.tooltipTextColor);               
                GlStateManager.popMatrix();
            } else if (this.hasAdvancedTooltip()) 
                this.advancedTooltip.draw(mouseX, mouseY);
        }
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {  	       	  	
        this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + (int) (this.getWidth() * this.getScale()) && mouseY < this.getY() + (int) (this.getHeight() * this.getScale()));   
    }

    public boolean isDebugMode() {    	
        return this.isDebugMode;
    }

    /**
     * Заливает рабочую область установленным цветом для отображения границ.
     * 
     * @return вызывающий объект
     */
    public T enableDebugMode() {  	
        this.isDebugMode = true;    	
        return (T) this;
    } 

    public boolean isVisible() {  	
        return this.isVisible;
    }

    /**
     * Определяет, будет ли отображаться элемент.
     * 
     * @param isVisible
     * 
     * @return вызывающий объект
     */
    public T setVisible(boolean isVisible) {  	
        this.isVisible = isVisible;	
        return (T) this;
    }

    public boolean isTextShadowEnabled() {  	
        return this.isTextShadowEnabled;
    }

    /**
     * Текст будет отбрасывать тень.
     * 
     * @return вызывающий объект
     */
    public T enableTextShadow() { 	
        this.isTextShadowEnabled = true;  	
        return (T) this;
    }

    public boolean isDynamicBackgroundEnabled() {  	
        return this.isDynBackgroundEnabled;
    }

    public boolean isStaticBackgroundEnabled() {  	
        return this.isStatBackgroundEnabled;
    }

    /**
     * Использовать заливку для фона элемента. Цвет заливки можно указать {@link GUISimpleElement#setEnabledBackgroundColor(int)}.
     * 
     * @return вызывающий объект
     */
    public T enableDynamicBackground() {  	
        this.isDynBackgroundEnabled = true;    	
        return (T) this;
    }

    public T enableDynamicBackground(int enabledColorHex, int disabledColorHex, int hoveredColorHex) {  
        this.enableDynamicBackground();
        this.enabledBackgroundColor = enabledColorHex;
        this.disabledBackgroundColor = disabledColorHex;
        this.hoveredBackgroundColor = hoveredColorHex;
        return (T) this;
    }

    public T enableStaticBackground() {   	
        this.isStatBackgroundEnabled = true;
        return (T) this;
    }

    public T enableStaticBackground(int colorHex) {   	
        this.enableStaticBackground();
        this.statBackgroundColor = colorHex;   	
        return (T) this;
    }

    public AbstractGUITooltip getTooltip() {   	
        return this.getTooltip();
    }

    public T initSimpleTooltip(String text) {  
        this.tooltipText = text;
        this.tooltipWidth = this.width(text) + 4;
        this.hasSimpleTooltip = true;
        return (T) this;
    }

    public T initSimpleTooltip(String text, float scale) {  
        this.tooltipText = text;
        this.tooltipScaleFactor = scale;
        this.tooltipWidth = this.width(text) + 4;
        this.hasSimpleTooltip = true;
        return (T) this;
    }

    public T initSimpleTooltip(String text, int textColorHex, int backgroundColorHex, float scale) {    
        this.tooltipText = text;
        this.tooltipScaleFactor = scale;
        this.tooltipWidth = this.width(text) + 4;
        this.tooltipTextColor = textColorHex;
        this.tooltipBackgroundColor = backgroundColorHex;
        this.hasSimpleTooltip = true;
        return (T) this;
    }

    public T initAdvancedTooltip(AbstractGUITooltip tooltip) { 	
        this.advancedTooltip = tooltip.setSize(this.getWidth(), this.getHeight()).setScale(this.getScale());; 	
        this.hasAdvancedTooltip = true;
        return (T) this;
    }

    public boolean hasSimpleTooltip() {           
        return this.hasSimpleTooltip;
    }

    public boolean hasAdvancedTooltip() {   	
        return this.hasAdvancedTooltip;
    }

    public String getDisplayText() {   	
        return this.displayText;
    }

    public boolean hasDisplayText() {   	
        return this.hasDisplayText;
    }

    /**
     * Установка текста, отображаемого элементом. 
     * 
     * @param displayText
     * 
     * @return вызывающий объект
     */
    public T setDisplayText(String displayText) {	
        this.hasDisplayText = true;
        this.displayText = displayText;   	
        return (T) this;
    }

    /**
     * Установка текста, отображаемого элементом. 
     * 
     * @param displayText
     * @param enableShadow
     * 
     * @return вызывающий объект
     */
    public T setDisplayText(String displayText, boolean enableShadow) {	
        this.setDisplayText(displayText);
        this.isTextShadowEnabled = enableShadow;
        return (T) this;
    }

    public T setDisplayText(String displayText, boolean enableShadow, float textScale) {	
        this.setDisplayText(displayText, enableShadow);
        this.textScaleFactor = textScale;
        return (T) this;
    }

    public int getDebugColor() {  	
        return this.debugColor;
    }

    /**
     * Установка цвета debug заливки.
     * 
     * @param colorHex
     * 
     * @return вызывающий объект
     */
    public T setDebugColor(int colorHex) {  	
        this.debugColor = colorHex;
        return (T) this;
    }

    public int getEnabledColor() {  	
        return this.enabledColor;
    }

    public T setEnabledColor(int colorHex) {	
        this.enabledColor = colorHex; 	
        return (T) this;
    }

    public int getDisabledColor() {   	
        return this.disabledColor;
    }

    public T setDisabledColor(int colorHex) {	
        this.disabledColor = colorHex;	
        return (T) this;
    }

    public int getHoveredColor() {	
        return this.hoveredColor;
    }

    public T setHoveredColor(int colorHex) {
        this.hoveredColor = colorHex; 	
        return (T) this;
    }

    public int getStaticBackgroundColor() {
        return this.statBackgroundColor;
    }

    public T setStaticBackgroundColor(int colorHex) {	
        this.statBackgroundColor = colorHex;   	
        return (T) this;
    }

    public T setDynamicBackgroundColor(int enabledColorHex, int disabledColorHex, int hoveredColorHex) {  
        this.enabledBackgroundColor = enabledColorHex;
        this.disabledBackgroundColor = disabledColorHex;
        this.hoveredBackgroundColor = hoveredColorHex;	
        return (T) this;
    }

    public int getEnabledBackgroundColor() {	
        return this.enabledBackgroundColor;
    }

    public T setEnabledBackgroundColor(int colorHex) {
        this.enabledBackgroundColor = colorHex;  	
        return (T) this;
    }

    public int getDisabledBackgroundColor() { 	
        return this.disabledBackgroundColor;
    }

    public T setDisabledBackgroundColor(int colorHex) { 	
        this.disabledBackgroundColor = colorHex;	
        return (T) this;
    }

    public int getHoveredBackgroundColor() { 	
        return this.hoveredBackgroundColor;
    }

    public T setHoveredBackgroundColor(int colorHex) {	
        this.hoveredBackgroundColor = colorHex;
        return (T) this;
    }

    public int getEnabledTextColor() {
        return this.enabledTextColor;
    }

    public T setTextDynamicColor(int enabledTextColorHex, int disabledTextColorHex, int hoveredTextColorHex) {
        this.enabledTextColor = enabledTextColorHex;
        this.disabledTextColor = disabledTextColorHex;
        this.hoveredTextColor = hoveredTextColorHex;
        return (T) this;
    }

    public T setEnabledTextColor(int colorHex) {
        this.enabledTextColor = colorHex;
        return (T) this;
    }

    public int getDisabledTextColor() {
        return this.disabledTextColor;
    }

    public T setDisabledTextColor(int colorHex) {
        this.disabledTextColor = colorHex;
        return (T) this;
    }

    public int getHoveredTextColor() {
        return this.hoveredTextColor;
    }

    public T setHoveredTextColor(int colorHex) {
        this.hoveredTextColor = colorHex;
        return (T) this;
    }

    public float getScale() {
        return this.mainScaleFactor;
    }

    public float getTextScale() {
        return this.textScaleFactor;
    }

    /**
     * Позволяет быстро задать относительный размер, 1.0F по умолчанию.
     * 
     * @param scaleFactor
     * 
     * @return вызывающий объект
     */
    public T setScale(float scaleFactor) {
        this.mainScaleFactor = scaleFactor; 	
        return (T) this;
    }

    /**
     * Позволяет быстро задать относительный размер текста, 1.0F по умолчанию.
     * 
     * @param scaleFactor
     * 
     * @return вызывающий объект
     */
    public T setTextScale(float scaleFactor) {
        this.textScaleFactor = scaleFactor; 	
        return (T) this;
    }

    public EnumGUIAlignment getTextAlignment() {	
        return this.textAlignment;
    }

    public int getTextOffset() {
        return this.textOffset;
    }

    /**
     * Установка юстировки текста.
     * 
     * @param textAlignment
     * 
     * @return вызывающий объект
     */
    public T setTextAlignment(EnumGUIAlignment textAlignment, int offset) {
        this.textAlignment = textAlignment;	
        this.textOffset = offset;
        return (T) this;
    }

    /**
     * Возвращает длину переданной строки.
     * 
     * @param text
     * 
     * @return длина строки в пикселях
     */
    public int width(String text) {
        return this.mc.fontRenderer.getStringWidth(text);
    }

    public int width(String text, float scaleFactor) {
        return (int) ((float) this.mc.fontRenderer.getStringWidth(text) * scaleFactor);
    }

    /**
     * Отключает элемент (setEnabled(false) и setVisible(false)).
     */
    public T disableFull() {	
        this.setEnabled(false);
        this.setVisible(false);
        return (T) this;
    }

    /**
     * Включает элемент (setEnabled(true) и setVisible(true)).
     */
    public T enableFull() {
        this.setEnabled(true);
        this.setVisible(true);
        return (T) this;
    }

    public static void drawRect(int xStart, int yStart, int xEnd, int yEnd, int color) {	
        int j1;
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
        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;      
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double) xStart, (double) yEnd, 0.0D).endVertex();
        bufferbuilder.pos((double) xEnd, (double) yEnd, 0.0D).endVertex();
        bufferbuilder.pos((double) xEnd, (double) yStart, 0.0D).endVertex();
        bufferbuilder.pos((double) xStart, (double) yStart, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(int xStart, int yStart, int xEnd, int yEnd, int colorDec1, int colorDec2) {   	
        float f = (float) (colorDec1 >> 24 & 255) / 255.0F;
        float f1 = (float) (colorDec1 >> 16 & 255) / 255.0F;
        float f2 = (float) (colorDec1 >> 8 & 255) / 255.0F;
        float f3 = (float) (colorDec1 & 255) / 255.0F;
        float f4 = (float) (colorDec2 >> 24 & 255) / 255.0F;
        float f5 = (float) (colorDec2 >> 16 & 255) / 255.0F;
        float f6 = (float) (colorDec2 >> 8 & 255) / 255.0F;
        float f7 = (float) (colorDec2 & 255) / 255.0F;       
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) xStart, (double) yEnd, 0.0D).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) xEnd, (double) yEnd, 0.0D).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) xEnd, (double) yStart, 0.0D).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double) xStart, (double) yStart, 0.0D).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    } 
}

