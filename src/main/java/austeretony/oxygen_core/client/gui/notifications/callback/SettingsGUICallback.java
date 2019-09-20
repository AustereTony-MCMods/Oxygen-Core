package austeretony.oxygen_core.client.gui.notifications.callback;

import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.gui.elements.OxygenCallbackGUIFiller;
import austeretony.oxygen_core.client.gui.elements.OxygenCheckBoxGUIButton;
import austeretony.oxygen_core.client.gui.elements.OxygenGUIButton;
import austeretony.oxygen_core.client.gui.elements.OxygenGUIText;
import austeretony.oxygen_core.client.gui.notifications.NotificationsGUISection;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.common.main.OxygenMain;

public class SettingsGUICallback extends AbstractGUICallback {

    private final AbstractGUIScreen screen;

    private final NotificationsGUISection section;

    private OxygenCheckBoxGUIButton hideOverlayButton;

    private OxygenGUIButton closeButton;

    public SettingsGUICallback(AbstractGUIScreen screen, NotificationsGUISection section, int width, int height) {
        super(screen, section, width, height);
        this.screen = screen;
        this.section = section;
    }

    @Override
    public void init() {
        this.addElement(new OxygenCallbackGUIFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenGUIText(4, 5, ClientReference.localize("oxygen.gui.callback.settings"), GUISettings.get().getTextScale(), GUISettings.get().getEnabledTextColor()));

        this.addElement(this.hideOverlayButton = new OxygenCheckBoxGUIButton(6, 18));
        this.addElement(new OxygenGUIText(16, 19, ClientReference.localize("oxygen.gui.notifications.setting.hideOverlay"), GUISettings.get().getSubTextScale(), GUISettings.get().getEnabledTextColorDark()));

        this.addElement(this.closeButton = new OxygenGUIButton(this.getWidth() - 55, this.getHeight() - 12, 40, 10, ClientReference.localize("oxygen.gui.closeButton"))); 
    }

    @Override
    public void onOpen() {
        this.hideOverlayButton.setToggled(OxygenHelperClient.getClientSettingBoolean(OxygenMain.HIDE_REQUESTS_OVERLAY_SETTING_ID));
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {
        if (mouseButton == 0) {
            if (element == this.closeButton)
                this.close();
            else if (element == this.hideOverlayButton)                    
                OxygenHelperClient.setClientSetting(OxygenMain.HIDE_REQUESTS_OVERLAY_SETTING_ID, this.hideOverlayButton.isToggled());
        }
    }
}
