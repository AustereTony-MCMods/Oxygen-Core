package austeretony.oxygen_core.client.gui.privileges.information;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUIWorkspace;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.gui.menu.OxygenMenuEntry;
import austeretony.oxygen_core.client.gui.menu.PrivilegesMenuEntry;
import austeretony.oxygen_core.client.settings.gui.EnumCoreGUISetting;

public class PrivilegesScreen extends AbstractGUIScreen {

    public static final OxygenMenuEntry PRIVILEGES_MENU_ENTRY = new PrivilegesMenuEntry();

    protected AbstractGUISection privilegesSection;

    @Override
    protected GUIWorkspace initWorkspace() {
        EnumGUIAlignment alignment = EnumGUIAlignment.CENTER;
        switch (EnumCoreGUISetting.PRIVILEGES_MENU_ALIGNMENT.get().asInt()) {
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
        return new GUIWorkspace(this, 300, 202).setAlignment(alignment, 0, 0);
    }

    @Override
    protected void initSections() {
        this.privilegesSection = this.getWorkspace().initSection(new PrivilegesSection(this));  
    }

    @Override
    protected AbstractGUISection getDefaultSection() {
        return this.privilegesSection;
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element) {}

    @Override
    protected boolean doesGUIPauseGame() {
        return false;
    }
}
