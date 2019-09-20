package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import net.minecraft.client.renderer.GlStateManager;

public abstract class BackgroundGUIFiller extends GUISimpleElement<BackgroundGUIFiller> {

    public BackgroundGUIFiller(int xPosition, int yPosition, int width, int height) {             
        this.setPosition(xPosition, yPosition);         
        this.setSize(width, height);
        this.setDynamicBackgroundColor(GUISettings.get().getBaseGUIBackgroundColor(), GUISettings.get().getAdditionalGUIBackgroundColor(), 0);
    }

    @Override
    public void draw(int mouseX, int mouseY) {  
        GlStateManager.pushMatrix();            
        GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);   

        this.drawBackground();

        GlStateManager.popMatrix();            
    }

    public abstract void drawBackground();
}