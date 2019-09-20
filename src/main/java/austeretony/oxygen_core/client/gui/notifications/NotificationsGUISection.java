package austeretony.oxygen_core.client.gui.notifications;

import java.util.Set;
import java.util.TreeSet;

import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.OxygenGUIHelper;
import austeretony.oxygen_core.client.gui.OxygenGUITextures;
import austeretony.oxygen_core.client.gui.elements.ActivityStatusGUIDDList;
import austeretony.oxygen_core.client.gui.elements.OxygenGUIButtonPanel;
import austeretony.oxygen_core.client.gui.elements.OxygenGUIText;
import austeretony.oxygen_core.client.gui.elements.OxygenTexturedGUIButton;
import austeretony.oxygen_core.client.gui.notifications.callback.SettingsGUICallback;
import austeretony.oxygen_core.client.gui.settings.GUISettings;
import austeretony.oxygen_core.client.input.NotificationsMenuKeyHandler;
import austeretony.oxygen_core.common.notification.Notification;

public class NotificationsGUISection extends AbstractGUISection {

    private OxygenGUIButtonPanel notificationsPanel;

    private OxygenGUIText defaultNoteTextLabel;

    private OxygenTexturedGUIButton settingsButton;

    private AbstractGUICallback settingsCallback;

    private int prevSize;

    //TODO DEBUG
    private ActivityStatusGUIDDList list;

    public NotificationsGUISection(AbstractGUIScreen screen) {
        super(screen);
    }

    @Override
    public void init() {
        this.addElement(new NotificationsSectionGUIFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenGUIText(4, 5, ClientReference.localize("oxygen.gui.notifications.title"), GUISettings.get().getTitleScale(), GUISettings.get().getEnabledTextColor()));

        this.addElement(this.settingsButton = new OxygenTexturedGUIButton(this.getWidth() - 5, 0, 5, 5, OxygenGUITextures.TRIANGLE_TOP_RIGHT_CORNER_ICONS, 5, 5, ClientReference.localize("oxygen.tooltip.settings")));

        String baseNoticeStr = ClientReference.localize("oxygen.gui.notifications.empty");   
        this.addElement(this.defaultNoteTextLabel = new OxygenGUIText((this.getWidth() - this.textWidth(baseNoticeStr, GUISettings.get().getSubTextScale() - 0.05F)) / 2, 20, baseNoticeStr, GUISettings.get().getSubTextScale() - 0.05F, GUISettings.get().getEnabledTextColorDark()));

        this.addElement(this.notificationsPanel = new OxygenGUIButtonPanel(this.screen, 6, 16, this.getWidth() - 15, 20, 1, 50, 7, 1.0F, true));   

        this.settingsCallback = new SettingsGUICallback(this.screen, this, 140, 42).enableDefaultBackground();
    }

    public void updateNotifications() {
        Set<Notification> ordered = new TreeSet<Notification>(OxygenManagerClient.instance().getNotificationsManager().getNotifications().values());
        this.notificationsPanel.reset();
        for (Notification notification : ordered)
            this.notificationsPanel.addButton(new NotificationGUIButton(notification));
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {
        if (mouseButton == 0) {
            if (element == this.settingsButton)
                this.settingsCallback.open();
        }
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {  
        if (OxygenGUIHelper.isOxygenMenuEnabled()) {
            if (keyCode == NotificationsGUIScreen.NOTIFICATIONS_MENU_ENTRY.getIndex() + 2)
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