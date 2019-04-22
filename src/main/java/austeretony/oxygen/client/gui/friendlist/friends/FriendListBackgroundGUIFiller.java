package austeretony.oxygen.client.gui.friendlist.friends;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.oxygen.client.gui.BackgroundGUIFiller;
import austeretony.oxygen.client.gui.friendlist.FriendListGUIScreen;
import austeretony.oxygen.client.gui.settings.GUISettings;
import net.minecraft.client.renderer.GlStateManager;

public class FriendListBackgroundGUIFiller extends BackgroundGUIFiller {

    public FriendListBackgroundGUIFiller(int xPosition, int yPosition, int width, int height) {             
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
                this.mc.getTextureManager().bindTexture(FriendListGUIScreen.FRIEND_LIST_BACKGROUND_TEXTURE);                         
                GUIAdvancedElement.drawCustomSizedTexturedRect( - GUISettings.instance().getTextureOffsetX(), - GUISettings.instance().getTextureOffsetY(), 0, 0, this.textureWidth, this.textureHeight, this.textureWidth, this.textureHeight);             
                GlStateManager.disableBlend();   
            } else {
                drawRect(- 1, - 1, this.getWidth() + 1, this.getHeight() + 1, GUISettings.instance().getBaseGUIBackgroundColor());//main background
                drawRect(0, 0, this.getWidth(), 13, GUISettings.instance().getAdditionalGUIBackgroundColor());//title background
                drawRect(0, 14, 76, 23, GUISettings.instance().getAdditionalGUIBackgroundColor());//search panel background
                drawRect(77, 14, this.getWidth(), 23, GUISettings.instance().getAdditionalGUIBackgroundColor());//status background
                drawRect(0, 24, this.getWidth(), 34, GUISettings.instance().getAdditionalGUIBackgroundColor());//sorters background
                drawRect(0, 35, this.getWidth() - 3, this.getHeight() - 14, GUISettings.instance().getPanelGUIBackgroundColor());//panel background
                drawRect(this.getWidth() - 2, 35, this.getWidth(), this.getHeight() - 14, GUISettings.instance().getAdditionalGUIBackgroundColor());//slider background
                drawRect(0, this.getHeight() - 13, this.getWidth(), this.getHeight(), GUISettings.instance().getAdditionalGUIBackgroundColor());//add button background
            }
            GlStateManager.popMatrix();            
        }
    }
}
