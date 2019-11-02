package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;

public class SectionsGUIDDList extends GUISimpleElement<SectionsGUIDDList> {

    private final SectionsGUIDDListElement[] elements;

    private SectionChangeListener sectionChangeListener;

    public SectionsGUIDDList(int xPosition, int yPosition, AbstractGUISection... sections) {           
        this.setScale(GUISettings.get().getDropDownListScale());
        this.setTextScale(GUISettings.get().getTextScale());
        this.setDisplayText(sections[0].getDisplayText());

        int 
        textWidth, 
        width = 0,
        height = this.textHeight(this.getTextScale());
        for (AbstractGUISection section : sections) {
            textWidth = this.textWidth(section.getDisplayText(), this.getTextScale());
            if (width == 0 || width < textWidth)
                width = textWidth;
        }
        this.setSize(width + 8, height + 2);
        this.setPosition(xPosition - this.getWidth(), yPosition);        

        this.elements = new SectionsGUIDDListElement[sections.length];

        int index = 0;               
        SectionsGUIDDListElement element;
        for (AbstractGUISection section : sections) {
            element = new SectionsGUIDDListElement(section);
            element.initScreen(this.getScreen());
            element.setPosition(this.getX(), this.getY() + this.getHeight() * (index + 1));                
            element.setSize(this.getWidth(), this.getHeight());   
            element.setScale(this.getScale());
            element.setTextScale(this.getTextScale());
            this.elements[index++] = element;
            this.bind(element);
        }

        this.setDynamicBackgroundColor(GUISettings.get().getBaseGUIBackgroundColor(), GUISettings.get().getAdditionalGUIBackgroundColor(), 0);
        this.setTextDynamicColor(GUISettings.get().getEnabledTextColor(), GUISettings.get().getDisabledTextColor(), GUISettings.get().getHoveredTextColor());
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
                //background
                drawRect(0, this.getHeight(), this.getWidth(), (this.getHeight() + 1) * (this.elements.length + 1), this.getEnabledBackgroundColor());

                //frame
                int size = this.elements.length;
                CustomRectUtils.drawRect(0.0D, this.getHeight(), 0.4D, (this.getHeight() + 1.0D) * (size + 1) - 0.5D, this.getDisabledBackgroundColor());
                CustomRectUtils.drawRect(this.getWidth() - 0.4D, this.getHeight(), this.getWidth(), (this.getHeight() + 1.0D) * (size + 1) - 0.4D, this.getDisabledBackgroundColor());
                CustomRectUtils.drawRect(0.0D, this.getHeight(), this.getWidth(), this.getHeight() + 0.4D, this.getDisabledBackgroundColor());
                CustomRectUtils.drawRect(0.0D, (this.getHeight() + 1.0D) * (size + 1) - 0.4D, this.getWidth(), (this.getHeight() + 1.0D) * (size + 1), this.getDisabledBackgroundColor());
            }

            GlStateManager.pushMatrix();           
            GlStateManager.translate(4.0F, (this.getHeight() - this.textHeight(this.getTextScale())) / 2, 0.0F);            
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
            GUIAdvancedElement.drawCustomSizedTexturedRect(this.textWidth(this.getDisplayText(), this.getTextScale()) + 6, (this.getHeight() - 3) / 2, iconU, 0, 3, 3, 9, 3);

            GlStateManager.popMatrix();      

            if (this.isDragged())               
                for (SectionsGUIDDListElement element : this.elements)                 
                    element.draw(mouseX, mouseY); 
        }
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {   
        if (this.isEnabled()) {
            this.setHovered(mouseX >= this.getX() && mouseY >= this.getY() && mouseX <= this.getX() + (int) (this.getScale() * this.getWidth()) && mouseY < this.getY() + (int) (this.getScale() * (this.isDragged() ? this.getHeight() * (this.elements.length + 1) : this.getHeight())));   
            if (this.isDragged())  
                for (SectionsGUIDDListElement element : this.elements)
                    element.mouseOver(mouseX, mouseY);         
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {      
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);                         
        if (flag && mouseButton == 0) {                  
            for (SectionsGUIDDListElement element : this.elements) {                           
                if (element.mouseClicked(mouseX, mouseY, mouseButton)) {                                                                
                    this.setDragged(false);   
                    element.setHovered(false);                      
                    if (this.sectionChangeListener != null)
                        this.sectionChangeListener.onChange(element.section);
                    if (this.screen.getWorkspace().getCurrentSection() != element.section)
                        element.section.open();
                    this.mc.player.playSound(OxygenSoundEffects.CONTEXT_CLOSE.soundEvent, 0.5F, 1.0F);
                    return true;
                }
            }
        }       
        if (flag && mouseButton == 0 && !this.isDragged())
            this.mc.player.playSound(OxygenSoundEffects.DROP_DOWN_LIST_OPEN.soundEvent, 0.5F, 1.0F);
        this.setDragged(flag && mouseButton == 0);      
        return false;
    }

    public static interface SectionChangeListener {

        void onChange(AbstractGUISection newSection);
    }
}
