package austeretony.oxygen.client.gui;

import austeretony.alternateui.screen.image.GUIImageLabel;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen.client.gui.friends.FriendsListGUIScreen;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.common.api.EnumDimensions;
import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.main.FriendListEntry;
import austeretony.oxygen.common.main.OxygenPlayerData;
import net.minecraft.client.renderer.GlStateManager;

public class PlayerListEntryGUIButton extends PlayerGUIButton {

    public final FriendListEntry listEntry;

    private String dimension;

    private int statusIconU;

    private GUIImageLabel noteLabel;

    private boolean initialized, hasNote;

    public PlayerListEntryGUIButton(FriendListEntry listEntry, OxygenPlayerData.EnumStatus status) {
        super(listEntry.playerUUID);
        this.listEntry = listEntry;
        this.dimension = EnumDimensions.getLocalizedNameFromId(
                status == OxygenPlayerData.EnumStatus.ONLINE ? OxygenHelperClient.getSharedPlayerData(listEntry.playerUUID).getDimension() : listEntry.getDimension());
        this.setDisplayText(listEntry.username);//need for search mechanic
        this.setTextAlignment(EnumGUIAlignment.LEFT, 24);
        this.statusIconU = status.ordinal() * 3;
        this.hasNote = !listEntry.getNote().isEmpty();
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (!this.initialized) {
            this.initialized = true;
            this.noteLabel = new GUIImageLabel(this.getWidth() - 20, 1, 8, 8).setTexture(FriendsListGUIScreen.NOTE_ICONS, 8, 8, 0, 0, 24, 8).initScreen(this.getScreen()).initSimpleTooltip(this.listEntry.getNote(), GUISettings.instance().getTooltipScale());
        }
        super.draw(mouseX, mouseY);
        if (this.isVisible()) {  
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);      
            this.mc.getTextureManager().bindTexture(FriendsListGUIScreen.STATUS_ICONS); 
            this.drawCustomSizedTexturedRect(7, 3, this.statusIconU, 0, 3, 3, 12, 3);   
            if (this.hasNote)
                this.noteLabel.draw(mouseX, mouseY);
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);    
            int color;                      
            if (!this.isEnabled())                  
                color = this.getDisabledTextColor();           
            else if (this.isHovered() || this.isToggled())                                          
                color = this.getHoveredTextColor();
            else                    
                color = this.getEnabledTextColor();        
            this.mc.fontRenderer.drawString(this.dimension, 157, 2, color, this.isTextShadowEnabled());
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {
        if (this.initialized && this.hasNote)
            this.noteLabel.drawTooltip(mouseX - this.getX(), mouseY);
    }

    @Override
    public void mouseOver(int mouseX, int mouseY) {
        if (this.initialized && this.hasNote)
            this.noteLabel.mouseOver(mouseX - this.getX(), mouseY - this.getY());
        this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + (int) (this.getWidth() * this.getScale()) && mouseY < this.getY() + (int) (this.getHeight() * this.getScale()));   
    }
}
