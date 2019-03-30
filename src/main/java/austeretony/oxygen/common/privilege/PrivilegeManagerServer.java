package austeretony.oxygen.common.privilege;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.privilege.api.PrivilegedGroup;
import austeretony.oxygen.common.privilege.io.PrivilegeIOServer;

public class PrivilegeManagerServer {

    private final Map<UUID, String> players = new ConcurrentHashMap<UUID, String>();

    private final Map<String, IPrivilegedGroup> groups = new ConcurrentHashMap<String, IPrivilegedGroup>();

    private PrivilegeIOServer privilegeIO;

    private PrivilegeManagerServer() {}

    public static PrivilegeManagerServer create() {
        return new PrivilegeManagerServer();
    }

    public static PrivilegeManagerServer instance() {
        return OxygenMain.getPrivilegeManagerServer();
    }

    public void initIO() {
        this.privilegeIO = PrivilegeIOServer.create();
    }

    public PrivilegeIOServer getIO() {
        return this.privilegeIO;
    }

    public Map<UUID, String> getPlayers() {
        return this.players;
    }

    public void promotePlayer(UUID playerUUID, String groupName) {
        if (groupName.equals(PrivilegedGroup.DEFAULT_GROUP.getGroupName()))
            this.players.remove(playerUUID);
        else
            this.players.put(playerUUID, groupName);
        PrivilegeIOServer.instance().savePlayerListDelegated();
    }

    public Map<String, IPrivilegedGroup> getGroups() {
        return this.groups;
    }

    public boolean groupExist(String groupName) {
        return this.groups.containsKey(groupName);
    }

    public IPrivilegedGroup getGroup(String groupName) {
        return this.groups.get(groupName);
    }

    public void addGroup(IPrivilegedGroup group) {
        if (!this.groupExist(group.getGroupName()))
            this.groups.put(group.getGroupName(), group);
    }

    public void addDefaultGroup() {
        if (!this.groupExist(PrivilegedGroup.DEFAULT_GROUP.getGroupName())) {
            PrivilegeProviderServer.addGroup(PrivilegedGroup.DEFAULT_GROUP);
            PrivilegeIOServer.instance().savePrivilegedGroupsDelegated();
        }
    }

    public void removeGroup(String groupName) {
        this.groups.remove(groupName);
        PrivilegeIOServer.instance().savePrivilegedGroupsDelegated();
    }

    private boolean havePrivileges(UUID playerUUID) {
        return this.players.containsKey(playerUUID);
    }

    public IPrivilegedGroup getPlayerPrivilegedGroup(UUID playerUUID) {
        if (this.havePrivileges(playerUUID))
            return this.groups.get(this.players.get(playerUUID));
        else 
            return this.groups.get(PrivilegedGroup.DEFAULT_GROUP.getGroupName());
    }
}
