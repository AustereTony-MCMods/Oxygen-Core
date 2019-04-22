package austeretony.alternateui.screen.text;

import austeretony.alternateui.screen.core.GUISimpleElement;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Графический элемент в виде строки символов.
 * 
 * @author AustereTony
 */
public class GUITextLabel extends GUISimpleElement<GUITextLabel> {

    public GUITextLabel(int xPosition, int yPosition) {		
        this.setPosition(xPosition, yPosition);		
        this.enableFull();
    }

    public GUITextLabel(int xPosition, int yPosition, String diplayText) {		
        this(xPosition, yPosition);		
        this.setDisplayText(diplayText);
    }

    @Override
    public void draw(int mouseX, int mouseY) {   	
        if (this.isVisible()) { 
            GlStateManager.pushMatrix();            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);                                	
            if (this.isDebugMode())  		
                this.drawRect(0, 0, this.getWidth(), this.getHeight(), this.getDebugColor());	     	
            if (this.isStaticBackgroundEnabled())     		                
                this.drawRect(0, 0, this.getWidth(), this.getHeight(), this.getStaticBackgroundColor());
            if (this.hasDisplayText()) {   
                GlStateManager.pushMatrix();            
                GlStateManager.translate(this.getTextOffset(), (this.getHeight() - this.textHeight(this.getTextScale())) / 2, 0.0F);            
                GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  
                int color;       		
                if (!this.isEnabled())               	
                    color = this.getDisabledTextColor();
                else              	
                    color = this.getEnabledTextColor();                                             
                this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, color, this.isTextShadowEnabled());
                GlStateManager.popMatrix();
            }    
            GlStateManager.popMatrix();
        }
    }

    /**
     * Установка текста, отображаемого элементом. 
     * 
     * @param displayText
     * 
     * @return вызывающий объект
     */
    @Override
    public GUITextLabel setDisplayText(String displayText) {   	
        super.setDisplayText(displayText);    	
        this.setSize(this.textWidth(displayText, this.getTextScale()), FONT_HEIGHT);    	
        return this;
    }
}
