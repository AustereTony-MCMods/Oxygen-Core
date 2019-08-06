package austeretony.oxygen.common.privilege;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.client.CPSyncGroup;
import austeretony.oxygen.common.privilege.api.PrivilegedGroup;
import austeretony.oxygen.common.privilege.io.PrivilegeLoaderServer;

public class PrivilegeManagerServer {

    public static final Map<String, String> PRIVILEGES_REGISTRY = new HashMap<String, String>();

    private final Map<UUID, String> players = new ConcurrentHashMap<UUID, String>();

    private final Map<String, IPrivilegedGroup> groups = new ConcurrentHashMap<String, IPrivilegedGroup>(5);

    public Map<UUID, String> getPlayers() {
        return this.players;
    }

    public void promotePlayer(UUID playerUUID, String groupName) {
        if (groupName.equals(PrivilegedGroup.DEFAULT_GROUP.getName()))
            this.players.remove(playerUUID);
        else
            this.players.put(playerUUID, groupName);
        PrivilegeLoaderServer.savePlayerListDelegated();
        if (OxygenHelperServer.isOnline(playerUUID))
            OxygenMain.network().sendTo(new CPSyncGroup(), CommonReference.playerByUUID(playerUUID));
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

    public void addGroup(IPrivilegedGroup group, boolean save) {
        if (!this.groupExist(group.getName())) {
            this.groups.put(group.getName(), group);
            if (save)
                PrivilegeLoaderServer.savePrivilegedGroupsDelegated();
        }
    }

    public void addDefaultGroups() {
        this.addGroup(PrivilegedGroup.DEFAULT_GROUP, false);
        this.addGroup(PrivilegedGroup.OPERATORS_GROUP, false);
    }

    public void removeGroup(String groupName) {
        this.groups.remove(groupName);
        PrivilegeLoaderServer.savePrivilegedGroupsDelegated();
    }

    private boolean havePrivileges(UUID playerUUID) {
        if (this.players.containsKey(playerUUID)) {
            if (this.groups.containsKey(this.players.get(playerUUID)))
                return true;
            else {
                this.players.remove(playerUUID);
                return false;
            }
        } else
            return false;
    }

    public IPrivilegedGroup getPlayerPrivilegedGroup(UUID playerUUID) {
        if (this.havePrivileges(playerUUID))
            return this.groups.get(this.players.get(playerUUID));
        else 
            return this.groups.get(PrivilegedGroup.DEFAULT_GROUP.getName());
    }
}
