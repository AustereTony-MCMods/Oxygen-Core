package austeretony.oxygen_core.client.gui.elements;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenDropDownList extends GUISimpleElement<OxygenDropDownList> {

    private final List<OxygenDropDownListWrapperEntry> elements = new ArrayList<>(5);

    @Nullable
    private ElementClickListener elementClickListener;

    public OxygenDropDownList(int xPosition, int yPosition, int width, String displayText) {     
        this.setPosition(xPosition, yPosition);      
        this.setSize(width, 9);
        this.setTextScale(EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.05F);
        this.setDisplayText(displayText);
        this.setStaticBackgroundColor(EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt());
        this.setDynamicBackgroundColor(EnumBaseGUISetting.ELEMENT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.ELEMENT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.ELEMENT_HOVERED_COLOR.get().asInt());
        this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());
        this.enableFull();
    }

    public <T> void setElementClickListener(ElementClickListener<T> listener) {
        this.elementClickListener = listener;
    }

    public List<OxygenDropDownListWrapperEntry> getElements() {
        return this.elements;
    }

    public void addElement(OxygenDropDownListWrapperEntry element) {
        element.initScreen(this.getScreen());
        element.setPosition(this.getX(), this.getY() + this.getHeight() * (this.elements.size() + 1));                
        element.setSize(this.getWidth(), this.getHeight());   
        element.setScale(this.getScale());
        element.setTextScale(this.getTextScale());
        element.init();
        this.elements.add(element);  
        this.bind(element);
    }

    public void reset() {
        this.elements.clear();
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (this.isDragged()) {
                int size = this.elements.size() + 1;

                //background
                drawRect(0, this.getHeight(), this.getWidth(), this.getHeight() * size , this.getEnabledBackgroundColor());

                //frame
                OxygenGUIUtils.drawRect(0.0D, this.getHeight(), 0.4D, this.getHeight() * size, this.getStaticBackgroundColor());
                OxygenGUIUtils.drawRect(this.getWidth() - 0.4D, this.getHeight(), this.getWidth(), this.getHeight() * size, this.getStaticBackgroundColor());               
                OxygenGUIUtils.drawRect(0.0D, this.getHeight() + 0.4D, this.getWidth(), this.getHeight() + 0.4F, this.getStaticBackgroundColor());
                OxygenGUIUtils.drawRect(0.0D, this.getHeight() * size + 0.4D, this.getWidth(), this.getHeight() * size, this.getStaticBackgroundColor());
            }

            //background
            drawRect(0, 0, this.getWidth(), this.getHeight(), this.getEnabledBackgroundColor());

            //frame
            OxygenGUIUtils.drawRect(0.0D, 0.0D, 0.4D, this.getHeight(), this.getStaticBackgroundColor());
            OxygenGUIUtils.drawRect(this.getWidth() - 0.4D, 0.0D, this.getWidth(), this.getHeight(), this.getStaticBackgroundColor());
            OxygenGUIUtils.drawRect(0.0D, 0.0D, this.getWidth(), 0.4D, this.getStaticBackgroundColor());
            OxygenGUIUtils.drawRect(0.0D, this.getHeight() - 0.4D, this.getWidth(), this.getHeight(), this.getStaticBackgroundColor());

            GlStateManager.pushMatrix();           
            GlStateManager.translate(2.0F, this.getHeight() - this.textHeight(this.getTextScale()) - 2.0F, 0.0F);            
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
            GUIAdvancedElement.drawCustomSizedTexturedRect(this.getWidth() - 5, (this.getHeight() - 3) / 2, iconU, 0, 3, 3, 9, 3);

            GlStateManager.popMatrix();      

            if (this.isDragged())               
                for (OxygenDropDownListWrapperEntry element : this.elements)                 
                    element.draw(mouseX, mouseY); 
        }
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {   
        if (this.isEnabled()) {
            this.setHovered(mouseX >= this.getX() && mouseY >= this.getY() && mouseX <= this.getX() + (int) (this.getScale() * this.getWidth()) && mouseY < this.getY() + (int) (this.getScale() * (this.isDragged() ? this.getHeight() * (this.elements.size() + 1) : this.getHeight())));   
            if (this.isDragged())  
                for (OxygenDropDownListWrapperEntry element : this.elements)
                    element.mouseOver(mouseX, mouseY);         
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {      
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);                         
        if (flag && mouseButton == 0) {                  
            for (OxygenDropDownListWrapperEntry element : this.elements) {                           
                if (element.mouseClicked(mouseX, mouseY, mouseButton)) {                                                                
                    this.setDisplayText(element.getDisplayText());                              
                    this.setDragged(false);   
                    element.setHovered(false);                      
                    if (this.elementClickListener != null)
                        this.elementClickListener.click(element);
                    this.mc.player.playSound(OxygenSoundEffects.CONTEXT_CLOSE.getSoundEvent(), 0.5F, 1.0F);
                    return true;
                }
            }
        }       
        if (flag && mouseButton == 0 && !this.isDragged())
            this.mc.player.playSound(OxygenSoundEffects.DROP_DOWN_LIST_OPEN.getSoundEvent(), 0.5F, 1.0F);
        this.setDragged(flag && mouseButton == 0);      
        return false;
    }

    @FunctionalInterface
    public static interface ElementClickListener<T> {

        void click(T clicked);
    }

    public static class OxygenDropDownListWrapperEntry<T> extends GUISimpleElement<OxygenDropDownListWrapperEntry> {

        protected final T wrapped;

        public OxygenDropDownListWrapperEntry(T wrapped, String displayText) {
            this.wrapped = wrapped;
            this.setDisplayText(displayText);
            this.setStaticBackgroundColor(EnumBaseGUISetting.ELEMENT_HOVERED_COLOR.get().asInt());
            this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());
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
            this.mc.fontRenderer.drawString(this.getDisplayText(), 0, 0, textColor, false);

            GlStateManager.popMatrix();

            GlStateManager.popMatrix();
        }

        public T getWrapped() {
            return this.wrapped;
        }
    }
}
