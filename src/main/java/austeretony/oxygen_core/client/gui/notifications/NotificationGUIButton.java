package austeretony.oxygen_core.client.gui.notifications;

import java.util.ArrayList;
import java.util.List;

import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.elements.CustomRectUtils;
import austeretony.oxygen_core.client.gui.elements.OxygenTexturedGUIButton;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.common.notification.EnumNotification;
import austeretony.oxygen_core.common.notification.Notification;
import net.minecraft.client.renderer.GlStateManager;

public class NotificationGUIButton extends GUIButton {

    private Notification notification;

    private final List<String> description = new ArrayList<String>(2);

    private OxygenTexturedGUIButton acceptButton, rejectButton;

    public NotificationGUIButton(Notification notification) {
        super();
        this.notification = notification;
        this.setStaticBackgroundColor(GUISettings.get().getBaseGUIBackgroundColor());
        this.setDynamicBackgroundColor(GUISettings.get().getEnabledElementColor(), GUISettings.get().getDisabledElementColor(), GUISettings.get().getHoveredElementColor());
        this.setTextDynamicColor(GUISettings.get().getEnabledTextColor(), GUISettings.get().getDisabledTextColor(), GUISettings.get().getHoveredTextColor());
    }

    @Override
    public void init() {
        this.setTextScale(GUISettings.get().getSubTextScale());
        this.processDescription(ClientReference.localize(this.notification.getDescription(), (Object[]) this.notification.getArguments()));

        this.acceptButton = new OxygenTexturedGUIButton(this.getWidth() - 18, 7, 6, 6, OxygenGUITextures.CHECK_ICONS, 6, 6, "").initScreen(this.getScreen());
        this.rejectButton = new OxygenTexturedGUIButton(this.getWidth() - 10, 7, 6, 6, OxygenGUITextures.CROSS_ICONS, 6, 6, "").initScreen(this.getScreen());

        if (this.notification.getType() == EnumNotification.NOTIFICATION)
            this.acceptButton.disableFull();
    }

    @Override
    public void draw(int mouseX, int mouseY) {  
        if (this.isVisible()) {         
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);    

            int 
            color = this.getEnabledBackgroundColor(), 
            textColor = this.getEnabledTextColor(), 
            textY;                      
            if (!this.isEnabled()) {       
                color = this.getDisabledBackgroundColor();
                textColor = this.getDisabledTextColor();           
            } else if (this.isHovered()) {    
                color = this.getHoveredBackgroundColor();
                textColor = this.getHoveredTextColor();
            }

            int third = this.getWidth() / 3;

            CustomRectUtils.drawGradientRect(0.0D, 0.0D, third, this.getHeight(), 0x00000000, color, EnumGUIAlignment.RIGHT);
            drawRect(third, 0, this.getWidth() - third, this.getHeight(), color);
            CustomRectUtils.drawGradientRect(this.getWidth() - third, 0.0D, this.getWidth(), this.getHeight(), 0x00000000, color, EnumGUIAlignment.LEFT);

            textY = this.description.size() == 1 ? (this.getHeight() - this.textHeight(this.getTextScale())) / 2 : 4;

            GlStateManager.pushMatrix();           
            GlStateManager.translate(2.0F, textY, 0.0F); 
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);     

            int index = 0;
            for (String line : this.description) {
                this.mc.fontRenderer.drawString(line, 0.0F, (this.mc.fontRenderer.FONT_HEIGHT + 3) * index, textColor, false);
                index++;
            }

            GlStateManager.popMatrix();

            if (this.notification.getType() == EnumNotification.REQUEST) {
                GlStateManager.pushMatrix();           
                GlStateManager.translate(this.getWidth() - 36.0F, (this.getHeight() - this.textHeight(this.getTextScale())) / 2 + 1.0F, 0.0F); 
                GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);     
                this.mc.fontRenderer.drawString("(" + String.valueOf((this.notification.getExpirationTimeStamp() - System.currentTimeMillis()) / 1000) + ")", 0, 0, textColor, true); 
                GlStateManager.popMatrix();
            }

            this.acceptButton.draw(mouseX, mouseY);
            this.rejectButton.draw(mouseX, mouseY);

            GlStateManager.popMatrix();
        }
    }

    private void processDescription(String description) {     
        this.description.clear(); 
        int width = this.getWidth() - 45;
        StringBuilder builder = new StringBuilder();    
        int 
        index = 0, 
        wordStartIndex = 0;
        boolean
        rechedLimit = false,
        wordProcessing = false;
        char prevSymbol = '0';
        String line;
        for (char symbol : description.toCharArray()) {
            if ((this.textHeight(this.getTextScale()) + 3) * this.description.size() >= this.getHeight())
                break;
            if (symbol != ' ') {
                wordProcessing = true;
                if (prevSymbol == ' ')
                    wordStartIndex = index;
            }
            if (symbol == '\n') {
                this.description.add(builder.toString());
                builder.delete(0, builder.length());
                index = 0;
                continue;
            }
            if (this.textWidth(builder.toString() + String.valueOf(symbol), this.getTextScale()) <= width)
                builder.append(symbol);
            else {
                if (symbol == '.' 
                        || symbol == ',' 
                        || symbol == '!'
                        || symbol == '?')
                    builder.append(symbol);
                if (wordProcessing) {
                    this.description.add(builder.toString().substring(0, wordStartIndex));
                    builder.delete(0, wordStartIndex);
                } else {
                    this.description.add(builder.toString());
                    builder.delete(0, builder.length());
                }
                if (symbol != ' ')
                    builder.append(symbol);
                index = builder.length() - 1;
            }
            wordProcessing = false;
            prevSymbol = symbol;
            index++;
        }
        if (builder.length() != 0)
            this.description.add(builder.toString());     
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
