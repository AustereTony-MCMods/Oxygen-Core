package austeretony.oxygen_core.server.privilege;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.config.PrivilegesConfig;
import austeretony.oxygen_core.common.main.EnumOxygenStatusMessage;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPPrivilegeAction;
import austeretony.oxygen_core.common.network.client.CPRoleAction;
import austeretony.oxygen_core.common.network.client.CPSyncPlayerRoles;
import austeretony.oxygen_core.common.network.client.CPSyncPrivilegesData;
import austeretony.oxygen_core.common.network.client.CPSyncRolesData;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.PrivilegeRegistry;
import austeretony.oxygen_core.common.privilege.PrivilegeRegistry.PrivilegeRegistryEntry;
import austeretony.oxygen_core.common.privilege.PrivilegeUtils;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.privilege.RoleImpl;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;

public class PrivilegesManagerServer {

    private final Map<UUID, PlayerRolesContainer> players = new ConcurrentHashMap<>();

    private final Map<Integer, Role> roles = new ConcurrentHashMap<>(5);

    private volatile boolean rolesChanged, playersChanged;

    private ByteBuf compressedRolesData = Unpooled.buffer();

    public Map<UUID, PlayerRolesContainer> getPlayers() {
        return this.players;
    }

    public Collection<Role> getRoles() {
        return this.roles.values();
    }

    public boolean isRoleExist(int roleId) {
        return this.roles.containsKey(roleId);
    }

    public Role getRole(int roleId) {
        return this.roles.get(roleId);
    }

    public void addRole(Role role) {
        if (!this.isRoleExist(role.getId())) {
            this.roles.put(role.getId(), role);
            this.rolesChanged();
        }
    }

    public Role removeRole(int roleId) {
        Role role = this.roles.remove(roleId);
        this.onRoleRemoved(roleId);
        this.rolesChanged();
        return role;
    }

    private void onRoleRemoved(int roleId) {
        for (UUID playerUUID : this.players.keySet())
            this.removeRoleFromPlayer(playerUUID, roleId);
    }

    public void addDefaultRoles() {
        this.addRole(new RoleImpl("Operator", OxygenMain.OPERATOR_ROLE_ID, TextFormatting.AQUA));
    }

    public Set<Integer> getPlayerRolesIds(UUID playerUUID) {
        PlayerRolesContainer rolesContainer;
        if ((rolesContainer = this.players.get(playerUUID)) != null)
            return rolesContainer.roles;
        return null;
    }

    public void addRoleToPlayerFast(UUID playerUUID, int roleId) {
        PlayerRolesContainer rolesContainer;
        if ((rolesContainer = this.players.get(playerUUID)) != null) {
            if (rolesContainer.roles.size() < OxygenMain.MAX_ROLES_PER_PLAYER)
                rolesContainer.roles.add(roleId);
        } else {
            rolesContainer = new PlayerRolesContainer();
            rolesContainer.roles.add(roleId);
            rolesContainer.setChatFormattingRoleId(roleId);
            this.players.put(playerUUID, rolesContainer);
        }
    }

    public boolean addRoleToPlayer(UUID playerUUID, int roleId) {
        if (this.roles.containsKey(roleId)) {
            boolean added = false;
            synchronized (this.players) {
                PlayerRolesContainer rolesContainer;
                if ((rolesContainer = this.players.get(playerUUID)) != null) {
                    if (rolesContainer.roles.size() < OxygenMain.MAX_ROLES_PER_PLAYER)
                        added = rolesContainer.roles.add(roleId);
                } else {
                    rolesContainer = new PlayerRolesContainer();
                    added = rolesContainer.roles.add(roleId);
                    rolesContainer.setChatFormattingRoleId(roleId);
                    this.players.put(playerUUID, rolesContainer);
                }
            }
            if (added) {
                if (OxygenHelperServer.isPlayerOnline(playerUUID))
                    this.syncPlayerRoles(playerUUID);
                this.updatePlayerRolesSharedData(playerUUID);
                this.playersChanged();
            }
            return added;
        }
        return false;
    }

    public boolean removeRoleFromPlayer(UUID playerUUID, int roleId) {
        boolean removed = false;
        synchronized (this.players) {
            PlayerRolesContainer rolesContainer;
            if ((rolesContainer = this.players.get(playerUUID)) != null) {
                removed = rolesContainer.roles.remove(roleId);
                if (removed) {
                    if (rolesContainer.roles.isEmpty())
                        this.players.remove(playerUUID);
                    else if (rolesContainer.getChatFormattingRoleId() == roleId)
                        rolesContainer.setChatFormattingRoleId(((TreeSet<Integer>) rolesContainer.roles).first());
                }
            }
        }
        if (removed) {
            if (OxygenHelperServer.isPlayerOnline(playerUUID))
                this.syncPlayerRoles(playerUUID);
            this.updatePlayerRolesSharedData(playerUUID);
            this.playersChanged();
        }
        return removed;
    }

    public void setPlayerChatFormattingRoleFast(UUID playerUUID, int roleId) {
        PlayerRolesContainer rolesContainer;
        if ((rolesContainer = this.players.get(playerUUID)) != null)
            rolesContainer.setChatFormattingRoleId(roleId);
    }

    public void setPlayerChatFormattingRole(UUID playerUUID, int roleId) {
        if (PrivilegesConfig.ENABLE_CHAT_FORMATTING_ROLE_MANAGEMENT.asBoolean()) {
            synchronized (this.players) {
                PlayerRolesContainer rolesContainer;
                if ((rolesContainer = this.players.get(playerUUID)) != null) {
                    rolesContainer.setChatFormattingRoleId(roleId);
                    this.playersChanged();
                }
            }
        }
    }

    public void updatePlayerRolesSharedData(UUID playerUUID) {
        synchronized (this.players) {
            PlayerSharedData sharedData = OxygenHelperServer.getPlayerSharedData(playerUUID);
            if (sharedData != null) {
                PlayerRolesContainer rolesContainer;
                if ((rolesContainer = this.players.get(playerUUID)) != null) {
                    Iterator<Integer> iterator = rolesContainer.roles.iterator();
                    int roleId;
                    for (int i = 0; i < OxygenMain.MAX_ROLES_PER_PLAYER; i++) {
                        roleId = OxygenMain.DEFAULT_ROLE_INDEX;
                        if (iterator.hasNext())
                            roleId = iterator.next();
                        sharedData.setByte(i + OxygenMain.ROLES_SHARED_DATA_STARTING_INDEX, roleId);
                    }
                } else {
                    for (int i = 0; i < OxygenMain.MAX_ROLES_PER_PLAYER; i++)
                        sharedData.setByte(i + OxygenMain.ROLES_SHARED_DATA_STARTING_INDEX, OxygenMain.DEFAULT_ROLE_INDEX);
                }
            }
        }
    }

    public Role getChatFormattingPlayerRole(UUID playerUUID) {
        if (PrivilegesConfig.ENABLE_CHAT_FORMATTING_ROLE_MANAGEMENT.asBoolean()) {
            PlayerRolesContainer rolesContainer;
            if ((rolesContainer = this.players.get(playerUUID)) != null)
                if (rolesContainer.getChatFormattingRoleId() != OxygenMain.DEFAULT_ROLE_INDEX)
                    return this.getRole(rolesContainer.getChatFormattingRoleId());
        } else
            return this.getPriorityPlayerRole(playerUUID);
        return null;
    }

    public Role getPriorityPlayerRole(UUID playerUUID) {
        Set<Integer> rolesIds;
        if ((rolesIds = this.getPlayerRolesIds(playerUUID)) != null)
            return this.getRole(((TreeSet<Integer>) rolesIds).first());
        return null;
    }

    public Privilege getPriorityPlayerPrivilege(UUID playerUUID, int privilegeId) {
        Set<Integer> rolesIds;
        Role role;
        Privilege privilege = null;
        if ((rolesIds = this.getPlayerRolesIds(playerUUID)) != null) {
            for (int roleId : rolesIds) {
                role = this.getRole(roleId);
                if ((privilege = role.getPrivilege(privilegeId)) != null)
                    return privilege;
            }
        }
        return privilege;
    }

    public void compressRolesData() {
        synchronized (this.compressedRolesData) {
            this.compressedRolesData.clear();
            this.compressedRolesData.writeByte(this.getRoles().size());
            for (Role role : this.getRoles()) {
                this.compressedRolesData.writeByte(role.getId());
                ByteBufUtils.writeString(role.getName(), this.compressedRolesData);
                this.compressedRolesData.writeByte(role.getNameColor().ordinal());
            }    
        }
    }

    public void syncRolesData(EntityPlayerMP playerMP) {
        synchronized (this.compressedRolesData) {
            byte[] compressed = new byte[this.compressedRolesData.writerIndex()];
            this.compressedRolesData.getBytes(0, compressed);
            OxygenMain.network().sendTo(new CPSyncRolesData(compressed), playerMP);
        }
    }

    public void syncPlayerRoles(UUID playerUUID) {
        synchronized (this.players) {
            PlayerRolesContainer rolesContainer = this.players.get(playerUUID);
            if (rolesContainer != null) {
                ByteBuf buffer = null;
                try {
                    buffer = Unpooled.buffer();
                    if (PrivilegesConfig.ENABLE_CHAT_FORMATTING_ROLE_MANAGEMENT.asBoolean())
                        buffer.writeByte(rolesContainer.getChatFormattingRoleId());
                    else
                        buffer.writeByte(((TreeSet<Integer>) rolesContainer.roles).first());
                    buffer.writeByte(rolesContainer.roles.size());
                    for (int roleId : rolesContainer.roles)
                        this.getRole(roleId).write(buffer);
                    byte[] compressed = new byte[buffer.writerIndex()];
                    buffer.getBytes(0, compressed);
                    OxygenMain.network().sendTo(new CPSyncPlayerRoles(compressed), CommonReference.playerByUUID(playerUUID));
                } finally {
                    if (buffer != null)
                        buffer.release();
                }
            } else
                OxygenMain.network().sendTo(new CPSyncPlayerRoles(new byte[0]), CommonReference.playerByUUID(playerUUID));
        }
    }

    //management

    private void informPlayer(EntityPlayerMP playerMP, EnumOxygenStatusMessage message) {
        OxygenHelperServer.sendStatusMessage(playerMP, OxygenMain.OXYGEN_CORE_MOD_INDEX, message.ordinal());
    }

    public void syncPrivilegesData(EntityPlayerMP playerMP) {
        if (CommonReference.isPlayerOpped(playerMP)) {
            OxygenManagerServer.instance().getSharedDataManager().syncSharedData(playerMP, - 1);
            ByteBuf buffer = null;
            try {
                buffer = Unpooled.buffer();

                buffer.writeByte(this.getRoles().size());
                for (Role role : this.getRoles())
                    role.write(buffer);

                buffer.writeShort(this.getPlayers().size());
                PlayerSharedData sharedData;
                for (UUID playerUUID : this.getPlayers().keySet()) {
                    sharedData = OxygenHelperServer.getPlayerSharedData(playerUUID);
                    if (sharedData != null) {
                        buffer.writeBoolean(true);
                        sharedData.write(buffer);
                    } else
                        buffer.writeBoolean(false);
                }

                byte[] compressed = new byte[buffer.writerIndex()];
                buffer.getBytes(0, compressed);
                OxygenMain.network().sendTo(new CPSyncPrivilegesData(compressed), playerMP);
            } finally {
                if (buffer != null)
                    buffer.release();
            }
        }
    }

    public void createRole(EntityPlayerMP playerMP, int roleId, String roleName, String prefix, int roleNameColor, int prefixColor, int usernameColor, int chatColor) {
        if (CommonReference.isPlayerOpped(playerMP)
                && roleId != OxygenMain.OPERATOR_ROLE_ID
                && roleId >= 0 && roleId <= Byte.MAX_VALUE
                && roleNameColor >= 0 && roleNameColor < 16
                && prefixColor >= 0 && prefixColor < 16
                && usernameColor >= 0 && usernameColor < 16
                && chatColor >= 0 && chatColor < 16) {
            if (this.isRoleExist(roleId))
                this.removeRole(roleId);
            roleName = roleName.trim();
            prefix = prefix.trim();
            if (roleName.length() > RoleImpl.ROLE_NAME_MAX_LENGTH)
                roleName = roleName.substring(0, RoleImpl.ROLE_NAME_MAX_LENGTH);
            if (prefix.length() > RoleImpl.PREFIX_MAX_LENGTH)
                prefix = prefix.substring(0, RoleImpl.PREFIX_MAX_LENGTH);
            RoleImpl role = new RoleImpl(roleName, roleId, TextFormatting.values()[roleNameColor]);
            role.setPrefix(prefix);
            role.setPrefixColor(TextFormatting.values()[prefixColor]);
            role.setUsernameColor(TextFormatting.values()[usernameColor]);
            role.setChatColor(TextFormatting.values()[chatColor]);
            this.addRole(role);

            this.syncRolesData(playerMP);
            OxygenMain.network().sendTo(new CPRoleAction(CPRoleAction.EnumAction.CREATED, role), playerMP);
            this.informPlayer(playerMP, EnumOxygenStatusMessage.ROLE_CREATED);
        }
    }

    public void removeRole(EntityPlayerMP playerMP, int roleId) {
        if (CommonReference.isPlayerOpped(playerMP)
                && this.isRoleExist(roleId)
                && roleId != OxygenMain.OPERATOR_ROLE_ID) {
            Role role = this.removeRole(roleId);
            if (role != null) {
                OxygenMain.network().sendTo(new CPRoleAction(CPRoleAction.EnumAction.REMOVED, role), playerMP);
                this.informPlayer(playerMP, EnumOxygenStatusMessage.ROLE_REMOVED);
            }
        }
    }

    public void addPrivilege(EntityPlayerMP playerMP, int roleId, int privilegeId, String value) {
        if (CommonReference.isPlayerOpped(playerMP)
                && roleId != OxygenMain.OPERATOR_ROLE_ID) {
            Role role = this.getRole(roleId);
            if (role != null) {
                PrivilegeRegistryEntry entry = PrivilegeRegistry.getRegistryEntry(privilegeId);
                if (entry != null) {
                    value = value.trim();
                    Privilege privilege = null;
                    switch (entry.type) {  
                    case BOOLEAN:
                        privilege = PrivilegeUtils.getPrivilege(privilegeId, Boolean.parseBoolean(value));
                        break;
                    case INT:
                        try {
                            privilege = PrivilegeUtils.getPrivilege(privilegeId, Integer.parseInt(value));
                        } catch (NumberFormatException exception) {
                            OxygenMain.LOGGER.error("Wrong privilege integer value!", exception);
                        }
                        break;
                    case FLOAT:
                        try {
                            privilege = PrivilegeUtils.getPrivilege(privilegeId, Float.parseFloat(value));
                        } catch (NumberFormatException exception) {
                            OxygenMain.LOGGER.error("Wrong privilege float value!", exception);
                        }
                        break;
                    case LONG:
                        try {
                            privilege = PrivilegeUtils.getPrivilege(privilegeId, Long.parseLong(value));
                        } catch (NumberFormatException exception) {
                            OxygenMain.LOGGER.error("Wrong privilege long value!", exception);
                        }
                        break;
                    case STRING:
                        privilege = PrivilegeUtils.getPrivilege(privilegeId, value);
                        break;
                    default:
                        break;
                    }
                    if (privilege != null) {
                        role.addPrivilege(privilege);
                        this.rolesChanged();

                        OxygenMain.network().sendTo(new CPPrivilegeAction(CPPrivilegeAction.EnumAction.ADDED, role.getId(), privilege), playerMP);
                        this.informPlayer(playerMP, EnumOxygenStatusMessage.PRIVILEGE_ADDED);
                    }
                }
            }
        }
    }

    public void removePrivilege(EntityPlayerMP playerMP, int roleId, int privilegeId) {
        if (CommonReference.isPlayerOpped(playerMP)
                && roleId != OxygenMain.OPERATOR_ROLE_ID) {
            Role role = this.getRole(roleId);
            if (role != null) {
                Privilege privilege = role.removePrivilege(privilegeId);
                if (privilege != null) {
                    this.rolesChanged();

                    OxygenMain.network().sendTo(new CPPrivilegeAction(CPPrivilegeAction.EnumAction.REMOVED, role.getId(), privilege), playerMP);
                    this.informPlayer(playerMP, EnumOxygenStatusMessage.PRIVILEGE_REMOVED);
                }
            }
        }
    }

    public void addRoleToPlayer(EntityPlayerMP playerMP, UUID playerUUID, int roleId) {
        if (CommonReference.isPlayerOpped(playerMP)
                && OxygenHelperServer.getPlayerSharedData(playerUUID) != null) {
            if (this.addRoleToPlayer(playerUUID, roleId))
                this.informPlayer(playerMP, EnumOxygenStatusMessage.ROLE_ADDED_TO_PLAYER);
        }
    }

    public void removeRoleFromPlayer(EntityPlayerMP playerMP, UUID playerUUID, int roleId) {
        if (CommonReference.isPlayerOpped(playerMP)
                && OxygenHelperServer.getPlayerSharedData(playerUUID) != null) {
            if (this.removeRoleFromPlayer(playerUUID, roleId)) 
                this.informPlayer(playerMP, EnumOxygenStatusMessage.ROLE_REMOVED_FROM_PLAYER);
        }
    }

    public void rolesChanged() {
        this.rolesChanged = true;
        this.compressRolesData();
    }

    public void playersChanged() {
        this.playersChanged = true;
    }

    public void save() {
        if (this.rolesChanged) {
            this.rolesChanged = false;
            PrivilegesLoaderServer.saveRolesAsync();
        }
        if (this.playersChanged) {
            this.playersChanged = false;
            PrivilegesLoaderServer.savePlayersListAsync();
        }
    }
}
