package austeretony.oxygen.client.gui.notifications;

import java.util.Set;
import java.util.TreeSet;

import austeretony.alternateui.screen.browsing.GUIScroller;
import austeretony.alternateui.screen.button.GUICheckBoxButton;
import austeretony.alternateui.screen.button.GUISlider;
import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.panel.GUIButtonPanel;
import austeretony.alternateui.screen.panel.GUIButtonPanel.GUIEnumOrientation;
import austeretony.alternateui.screen.text.GUITextLabel;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.api.OxygenHelperClient;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.input.OxygenKeyHandler;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenSoundEffects;
import austeretony.oxygen.common.notification.INotification;
import net.minecraft.client.resources.I18n;

public class NotificationsGUISection extends AbstractGUISection {

    private GUIButtonPanel notificationsPanel;

    private GUITextLabel defaultNoteTextLabel, hideOverlayTextLabel;

    private GUICheckBoxButton hideOverlayButton;

    private int prevSize;

    public NotificationsGUISection(AbstractGUIScreen screen) {
        super(screen);
    }

    @Override
    public void init() {
        this.addElement(new NotificationsBackgroundGUIFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new GUITextLabel(2, 4).setDisplayText(I18n.format("oxygen.gui.notifications.title"), false, GUISettings.instance().getTitleScale()));
        String baseNoticeStr = I18n.format("oxygen.gui.notifications.empty");

        this.addElement(this.hideOverlayButton = new GUICheckBoxButton(2, 16, 6).setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent    )
                .enableDynamicBackground(GUISettings.instance().getEnabledButtonColor(), GUISettings.instance().getDisabledButtonColor(), GUISettings.instance().getHoveredButtonColor()));
        this.addElement(this.hideOverlayTextLabel = new GUITextLabel(10, 15).setDisplayText(I18n.format("oxygen.gui.notifications.hideOverlay"), false, GUISettings.instance().getSubTextScale()));
        this.hideOverlayButton.setToggled(OxygenHelperClient.getClientSettingBoolean(OxygenMain.HIDE_REQUESTS_OVERLAY_SETTING));

        this.addElement(this.defaultNoteTextLabel = new GUITextLabel((this.getWidth() - this.textWidth(baseNoticeStr, GUISettings.instance().getTextScale())) / 2, 31).setDisplayText(baseNoticeStr, false, GUISettings.instance().getTextScale()));

        this.addElement(this.notificationsPanel = new GUIButtonPanel(GUIEnumOrientation.VERTICAL, 0, 25, 214, 18).setButtonsOffset(1).setTextScale(GUISettings.instance().getPanelTextScale()));   
        GUIScroller scroller = new GUIScroller(20, 10);
        this.notificationsPanel.initScroller(scroller);
        GUISlider slider = new GUISlider(215, 25, 2, 189);
        slider.setDynamicBackgroundColor(GUISettings.instance().getEnabledSliderColor(), GUISettings.instance().getDisabledSliderColor(), GUISettings.instance().getHoveredSliderColor());
        scroller.initSlider(slider);
    }

    public void updateNotifications() {
        Set<INotification> ordered = new TreeSet<INotification>(OxygenManagerClient.instance().getNotificationsManager().getNotifications().values());
        NotificationGUIButton button;
        this.notificationsPanel.reset();
        for (INotification notification : ordered) {
            button = new NotificationGUIButton(notification);
            button.enableDynamicBackground(GUISettings.instance().getEnabledElementColor(), GUISettings.instance().getDisabledElementColor(), GUISettings.instance().getHoveredElementColor());
            button.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
            this.notificationsPanel.addButton(button);
        }
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {
        if (element == this.hideOverlayButton) {
            if (this.hideOverlayButton.isToggled()) {
                OxygenHelperClient.setClientSetting(OxygenMain.HIDE_REQUESTS_OVERLAY_SETTING, true);
                OxygenHelperClient.saveClientSettings();
            } else {
                OxygenHelperClient.setClientSetting(OxygenMain.HIDE_REQUESTS_OVERLAY_SETTING, false);
                OxygenHelperClient.saveClientSettings();
            }
        }
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {   
        if (keyCode == OxygenKeyHandler.NOTIFICATIONS_MENU.getKeyBinding().getKeyCode())
            this.screen.close();
        return super.keyTyped(typedChar, keyCode); 
    }

    @Override
    public void update() {
        if (OxygenManagerClient.instance().getNotificationsManager().getNotifications().size() != this.prevSize) {
            this.updateNotifications();
            if (OxygenManagerClient.instance().getNotificationsManager().getNotifications().size() == 0)
                this.defaultNoteTextLabel.enableFull();
            else
                this.defaultNoteTextLabel.disableFull();
        }
        this.prevSize = OxygenManagerClient.instance().getNotificationsManager().getNotifications().size();
    }
}
