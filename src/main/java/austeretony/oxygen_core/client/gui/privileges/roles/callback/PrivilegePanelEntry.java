package austeretony.oxygen_core.client.gui.privileges.roles.callback;

import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.client.gui.elements.OxygenIndexedPanelEntry;
import austeretony.oxygen_core.common.privilege.PrivilegeRegistry.PrivilegeRegistryEntry;
import net.minecraft.client.renderer.GlStateManager;

public class PrivilegePanelEntry extends OxygenIndexedPanelEntry<PrivilegeRegistryEntry> {

    private final String idStr, nameStr, typeStr;

    public PrivilegePanelEntry(PrivilegeRegistryEntry entry) {
        super(entry);
        this.setDynamicBackgroundColor(EnumBaseGUISetting.ELEMENT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.ELEMENT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.ELEMENT_HOVERED_COLOR.get().asInt());
        this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());
        this.idStr = String.valueOf(entry.id);
        this.nameStr = entry.name;
        this.typeStr = entry.type.toString();
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {          
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);  

            int color;    
            if (!this.isEnabled())                  
                color = this.getDisabledBackgroundColor();
            else if (this.isHovered() || this.isToggled())                  
                color = this.getHoveredBackgroundColor();
            else                    
                color = this.getEnabledBackgroundColor();     

            int third = this.getWidth() / 3;
            OxygenGUIUtils.drawGradientRect(0.0D, 0.0D, third, this.getHeight(), 0x00000000, color, EnumGUIAlignment.RIGHT);
            drawRect(third, 0, this.getWidth() - third, this.getHeight(), color);
            OxygenGUIUtils.drawGradientRect(this.getWidth() - third, 0.0D, this.getWidth(), this.getHeight(), 0x00000000, color, EnumGUIAlignment.LEFT);

            if (!this.isEnabled())                  
                color = this.getDisabledTextColor();           
            else if (this.isHovered() || this.isToggled())                                          
                color = this.getHoveredTextColor();
            else                    
                color = this.getEnabledTextColor();

            float yPos = (this.getHeight() - this.textHeight(this.getTextScale())) / 2.0F + 1.0F;

            //id
            GlStateManager.pushMatrix();           
            GlStateManager.translate(2.0F, yPos, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);           
            this.mc.fontRenderer.drawString(this.idStr, 0, 0, color, this.isTextShadowEnabled());
            GlStateManager.popMatrix();    

            //name
            GlStateManager.pushMatrix();           
            GlStateManager.translate(24.0F, yPos, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);           
            this.mc.fontRenderer.drawString(this.nameStr, 0, 0, color, this.isTextShadowEnabled());
            GlStateManager.popMatrix();     
            
            //value
            GlStateManager.pushMatrix();           
            GlStateManager.translate(150.0F, yPos, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);           
            this.mc.fontRenderer.drawString(this.typeStr, 0, 0, color, this.isTextShadowEnabled());
            GlStateManager.popMatrix();  

            GlStateManager.popMatrix();
        }
    }
}
