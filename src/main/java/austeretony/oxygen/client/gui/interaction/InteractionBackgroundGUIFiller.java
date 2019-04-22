package austeretony.oxygen.client.gui.interaction;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.oxygen.client.gui.BackgroundGUIFiller;
import austeretony.oxygen.client.gui.settings.GUISettings;
import net.minecraft.client.renderer.GlStateManager;

public class InteractionBackgroundGUIFiller extends BackgroundGUIFiller {

    public InteractionBackgroundGUIFiller(int xPosition, int yPosition, int width, int height) {             
        super(xPosition, yPosition, width, height);
    }

    @Override
    public void draw(int mouseX, int mouseY) {  
        if (this.isVisible()) {         
            GlStateManager.pushMatrix();            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);                      
            if (GUISettings.instance().shouldUseTextures()) {  
                GlStateManager.enableBlend();    
                this.mc.getTextureManager().bindTexture(InteractionGUIScreen.BACKGROUND_TEXTURE);                         
                GUIAdvancedElement.drawCustomSizedTexturedRect( - GUISettings.instance().getTextureOffsetX(), - GUISettings.instance().getTextureOffsetY(), 0, 0, this.textureWidth, this.textureHeight, this.textureWidth, this.textureHeight);             
                GlStateManager.disableBlend();   
            } else {                
                drawCircle(60, 58, 50, 42, 68, GUISettings.instance().getBaseGUIBackgroundColor());
                drawCircle(60, 58, 50, 43, 67, GUISettings.instance().getAdditionalGUIBackgroundColor());
            }
            GlStateManager.popMatrix();            
        }
    }
}
