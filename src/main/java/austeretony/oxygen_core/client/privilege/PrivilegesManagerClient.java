package austeretony.oxygen_core.client.privilege;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.gui.privileges.PrivilegesScreen;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.server.SPAddPrivilege;
import austeretony.oxygen_core.common.network.server.SPCreateRole;
import austeretony.oxygen_core.common.network.server.SPManagePlayerRoles;
import austeretony.oxygen_core.common.network.server.SPRemovePrivilege;
import austeretony.oxygen_core.common.network.server.SPRemoveRole;
import austeretony.oxygen_core.common.network.server.SPSetChatFormattingRole;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.privilege.RoleImpl;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextFormatting;

public class PrivilegesManagerClient {

    private final Map<Integer, Role> playerRoles = new ConcurrentHashMap<>(5);

    private final Set<Integer> orderedRolesIds = new TreeSet<>((i, j)->(j - i));

    private final Map<Integer, RoleDataClient> rolesData = new ConcurrentHashMap<>(5);

    private int chatFormattingRoleId = OxygenMain.DEFAULT_ROLE_INDEX;

    //management

    private final Map<Integer, Role> roles = new ConcurrentHashMap<>();

    private final Map<UUID, PlayerSharedData> players = new ConcurrentHashMap<>();

    public void playerRolesReceived(ByteBuf buffer) { 
        OxygenMain.LOGGER.info("Player roles received.");
        this.playerRoles.clear();
        this.orderedRolesIds.clear();
        try {
            if (buffer.readableBytes() != 0) {
                this.chatFormattingRoleId = buffer.readByte();
                int amount = buffer.readByte();
                for (int i = 0; i < amount; i++)
                    this.addPlayerRole(RoleImpl.read(buffer));
                OxygenMain.LOGGER.info("Synchronized {} player roles entries.", amount);
            }
        } finally {
            if (buffer != null)
                buffer.release();
        }
    }

    private void addPlayerRole(Role role) {
        this.playerRoles.put(role.getId(), role);
        this.orderedRolesIds.add(role.getId());
    }

    public Role getPlayerRole(int roleId) {
        return this.playerRoles.get(roleId);
    }

    public void rolesDataReceived(ByteBuf buffer) {
        this.rolesData.clear();
        try {
            int amount = buffer.readByte();
            for (int i = 0; i < amount; i++)
                this.addRoleData(buffer.readByte(), ByteBufUtils.readString(buffer), TextFormatting.values()[buffer.readByte()]);
            OxygenMain.LOGGER.info("Synchronized {} roles data entries.", amount);
        } finally {
            if (buffer != null)
                buffer.release();
        }
    }

    public void addRoleData(int roleId, String name, TextFormatting nameColor) {
        this.rolesData.put(roleId, new RoleDataClient(name, nameColor));
    }

    public RoleDataClient getRoleData(int roleId) {
        return this.rolesData.get(roleId);
    }

    public int getChatFormattingRoleId() {
        return this.chatFormattingRoleId;
    }

    public Role getChatFormattingRole() {
        return this.playerRoles.get(this.chatFormattingRoleId);
    }

    public void setChatFormattingRoleSynced(int roleId) {
        this.chatFormattingRoleId = roleId;
        OxygenMain.network().sendToServer(new SPSetChatFormattingRole(roleId));
    }

    public Set<Integer> getPlayerRolesIds() {
        return this.orderedRolesIds;
    }

    public Role getPriorityPlayerRole() {
        if (!this.getPlayerRolesIds().isEmpty())
            return this.getPlayerRole(((TreeSet<Integer>) this.getPlayerRolesIds()).first());
        return null;
    }

    public Privilege getPriorityPlayerPrivilege(int privilegeId) {
        Role role;
        Privilege privilege;
        for (int roleId : this.getPlayerRolesIds()) {
            role = this.getPlayerRole(roleId);
            if ((privilege = role.getPrivilege(privilegeId)) != null)
                return privilege;
        }
        return null;
    }

    //management

    public void privilegesDataReceived(ByteBuf buffer) {
        this.roles.clear();
        this.players.clear();
        try {
            int i, amount = buffer.readByte();
            for (i = 0; i < amount; i++)
                this.addRole(RoleImpl.read(buffer));

            amount = buffer.readShort();
            PlayerSharedData sharedData;
            for (i = 0; i < amount; i++) {
                sharedData = PlayerSharedData.read(buffer);
                this.players.put(sharedData.getPlayerUUID(), sharedData);
            }
        } finally {
            if (buffer != null)
                buffer.release();
        }
        this.onPrivilegesDataReceived();
    }

    public Collection<Role> getRoles() {
        return this.roles.values();
    }

    public Role getRole(int roleId) {
        return this.roles.get(roleId);
    }

    private void addRole(Role role) {
        this.roles.put(role.getId(), role);
    }

    public Collection<PlayerSharedData> getPlayersData() {
        return this.players.values();
    }

    public PlayerSharedData getPlayerData(UUID playerUUID) {
        PlayerSharedData sharedData = this.players.get(playerUUID);
        if (sharedData == null)
            sharedData = OxygenHelperClient.getPlayerSharedData(playerUUID);
        return sharedData;
    }

    public void createRoleSynced(int roleId, String roleName, String prefix, int roleNameColor, int prefixColor, int usernameColor, int chatColor) {
        OxygenMain.network().sendToServer(new SPCreateRole(roleId, roleName, prefix, roleNameColor, prefixColor, usernameColor, chatColor));
    }

    public void removeRoleSynced(int roleId) {
        OxygenMain.network().sendToServer(new SPRemoveRole(roleId));
    }

    public void addPrivilegeSynced(int roleId, int privilegeId, String value) {
        OxygenMain.network().sendToServer(new SPAddPrivilege(roleId, privilegeId, value));
    }

    public void removePrivilegeSynced(int roleId, int privilegeId) {
        OxygenMain.network().sendToServer(new SPRemovePrivilege(roleId, privilegeId));
    }

    public void addRoleToPlayerSynced(UUID playerUUID, int roleId) {
        OxygenMain.network().sendToServer(new SPManagePlayerRoles(SPManagePlayerRoles.EnumAction.ADD_ROLE, roleId, playerUUID));
    }

    public void removeRoleFromPlayerSynced(UUID playerUUID, int roleId) {
        OxygenMain.network().sendToServer(new SPManagePlayerRoles(SPManagePlayerRoles.EnumAction.REMOVE_ROLE, roleId, playerUUID));
    }

    public void roleCreated(Role role) {
        this.addRole(role);
        ClientReference.delegateToClientThread(()->{
            if (isMenuOpened())
                ((PrivilegesScreen) ClientReference.getCurrentScreen()).roleCreated(role);;
        }); 
    }

    public void roleRemoved(Role role) {
        this.roles.remove(role.getId());
        ClientReference.delegateToClientThread(()->{
            if (isMenuOpened())
                ((PrivilegesScreen) ClientReference.getCurrentScreen()).roleRemoved(role);;
        }); 
    }

    public void privilegeAdded(int roleId, Privilege privilege) {        
        this.getRole(roleId).addPrivilege(privilege);
        ClientReference.delegateToClientThread(()->{
            if (isMenuOpened())
                ((PrivilegesScreen) ClientReference.getCurrentScreen()).privilegeAdded(roleId, privilege);;
        }); 
    }

    public void privilegeRemoved(int roleId, Privilege privilege) {
        this.getRole(roleId).removePrivilege(privilege.getId());
        ClientReference.delegateToClientThread(()->{
            if (isMenuOpened())
                ((PrivilegesScreen) ClientReference.getCurrentScreen()).privilegeRemoved(roleId, privilege);;
        }); 
    }

    //menu

    public void onPrivilegesDataReceived() {
        ClientReference.delegateToClientThread(()->{
            if (isMenuOpened())
                ((PrivilegesScreen) ClientReference.getCurrentScreen()).privilegesDataReceived();;
        }); 
    }

    public static boolean isMenuOpened() {
        return ClientReference.hasActiveGUI() && ClientReference.getCurrentScreen() instanceof PrivilegesScreen;
    }
}
