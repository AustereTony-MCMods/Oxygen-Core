package austeretony.oxygen_core.client;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.privilege.RoleData;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.PrivilegeUtils;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.privilege.RoleImpl;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextFormatting;

public class PrivilegesContainerClient {

    private final OxygenManagerClient manager;

    private final Map<Integer, Privilege> defaultPrivileges = new ConcurrentHashMap<>();

    private final Map<Integer, Role> clientPlayerRoles = new ConcurrentHashMap<>(1);

    private final Set<Integer> orderedRolesIds = new TreeSet<>((i, j)->(j - i));

    private int chatFormattingRoleId = OxygenMain.DEFAULT_ROLE_INDEX;

    private final Map<Integer, RoleData> rolesData = new ConcurrentHashMap<>(5);

    //management

    private final Map<Integer, Role> serverRoles = new ConcurrentHashMap<>();

    private final Map<UUID, PlayerSharedData> serverPlayers = new ConcurrentHashMap<>();

    public PrivilegesContainerClient(OxygenManagerClient manager) {
        this.manager = manager;
    }

    public void rolesDataReceived(ByteBuf buffer) {
        this.rolesData.clear();
        try {
            int amount = buffer.readByte();
            for (int i = 0; i < amount; i++)
                this.addRoleData(buffer.readByte(), ByteBufUtils.readString(buffer), TextFormatting.values()[buffer.readByte()]);
            OxygenMain.LOGGER.info("[Core] Synchronized <{}> roles data entries.", amount);
        } finally {
            if (buffer != null)
                buffer.release();
        }
    }

    public void addRoleData(int roleId, String name, TextFormatting nameColor) {
        this.rolesData.put(roleId, new RoleData(name, nameColor));
    }

    @Nullable
    public RoleData getRoleData(int roleId) {
        return this.rolesData.get(roleId);
    }

    public void playerRolesReceived(ByteBuf buffer) { 
        OxygenMain.LOGGER.info("[Core] Player roles data received.");
        this.defaultPrivileges.clear();
        this.clientPlayerRoles.clear();
        this.orderedRolesIds.clear();
        try {
            int i, amount = buffer.readShort();

            for (i = 0; i < amount; i++) 
                this.addDefaultPrivilege(PrivilegeUtils.read(buffer));

            if (buffer.readBoolean()) {
                this.chatFormattingRoleId = buffer.readByte();

                amount = buffer.readByte();
                for (i = 0; i < amount; i++)
                    this.addClientPlayerRole(RoleImpl.read(buffer));
                OxygenMain.LOGGER.info("[Core] Synchronized <{}> client player roles entries.", amount);
            }
        } finally {
            if (buffer != null)
                buffer.release();
        }
    }

    public Collection<Privilege> getDefaultPrivileges() {
        return this.defaultPrivileges.values();
    }

    @Nullable
    public Privilege getDefaultPrivilege(int privilegeId) {
        return this.defaultPrivileges.get(privilegeId);
    }

    public void addDefaultPrivilege(Privilege privilege) {
        this.defaultPrivileges.put(privilege.getId(), privilege);
    }

    public Set<Integer> getClientPlayerRolesIds() {
        return this.orderedRolesIds;
    }

    private void addClientPlayerRole(Role role) {
        this.clientPlayerRoles.put(role.getId(), role);
        this.orderedRolesIds.add(role.getId());
    }

    @Nullable
    public Role getClientPlayerRole(int roleId) {
        return this.clientPlayerRoles.get(roleId);
    }

    public int getChatFormattingRoleId() {
        return this.chatFormattingRoleId;
    }

    public Role getChatFormattingRole() {
        return this.clientPlayerRoles.get(this.chatFormattingRoleId);
    }

    //management

    public void managementDataReceived(ByteBuf buffer) {
        this.defaultPrivileges.clear();
        this.serverRoles.clear();
        this.serverPlayers.clear();
        try {
            int i, amount = buffer.readShort();

            for (i = 0; i < amount; i++)
                this.addDefaultPrivilege(PrivilegeUtils.read(buffer));

            amount = buffer.readByte();
            for (i = 0; i < amount; i++)
                this.addServerRole(RoleImpl.read(buffer));

            amount = buffer.readShort();
            PlayerSharedData sharedData;
            boolean exist;
            for (i = 0; i < amount; i++) {
                exist = buffer.readBoolean();
                if (exist) {
                    sharedData = PlayerSharedData.read(buffer);
                    this.serverPlayers.put(sharedData.getPlayerUUID(), sharedData);
                }
            }
        } finally {
            if (buffer != null)
                buffer.release();
        }
    }

    public Collection<Role> getServerRoles() {
        return this.serverRoles.values();
    }

    @Nullable
    public Role getServerRole(int roleId) {
        return this.serverRoles.get(roleId);
    }

    public void addServerRole(Role role) {
        this.serverRoles.put(role.getId(), role);
    }

    public void removeServerRole(int roleId) {
        this.serverRoles.remove(roleId);
    }

    public Collection<PlayerSharedData> getServerPlayersData() {
        return this.serverPlayers.values();
    }

    @Nullable
    public PlayerSharedData getServerPlayerData(UUID playerUUID) {
        PlayerSharedData sharedData = this.serverPlayers.get(playerUUID);
        if (sharedData == null)
            sharedData = OxygenHelperClient.getPlayerSharedData(playerUUID);
        return sharedData;
    }

    public void roleRemoved(Role role) {
        this.removeServerRole(role.getId());
        this.clientPlayerRoles.remove(role.getId());
        this.orderedRolesIds.remove(role.getId());
    }

    public void rolePrivilegeAdded(int roleId, Privilege privilege) {        
        this.getServerRole(roleId).addPrivilege(privilege);
        Role role = this.clientPlayerRoles.get(roleId);
        if (role != null)
            role.addPrivilege(privilege);
    }

    public void rolePrivilegeRemoved(int roleId, Privilege privilege) {
        this.getServerRole(roleId).removePrivilege(privilege.getId());
        Role role = this.clientPlayerRoles.get(roleId);
        if (role != null)
            role.removePrivilege(privilege.getId());
    }

    public void defaultPrivilegeAdded(Privilege privilege) {
        this.addDefaultPrivilege(privilege);
    }

    public void defaultPrivilegeRemoved(Privilege privilege) {
        this.defaultPrivileges.remove(privilege.getId());
    }

    public void playerRolesChanged(int roleId, PlayerSharedData sharedData) {
        OxygenManagerClient.instance().getSharedDataManager().addSharedData(sharedData);
        this.serverPlayers.put(sharedData.getPlayerUUID(), sharedData);
    }
}
