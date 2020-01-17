package austeretony.oxygen_core.client.gui.notifications;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUIWorkspace;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.gui.menu.NotificationsMenuEntry;
import austeretony.oxygen_core.client.gui.menu.OxygenMenuEntry;
import austeretony.oxygen_core.client.settings.gui.EnumCoreGUISetting;

public class NotificationsScreen extends AbstractGUIScreen {

    public static final OxygenMenuEntry NOTIFICATIONS_MENU_ENTRY = new NotificationsMenuEntry();

    protected AbstractGUISection notificationsSection;

    @Override
    protected GUIWorkspace initWorkspace() {
        EnumGUIAlignment alignment = EnumGUIAlignment.CENTER;
        switch (EnumCoreGUISetting.NOTIFICATIONS_MENU_ALIGNMENT.get().asInt()) {
        case - 1: 
            alignment = EnumGUIAlignment.LEFT;
            break;
        case 0:
            alignment = EnumGUIAlignment.CENTER;
            break;
        case 1:
            alignment = EnumGUIAlignment.RIGHT;
            break;    
        default:
            alignment = EnumGUIAlignment.CENTER;
            break;
        }
        return new GUIWorkspace(this, 190, 169).setAlignment(alignment, 0, 0);
    }

    @Override
    protected void initSections() {
        this.notificationsSection = this.getWorkspace().initSection(new NotificationsSection(this));        
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
