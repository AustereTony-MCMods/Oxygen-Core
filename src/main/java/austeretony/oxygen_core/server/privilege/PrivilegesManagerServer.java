package austeretony.oxygen_core.server.privilege;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncGroup;
import austeretony.oxygen_core.common.privilege.PrivilegedGroup;
import austeretony.oxygen_core.common.privilege.PrivilegedGroupImpl;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PrivilegesManagerServer {

    private final Map<UUID, String> players = new ConcurrentHashMap<>();

    private final Map<String, PrivilegedGroup> groups = new ConcurrentHashMap<>(5);

    public Map<UUID, String> getPlayers() {
        return this.players;
    }

    public void syncGroup(EntityPlayerMP playerMP) {
        OxygenMain.network().sendTo(new CPSyncGroup(this.getPlayerPrivilegedGroup(CommonReference.getPersistentUUID(playerMP))), playerMP);
    }

    public void promotePlayer(UUID playerUUID, String groupName) {
        if (groupName.equals(PrivilegedGroupImpl.DEFAULT_GROUP.getName()))
            this.players.remove(playerUUID);
        else
            this.players.put(playerUUID, groupName);
        PrivilegesLoaderServer.savePlayersListAsync();
        if (OxygenHelperServer.isPlayerOnline(playerUUID))
            OxygenMain.network().sendTo(new CPSyncGroup(this.getGroup(groupName)), CommonReference.playerByUUID(playerUUID));
    }

    public Map<String, PrivilegedGroup> getGroups() {
        return this.groups;
    }

    public boolean groupExist(String groupName) {
        return this.groups.containsKey(groupName);
    }

    public PrivilegedGroup getGroup(String groupName) {
        return this.groups.get(groupName);
    }

    public void addGroup(PrivilegedGroup group, boolean save) {
        if (!this.groupExist(group.getName())) {
            this.groups.put(group.getName(), group);
            if (save)
                PrivilegesLoaderServer.savePrivilegedGroupsAsync();
        }
    }

    public void addDefaultGroups() {
        this.addGroup(PrivilegedGroupImpl.DEFAULT_GROUP, false);
        this.addGroup(PrivilegedGroupImpl.OPERATORS_GROUP, false);
    }

    public void removeGroup(String groupName) {
        this.groups.remove(groupName);
        PrivilegesLoaderServer.savePrivilegedGroupsAsync();
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

    public PrivilegedGroup getPlayerPrivilegedGroup(UUID playerUUID) {
        if (this.havePrivileges(playerUUID))
            return this.groups.get(this.players.get(playerUUID));
        else 
            return this.groups.get(PrivilegedGroupImpl.DEFAULT_GROUP.getName());
    }
}
