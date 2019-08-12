package austeretony.oxygen.client.gui.notifications;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUIWorkspace;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen.client.gui.AbstractMenuEntry;
import austeretony.oxygen.client.gui.NotificationsMenuEntry;
import austeretony.oxygen.common.main.OxygenMain;
import net.minecraft.util.ResourceLocation;

public class NotificationsGUIScreen extends AbstractGUIScreen {

    public static final ResourceLocation 
    NOTIFICATIONS_MENU_BACKGROUND = new ResourceLocation(OxygenMain.MODID, "textures/gui/notifications/notifications_menu.png"),
    SETTINGS_CALLBACK_BACKGROUND = new ResourceLocation(OxygenMain.MODID, "textures/gui/notifications/settings_callback.png");

    public static final AbstractMenuEntry NOTIFICATIONS_MENU_ENTRY = new NotificationsMenuEntry();

    protected AbstractGUISection notificationsSection;

    @Override
    protected GUIWorkspace initWorkspace() {
        return new GUIWorkspace(this, 180, 160).setAlignment(EnumGUIAlignment.RIGHT, - 10, 0);
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
