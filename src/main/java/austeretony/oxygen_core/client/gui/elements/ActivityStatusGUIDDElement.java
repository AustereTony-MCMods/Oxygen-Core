package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.server.OxygenPlayerData.EnumActivityStatus;
import net.minecraft.client.renderer.GlStateManager;

public class ActivityStatusGUIDDElement extends GUISimpleElement<ActivityStatusGUIDDElement> {

    public final EnumActivityStatus activityStatus;

    public ActivityStatusGUIDDElement(EnumActivityStatus activityStatus) {
        this.activityStatus = activityStatus;
        this.setDisplayText(this.activityStatus.localizedName());
        this.setStaticBackgroundColor(GUISettings.get().getHoveredElementColor());
        this.setTextDynamicColor(GUISettings.get().getEnabledTextColor(), GUISettings.get().getDisabledTextColor(), GUISettings.get().getHoveredTextColor());
        this.enableFull();
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        GlStateManager.pushMatrix();           
        GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
        GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.isHovered()) {
            int third = this.getWidth() / 3;

            CustomRectUtils.drawGradientRect(0.0D, 0.0D, third, this.getHeight(), 0x00000000, this.getStaticBackgroundColor(), EnumGUIAlignment.RIGHT);
            drawRect(third, 0, this.getWidth() - third, this.getHeight(), this.getStaticBackgroundColor());
            CustomRectUtils.drawGradientRect(this.getWidth() - third, 0.0D, this.getWidth(), this.getHeight(), 0x00000000, this.getStaticBackgroundColor(), EnumGUIAlignment.LEFT);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(OxygenGUITextures.STATUS_ICONS);
        GUIAdvancedElement.drawCustomSizedTexturedRect(0, 3, this.activityStatus.ordinal() * 3, 0, 3, 3, 12, 3);

        GlStateManager.pushMatrix();           
        GlStateManager.translate(4.0F, (this.getHeight() - this.textHeight(this.getTextScale())) / 2, 0.0F);            
        GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        int textColor = this.getEnabledTextColor();                      
        if (!this.isEnabled())                  
            textColor = this.getDisabledTextColor();           
        else if (this.isHovered())                                          
            textColor = this.getHoveredTextColor();                                        
        this.mc.fontRenderer.drawString(this.getDisplayText(), 4, 0, textColor, false);

        GlStateManager.popMatrix();

        GlStateManager.popMatrix();
    }
}
