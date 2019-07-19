package austeretony.alternateui.screen.image;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Графический элемент в виде изображения (заливка, текстура или ItemStack).
 * 
 * @author AustereTony
 */
public class GUIImageLabel extends GUIAdvancedElement<GUIImageLabel> {

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
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            if (this.isDebugMode())  		
                drawRect(0, 0, this.getWidth(), this.getHeight(), this.getDebugColor());	     	
            if (this.isStaticBackgroundEnabled())    		                
                drawRect(0, 0, this.getWidth(), this.getHeight(), this.getStaticBackgroundColor());
            else if (this.isDynamicBackgroundEnabled()) {       		
                int color;        		
                if (!this.isEnabled())               	
                    color = this.getDisabledColor();            
                else if (this.isHovered() || this.isToggled())              	
                    color = this.getHoveredColor();              
                else             	
                    color = this.getEnabledColor();  		
                drawRect(0, 0, this.getWidth(), this.getHeight(), color);
            } else if (this.isTextureEnabled()) {   
                GlStateManager.enableBlend();    
                this.mc.getTextureManager().bindTexture(this.getTexture());                         
                drawCustomSizedTexturedRect(0, 0, this.getTextureU(), this.getTextureV(), this.getTextureWidth(), this.getTextureHeight(), this.getImageWidth(), this.getImageHeight());       	
                GlStateManager.disableBlend();      
            }        	    	
            GlStateManager.popMatrix();
        }
    } 
}
