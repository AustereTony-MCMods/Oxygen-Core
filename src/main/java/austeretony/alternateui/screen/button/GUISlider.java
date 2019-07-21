package austeretony.alternateui.screen.button;

import org.lwjgl.input.Mouse;

import austeretony.alternateui.screen.browsing.GUIScroller;
import austeretony.alternateui.screen.core.GUIAdvancedElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Слайдер для скроллера.
 * 
 * @author AustereTony
 */
public class GUISlider extends GUIAdvancedElement<GUISlider> {

    private GUIScroller scroller;

    private ResourceLocation slidebarTexture;

    private boolean isSlidebarBackgroundEnabled, isSlidebarTextureEnabled;

    private int slidebarX, slidebarY, slidebarWidth, slidebarHeight, slidebarTextureU, slidebarTextureV;     

    /**
     * Слайдер для скроллера.
     * @param xPosition позиция слайдера по x
     * @param yPosition позиция слайдера по y
     * @param sliderWidth ширина слайдера
     * @param sliderHeight высота слайдера
     */
    public GUISlider(int xPosition, int yPosition, int sliderWidth, int sliderHeight) {
        this.setPosition(xPosition, yPosition);
        this.setSize(sliderWidth, sliderHeight);
        this.setSlidebarPosition(xPosition, yPosition);
        this.setEnabled(true);
        this.setVisible(true);
        this.enableSlidebarBackground();
        this.cancelDraggedElementLogic();
    }

    public void setScroller(GUIScroller scroller) {
        this.scroller = scroller;
        this.setSlidebarSize(this.getWidth(), (int) ((float) this.getHeight() / ((float) scroller.getRowsAmount() / (float) scroller.rowsVisible)) > 10 ? (int) ((float) this.getHeight() / ((float) scroller.getRowsAmount() / (float) scroller.rowsVisible)) : 10);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            if (this.isTextureEnabled()) {
                this.mc.getTextureManager().bindTexture(this.getTexture());      
                GlStateManager.enableBlend(); 
                drawCustomSizedTexturedRect((this.getWidth() - this.getTextureWidth()) / 2, (this.getHeight() - this.getTextureHeight()) / 2, this.getTextureU(), this.getTextureV(), this.getTextureWidth(), this.getTextureHeight(), this.getImageWidth(), this.getImageHeight());
                GlStateManager.disableBlend(); 
            }  
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();            
            GlStateManager.translate(this.getSlidebarX(), this.getSlidebarY(), 0.0F);
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);          
            if (this.isSlidebarTextureEnabled()) {
                int u = this.getSlidebarTextureU();
                this.mc.getTextureManager().bindTexture(this.getSlidebarTexture());          
                if (this.isHovered() || this.isToggled())
                    u += this.getSlidebarWidth() * 2;
                else
                    u += this.getSlidebarWidth();
                drawCustomSizedTexturedRect(0, 0, u, this.getSlidebarTextureV(), this.getTextureWidth(), this.getTextureHeight(), this.getImageWidth(), this.getImageHeight());
            }
            int color;
            if (!this.isEnabled())
                color = this.getDisabledBackgroundColor();
            else if (this.isHovered())
                color = this.getHoveredBackgroundColor();
            else
                color = this.getEnabledBackgroundColor();
            drawRect(0, 0, this.getSlidebarWidth(), this.getSlidebarHeight(), color);
            GlStateManager.popMatrix();
        }
    }       

    @Override
    public void mouseOver(int mouseX, int mouseY) {
        if (!this.isDragged())
            this.setHovered(this.isEnabled() && mouseX >= this.getSlidebarX() && mouseY >= this.getSlidebarY() && mouseX < this.getSlidebarX() + (int) (this.getSlidebarWidth() * this.getScale()) && mouseY < this.getSlidebarY() + (int) (this.getSlidebarHeight() * this.getScale()));   		    
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (Mouse.isButtonDown(0) && this.isHovered()) {
            this.setDragged(true);
            return true;
        }
        return false;
    }

    public void reset() {
        this.scroller.resetPosition();
        this.handleSlidebarViaScroller();
    }

    public void setSlidebarNotDragged() {
        this.setDragged(false);
        this.setHovered(false);
    }

    public void handleSlidebarViaScroller() {
        this.setSlidebarY(this.getY() + (int) ((((float) (this.getHeight() - this.getSlidebarHeight())) / (float) this.scroller.getMaxPosition()) * (float) this.scroller.getPosition()));
    }

    public void handleSlidebarViaCursor(int slidebarY) {
        this.setSlidebarY(slidebarY >= this.getY() ? (slidebarY <= this.getY() + (this.getHeight() - this.getSlidebarHeight()) ? slidebarY : this.getY() + (this.getHeight() - this.getSlidebarHeight())) : this.getY());
    }

    public boolean isSlidebarBackgroundEnabled() {
        return this.isSlidebarBackgroundEnabled;
    }

    public GUISlider enableSlidebarBackground() {
        this.isSlidebarBackgroundEnabled = true;
        return this;
    }

    public int getSlidebarX() {
        return this.slidebarX;
    }

    public void setSlidebarX(int xPosition) {
        this.slidebarX = xPosition;
    }

    public int getSlidebarY() {
        return this.slidebarY;
    }

    public void setSlidebarY(int yPosition) {
        this.slidebarY = yPosition;
    }

    public void setSlidebarPosition(int xPosition, int yPosition) {
        this.slidebarX = xPosition;
        this.slidebarY = yPosition;
    }

    public int getSlidebarWidth() {
        return this.slidebarWidth;
    }

    public void setSlidebarWidth(int slidebarWidth) {
        this.slidebarWidth = slidebarWidth;
    }

    public int getSlidebarHeight() {
        return this.slidebarHeight;
    }

    public void setSlidebarHeight(int slidebarHeight) {
        this.slidebarHeight = slidebarHeight;
    }

    public void setSlidebarSize(int slidebarWidth, int slidebarHeight) {
        this.slidebarWidth = slidebarWidth;
        this.slidebarHeight = slidebarHeight;
    }

    public boolean isSlidebarTextureEnabled() {
        return this.isSlidebarTextureEnabled;
    }

    public GUISlider enableSlidebarTexture() {
        this.isSlidebarTextureEnabled = true;
        return this;
    }

    public ResourceLocation getSlidebarTexture() {
        return this.slidebarTexture;
    }

    public int getSlidebarTextureU() {
        return this.slidebarTextureU;
    }

    public int getSlidebarTextureV() {
        return this.slidebarTextureV;
    }

    public GUISlider setSlidebarTexture(ResourceLocation texture) {
        this.slidebarTexture = texture;
        return this;
    }

    public GUISlider setSlidebarTexture(ResourceLocation texture, int u, int v) {
        this.slidebarTexture = texture;
        this.slidebarTextureU = u;
        this.slidebarTextureV = v;
        return this;	
    }
}