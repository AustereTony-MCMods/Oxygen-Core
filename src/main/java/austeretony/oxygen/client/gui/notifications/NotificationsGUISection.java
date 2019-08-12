package austeretony.oxygen.client.gui.notifications;

import java.util.Set;
import java.util.TreeSet;

import austeretony.alternateui.screen.browsing.GUIScroller;
import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.screen.button.GUISlider;
import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.panel.GUIButtonPanel;
import austeretony.alternateui.screen.text.GUITextLabel;
import austeretony.alternateui.util.EnumGUIOrientation;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.api.OxygenGUIHelper;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.gui.OxygenGUITextures;
import austeretony.oxygen.client.gui.notifications.callback.SettingsGUICallback;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.input.NotificationsMenuKeyHandler;
import austeretony.oxygen.common.main.OxygenSoundEffects;
import austeretony.oxygen.common.notification.INotification;

public class NotificationsGUISection extends AbstractGUISection {

    private GUIButtonPanel notificationsPanel;

    private GUITextLabel defaultNoteTextLabel;

    private GUIButton settingsButton;

    private AbstractGUICallback settingsCallback;

    private int prevSize;

    public NotificationsGUISection(AbstractGUIScreen screen) {
        super(screen);
    }

    @Override
    public void init() {
        this.addElement(new NotificationsGUIFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new GUITextLabel(2, 4).setDisplayText(ClientReference.localize("oxygen.gui.notifications.title"), false, GUISettings.instance().getTitleScale()));
        this.addElement(this.settingsButton = new GUIButton(this.getWidth() - 5, 0, 5, 5).setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent).setTexture(OxygenGUITextures.TRIANGLE_TOP_RIGHT_CORNER_ICONS, 5, 5).initSimpleTooltip(ClientReference.localize("oxygen.tooltip.settings"), GUISettings.instance().getTooltipScale()));

        String baseNoticeStr = ClientReference.localize("oxygen.gui.notifications.empty");   
        this.addElement(this.defaultNoteTextLabel = new GUITextLabel((this.getWidth() - this.textWidth(baseNoticeStr, GUISettings.instance().getSubTextScale())) / 2, 16).setDisplayText(baseNoticeStr, false, GUISettings.instance().getSubTextScale()));

        this.addElement(this.notificationsPanel = new GUIButtonPanel(EnumGUIOrientation.VERTICAL, 0, 14, this.getWidth() - 3, 20).setButtonsOffset(1).setTextScale(GUISettings.instance().getPanelTextScale()));   
        GUIScroller scroller = new GUIScroller(50, 7);
        this.notificationsPanel.initScroller(scroller);
        GUISlider slider = new GUISlider(this.getWidth() - 2, 14, 2, 146);
        slider.setDynamicBackgroundColor(GUISettings.instance().getEnabledSliderColor(), GUISettings.instance().getDisabledSliderColor(), GUISettings.instance().getHoveredSliderColor());
        scroller.initSlider(slider);

        this.settingsCallback = new SettingsGUICallback(this.screen, this, 140, 38).enableDefaultBackground();
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
        if (mouseButton == 0)
            if (element == this.settingsButton)
                this.settingsCallback.open();
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {  
        if (OxygenGUIHelper.isOxygenMenuEnabled()) {
            if (keyCode == NotificationsGUIScreen.NOTIFICATIONS_MENU_ENTRY.index + 2)
                this.screen.close();
        } else if (keyCode == NotificationsMenuKeyHandler.NOTIFICATIONS_MENU.getKeyCode())
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
