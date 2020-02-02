package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.common.EnumActivityStatus;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenActivityStatusSwitcher extends GUISimpleElement<OxygenActivityStatusSwitcher> {

    private final ActivityStatusSwitcherEntry[] elements;

    private ActivityStatusChangeListener statusChangeListener;

    private final int width, height;

    private int 
    previous = - 1, 
    current = - 1;

    public OxygenActivityStatusSwitcher(int xPosition, int yPosition) {
        this.setPosition(xPosition, yPosition);
        this.setSize(14, 8);
        this.setTextScale(EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat());
        this.setDisplayText(OxygenHelperClient.getPlayerUsername());


        this.elements = new ActivityStatusSwitcherEntry[EnumActivityStatus.values().length];

        int 
        textWidth, 
        width = 0,
        height = this.textHeight(this.getTextScale()) + 3;
        for (EnumActivityStatus status : EnumActivityStatus.values()) {
            textWidth = this.textWidth(status.localized(), this.getTextScale());
            if (width == 0 || width < textWidth)
                width = textWidth + 6;
        }
        this.width = width;
        this.height = height * EnumActivityStatus.values().length;

        int index = 0;
        ActivityStatusSwitcherEntry element;
        for (EnumActivityStatus status : EnumActivityStatus.values()) {
            element = new ActivityStatusSwitcherEntry(status);
            element.initScreen(this.getScreen());
            element.setPosition(this.getX(), this.getY() + height * (index + 1) + 1);                
            element.setSize(width, height);   
            element.setTextScale(this.getTextScale() - 0.05F);
            this.elements[index++] = element;
            this.bind(element);
        }

        this.setDynamicBackgroundColor(EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get().asInt(), EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt(), 0);
        this.setTextDynamicColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());    
        this.enableFull();
    }

    public void updateActivityStatus() {
        this.current = OxygenHelperClient.getPlayerActivityStatus().ordinal();
    }

    public void setActivityStatusChangeListener(ActivityStatusChangeListener listener) {
        this.statusChangeListener = listener;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (this.isDragged()) {
                int size = this.elements.length;

                //background
                drawRect(- 2, this.getHeight(), this.width + 2, this.getHeight() + this.height + 2, this.getEnabledBackgroundColor());

                //frame
                OxygenGUIUtils.drawRect(- 2.4D, this.getHeight(), - 2.0D, this.getHeight() + this.height + 2, this.getDisabledBackgroundColor());
                OxygenGUIUtils.drawRect(this.width + 1.6D, this.getHeight(), this.width + 2.0D, this.getHeight() + this.height + 2, this.getDisabledBackgroundColor());
                OxygenGUIUtils.drawRect(- 2.0D, this.getHeight(), this.width + 2.0D, this.getHeight() + 0.4D, this.getDisabledBackgroundColor());
                OxygenGUIUtils.drawRect(- 2.0D, this.getHeight() + this.height + 2, this.width + 2.0D, this.getHeight() + this.height + 2 + 0.4D, this.getDisabledBackgroundColor());
            }

            GlStateManager.pushMatrix();           
            GlStateManager.translate(4.0F, this.getHeight() - this.textHeight(this.getTextScale()) - 2.0F, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);  
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            this.mc.fontRenderer.drawString(this.getDisplayText(), 14, 0, this.getEnabledTextColor(), false);

            GlStateManager.popMatrix();

            if (this.current != - 1) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                this.mc.getTextureManager().bindTexture(OxygenGUITextures.STATUS_ICONS);
                GUIAdvancedElement.drawCustomSizedTexturedRect(0, 1, this.current * 3, 0, 3, 3, 12, 3);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int iconU = 0;                      
            if (!this.isEnabled())          
                iconU = 3;
            else if (this.isHovered() && !this.isDragged())    
                iconU = 6;

            this.mc.getTextureManager().bindTexture(OxygenGUITextures.SORT_DOWN_ICONS);
            GUIAdvancedElement.drawCustomSizedTexturedRect(6, 1, iconU, 0, 3, 3, 9, 3);

            GlStateManager.popMatrix();      

            if (this.isDragged())               
                for (ActivityStatusSwitcherEntry element : this.elements)                 
                    element.draw(mouseX, mouseY); 
        }
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {   
        if (this.isEnabled()) {
            this.setHovered(mouseX >= this.getX() && mouseY >= this.getY() && mouseX <= this.getX() + (int) (this.getScale() * this.getWidth()) && mouseY < this.getY() + (int) (this.getScale() * (this.isDragged() ? this.getHeight() * (this.elements.length + 1) : this.getHeight())));   
            if (this.isDragged())  
                for (ActivityStatusSwitcherEntry element : this.elements)
                    element.mouseOver(mouseX, mouseY);         
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {      
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);                         
        if (flag && mouseButton == 0) {                  
            for (ActivityStatusSwitcherEntry element : this.elements) {                           
                if (element.mouseClicked(mouseX, mouseY, mouseButton)) {                                                                
                    this.setDragged(false);   
                    element.setHovered(false);                      
                    if (this.previous == - 1 || this.previous != element.activityStatus.ordinal()) {
                        OxygenManagerClient.instance().getClientDataManager().changeActivityStatusSynced(element.activityStatus);
                        OxygenHelperClient.getPlayerSharedData().setByte(OxygenMain.ACTIVITY_STATUS_SHARED_DATA_ID, element.activityStatus.ordinal());
                    }
                    if (this.statusChangeListener != null)
                        this.statusChangeListener.onChange(element.activityStatus);
                    this.current = this.previous = element.activityStatus.ordinal();
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

    public static interface ActivityStatusChangeListener {

        void onChange(EnumActivityStatus newStatus);
    }

    public static class ActivityStatusSwitcherEntry extends GUISimpleElement<ActivityStatusSwitcherEntry> {

        public final EnumActivityStatus activityStatus;

        public ActivityStatusSwitcherEntry(EnumActivityStatus activityStatus) {
            this.activityStatus = activityStatus;
            this.setDisplayText(this.activityStatus.localized());
            this.setDynamicBackgroundColor(EnumBaseGUISetting.ELEMENT_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.ELEMENT_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.ELEMENT_HOVERED_COLOR.get().asInt());
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

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            this.mc.getTextureManager().bindTexture(OxygenGUITextures.STATUS_ICONS);
            GUIAdvancedElement.drawCustomSizedTexturedRect(0, 3, this.activityStatus.ordinal() * 3, 0, 3, 3, 12, 3);

            GlStateManager.pushMatrix();           
            GlStateManager.translate(4.0F, this.getHeight() - this.textHeight(this.getTextScale()) - 2.0F, 0.0F);            
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
}
