package austeretony.oxygen_core.client.gui.privileges;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUIWorkspace;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.gui.menu.OxygenMenuEntry;
import austeretony.oxygen_core.client.gui.menu.PrivilegesMenuEntry;
import austeretony.oxygen_core.client.settings.gui.EnumCoreGUISetting;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.server.SPRequestPrivilegesData;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.Role;

public class PrivilegesScreen extends AbstractGUIScreen {

    public static final OxygenMenuEntry PRIVILEGES_MENU_ENTRY = new PrivilegesMenuEntry();

    protected InformationSection informationSection;

    protected RolesSection rolesSection;

    protected PlayersSection playersSection;

    public PrivilegesScreen() {
        OxygenMain.network().sendToServer(new SPRequestPrivilegesData());
    }

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
        return new GUIWorkspace(this, 290, 140).setAlignment(alignment, 0, 0);
    }

    @Override
    protected void initSections() {
        this.informationSection = (InformationSection) this.getWorkspace().initSection(new InformationSection(this).enable());  
        this.rolesSection = (RolesSection) this.getWorkspace().initSection(new RolesSection(this).enable()); 
        this.playersSection = (PlayersSection) this.getWorkspace().initSection(new PlayersSection(this).enable()); 
    }

    @Override
    protected AbstractGUISection getDefaultSection() {
        return this.informationSection;
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element) {}

    @Override
    protected boolean doesGUIPauseGame() {
        return false;
    }

    public void privilegesDataReceived() {
        this.rolesSection.privilegesDataReceived();
        this.playersSection.privilegesDataReceived();
    }

    public void roleCreated(Role role) {
        this.rolesSection.roleCreated(role);
    }

    public void roleRemoved(Role role) {
        this.rolesSection.roleRemoved(role);
    }

    public void privilegeAdded(int roleId, Privilege privilege) {
        this.rolesSection.privilegeAdded(roleId, privilege);
    }

    public void privilegeRemoved(int roleId, Privilege privilege) {
        this.rolesSection.privilegeRemoved(roleId, privilege);
    }

    public InformationSection getInformationSection() {
        return this.informationSection;
    }

    public RolesSection getRolesSection() {
        return this.rolesSection;
    }

    public PlayersSection getPlayersSection() {
        return this.playersSection;
    }
}
