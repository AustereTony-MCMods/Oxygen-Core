package austeretony.oxygen.client.gui.notifications;

import java.util.ArrayList;
import java.util.List;

import austeretony.alternateui.screen.button.GUIButton;
import austeretony.oxygen.client.reference.ClientReference;
import austeretony.oxygen.common.notification.EnumNotifications;
import austeretony.oxygen.common.notification.IOxygenNotification;
import austeretony.oxygen.common.notification.NotificationManagerClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class NotificationGUIButton extends GUIButton {

    private IOxygenNotification notification;

    private final List<String> description = new ArrayList<String>();

    private GUIButton acceptButton, rejectButton;

    private boolean initialized, hasIcon;

    private ResourceLocation icon;

    public NotificationGUIButton(IOxygenNotification notification) {
        super();
        this.notification = notification;
        this.hasIcon = (this.icon = NotificationManagerClient.instance().getIcon(notification.getIndex())) != null;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (!this.initialized) {
            this.initialized = true;
            this.processDescription(I18n.format(this.notification.getDescription(), (Object[]) this.notification.getArguments()));
            this.acceptButton = new GUIButton(this.getWidth() - 22, 6, 6, 6).setTexture(NotificationsGUIScreen.CHECK_ICONS, 6, 6).initScreen(this.getScreen());
            this.rejectButton = new GUIButton(this.getWidth() - 10, 6, 6, 6).setTexture(NotificationsGUIScreen.CROSS_ICONS, 6, 6).initScreen(this.getScreen());
            if (notification.getType() == EnumNotifications.NOTICE)
                this.rejectButton.disableFull();
        }
        super.draw(mouseX, mouseY);
        if (this.isVisible()) {         
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);     
            if (this.hasIcon) {
                this.mc.getTextureManager().bindTexture(this.icon);
                this.drawCustomSizedTexturedRect(6, 5, 0, 0, 8, 8, 8, 8);
            }
            this.acceptButton.draw(mouseX, mouseY);
            this.rejectButton.draw(mouseX, mouseY);
            GlStateManager.scale(0.75F, 0.75F, 0.0F);    
            int color;                          
            if (this.isHovered() || this.isToggled())                                          
                color = this.getHoveredTextColor();
            else                    
                color = this.getEnabledTextColor();   
            if (this.description.size() == 1) 
                this.mc.fontRenderer.drawString(this.description.get(0), 26, 7, color, true); 
            else
                for (String line : this.description)                  
                    this.mc.fontRenderer.drawString(line, 26, 3 + 10 * this.description.indexOf(line), color, true); 
            if (this.notification.getType() == EnumNotifications.REQUEST)
                this.mc.fontRenderer.drawString("(" + String.valueOf(this.notification.getCounter() / 20) + ")", 226, 8, color, true); 
            GlStateManager.popMatrix();
        }
    }

    private void processDescription(String description) {     
        this.description.clear();     
        StringBuilder stringBuilder = new StringBuilder();      
        String[] words = description.split("[ ]");        
        if (words.length > 0) {                 
            for (int i = 0; i < words.length; i++) {            
                if (this.width(stringBuilder.toString() + words[i]) <= this.getWidth() - 20)                          
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
    public boolean mouseClicked(int mouseX, int mouseY) {       
        if (this.acceptButton.mouseClicked(mouseX, mouseY)) {
            this.notification.accepted(ClientReference.getClientPlayer());
            return true;
        } else if (this.rejectButton.mouseClicked(mouseX, mouseY)) {
            this.notification.rejected(ClientReference.getClientPlayer());;
            return true;
        }
        return false;
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {
        if (this.initialized) {
            this.acceptButton.mouseOver(mouseX - this.getX(), mouseY - this.getY());
            this.rejectButton.mouseOver(mouseX - this.getX(), mouseY - this.getY());
        }
        this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + (int) (this.getWidth() * this.getScale()) && mouseY < this.getY() + (int) (this.getHeight() * this.getScale()));   
    }
}
