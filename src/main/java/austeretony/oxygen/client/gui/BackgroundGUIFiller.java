package austeretony.oxygen.client.gui;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.gui.settings.GUISettings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public abstract class BackgroundGUIFiller extends GUISimpleElement<BackgroundGUIFiller> {

    private final int textureWidth, textureHeight;

    private final ResourceLocation texture;

    private final boolean textureExist;

    public BackgroundGUIFiller(int xPosition, int yPosition, int width, int height, ResourceLocation texture) {             
        this.setPosition(xPosition, yPosition);         
        this.setSize(width, height);
        this.textureWidth = width + GUISettings.instance().getTextureOffsetX() * 2;
        this.textureHeight = height + GUISettings.instance().getTextureOffsetY() * 2;
        this.texture = texture;
        this.textureExist = ClientReference.isTextureExist(texture);
    }

    @Override
    public void draw(int mouseX, int mouseY) {  
        GlStateManager.pushMatrix();            
        GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);                      
        if (this.textureExist) {  
            GlStateManager.enableBlend();    
            this.mc.getTextureManager().bindTexture(this.texture);                         
            GUIAdvancedElement.drawCustomSizedTexturedRect( - GUISettings.instance().getTextureOffsetX(), - GUISettings.instance().getTextureOffsetY(),
                    0, 0, this.textureWidth, this.textureHeight, this.textureWidth, this.textureHeight);             
            GlStateManager.disableBlend();   
        } else
            this.drawDefaultBackground();
        GlStateManager.popMatrix();            
    }

    public abstract void drawDefaultBackground();
}
