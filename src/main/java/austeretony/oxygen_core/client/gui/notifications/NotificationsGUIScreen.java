package austeretony.oxygen_core.client.gui.notifications;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUIWorkspace;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.gui.menu.OxygenMenuEntry;

public class NotificationsGUIScreen extends AbstractGUIScreen {

    public static final OxygenMenuEntry NOTIFICATIONS_MENU_ENTRY = new NotificationsMenuEntry();

    protected AbstractGUISection notificationsSection;

    @Override
    protected GUIWorkspace initWorkspace() {
        return new GUIWorkspace(this, 180, 164).setAlignment(EnumGUIAlignment.RIGHT, - 10, 0);
    }

    @Override
    protected void initSections() {
        this.notificationsSection = this.getWorkspace().initSection(new NotificationsGUISection(this));        
    }

    @Override
    protected AbstractGUISection getDefaultSection() {
        return this.notificationsSection;
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element) {}

    @Override
    protected boolean doesGUIPauseGame() {
        return false;
    }
}
