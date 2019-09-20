package austeretony.oxygen_core.client.gui.elements;

import java.util.Set;
import java.util.TreeSet;

import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.common.PlayerSharedData;
import net.minecraft.client.renderer.GlStateManager;

public class UsernameGUITextField extends OxygenGUITextField {

    private final Set<String> usernames = new TreeSet<>();

    private UsernameGUIElement[] elements = new UsernameGUIElement[5];

    private int elementsAmount, baseColorHex, additionalColorHex;

    private UsernameSelectListener selectListener;

    private boolean active;

    public UsernameGUITextField(int xPosition, int yPosition, int width) {
        super(xPosition, yPosition, width, 8, 24, "", 3, false, - 1L);
        this.baseColorHex = GUISettings.get().getBaseGUIBackgroundColor();
        this.additionalColorHex = GUISettings.get().getAdditionalGUIBackgroundColor();
        this.cancelDraggedElementLogic(false);
    }

    public void setUsernameSelectListener(UsernameSelectListener listener) {
        this.selectListener = listener;
    }

    public void load() {
        for (PlayerSharedData sharedData : OxygenHelperClient.getPlayersSharedData())
            this.usernames.add(sharedData.getUsername());
        this.usernames.remove(OxygenHelperClient.getPlayerUsername());
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
            CustomRectUtils.drawRect(0.0D, this.getHeight() + 1.0D, 0.4D, (this.getHeight() + 1.0D) * (this.elementsAmount + 1) - 0.4D, this.additionalColorHex);
            CustomRectUtils.drawRect(this.getWidth() - 0.4D, this.getHeight() + 1.0D, this.getWidth(), (this.getHeight() + 1.0D) * (this.elementsAmount + 1) - 0.4D, this.additionalColorHex);
            CustomRectUtils.drawRect(0.0D, this.getHeight() + 0.4D, this.getWidth(), this.getHeight() + 1.0D, this.additionalColorHex);
            CustomRectUtils.drawRect(0.0D, (this.getHeight() + 1.0D) * (this.elementsAmount + 1) - 0.4D, this.getWidth(), (this.getHeight() + 1.0D) * (this.elementsAmount + 1), this.additionalColorHex);

            GlStateManager.popMatrix();

            for (UsernameGUIElement element : this.elements)       
                if (element != null)
                    element.draw(mouseX, mouseY); 
        }

    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {   
        if (this.isEnabled()) {
            super.mouseOver(mouseX, mouseY);
            if (this.isDragged() && this.active)  
                for (UsernameGUIElement element : this.elements)
                    if (element != null)
                        element.mouseOver(mouseX, mouseY);         
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.active && mouseButton == 0) {                  
            for (UsernameGUIElement element : this.elements) {        
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
                        this.elements[index] = new UsernameGUIElement(this.getX(), this.getY() + (this.getHeight() + 1) * (index + 1), this.getWidth(), this.getHeight(), username);
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
}
