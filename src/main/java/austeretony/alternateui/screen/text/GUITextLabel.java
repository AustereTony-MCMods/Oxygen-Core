package austeretony.alternateui.screen.text;

import austeretony.alternateui.screen.core.GUISimpleElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Графический элемент в виде строки символов.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
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
                this.drawRect(ZERO, ZERO, this.getWidth(), this.getHeight(), this.getDebugColor());	     	
            if (this.isStaticBackgroundEnabled())     		                
                this.drawRect(ZERO, ZERO, this.getWidth(), this.getHeight(), this.getStaticBackgroundColor());
            if (this.hasDisplayText()) {   
                GlStateManager.pushMatrix();            
                GlStateManager.translate(this.getTextOffset(), ((float) this.getHeight() - ((float) FONT_HEIGHT * this.getTextScale())) / 2, 0.0F);            
                GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  
                int color;       		
                if (!this.isEnabled())               	
                    color = this.getDisabledTextColor();
                else              	
                    color = this.getEnabledTextColor();                                             
                this.mc.fontRenderer.drawString(this.getDisplayText(), ZERO, ZERO, color, this.isTextShadowEnabled());
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
        this.setSize(this.width(displayText), FONT_HEIGHT);    	
        return this;
    }
}
