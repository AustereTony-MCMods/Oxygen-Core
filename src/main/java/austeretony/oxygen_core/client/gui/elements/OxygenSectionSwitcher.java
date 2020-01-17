package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.api.EnumBaseClientSetting;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenSectionSwitcher extends GUISimpleElement<OxygenSectionSwitcher> {

    private final OxygenSectionsSwitcherEntry[] elements;

    private SectionChangeListener sectionChangeListener;

    public OxygenSectionSwitcher(int xPosition, int yPosition, AbstractGUISection... sections) {           
        this.setTextScale(EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat());
        this.setDisplayText(sections[0].getDisplayText());

        int 
        textWidth, 
        width = 0;
        for (AbstractGUISection section : sections) {
            textWidth = this.textWidth(section.getDisplayText(), this.getTextScale());
            if (width == 0 || width < textWidth)
                width = textWidth;
        }
        this.setSize(width + 8, 9);
        this.setPosition(xPosition - this.getWidth(), yPosition);        

        this.elements = new OxygenSectionsSwitcherEntry[sections.length];

        int index = 0;               
        for (AbstractGUISection section : sections)
            this.bind(this.elements[index++] = new OxygenSectionsSwitcherEntry(this.getX(), this.getY() + index * 9 + 2, this.getWidth(), 9, section));

        this.setDynamicBackgroundColor(EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get().asInt(), EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt(), 0);
        this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());
        this.enableFull();
    }

    public void setSectionChangeListener(SectionChangeListener listener) {
        this.sectionChangeListener = listener;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (this.isDragged()) {
                int size = this.elements.length + 1;

                //background
                drawRect(0, this.getHeight() + 2, this.getWidth(), this.getHeight() * size + 2, this.getEnabledBackgroundColor());

                //frame
                OxygenGUIUtils.drawRect(0.0D, this.getHeight() + 2.0D, 0.4D, this.getHeight() * size + 1.6D, this.getDisabledBackgroundColor());
                OxygenGUIUtils.drawRect(this.getWidth() - 0.4D, this.getHeight() + 2.0D, this.getWidth(), this.getHeight() * size + 1.6D, this.getDisabledBackgroundColor());
                OxygenGUIUtils.drawRect(0.0D, this.getHeight() + 1.6D, this.getWidth(), this.getHeight() + 2.0D, this.getDisabledBackgroundColor());
                OxygenGUIUtils.drawRect(0.0D, this.getHeight() * size + 2.4F, this.getWidth(), this.getHeight() * size + 2.0D, this.getDisabledBackgroundColor());
            }

            GlStateManager.pushMatrix();           
            GlStateManager.translate(2.0F, this.getHeight() - this.textHeight(this.getTextScale()) - 1.0F, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int 
            iconU = 0, 
            textColor = this.getEnabledTextColor();                      
            if (!this.isEnabled()) {           
                iconU = 3;
                textColor = this.getDisabledTextColor();           
            } else if (this.isHovered() && !this.isDragged()) {      
                iconU = 6;
                textColor = this.getHoveredTextColor();       
            }
            this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, textColor, false);

            GlStateManager.popMatrix();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            this.mc.getTextureManager().bindTexture(OxygenGUITextures.SORT_DOWN_ICONS);
            GUIAdvancedElement.drawCustomSizedTexturedRect(this.textWidth(this.getDisplayText(), this.getTextScale()) + 4, (this.getHeight() - 3) / 2 + 1, iconU, 0, 3, 3, 9, 3);

            GlStateManager.popMatrix();      

            if (this.isDragged())               
                for (OxygenSectionsSwitcherEntry element : this.elements)                 
                    element.draw(mouseX, mouseY); 
        }
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {   
        if (this.isEnabled()) {
            this.setHovered(mouseX >= this.getX() && mouseY >= this.getY() && mouseX <= this.getX() + (int) (this.getScale() * this.getWidth()) && mouseY < this.getY() + (int) (this.getScale() * (this.isDragged() ? this.getHeight() * (this.elements.length + 1) : this.getHeight())));   
            if (this.isDragged())  
                for (OxygenSectionsSwitcherEntry element : this.elements)
                    element.mouseOver(mouseX, mouseY);         
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {      
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);                         
        if (flag && mouseButton == 0) {                  
            for (OxygenSectionsSwitcherEntry element : this.elements) {                           
                if (element.mouseClicked(mouseX, mouseY, mouseButton)) {                                                                
                    this.setDragged(false);   
                    element.setHovered(false);                      
                    if (this.sectionChangeListener != null)
                        this.sectionChangeListener.onChange(element.section);
                    if (this.screen.getWorkspace().getCurrentSection() != element.section)
                        element.section.open();
                    if (EnumBaseClientSetting.ENABLE_SOUND_EFFECTS.get().asBoolean())
                        this.mc.player.playSound(OxygenSoundEffects.CONTEXT_CLOSE.soundEvent, 0.5F, 1.0F);
                    return true;
                }
            }
        }       
        if (flag && mouseButton == 0 && !this.isDragged() && EnumBaseClientSetting.ENABLE_SOUND_EFFECTS.get().asBoolean())
            this.mc.player.playSound(OxygenSoundEffects.DROP_DOWN_LIST_OPEN.soundEvent, 0.5F, 1.0F);
        this.setDragged(flag && mouseButton == 0);      
        return false;
    }

    public static interface SectionChangeListener {

        void onChange(AbstractGUISection newSection);
    }

    public static class OxygenSectionsSwitcherEntry extends GUISimpleElement<OxygenSectionsSwitcherEntry> {

        public final AbstractGUISection section;

        public OxygenSectionsSwitcherEntry(int xPosition, int yPosition, int width, int height, AbstractGUISection section) {
            this.section = section;
            this.setPosition(xPosition, yPosition);
            this.setSize(width, height);
            this.setTextScale(EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat());
            this.setStaticBackgroundColor(EnumBaseGUISetting.ELEMENT_HOVERED_COLOR.get().asInt());
            this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());
            this.setEnabled(section.isEnabled());
        }

        @Override
        public void draw(int mouseX, int mouseY) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (this.isHovered()) {
                int third = this.getWidth() / 3;

                OxygenGUIUtils.drawGradientRect(0.0D, 0.0D, third, this.getHeight(), 0x00000000, this.getStaticBackgroundColor(), EnumGUIAlignment.RIGHT);
                drawRect(third, 0, this.getWidth() - third, this.getHeight(), this.getStaticBackgroundColor());
                OxygenGUIUtils.drawGradientRect(this.getWidth() - third, 0.0D, this.getWidth(), this.getHeight(), 0x00000000, this.getStaticBackgroundColor(), EnumGUIAlignment.LEFT);
            }

            GlStateManager.pushMatrix();           
            GlStateManager.translate(2.0F, this.getHeight() - this.textHeight(this.getTextScale()) - 2.0F, 0.0F);            
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
}
