package austeretony.oxygen.client.gui.friends.friends;

import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.image.GUIImageLabel;
import austeretony.alternateui.screen.text.GUITextLabel;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.gui.friends.FriendsGUISection;
import austeretony.oxygen.client.gui.friends.FriendsListGUIScreen;
import austeretony.oxygen.client.gui.settings.GUISettings;
import net.minecraft.client.resources.I18n;

public class DownloadDataGUICallback extends AbstractGUICallback {

    private final FriendsListGUIScreen screen;

    private final FriendsGUISection section;

    private GUIButton confirmButton, cancelButton;

    public DownloadDataGUICallback(FriendsListGUIScreen screen, FriendsGUISection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;
        this.section = section;
    }

    @Override
    protected void init() {
        this.addElement(new GUIImageLabel(- 1, - 1, this.getWidth() + 2, this.getHeight() + 2).enableStaticBackground(GUISettings.instance().getBaseGUIBackgroundColor()));//main background 1st layer
        this.addElement(new GUIImageLabel(0, 0, this.getWidth(), 11).enableStaticBackground(GUISettings.instance().getAdditionalGUIBackgroundColor()));//main background 2nd layer
        this.addElement(new GUIImageLabel(0, 12, this.getWidth(), this.getHeight() - 12).enableStaticBackground(GUISettings.instance().getAdditionalGUIBackgroundColor()));//main background 2nd layer
        this.addElement(new GUITextLabel(2, 2).setDisplayText(I18n.format("oxygen.gui.downloadCallback"), true, GUISettings.instance().getTitleScale()));
        this.addElement(new GUITextLabel(2, 16).setDisplayText(I18n.format("oxygen.gui.downloadCallback.request"), true, GUISettings.instance().getTextScale()));        

        this.addElement(this.confirmButton = new GUIButton(15, this.getHeight() - 12, 40, 10).enableDynamicBackground().setDisplayText(I18n.format("oxygen.gui.confirmButton"), true, GUISettings.instance().getButtonTextScale()));
        this.addElement(this.cancelButton = new GUIButton(this.getWidth() - 55, this.getHeight() - 12, 40, 10).enableDynamicBackground().setDisplayText(I18n.format("oxygen.gui.cancelButton"), true, GUISettings.instance().getButtonTextScale()));
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element) {
        if (element == this.cancelButton)
            this.close();
        else if (element == this.confirmButton) {
            OxygenManagerClient.instance().getFriendListManager().downloadFriendsListDataSynced();
            this.close();            
        }
    }
}
