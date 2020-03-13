package austeretony.oxygen_core.client.privilege;

import java.util.TreeSet;
import java.util.UUID;

import javax.annotation.Nullable;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.gui.privileges.management.PrivilegesManagementScreen;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.server.SPDefaultPrivilegeOperation;
import austeretony.oxygen_core.common.network.server.SPPlayerRoleOperation;
import austeretony.oxygen_core.common.network.server.SPRemoveRole;
import austeretony.oxygen_core.common.network.server.SPRoleOperation;
import austeretony.oxygen_core.common.network.server.SPRolePrivilegeOperation;
import austeretony.oxygen_core.common.network.server.SPSetChatFormattingRole;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.Role;
import io.netty.buffer.ByteBuf;

public class PrivilegesManagerClient {

    private final OxygenManagerClient manager;

    public PrivilegesManagerClient(OxygenManagerClient manager) {
        this.manager = manager;
    }

    public void setChatFormattingRoleSynced(int roleId) {
        OxygenMain.network().sendToServer(new SPSetChatFormattingRole(roleId));
    }

    @Nullable
    public Role getPriorityPlayerRole() {
        if (!this.manager.getPrivilegesContainer().getClientPlayerRolesIds().isEmpty())
            return this.manager.getPrivilegesContainer().getClientPlayerRole(((TreeSet<Integer>) this.manager.getPrivilegesContainer().getClientPlayerRolesIds()).first());
        return null;
    }

    @Nullable
    public Privilege getPriorityPlayerPrivilege(int privilegeId) {
        Role role;
        Privilege privilege;
        for (int roleId : this.manager.getPrivilegesContainer().getClientPlayerRolesIds()) {
            role = this.manager.getPrivilegesContainer().getClientPlayerRole(roleId);
            if ((privilege = role.getPrivilege(privilegeId)) != null)
                return privilege;
        }
        return this.manager.getPrivilegesContainer().getDefaultPrivilege(privilegeId);
    }

    //management

    public void managementDataReceived(ByteBuf buffer) {
        this.manager.getPrivilegesContainer().managementDataReceived(buffer);
        ClientReference.delegateToClientThread(()->{
            if (isManagementMenuOpened())
                ((PrivilegesManagementScreen) ClientReference.getCurrentScreen()).privilegesDataReceived();
        });
    }

    public void createRoleSynced(int roleId, String roleName, String prefix, int roleNameColor, int prefixColor, int usernameColor, int chatColor) {
        OxygenMain.network().sendToServer(new SPRoleOperation(SPRoleOperation.EnumOperation.CREATE, roleId, roleName, prefix, roleNameColor, prefixColor, usernameColor, chatColor));
    }


    public void editRoleSynced(int roleId, String roleName, String prefix, int roleNameColor, int prefixColor, int usernameColor, int chatColor) {        
        OxygenMain.network().sendToServer(new SPRoleOperation(SPRoleOperation.EnumOperation.EDIT, roleId, roleName, prefix, roleNameColor, prefixColor, usernameColor, chatColor));
    }

    public void removeRoleSynced(int roleId) {
        OxygenMain.network().sendToServer(new SPRemoveRole(roleId));
    }

    public void addPrivilegeSynced(int roleId, int privilegeId, String value) {
        OxygenMain.network().sendToServer(new SPRolePrivilegeOperation(SPRolePrivilegeOperation.EnumOperation.ADD, roleId, privilegeId, value));
    }

    public void removePrivilegeSynced(int roleId, int privilegeId) {
        OxygenMain.network().sendToServer(new SPRolePrivilegeOperation(SPRolePrivilegeOperation.EnumOperation.REMOVE, roleId, privilegeId, ""));
    }

    public void addDefaultPrivilegeSynced(int privilegeId, String privilegeValue) {
        OxygenMain.network().sendToServer(new SPDefaultPrivilegeOperation(SPDefaultPrivilegeOperation.EnumOperation.ADD, privilegeId, privilegeValue));
    }

    public void removeDefaultPrivilegeSynced(int privilegeId) {
        OxygenMain.network().sendToServer(new SPDefaultPrivilegeOperation(SPDefaultPrivilegeOperation.EnumOperation.REMOVE, privilegeId, ""));
    }

    public void addRoleToPlayerSynced(UUID playerUUID, int roleId) {
        OxygenMain.network().sendToServer(new SPPlayerRoleOperation(SPPlayerRoleOperation.EnumOperation.ADD_ROLE, roleId, playerUUID));
    }

    public void removeRoleFromPlayerSynced(UUID playerUUID, int roleId) {
        OxygenMain.network().sendToServer(new SPPlayerRoleOperation(SPPlayerRoleOperation.EnumOperation.REMOVE_ROLE, roleId, playerUUID));
    }

    public void roleCreated(Role role) {
        this.manager.getPrivilegesContainer().addServerRole(role);
        ClientReference.delegateToClientThread(()->{
            if (isManagementMenuOpened())
                ((PrivilegesManagementScreen) ClientReference.getCurrentScreen()).roleCreated(role);;
        }); 
    }

    public void roleRemoved(Role role) {
        this.manager.getPrivilegesContainer().roleRemoved(role);
        ClientReference.delegateToClientThread(()->{
            if (isManagementMenuOpened())
                ((PrivilegesManagementScreen) ClientReference.getCurrentScreen()).roleRemoved(role);;
        }); 
    }

    public void rolePrivilegeAdded(int roleId, Privilege privilege) {        
        this.manager.getPrivilegesContainer().rolePrivilegeAdded(roleId, privilege);
        ClientReference.delegateToClientThread(()->{
            if (isManagementMenuOpened())
                ((PrivilegesManagementScreen) ClientReference.getCurrentScreen()).rolePrivilegeAdded(roleId, privilege);;
        }); 
    }

    public void rolePrivilegeRemoved(int roleId, Privilege privilege) {
        this.manager.getPrivilegesContainer().rolePrivilegeRemoved(roleId, privilege);
        ClientReference.delegateToClientThread(()->{
            if (isManagementMenuOpened())
                ((PrivilegesManagementScreen) ClientReference.getCurrentScreen()).rolePrivilegeRemoved(roleId, privilege);;
        }); 
    }

    public void defaultPrivilegeAdded(Privilege privilege) {
        this.manager.getPrivilegesContainer().defaultPrivilegeAdded(privilege);
        ClientReference.delegateToClientThread(()->{
            if (isManagementMenuOpened())
                ((PrivilegesManagementScreen) ClientReference.getCurrentScreen()).defaultPrivilegeAdded(privilege);;
        }); 
    }

    public void defaultPrivilegeRemoved(Privilege privilege) {
        this.manager.getPrivilegesContainer().defaultPrivilegeRemoved(privilege);
        ClientReference.delegateToClientThread(()->{
            if (isManagementMenuOpened())
                ((PrivilegesManagementScreen) ClientReference.getCurrentScreen()).defaultPrivilegeRemoved(privilege);;
        }); 
    }

    public void playerRolesChanged(int roleId, PlayerSharedData sharedData) {
        this.manager.getPrivilegesContainer().playerRolesChanged(roleId, sharedData);
        ClientReference.delegateToClientThread(()->{
            if (isManagementMenuOpened())
                ((PrivilegesManagementScreen) ClientReference.getCurrentScreen()).playerRolesChanged(roleId, sharedData);;
        }); 
    }

    public static boolean isManagementMenuOpened() {
        return ClientReference.hasActiveGUI() && ClientReference.getCurrentScreen() instanceof PrivilegesManagementScreen;
    }
}
