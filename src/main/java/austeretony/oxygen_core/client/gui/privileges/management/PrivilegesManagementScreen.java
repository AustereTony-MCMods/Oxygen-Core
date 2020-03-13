package austeretony.oxygen_core.client.gui.privileges.management;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUIWorkspace;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.settings.gui.EnumCoreGUISetting;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.server.SPRequestPrivilegesData;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.Role;

public class PrivilegesManagementScreen extends AbstractGUIScreen {

    protected RolesSection rolesSection;

    protected DefaultPrivilegesSection defaultPrivilegesSection;

    protected PlayersSection playersSection;

    public PrivilegesManagementScreen() {
        OxygenHelperClient.syncSharedData(- 1);
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
        return new GUIWorkspace(this, 300, 178).setAlignment(alignment, 0, 0);
    }

    @Override
    protected void initSections() {
        this.rolesSection = (RolesSection) this.getWorkspace().initSection(new RolesSection(this).enable()); 
        this.defaultPrivilegesSection = (DefaultPrivilegesSection) this.getWorkspace().initSection(new DefaultPrivilegesSection(this).enable()); 
        this.playersSection = (PlayersSection) this.getWorkspace().initSection(new PlayersSection(this).enable()); 
    }

    @Override
    protected AbstractGUISection getDefaultSection() {
        return this.rolesSection;
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element) {}

    @Override
    protected boolean doesGUIPauseGame() {
        return false;
    }

    public void privilegesDataReceived() {
        this.rolesSection.managementDataReceived();
        this.defaultPrivilegesSection.privilegesDataReceived();
        this.playersSection.privilegesDataReceived();
    }

    public void roleCreated(Role role) {
        this.rolesSection.roleCreated(role);
    }

    public void roleRemoved(Role role) {
        this.rolesSection.roleRemoved(role);
    }

    public void rolePrivilegeAdded(int roleId, Privilege privilege) {
        this.rolesSection.rolePrivilegeAdded(roleId, privilege);
    }

    public void rolePrivilegeRemoved(int roleId, Privilege privilege) {
        this.rolesSection.rolePrivilegeRemoved(roleId, privilege);
    }

    public void defaultPrivilegeAdded(Privilege privilege) {
        this.defaultPrivilegesSection.defaultPrivilegeAdded(privilege);
    }

    public void defaultPrivilegeRemoved(Privilege privilege) {
        this.defaultPrivilegesSection.defaultPrivilegeRemoved(privilege);
    }

    public void playerRolesChanged(int roleId, PlayerSharedData sharedData) {
        this.rolesSection.playerRolesChanged(roleId, sharedData);
        this.playersSection.playerRolesChanged(roleId, sharedData);
    }

    public RolesSection getRolesSection() {
        return this.rolesSection;
    }

    public DefaultPrivilegesSection getDefaultPrivilegesSection() {
        return this.defaultPrivilegesSection;
    }

    public PlayersSection getPlayersSection() {
        return this.playersSection;
    }
}
