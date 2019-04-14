package austeretony.oxygen.client.gui.notifications;

import java.util.Set;
import java.util.TreeSet;

import austeretony.alternateui.screen.browsing.GUIScroller;
import austeretony.alternateui.screen.button.GUISlider;
import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.image.GUIImageLabel;
import austeretony.alternateui.screen.panel.GUIButtonPanel;
import austeretony.alternateui.screen.panel.GUIButtonPanel.GUIEnumOrientation;
import austeretony.alternateui.screen.text.GUITextLabel;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.handler.OxygenKeyHandler;
import austeretony.oxygen.common.notification.IOxygenNotification;
import net.minecraft.client.resources.I18n;

public class NotificationsGUISection extends AbstractGUISection {

    private GUIButtonPanel notificationsPanel;

    private GUITextLabel defaultNoteTextLabel;

    private int prevSize;

    public NotificationsGUISection(AbstractGUIScreen screen) {
        super(screen);
    }

    @Override
    protected void init() {
        this.addElement(new GUIImageLabel(- 1, - 1, this.getWidth() + 2, this.getHeight() + 2).enableStaticBackground(GUISettings.instance().getBaseGUIBackgroundColor()));//main background
        this.addElement(new GUIImageLabel(0, 0, this.getWidth(), 15).enableStaticBackground(GUISettings.instance().getAdditionalGUIBackgroundColor()));//title background
        this.addElement(new GUIImageLabel(0, 16, this.getWidth() - 3, this.getHeight() - 16).enableStaticBackground(GUISettings.instance().getAdditionalGUIBackgroundColor()));//list background
        this.addElement(new GUIImageLabel(this.getWidth() - 2, 16, 2, this.getHeight() - 16).enableStaticBackground(GUISettings.instance().getAdditionalGUIBackgroundColor()));//slider background
        this.addElement(new GUITextLabel(2, 4).setDisplayText(I18n.format("oxygen.gui.notifications.title"), false, GUISettings.instance().getTitleScale()));
        String baseNoticeStr = I18n.format("oxygen.gui.notifications.empty");
        this.addElement(this.defaultNoteTextLabel = new GUITextLabel((this.getWidth() - this.width(baseNoticeStr, GUISettings.instance().getTextScale())) / 2, 22).setDisplayText(baseNoticeStr, false, GUISettings.instance().getTextScale()));

        this.addElement(this.notificationsPanel = new GUIButtonPanel(GUIEnumOrientation.VERTICAL, 0, 16, 214, 18).setButtonsOffset(1).setTextScale(GUISettings.instance().getTextScale()));   
        GUIScroller scroller = new GUIScroller(20, 10);
        this.notificationsPanel.initScroller(scroller);
        scroller.initSlider(new GUISlider(215, 16, 2, 189));
    }

    public void updateNotifications() {
        Set<IOxygenNotification> ordered = new TreeSet<IOxygenNotification>(OxygenManagerClient.instance().getNotificationsManager().getNotifications().values());
        NotificationGUIButton button;
        this.notificationsPanel.reset();
        for (IOxygenNotification notification : ordered) {
            button = new NotificationGUIButton(notification);
            button.enableDynamicBackground(GUISettings.instance().getEnabledElementColor(), GUISettings.instance().getDisabledElementColor(), GUISettings.instance().getHoveredElementColor());
            button.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
            this.notificationsPanel.addButton(button);
        }
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element) {}

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
