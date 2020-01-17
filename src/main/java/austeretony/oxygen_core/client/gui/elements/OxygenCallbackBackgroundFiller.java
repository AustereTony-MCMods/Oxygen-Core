package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenCallbackBackgroundFiller extends GUISimpleElement<OxygenBackgroundFiller> {

    public OxygenCallbackBackgroundFiller(int xPosition, int yPosition, int width, int height) {             
        this.setPosition(xPosition, yPosition);         
        this.setSize(width, height);
        this.setDynamicBackgroundColor(EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get().asInt(), EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt(), 0);
    }

    @Override
    public void draw(int mouseX, int mouseY) {  
        GlStateManager.pushMatrix();            
        GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);   

        //main background  
        drawRect(0, 0, this.getWidth(), this.getHeight(), this.getEnabledBackgroundColor());      

        //title underline
        OxygenGUIUtils.drawRect(4.0D, 13.0D, this.getWidth() - 4.0D, 13.4D, this.getDisabledBackgroundColor());

        //frame
        OxygenGUIUtils.drawRect(0.0D, 0.0D, 0.4D, this.getHeight(), this.getDisabledBackgroundColor());
        OxygenGUIUtils.drawRect(this.getWidth() - 0.4D, 0.0D, this.getWidth(), this.getHeight(), this.getDisabledBackgroundColor());
        OxygenGUIUtils.drawRect(0.0D, 0.0D, this.getWidth(), 0.4D, this.getDisabledBackgroundColor());
        OxygenGUIUtils.drawRect(0.0D, this.getHeight() - 0.4D, this.getWidth(), this.getHeight(), this.getDisabledBackgroundColor());

        GlStateManager.popMatrix();            
    }  
}
