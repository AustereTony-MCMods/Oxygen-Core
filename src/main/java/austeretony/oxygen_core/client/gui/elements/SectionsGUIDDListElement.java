package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import net.minecraft.client.renderer.GlStateManager;

public class SectionsGUIDDListElement extends GUISimpleElement<SectionsGUIDDListElement> {

    public final AbstractGUISection section;

    public SectionsGUIDDListElement(AbstractGUISection section) {
        this.section = section;
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

        GlStateManager.pushMatrix();           
        GlStateManager.translate(4.0F, (this.getHeight() - this.textHeight(this.getTextScale())) / 2, 0.0F);            
        GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        int textColor = this.getEnabledTextColor();                      
        if (!this.isEnabled())                  
            textColor = this.getDisabledTextColor();           
        else if (this.isHovered())                                          
            textColor = this.getHoveredTextColor();                                        
        this.mc.fontRenderer.drawString(this.section.getDisplayText(), 0, 0, textColor, false);

        GlStateManager.popMatrix();

        GlStateManager.popMatrix();
    }
}