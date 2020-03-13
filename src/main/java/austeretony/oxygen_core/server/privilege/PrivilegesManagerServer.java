package austeretony.oxygen_core.server.privilege;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.annotation.Nullable;

import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.config.PrivilegesConfig;
import austeretony.oxygen_core.common.main.EnumOxygenStatusMessage;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPDefaultPrivilegeOperation;
import austeretony.oxygen_core.common.network.client.CPPlayerRolesChanged;
import austeretony.oxygen_core.common.network.client.CPRoleAction;
import austeretony.oxygen_core.common.network.client.CPRolePrivilegeOperation;
import austeretony.oxygen_core.common.network.client.CPSyncPlayerRoles;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.PrivilegeRegistry;
import austeretony.oxygen_core.common.privilege.PrivilegeRegistry.PrivilegeRegistryEntry;
import austeretony.oxygen_core.common.privilege.PrivilegeUtils;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.privilege.RoleImpl;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;

public class PrivilegesManagerServer {

    private final OxygenManagerServer manager;

    public PrivilegesManagerServer(OxygenManagerServer manager) {
        this.manager = manager;
    }

    private void roleRemoved(int roleId) {
        for (UUID playerUUID : this.manager.getPrivilegesContainer().getPlayersRoles().keySet())
            this.removeRoleFromPlayer(playerUUID, roleId);
    }

    public void addDefaultRoles() {
        if (this.manager.getPrivilegesContainer().getRole(OxygenMain.OPERATOR_ROLE_ID) == null) {
            this.manager.getPrivilegesContainer().addRole(new RoleImpl("Operator", OxygenMain.OPERATOR_ROLE_ID, TextFormatting.AQUA));
            this.manager.getPrivilegesContainer().markChanged();
        }
    }

    public void addRoleToPlayerFast(UUID playerUUID, int roleId) {
        Set<Integer> ids;
        if ((ids = this.manager.getPrivilegesContainer().getPlayerRolesIds(playerUUID)) != null) {
            if (ids.size() < OxygenMain.MAX_ROLES_PER_PLAYER)
                ids.add(roleId);
        } else {
            ids = new TreeSet<>((i, j)->(j - i));
            ids.add(roleId);
            this.manager.getPrivilegesContainer().setPlayerRolesIds(playerUUID, ids);
            this.manager.getPrivilegesContainer().setPlayerChatFormattingRole(playerUUID, roleId);
        }
    }

    public boolean addRoleToPlayer(UUID playerUUID, int roleId) {
        if (this.manager.getPrivilegesContainer().getRole(roleId) != null) {
            boolean added = false;
            synchronized (this.manager.getPrivilegesContainer().getPlayersRoles()) {
                Set<Integer> ids;
                if ((ids = this.manager.getPrivilegesContainer().getPlayerRolesIds(playerUUID)) != null) {
                    if (ids.size() < OxygenMain.MAX_ROLES_PER_PLAYER)
                        added = ids.add(roleId);
                } else {
                    ids = new TreeSet<>((i, j)->(j - i));
                    added = ids.add(roleId);
                    this.manager.getPrivilegesContainer().setPlayerRolesIds(playerUUID, ids);
                    this.manager.getPrivilegesContainer().setPlayerChatFormattingRole(playerUUID, roleId);
                }
            }
            if (added) {
                if (OxygenHelperServer.isPlayerOnline(playerUUID))
                    this.syncPlayerPrivileges(playerUUID);
                this.updatePlayerRolesSharedData(playerUUID);
                this.manager.getPrivilegesContainer().markChanged();
            }
            return added;
        }
        return false;
    }

    public boolean removeRoleFromPlayer(UUID playerUUID, int roleId) {
        boolean removed = false;
        synchronized (this.manager.getPrivilegesContainer().getPlayersRoles()) {
            Set<Integer> ids;
            if ((ids = this.manager.getPrivilegesContainer().getPlayerRolesIds(playerUUID)) != null) {
                removed = ids.remove(roleId);
                if (removed) {
                    if (ids.isEmpty()) {
                        this.manager.getPrivilegesContainer().removePlayerRolesIds(playerUUID);
                        this.manager.getPrivilegesContainer().removePlayerChatFormattingRole(playerUUID);
                    } else {
                        if (this.manager.getPrivilegesContainer().getPlayerChatFormattingRole(playerUUID) == roleId)
                            this.manager.getPrivilegesContainer().setPlayerChatFormattingRole(playerUUID, ((TreeSet<Integer>) ids).first());
                    }
                }
            }
        }
        if (removed) {
            if (OxygenHelperServer.isPlayerOnline(playerUUID))
                this.syncPlayerPrivileges(playerUUID);
            this.updatePlayerRolesSharedData(playerUUID);
            this.manager.getPrivilegesContainer().markChanged();
        }
        return removed;
    }

    public void setPlayerChatFormattingRole(UUID playerUUID, int roleId) {
        if (PrivilegesConfig.ENABLE_CHAT_FORMATTING_ROLE_MANAGEMENT.asBoolean()) {
            Set<Integer> ids = this.manager.getPrivilegesContainer().getPlayerRolesIds(playerUUID);
            if (ids != null && (ids.contains(roleId) || roleId == OxygenMain.DEFAULT_ROLE_INDEX)) {
                this.manager.getPrivilegesContainer().setPlayerChatFormattingRole(playerUUID, roleId);
                this.manager.getPrivilegesContainer().markChanged();
            }
        }
    }

    public void updatePlayerRolesSharedData(UUID playerUUID) {
        synchronized (this.manager.getPrivilegesContainer().getPlayersRoles()) {
            PlayerSharedData sharedData = OxygenHelperServer.getPlayerSharedData(playerUUID);
            if (sharedData != null) {
                Set<Integer> ids;
                if ((ids = this.manager.getPrivilegesContainer().getPlayerRolesIds(playerUUID)) != null) {
                    Iterator<Integer> iterator = ids.iterator();
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
                OxygenManagerServer.instance().getSharedDataManager().setChanged(true);
            }
        }
    }

    @Nullable
    public Role getChatFormattingPlayerRole(UUID playerUUID) {
        if (PrivilegesConfig.ENABLE_CHAT_FORMATTING_ROLE_MANAGEMENT.asBoolean()) {
            int formattingRole = this.manager.getPrivilegesContainer().getPlayerChatFormattingRole(playerUUID);
            if (formattingRole != OxygenMain.DEFAULT_ROLE_INDEX)
                return this.manager.getPrivilegesContainer().getRole(formattingRole);
        } else
            return this.getPriorityPlayerRole(playerUUID);
        return null;
    }

    @Nullable
    public Role getPriorityPlayerRole(UUID playerUUID) {
        Set<Integer> ids;
        if ((ids = this.manager.getPrivilegesContainer().getPlayerRolesIds(playerUUID)) != null)
            return this.manager.getPrivilegesContainer().getRole(((TreeSet<Integer>) ids).first());
        return null;
    }

    @Nullable
    public Privilege getPriorityPlayerPrivilege(UUID playerUUID, int privilegeId) {
        Set<Integer> ids;
        Role role;
        Privilege privilege = null;
        if ((ids = this.manager.getPrivilegesContainer().getPlayerRolesIds(playerUUID)) != null) {
            for (int roleId : ids) {
                role = this.manager.getPrivilegesContainer().getRole(roleId);
                if ((privilege = role.getPrivilege(privilegeId)) != null)
                    return privilege;
            }
        }
        return this.manager.getPrivilegesContainer().getDefaultPrivilege(privilegeId);
    }

    public void syncPlayerPrivileges(UUID playerUUID) {
        synchronized (this.manager.getPrivilegesContainer().getPlayersRoles()) {
            ByteBuf buffer = null;
            try {
                buffer = Unpooled.buffer();

                buffer.writeShort(this.manager.getPrivilegesContainer().getDefaultPrivileges().size());
                for (Privilege privilege : this.manager.getPrivilegesContainer().getDefaultPrivileges())
                    privilege.write(buffer);

                Set<Integer> ids;
                if ((ids = this.manager.getPrivilegesContainer().getPlayerRolesIds(playerUUID)) != null) {
                    buffer.writeBoolean(true);

                    if (PrivilegesConfig.ENABLE_CHAT_FORMATTING_ROLE_MANAGEMENT.asBoolean())
                        buffer.writeByte(this.manager.getPrivilegesContainer().getPlayerChatFormattingRole(playerUUID));
                    else
                        buffer.writeByte(((TreeSet<Integer>) ids).first());

                    buffer.writeByte(ids.size());
                    for (int roleId : ids)
                        this.manager.getPrivilegesContainer().getRole(roleId).write(buffer);
                } else
                    buffer.writeBoolean(false);

                byte[] compressed = new byte[buffer.writerIndex()];
                buffer.getBytes(0, compressed);
                OxygenMain.network().sendTo(new CPSyncPlayerRoles(compressed), CommonReference.playerByUUID(playerUUID));
            } finally {
                if (buffer != null)
                    buffer.release();
            }
        }
    }

    //management

    public void createRole(EntityPlayerMP playerMP, int roleId, String roleName, String prefix, int roleNameColor, int prefixColor, int usernameColor, int chatColor) {
        if (CommonReference.isPlayerOpped(playerMP)
                && roleId != OxygenMain.OPERATOR_ROLE_ID
                && roleId >= 0 && roleId <= Byte.MAX_VALUE
                && roleNameColor >= 0 && roleNameColor < 16
                && prefixColor >= 0 && prefixColor < 16
                && usernameColor >= 0 && usernameColor < 16
                && chatColor >= 0 && chatColor < 16) {
            if (this.manager.getPrivilegesContainer().getRole(roleId) != null) return;
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
            this.manager.getPrivilegesContainer().addRole(role);
            this.manager.getPrivilegesContainer().compressPrivilegesData();
            this.manager.getPrivilegesContainer().markChanged();

            this.manager.getPrivilegesContainer().syncPrivilegesData(playerMP);
            OxygenMain.network().sendTo(new CPRoleAction(CPRoleAction.EnumAction.CREATED, role), playerMP);
            this.manager.sendStatusMessage(playerMP, EnumOxygenStatusMessage.ROLE_CREATED);

            if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                OxygenMain.LOGGER.info("[Core] <{}/{}> created role <{}({})>.",
                        CommonReference.getName(playerMP),
                        CommonReference.getPersistentUUID(playerMP),
                        roleName,
                        roleId);
        }
    }

    public void editRole(EntityPlayerMP playerMP, int roleId, String roleName, String prefix, int roleNameColor, int prefixColor, int usernameColor, int chatColor) {
        if (CommonReference.isPlayerOpped(playerMP)
                && roleId != OxygenMain.OPERATOR_ROLE_ID
                && roleId >= 0 && roleId <= Byte.MAX_VALUE
                && roleNameColor >= 0 && roleNameColor < 16
                && prefixColor >= 0 && prefixColor < 16
                && usernameColor >= 0 && usernameColor < 16
                && chatColor >= 0 && chatColor < 16) {
            RoleImpl role;
            if ((role = (RoleImpl) this.manager.getPrivilegesContainer().getRole(roleId)) == null) return;
            roleName = roleName.trim();
            prefix = prefix.trim();
            if (roleName.length() > RoleImpl.ROLE_NAME_MAX_LENGTH)
                roleName = roleName.substring(0, RoleImpl.ROLE_NAME_MAX_LENGTH);
            if (prefix.length() > RoleImpl.PREFIX_MAX_LENGTH)
                prefix = prefix.substring(0, RoleImpl.PREFIX_MAX_LENGTH);
            role.setName(roleName);
            role.setPrefix(prefix);
            role.setNameColor(TextFormatting.values()[roleNameColor]);
            role.setPrefixColor(TextFormatting.values()[prefixColor]);
            role.setUsernameColor(TextFormatting.values()[usernameColor]);
            role.setChatColor(TextFormatting.values()[chatColor]);
            this.manager.getPrivilegesContainer().compressPrivilegesData();
            this.manager.getPrivilegesContainer().markChanged();

            this.manager.getPrivilegesContainer().syncPrivilegesData(playerMP);
            OxygenMain.network().sendTo(new CPRoleAction(CPRoleAction.EnumAction.EDITED, role), playerMP);
            this.manager.sendStatusMessage(playerMP, EnumOxygenStatusMessage.ROLE_EDITED);

            if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                OxygenMain.LOGGER.info("[Core] <{}/{}> edited role ({}).",
                        CommonReference.getName(playerMP),
                        CommonReference.getPersistentUUID(playerMP),
                        roleId);
        }
    }

    public void removeRole(EntityPlayerMP playerMP, int roleId) {
        if (CommonReference.isPlayerOpped(playerMP)
                && roleId != OxygenMain.OPERATOR_ROLE_ID) {
            Role role = this.manager.getPrivilegesContainer().removeRole(roleId);
            if (role != null) {
                this.roleRemoved(roleId);
                this.manager.getPrivilegesContainer().compressPrivilegesData();
                this.manager.getPrivilegesContainer().markChanged();

                OxygenMain.network().sendTo(new CPRoleAction(CPRoleAction.EnumAction.REMOVED, role), playerMP);
                this.manager.sendStatusMessage(playerMP, EnumOxygenStatusMessage.ROLE_REMOVED);

                if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                    OxygenMain.LOGGER.info("[Core] <{}/{}> removed role <{}({})>.",
                            CommonReference.getName(playerMP),
                            CommonReference.getPersistentUUID(playerMP),
                            role.getName(),
                            roleId);
            }
        }
    }

    public void addPrivilege(EntityPlayerMP playerMP, int roleId, int privilegeId, String value) {
        if (CommonReference.isPlayerOpped(playerMP)
                && roleId != OxygenMain.OPERATOR_ROLE_ID) {
            Role role = this.manager.getPrivilegesContainer().getRole(roleId);
            if (role != null) {
                Privilege privilege = createPrivilege(privilegeId, value);
                if (privilege != null) {
                    role.addPrivilege(privilege);
                    this.manager.getPrivilegesContainer().compressPrivilegesData();
                    this.manager.getPrivilegesContainer().markChanged();

                    OxygenMain.network().sendTo(new CPRolePrivilegeOperation(CPRolePrivilegeOperation.EnumAction.ADDED, role.getId(), privilege), playerMP);
                    this.manager.sendStatusMessage(playerMP, EnumOxygenStatusMessage.PRIVILEGE_ADDED);

                    if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                        OxygenMain.LOGGER.info("[Core] <{}/{}> added privilege ({}) with value [{}] to role <{}({})>.",
                                CommonReference.getName(playerMP),
                                CommonReference.getPersistentUUID(playerMP),
                                privilegeId,
                                value,
                                role.getName(),
                                roleId);
                }
            }
        }
    }

    public void removePrivilege(EntityPlayerMP playerMP, int roleId, int privilegeId) {
        if (CommonReference.isPlayerOpped(playerMP)
                && roleId != OxygenMain.OPERATOR_ROLE_ID) {
            Role role = this.manager.getPrivilegesContainer().getRole(roleId);
            if (role != null) {
                Privilege privilege = role.removePrivilege(privilegeId);
                if (privilege != null) {
                    this.manager.getPrivilegesContainer().compressPrivilegesData();
                    this.manager.getPrivilegesContainer().markChanged();

                    OxygenMain.network().sendTo(new CPRolePrivilegeOperation(CPRolePrivilegeOperation.EnumAction.REMOVED, role.getId(), privilege), playerMP);
                    this.manager.sendStatusMessage(playerMP, EnumOxygenStatusMessage.PRIVILEGE_REMOVED);

                    if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                        OxygenMain.LOGGER.info("[Core] <{}/{}> removed privilege ({}) from role <{}({})>.",
                                CommonReference.getName(playerMP),
                                CommonReference.getPersistentUUID(playerMP),
                                privilegeId,
                                role.getName(),
                                roleId);
                }
            }
        }
    }

    public void addDefaultPrivilege(EntityPlayerMP playerMP, int privilegeId, String value) {
        if (CommonReference.isPlayerOpped(playerMP)) {
            Privilege privilege = createPrivilege(privilegeId, value);
            if (privilege != null) {
                this.manager.getPrivilegesContainer().addDefaultPrivilege(privilege);
                this.manager.getPrivilegesContainer().compressPrivilegesData();
                this.manager.getPrivilegesContainer().markChanged();

                OxygenMain.network().sendTo(new CPDefaultPrivilegeOperation(CPDefaultPrivilegeOperation.EnumOperation.ADDED, privilege), playerMP);
                this.manager.sendStatusMessage(playerMP, EnumOxygenStatusMessage.DEFAULT_PRIVILEGE_ADDED);

                if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                    OxygenMain.LOGGER.info("[Core] <{}/{}> added default privilege ({}) with value [{}].",
                            CommonReference.getName(playerMP),
                            CommonReference.getPersistentUUID(playerMP),
                            privilegeId,
                            value);
            }
        }
    }

    public void removeDefaultPrivilege(EntityPlayerMP playerMP, int privilegeId) {
        if (CommonReference.isPlayerOpped(playerMP)) {
            Privilege privilege = this.manager.getPrivilegesContainer().removeDefaultPrivilege(privilegeId);
            this.manager.getPrivilegesContainer().compressPrivilegesData();
            this.manager.getPrivilegesContainer().markChanged();

            if (privilege != null) {
                OxygenMain.network().sendTo(new CPDefaultPrivilegeOperation(CPDefaultPrivilegeOperation.EnumOperation.REMOVED, privilege), playerMP);
                this.manager.sendStatusMessage(playerMP, EnumOxygenStatusMessage.DEFAULT_PRIVILEGE_REMOVED);

                if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                    OxygenMain.LOGGER.info("[Core] <{}/{}> removed default privilege ({}).",
                            CommonReference.getName(playerMP),
                            CommonReference.getPersistentUUID(playerMP),
                            privilegeId);
            }
        }
    }

    @Nullable
    private static Privilege createPrivilege(int privilegeId, String value) {
        PrivilegeRegistryEntry entry = PrivilegeRegistry.getRegistryEntry(privilegeId);
        if (entry != null) {
            value = value.trim();
            switch (entry.type) {  
            case BOOLEAN:
                return PrivilegeUtils.getPrivilege(privilegeId, Boolean.parseBoolean(value));
            case INT:
                try {
                    return PrivilegeUtils.getPrivilege(privilegeId, Integer.parseInt(value));
                } catch (NumberFormatException exception) {
                    OxygenMain.LOGGER.error("[Core] Wrong privilege integer value!", exception);
                }
            case FLOAT:
                try {
                    return PrivilegeUtils.getPrivilege(privilegeId, Float.parseFloat(value));
                } catch (NumberFormatException exception) {
                    OxygenMain.LOGGER.error("[Core] Wrong privilege float value!", exception);
                }
            case LONG:
                try {
                    return PrivilegeUtils.getPrivilege(privilegeId, Long.parseLong(value));
                } catch (NumberFormatException exception) {
                    OxygenMain.LOGGER.error("[Core] Wrong privilege long value!", exception);
                }
            case STRING:
                return PrivilegeUtils.getPrivilege(privilegeId, value);
            default:
                return null;
            }
        }
        return null;
    }

    public void addRoleToPlayer(EntityPlayerMP playerMP, UUID playerUUID, int roleId) {
        if (CommonReference.isPlayerOpped(playerMP)
                && OxygenHelperServer.getPlayerSharedData(playerUUID) != null) {
            if (this.addRoleToPlayer(playerUUID, roleId)) {
                OxygenMain.network().sendTo(new CPPlayerRolesChanged(roleId, OxygenHelperServer.getPlayerSharedData(playerUUID)), playerMP);
                this.manager.sendStatusMessage(playerMP, EnumOxygenStatusMessage.ROLE_ADDED_TO_PLAYER);

                if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                    OxygenMain.LOGGER.info("[Core] <{}/{}> added role ({}) to {}.",
                            CommonReference.getName(playerMP),
                            CommonReference.getPersistentUUID(playerMP),
                            roleId,
                            playerUUID);
            }
        }
    }

    public void removeRoleFromPlayer(EntityPlayerMP playerMP, UUID playerUUID, int roleId) {
        if (CommonReference.isPlayerOpped(playerMP)
                && OxygenHelperServer.getPlayerSharedData(playerUUID) != null) {
            if (this.removeRoleFromPlayer(playerUUID, roleId)) {
                OxygenMain.network().sendTo(new CPPlayerRolesChanged(roleId, OxygenHelperServer.getPlayerSharedData(playerUUID)), playerMP);
                this.manager.sendStatusMessage(playerMP, EnumOxygenStatusMessage.ROLE_REMOVED_FROM_PLAYER);

                if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                    OxygenMain.LOGGER.info("[Core] <{}/{}> removed role ({}) from {}.",
                            CommonReference.getName(playerMP),
                            CommonReference.getPersistentUUID(playerMP),
                            roleId,
                            playerUUID);
            }
        }
    }
}
