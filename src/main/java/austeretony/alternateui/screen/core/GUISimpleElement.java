package austeretony.alternateui.screen.core;

import austeretony.alternateui.util.AlternateUIReference;
import austeretony.alternateui.util.EnumGUIAlignment;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

/**
 * Класс-основа простых (без текстур) графических элементов ГПИ.
 * 
 * @author AustereTony
 */
public class GUISimpleElement<T extends GUIBaseElement> extends GUIBaseElement<T> {

    public final static int FONT_HEIGHT = 9;

    private boolean isDebugMode, isStatBackgroundEnabled, isDynBackgroundEnabled, isTextShadowEnabled, hasTooltip, hasDisplayText;

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

    protected RenderItem itemRender = AlternateUIReference.getRenderItem();

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) { 
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            if (this.isDebugMode())  		
                drawRect(0, 0, this.getWidth(), this.getHeight(), this.getDebugColor());	     	
            if (this.isStaticBackgroundEnabled())     		                
                drawRect(0, 0, this.getWidth(), this.getHeight(), this.getStaticBackgroundColor());
            else if (this.isDynamicBackgroundEnabled()) {    
                int color;        		
                if (!this.isEnabled())              	
                    color = this.getDisabledBackgroundColor();
                else if (this.isHovered() || this.isToggled())               	
                    color = this.getHoveredBackgroundColor();
                else               	
                    color = this.getEnabledBackgroundColor();   		                
                drawRect(0, 0, this.getWidth(), this.getHeight(), color);
            } 
            if (this.hasDisplayText()) {   
                int textOffset = (int) (this.getTextAlignment() == EnumGUIAlignment.CENTER ? 
                        (this.getWidth() - this.textWidth(this.getDisplayText(), this.getTextScale())) / 2 + this.getTextOffset() : (this.getTextAlignment() == EnumGUIAlignment.LEFT ? 
                                0 + this.getTextOffset() : this.getWidth() - this.textWidth(this.getDisplayText(), this.getTextScale()) - this.getTextOffset())); 
                GlStateManager.pushMatrix();           
                GlStateManager.translate(textOffset, (this.getHeight() - this.textHeight(this.getTextScale())) / 2, 0.0F);            
                GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  
                int color;       		
                if (!this.isEnabled())               	
                    color = this.getDisabledTextColor();           
                else if (this.isHovered() || this.isToggled())               	                	
                    color = this.getHoveredTextColor();
                else               	
                    color = this.getEnabledTextColor();                                           
                this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, color, this.isTextShadowEnabled());
                GlStateManager.popMatrix();
            }    
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {  	
        if (this.isVisible() && this.isHovered() && this.hasTooltip()) {   
            int width = (int) ((float) this.tooltipWidth * this.tooltipScaleFactor);
            GlStateManager.pushMatrix();           
            GlStateManager.translate(mouseX + this.screen.guiLeft + width >= this.screen.width ? mouseX - width : mouseX, mouseY - 11.0F, 0.0F);           
            GlStateManager.scale(this.tooltipScaleFactor, this.tooltipScaleFactor, 0.0F);    
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            drawRect(0, 0, this.tooltipWidth, 11, this.tooltipBackgroundColor);               
            this.mc.fontRenderer.drawString(this.tooltipText, 2, 2, this.tooltipTextColor);               
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {  	 
        this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + (int) (this.getWidth() * this.getScale()) && mouseY < this.getY() + (int) (this.getHeight() * this.getScale()));   
    }

    public T debugMode() {          
        this.isDebugMode = true;
        return (T) this;
    }

    public boolean isDebugMode() {    	
        return this.isDebugMode;
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

    public T initTooltip(String text) {  
        this.tooltipText = text;
        this.tooltipWidth = this.textWidth(text, 1.0F) + 4;
        this.hasTooltip = true;
        return (T) this;
    }

    public T initTooltip(String text, float scale) {  
        this.tooltipText = text;
        this.tooltipScaleFactor = scale;
        this.tooltipWidth = this.textWidth(text, 1.0F) + 4;
        this.hasTooltip = true;
        return (T) this;
    }

    public T initTooltip(String text, int textColorHex, int backgroundColorHex, float scale) {    
        this.tooltipText = text;
        this.tooltipScaleFactor = scale;
        this.tooltipWidth = this.textWidth(text, 1.0F) + 4;
        this.tooltipTextColor = textColorHex;
        this.tooltipBackgroundColor = backgroundColorHex;
        this.hasTooltip = true;
        return (T) this;
    }

    public boolean hasTooltip() {           
        return this.hasTooltip;
    }

    public String getTooltipText() {
        return this.tooltipText;
    }

    public void setTooltipText(String text) {
        this.tooltipText = text;
    }

    public float getTooltipScaleFactor() {
        return this.tooltipScaleFactor;
    }

    public void setTooltipScaleFactor(float scale) {
        this.tooltipScaleFactor = scale;
    }

    public int getTooltipWidth() {
        return this.tooltipWidth;
    }

    public void setTooltipWidth(int width) {
        this.tooltipWidth = width;
    }

    public int getTooltipTextColor() {
        return this.tooltipTextColor;
    }

    public void setTooltipTextColor(int colorHex) {
        this.tooltipTextColor = colorHex;
    }

    public int getTooltipBackgroundColor() {
        return this.tooltipBackgroundColor;
    }

    public void setTooltipBackgroundColor(int colorHex) {
        this.tooltipBackgroundColor = colorHex;
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

    public int textWidth(String text, float scaleFactor) {
        return (int) (this.mc.fontRenderer.getStringWidth(text) * scaleFactor);
    }

    public int textHeight(float scaleFactor) {
        return (int) (this.mc.fontRenderer.FONT_HEIGHT * scaleFactor);
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

    /**
     * Управляет состоянием элемента.
     */
    public T enableFull(boolean flag) {
        this.setEnabled(flag);
        this.setVisible(flag);
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

        float 
        alpha = (float) (color >> 24 & 255) / 255.0F,
        red = (float) (color >> 16 & 255) / 255.0F,
        green = (float) (color >> 8 & 255) / 255.0F,
        blue = (float) (color & 255) / 255.0F;      

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(red, green, blue, alpha);

        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos((double) xStart, (double) yEnd, 0.0D).endVertex();
        bufferBuilder.pos((double) xEnd, (double) yEnd, 0.0D).endVertex();
        bufferBuilder.pos((double) xEnd, (double) yStart, 0.0D).endVertex();
        bufferBuilder.pos((double) xStart, (double) yStart, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(int xStart, int yStart, int xEnd, int yEnd, int colorDec1, int colorDec2) {   	
        float 
        alpha1 = (float) (colorDec1 >> 24 & 255) / 255.0F,
        red1 = (float) (colorDec1 >> 16 & 255) / 255.0F,
        green1 = (float) (colorDec1 >> 8 & 255) / 255.0F,
        blue1 = (float) (colorDec1 & 255) / 255.0F,
        alpha2 = (float) (colorDec2 >> 24 & 255) / 255.0F,
        red2 = (float) (colorDec2 >> 16 & 255) / 255.0F,
        green2 = (float) (colorDec2 >> 8 & 255) / 255.0F,
        blue2 = (float) (colorDec2 & 255) / 255.0F;   

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);  

        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos((double) xStart, (double) yEnd, 0.0D).color(red1, green1, blue1, alpha1).endVertex();
        bufferBuilder.pos((double) xEnd, (double) yEnd, 0.0D).color(red1, green1, blue1, alpha1).endVertex();
        bufferBuilder.pos((double) xEnd, (double) yStart, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
        bufferBuilder.pos((double) xStart, (double) yStart, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
        tessellator.draw();

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    } 

    public static void drawCircle(int x, int y, int steps, int radius, int color) {
        float 
        green = (float) (color >> 8 & 255) / 255.0F,
        red = (float) (color >> 16 & 255) / 255.0F,
        alpha = (float) (color >> 24 & 255) / 255.0F,
        blue = (float) (color & 255) / 255.0F;

        double angle;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(red, green, blue, alpha);  

        bufferBuilder.begin(6, DefaultVertexFormats.POSITION);
        bufferBuilder.pos((double) x, (double) y, 0.0D).endVertex();
        for (double i = 0.0D; i <= (double) steps; i++)  {
            angle = (Math.PI * 2.0D * i / (double) steps) + Math.toRadians(180.0D);
            bufferBuilder.pos((double) x + Math.sin(angle) * (double) radius, (double) y + Math.cos(angle) * (double) radius, 0.0D).endVertex();
        }
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawCircle(int x, int y, int steps, int innerRadius, int outerRadius, int color) {
        float 
        green = (float) (color >> 8 & 255) / 255.0F,
        red = (float) (color >> 16 & 255) / 255.0F,
        alpha = (float) (color >> 24 & 255) / 255.0F,
        blue = (float) (color & 255) / 255.0F;

        double 
        doublePI = Math.PI * 2.0D, 
        angle1, 
        angle2;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(red, green, blue, alpha);  

        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        for (double i = 0.0D; i < (double) steps; i++) {
            angle1 = ((doublePI) / (double) steps);
            angle2 = i * angle1 + doublePI * 2.0D - doublePI / 2.0D;
            bufferBuilder.pos((double) x + Math.sin(angle2) * outerRadius, (double) y + Math.cos(angle2) * outerRadius, 0.0D).endVertex();
            bufferBuilder.pos((double) x + Math.sin(angle2 + angle1) * outerRadius, (double) y + Math.cos(angle2 + angle1) * outerRadius, 0.0D).endVertex();
            bufferBuilder.pos((double) x + Math.sin(angle2 + angle1) * innerRadius, (double) y + Math.cos(angle2 + angle1) * innerRadius, 0.0D).endVertex();
            bufferBuilder.pos((double) x + Math.sin(angle2) * innerRadius, (double) y + Math.cos(angle2) * innerRadius, 0.0D).endVertex();
        }
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}

