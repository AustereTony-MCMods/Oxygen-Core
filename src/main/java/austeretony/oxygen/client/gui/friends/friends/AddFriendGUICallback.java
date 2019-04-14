package austeretony.oxygen.client.gui.friends.friends;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.image.GUIImageLabel;
import austeretony.alternateui.screen.text.GUITextField;
import austeretony.alternateui.screen.text.GUITextLabel;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.gui.friends.FriendsGUISection;
import austeretony.oxygen.client.gui.friends.FriendsListGUIScreen;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.SharedPlayerData;
import net.minecraft.client.resources.I18n;

public class AddFriendGUICallback extends AbstractGUICallback {

    private final FriendsListGUIScreen screen;

    private final FriendsGUISection section;

    private GUITextField usernameField;

    private GUITextLabel playerStatusLabel;

    private GUIButton confirmButton, cancelButton;

    private String 
    playerFoundStr = I18n.format("oxygen.gui.playerFound"),
    playerNotFoundStr = I18n.format("oxygen.gui.playerNotFound");

    private final Map<String, UUID> players = new HashMap<String, UUID>();

    public AddFriendGUICallback(FriendsListGUIScreen screen, FriendsGUISection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;
        this.section = section;
    }

    @Override
    protected void init() {
        for (SharedPlayerData playerData : OxygenHelperClient.getSharedPlayersData())
            if (playerData != OxygenHelperClient.getSharedClientPlayerData() && this.getPlayerStatus(playerData) != OxygenPlayerData.EnumStatus.OFFLINE)
                this.players.put(playerData.getUsername(), playerData.getPlayerUUID());

        this.addElement(new GUIImageLabel(- 1, - 1, this.getWidth() + 2, this.getHeight() + 2).enableStaticBackground(GUISettings.instance().getBaseGUIBackgroundColor()));//main background 1st layer
        this.addElement(new GUIImageLabel(0, 0, this.getWidth(), 11).enableStaticBackground(GUISettings.instance().getAdditionalGUIBackgroundColor()));//main background 2nd layer
        this.addElement(new GUIImageLabel(0, 12, this.getWidth(), this.getHeight() - 12).enableStaticBackground(GUISettings.instance().getAdditionalGUIBackgroundColor()));//main background 2nd layer
        this.addElement(new GUITextLabel(2, 2).setDisplayText(I18n.format("oxygen.gui.friends.addFriendCallback"), true, GUISettings.instance().getTitleScale()));   
        this.addElement(new GUITextLabel(2, 16).setDisplayText(I18n.format("oxygen.gui.friends.addFriendCallback.request"), false, GUISettings.instance().getTextScale()));  
        this.addElement(new GUITextLabel(2, 26).setDisplayText(I18n.format("oxygen.gui.username"), false, GUISettings.instance().getSubTextScale()));  
        this.addElement(this.usernameField = new GUITextField(2, 35, 187, 24).setScale(0.7F).enableDynamicBackground().cancelDraggedElementLogic());       
        this.addElement(this.playerStatusLabel = new GUITextLabel(2, 45).setTextScale(GUISettings.instance().getSubTextScale()).disableFull());    

        this.addElement(this.confirmButton = new GUIButton(15, this.getHeight() - 12, 40, 10).enableDynamicBackground().setDisplayText(I18n.format("oxygen.gui.confirmButton"), true, GUISettings.instance().getButtonTextScale()));
        this.addElement(this.cancelButton = new GUIButton(this.getWidth() - 55, this.getHeight() - 12, 40, 10).enableDynamicBackground().setDisplayText(I18n.format("oxygen.gui.cancelButton"), true, GUISettings.instance().getButtonTextScale()));

        this.confirmButton.disable();
    }

    private OxygenPlayerData.EnumStatus getPlayerStatus(SharedPlayerData playerData) {
        return OxygenPlayerData.EnumStatus.values()[playerData.getData(OxygenMain.STATUS_DATA_ID).get(0)];
    }

    @Override
    protected void onClose() {
        this.usernameField.reset();
        this.playerStatusLabel.disableFull();
        this.confirmButton.disable();
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        boolean flag = super.keyTyped(typedChar, keyCode);   
        if (this.usernameField.isDragged()) {
            if (!this.usernameField.getTypedText().isEmpty()) {
                this.playerStatusLabel.enableFull();
                if (this.players.containsKey(this.usernameField.getTypedText())) {
                    this.playerStatusLabel.setDisplayText(this.playerFoundStr);
                    this.confirmButton.enable();
                } else {
                    this.playerStatusLabel.setDisplayText(this.playerNotFoundStr);
                    this.confirmButton.disable();
                }
            } else {
                this.playerStatusLabel.disableFull();
                this.confirmButton.disable();
            }
        }
        return flag;   
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element) {
        if (element == this.cancelButton)
            this.close();
        else if (element == this.confirmButton) {
            if (this.players.containsKey(this.usernameField.getTypedText())) 
                OxygenManagerClient.instance().getFriendListManager().sendFriendRequestSynced(this.players.get(this.usernameField.getTypedText()));
            this.section.lockAddButton();
            this.close();
        }
    }
}
