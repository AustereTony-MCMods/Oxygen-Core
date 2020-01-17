package austeretony.oxygen_core.client.gui.elements;

import java.util.Set;
import java.util.TreeSet;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.api.PrivilegesProviderClient;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.common.EnumActivityStatus;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.main.EnumOxygenPrivilege;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenUsernameField extends OxygenTextField {

    private final Set<String> usernames = new TreeSet<>();

    private UsernameFieldEntry[] elements = new UsernameFieldEntry[5];

    private int elementsAmount, baseColorHex, additionalColorHex;

    private UsernameSelectListener selectListener;

    private boolean active;

    public OxygenUsernameField(int xPosition, int yPosition, int width) {
        super(xPosition, yPosition, width, 24, "");
        this.baseColorHex = EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get().asInt();
        this.additionalColorHex = EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt();
        this.cancelDraggedElementLogic(false);
    }

    public void setUsernameSelectListener(UsernameSelectListener listener) {
        this.selectListener = listener;
    }

    public void load() {
        for (PlayerSharedData sharedData : OxygenHelperClient.getPlayersSharedData())
            if (this.isPlayerAvailable(sharedData))
                this.usernames.add(sharedData.getUsername());
        this.usernames.remove(OxygenHelperClient.getPlayerUsername());
    }

    private boolean isPlayerAvailable(PlayerSharedData sharedData) {
        if (sharedData.getPlayerUUID().equals(OxygenHelperClient.getPlayerUUID()))
            return false;
        return sharedData != null
                && OxygenHelperClient.isPlayerOnline(sharedData.getIndex())
                && PrivilegesProviderClient.getAsBoolean(EnumOxygenPrivilege.EXPOSE_OFFLINE_PLAYERS.id(), OxygenHelperClient.getPlayerActivityStatus(sharedData) != EnumActivityStatus.OFFLINE);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        if (this.isDragged() && this.active) {
            GlStateManager.pushMatrix();            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);      

            //background
            drawRect(0, this.getHeight(), this.getWidth(), (this.getHeight() + 1) * (this.elementsAmount + 1), this.baseColorHex);

            //frame
            OxygenGUIUtils.drawRect(0.0D, this.getHeight() + 1.0D, 0.4D, (this.getHeight() + 1.0D) * (this.elementsAmount + 1) - 0.4D, this.additionalColorHex);
            OxygenGUIUtils.drawRect(this.getWidth() - 0.4D, this.getHeight() + 1.0D, this.getWidth(), (this.getHeight() + 1.0D) * (this.elementsAmount + 1) - 0.4D, this.additionalColorHex);
            OxygenGUIUtils.drawRect(0.0D, this.getHeight() + 0.4D, this.getWidth(), this.getHeight() + 1.0D, this.additionalColorHex);
            OxygenGUIUtils.drawRect(0.0D, (this.getHeight() + 1.0D) * (this.elementsAmount + 1), this.getWidth(), (this.getHeight() + 1.0D) * (this.elementsAmount + 1) + 0.4D, this.additionalColorHex);

            GlStateManager.popMatrix();

            for (UsernameFieldEntry element : this.elements)       
                if (element != null)
                    element.draw(mouseX, mouseY); 
        }

    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {   
        if (this.isEnabled()) {
            super.mouseOver(mouseX, mouseY);
            if (this.isDragged() && this.active)  
                for (UsernameFieldEntry element : this.elements)
                    if (element != null)
                        element.mouseOver(mouseX, mouseY);         
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.active && mouseButton == 0) {                  
            for (UsernameFieldEntry element : this.elements) {        
                if (element != null && element.mouseClicked(mouseX, mouseY, mouseButton)) {   
                    this.setText(element.getDisplayText());
                    this.setDragged(false);   
                    element.setHovered(false);                      
                    if (this.selectListener != null)
                        this.selectListener.onSelect(OxygenHelperClient.getPlayerSharedData(element.getDisplayText()));
                    this.active = false;
                }
            }
        }       
        return flag;
    }

    private void resetElements() {
        for (int i = 0; i < 5; i++) {
            this.unbind(this.elements[i]);
            this.elements[i] = null;
        }
    }

    @Override
    public boolean keyTyped(char keyChar, int keyCode) {
        boolean flag = super.keyTyped(keyChar, keyCode);
        if (flag) {
            String 
            typed = this.getTypedText().toLowerCase(),
            formatted;
            if (!typed.isEmpty()) {
                this.resetElements();
                int index = 0;
                for (String username : this.usernames) {
                    if (index > 4) 
                        break;
                    formatted = username.toLowerCase();
                    if (formatted.startsWith(typed)) {
                        this.elements[index] = new UsernameFieldEntry(this.getX(), this.getY() + (this.getHeight() + 1) * (index + 1), this.getWidth(), this.getHeight(), username);
                        this.bind(this.elements[index]);
                        index++;
                    }
                }
                this.elementsAmount = index;
                this.active = index > 0;
            } else
                this.active = false;
        }
        return flag;
    }

    public static interface UsernameSelectListener {

        void onSelect(PlayerSharedData selectedData);
    }

    public static class UsernameFieldEntry extends GUISimpleElement<UsernameFieldEntry> {

        public UsernameFieldEntry(int xPosition, int yPosition, int width, int height, String username) {
            this.setPosition(xPosition, yPosition);
            this.setSize(width, height);
            this.setDisplayText(username);
            this.setTextScale(EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.05F);
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
    }
}
