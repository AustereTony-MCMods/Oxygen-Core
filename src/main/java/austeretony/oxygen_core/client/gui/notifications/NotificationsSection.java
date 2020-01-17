package austeretony.oxygen_core.client.gui.notifications;

import java.util.Set;
import java.util.TreeSet;

import org.lwjgl.input.Keyboard;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.api.OxygenGUIHelper;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.gui.elements.OxygenDefaultBackgroundFiller;
import austeretony.oxygen_core.client.gui.elements.OxygenScrollablePanel;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.notification.Notification;

public class NotificationsSection extends AbstractGUISection {

    private OxygenScrollablePanel notificationsPanel;

    private OxygenTextLabel defaultNoteTextLabel;

    //cache

    private int prevSize;

    public NotificationsSection(AbstractGUIScreen screen) {
        super(screen);
    }

    @Override
    public void init() {
        this.addElement(new OxygenDefaultBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.notifications.title"), EnumBaseGUISetting.TEXT_TITLE_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        String notification = ClientReference.localize("oxygen_core.gui.notifications.empty");   
        this.addElement(this.defaultNoteTextLabel = new OxygenTextLabel((this.getWidth() - this.textWidth(notification, EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.05F)) / 2, 25, notification, EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.05F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        this.addElement(this.notificationsPanel = new OxygenScrollablePanel(this.screen, 6, 16, this.getWidth() - 12, 18, 1, 50, 8, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), false));   
    }

    public void updateNotifications() {
        Set<Notification> ordered = new TreeSet<>(OxygenManagerClient.instance().getNotificationsManager().getNotifications().values());
        this.notificationsPanel.reset();
        for (Notification notification : ordered)
            this.notificationsPanel.addEntry(new NotificationPanelEntry(notification));
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {}

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {  
        if (OxygenGUIHelper.isOxygenMenuEnabled()) {
            if (keyCode == Keyboard.KEY_N)
                this.screen.close();
        } else if (OxygenConfig.ENABLE_NOTIFICATIONS_KEY.asBoolean() 
                && keyCode == OxygenHelperClient.getKeyHandler().getNotificationsKeybinding().getKeyCode())
            this.screen.close();
        return super.keyTyped(typedChar, keyCode); 
    }

    @Override
    public void update() {
        int size = OxygenManagerClient.instance().getNotificationsManager().getNotifications().size();
        if (size != this.prevSize) {
            this.defaultNoteTextLabel.setVisible(size == 0);
            this.updateNotifications();
        }
        this.prevSize = size;
    }
}
