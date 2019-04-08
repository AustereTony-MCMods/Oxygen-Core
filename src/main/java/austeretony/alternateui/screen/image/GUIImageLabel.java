package austeretony.alternateui.screen.image;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Графический элемент в виде изображения (заливка, текстура или ItemStack).
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIImageLabel extends GUIAdvancedElement<GUIImageLabel> {

    private ItemStack itemStack;

    private boolean hasItemStack, isStackOverlayEnabled;

    public GUIImageLabel(int xPosition, int yPosition) {		
        this.setPosition(xPosition, yPosition);		
        this.enableFull();
    }

    public GUIImageLabel(int xPosition, int yPosition, int width, int height) {		
        this(xPosition, yPosition);
        this.setSize(width, height);
    }

    @Override
    public void draw(int mouseX, int mouseY) {  
        if (this.isVisible()) {       	
            GlStateManager.pushMatrix();            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);                              	
            if (this.isDebugMode())  		
                this.drawRect(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), this.getDebugColor());	     	
            if (this.isStaticBackgroundEnabled())    		                
                this.drawRect(ZERO, ZERO, this.getWidth(), this.getHeight(), this.getStaticBackgroundColor());
            if (this.isDynamicBackgroundEnabled()) {       		
                int color;        		
                if (!this.isEnabled())               	
                    color = this.getDisabledColor();            
                else if (this.isHovered() || this.isToggled())              	
                    color = this.getHoveredColor();              
                else             	
                    color = this.getEnabledColor();  		
                this.drawRect(ZERO, ZERO, this.getWidth(), this.getHeight(), color);
            } 
            if (this.isTextureEnabled()) {   
                GlStateManager.enableBlend();    
                this.mc.getTextureManager().bindTexture(this.getTexture());                         
                this.drawCustomSizedTexturedRect(ZERO, ZERO, this.getTextureU(), this.getTextureV(), this.getTextureWidth(), this.getTextureHeight(), this.getImageWidth(), this.getImageHeight());       	
                GlStateManager.disableBlend();      
            }        	
            if (this.hasItemStack()) {            	
                RenderHelper.enableGUIStandardItemLighting();        
                RenderHelper.enableStandardItemLighting();	   	        
                itemRender.renderItemAndEffectIntoGUI(this.getItemStack(), ZERO, ZERO);                
                if (this.isStackOverlayEnabled())               	
                    itemRender.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.getItemStack(), ZERO, ZERO, null);
            }        	
            GlStateManager.popMatrix();
        }
    }

    public boolean hasItemStack() {  	
        return this.hasItemStack;
    }

    public ItemStack getItemStack() {	
        return this.itemStack;
    }

    public GUIImageLabel setItemStack(ItemStack itemStack) {		
        this.itemStack = itemStack;
        this.hasItemStack = true;		
        return this;
    }

    public void removeItemStack() {				
        this.hasItemStack = false;		
        this.itemStack = null;
    }

    public boolean isStackOverlayEnabled() {    	
        return this.isStackOverlayEnabled;
    }

    public GUIImageLabel enableStackOverlay() {   	
        this.isStackOverlayEnabled = true;		
        return this;
    }
}
