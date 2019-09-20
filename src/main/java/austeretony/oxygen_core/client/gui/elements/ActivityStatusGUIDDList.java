package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import austeretony.oxygen_core.server.OxygenPlayerData.EnumActivityStatus;
import net.minecraft.client.renderer.GlStateManager;

public class ActivityStatusGUIDDList extends GUISimpleElement<ActivityStatusGUIDDList> {

    private final ActivityStatusGUIDDElement[] elements;

    private ActivityStatusChangeListener statusChangeListener;

    private final int width, height;

    private int 
    previous = - 1, 
    current = - 1;

    public ActivityStatusGUIDDList(int xPosition, int yPosition) {
        this.setPosition(xPosition, yPosition);
        this.setSize(14, 8);
        this.setTextScale(GUISettings.get().getSubTextScale());
        this.setDisplayText(OxygenHelperClient.getPlayerUsername());

        this.elements = new ActivityStatusGUIDDElement[EnumActivityStatus.values().length];

        int 
        textWidth, 
        width = 0,
        height = this.textHeight(this.getTextScale()) + 3;
        for (EnumActivityStatus status : EnumActivityStatus.values()) {
            textWidth = this.textWidth(status.localizedName(), this.getTextScale());
            if (width == 0 || width < textWidth)
                width = textWidth + 6;
        }
        this.width = width;
        this.height = height * EnumActivityStatus.values().length;

        int index = 0;
        ActivityStatusGUIDDElement element;
        for (EnumActivityStatus status : EnumActivityStatus.values()) {
            element = new ActivityStatusGUIDDElement(status);
            element.initScreen(this.getScreen());
            element.setPosition(this.getX(), this.getY() + height * (index + 1) + 1);                
            element.setSize(width, height);   
            element.setTextScale(this.getTextScale() - 0.05F);
            this.elements[index++] = element;
            this.bind(element);
        }

        this.setDynamicBackgroundColor(GUISettings.get().getBaseGUIBackgroundColor(), GUISettings.get().getAdditionalGUIBackgroundColor(), 0);
        this.setTextDynamicColor(GUISettings.get().getEnabledTextColor(), GUISettings.get().getDisabledTextColor(), GUISettings.get().getHoveredTextColor());
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
                CustomRectUtils.drawRect(- 2.0D, this.getHeight(), - 1.6D, this.getHeight() + this.height + 2, this.getDisabledBackgroundColor());
                CustomRectUtils.drawRect(this.width + 1.6D, this.getHeight(), this.width + 2.0D, this.getHeight() + this.height + 2, this.getDisabledBackgroundColor());
                CustomRectUtils.drawRect(- 2.0D, this.getHeight(), this.width + 2.0D, this.getHeight() + 0.4D, this.getDisabledBackgroundColor());
                CustomRectUtils.drawRect(- 2.0D, this.getHeight() + this.height + 2 - 0.4D, this.width + 2.0D, this.getHeight() + this.height + 2, this.getDisabledBackgroundColor());
            }

            GlStateManager.pushMatrix();           
            GlStateManager.translate(4.0F, (this.getHeight() - this.textHeight(this.getTextScale())) / 2, 0.0F);            
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
                for (ActivityStatusGUIDDElement element : this.elements)                 
                    element.draw(mouseX, mouseY); 
        }
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {   
        if (this.isEnabled()) {
            this.setHovered(mouseX >= this.getX() && mouseY >= this.getY() && mouseX <= this.getX() + (int) (this.getScale() * this.getWidth()) && mouseY < this.getY() + (int) (this.getScale() * (this.isDragged() ? this.getHeight() * (this.elements.length + 1) : this.getHeight())));   
            if (this.isDragged())  
                for (ActivityStatusGUIDDElement element : this.elements)
                    element.mouseOver(mouseX, mouseY);         
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {      
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);                         
        if (flag && mouseButton == 0) {                  
            for (ActivityStatusGUIDDElement element : this.elements) {                           
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
}
