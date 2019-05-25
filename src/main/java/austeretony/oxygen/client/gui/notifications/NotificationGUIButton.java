package austeretony.oxygen.client.gui.notifications;

import java.util.ArrayList;
import java.util.List;

import austeretony.alternateui.screen.button.GUIButton;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.gui.OxygenGUITextures;
import austeretony.oxygen.common.main.OxygenSoundEffects;
import austeretony.oxygen.common.notification.EnumNotifications;
import austeretony.oxygen.common.notification.INotification;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class NotificationGUIButton extends GUIButton {

    private INotification notification;

    private final List<String> description = new ArrayList<String>();

    private GUIButton acceptButton, rejectButton;

    private boolean hasIcon;

    private ResourceLocation icon;

    public NotificationGUIButton(INotification notification) {
        super();
        this.notification = notification;
        this.hasIcon = (this.icon = OxygenManagerClient.instance().getNotificationsManager().getIcon(notification.getIndex())) != null;
    }
    
    @Override
    public void init() {
        this.processDescription(I18n.format(this.notification.getDescription(), (Object[]) this.notification.getArguments()));
        this.acceptButton = new GUIButton(this.getWidth() - 22, 6, 6, 6).setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent).setTexture(OxygenGUITextures.CHECK_ICONS, 6, 6).initScreen(this.getScreen());
        this.rejectButton = new GUIButton(this.getWidth() - 10, 6, 6, 6).setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent).setTexture(OxygenGUITextures.CROSS_ICONS, 6, 6).initScreen(this.getScreen());
        if (notification.getType() == EnumNotifications.NOTICE)
            this.rejectButton.disableFull();
    }

    @Override
    public void draw(int mouseX, int mouseY) {  
        if (this.isVisible()) {         
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);    
            int color, textColor, textY;                      
            if (!this.isEnabled()) {                 
                color = this.getDisabledBackgroundColor();
                textColor = this.getDisabledTextColor();           
            } else if (this.isHovered() || this.isToggled()) {                 
                color = this.getHoveredBackgroundColor();
                textColor = this.getHoveredTextColor();
            } else {                   
                color = this.getEnabledBackgroundColor(); 
                textColor = this.getEnabledTextColor();      
            }
            drawRect(0, 0, this.getWidth(), this.getHeight(), color);
            textY = (this.getHeight() - this.textHeight(this.getTextScale())) / 2;
            GlStateManager.pushMatrix();           
            GlStateManager.translate(24.0F, textY, 0.0F); 
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);     
            if (this.description.size() == 1) 
                this.mc.fontRenderer.drawString(this.description.get(0), 0, 0, textColor, true); 
            else
                for (String line : this.description)                  
                    this.mc.fontRenderer.drawString(line, 0, 10 * this.description.indexOf(line), textColor, true); 
            if (this.notification.getType() == EnumNotifications.REQUEST)
                this.mc.fontRenderer.drawString("(" + String.valueOf(this.notification.getCounter() / 20) + ")", 250, 0, textColor, true); 
            GlStateManager.popMatrix();
            if (this.hasIcon) {
                this.mc.getTextureManager().bindTexture(this.icon);
                drawCustomSizedTexturedRect(4, 3, 0, 0, 10, 10, 10, 10);
            }
            this.acceptButton.draw(mouseX, mouseY);
            this.rejectButton.draw(mouseX, mouseY);
            GlStateManager.popMatrix();
        }
    }

    private void processDescription(String description) {     
        this.description.clear();     
        StringBuilder stringBuilder = new StringBuilder();      
        String[] words = description.split("[ ]");        
        if (words.length > 0) {                 
            for (int i = 0; i < words.length; i++) {            
                if (this.textWidth(stringBuilder.toString() + words[i], this.getTextScale()) <= this.getWidth() - 20)                          
                    stringBuilder.append(words[i]).append(" ");
                else {                          
                    if (this.description.size() * 10 <= 16)                                      
                        this.description.add(stringBuilder.toString());                       
                    stringBuilder = new StringBuilder();                                
                    stringBuilder.append(words[i]).append(" ");
                }                       
                if (i == words.length - 1)                              
                    if (this.description.size() * 10 <= 16)                            
                        this.description.add(stringBuilder.toString());
            }
        }       
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {       
        if (this.acceptButton.mouseClicked(mouseX, mouseY, mouseButton)) {
            this.notification.accepted(ClientReference.getClientPlayer());
            return true;
        } else if (this.rejectButton.mouseClicked(mouseX, mouseY, mouseButton)) {
            this.notification.rejected(ClientReference.getClientPlayer());;
            return true;
        }
        return false;
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {
        this.acceptButton.mouseOver(mouseX - this.getX(), mouseY - this.getY());
        this.rejectButton.mouseOver(mouseX - this.getX(), mouseY - this.getY());
        this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + (int) (this.getWidth() * this.getScale()) && mouseY < this.getY() + (int) (this.getHeight() * this.getScale()));   
    }
}
