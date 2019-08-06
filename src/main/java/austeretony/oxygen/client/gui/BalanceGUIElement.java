package austeretony.oxygen.client.gui;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen.client.gui.settings.GUISettings;
import net.minecraft.client.renderer.GlStateManager;

public class BalanceGUIElement extends GUISimpleElement<BalanceGUIElement> {

    private boolean isRed;

    private int balance;

    public BalanceGUIElement(int x, int y) {
        this.setPosition(x, y);
        this.setSize(6, 6);
        this.setBalance(0);
        this.enableFull();
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.enableBlend(); 
            this.mc.getTextureManager().bindTexture(OxygenGUITextures.COIN_ICON);
            GUIAdvancedElement.drawCustomSizedTexturedRect(0, 0, 0, 0, 6, 6, 6, 6);          
            GlStateManager.disableBlend();
            GlStateManager.pushMatrix();           
            GlStateManager.translate(- 1.0F - this.textWidth(this.getDisplayText(), GUISettings.instance().getSubTextScale()), 1.0F, 0.0F);            
            GlStateManager.scale(GUISettings.instance().getSubTextScale(), GUISettings.instance().getSubTextScale(), 0.0F);                                      
            this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, this.isRed ? 0xFFCC0000 : this.getEnabledTextColor(), false);
            GlStateManager.popMatrix();

            GlStateManager.popMatrix();
        }
    }

    public BalanceGUIElement setBalance(int value) {
        this.balance = value;
        this.setDisplayText(String.valueOf(value));
        return this;
    }

    public int getBalance() {
        return this.balance;
    }

    public void setRed(boolean flag) {
        this.isRed = flag;   
    }
}
