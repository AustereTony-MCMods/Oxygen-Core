package austeretony.oxygen_core.client.privilege;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.server.SPOxygenRequest;
import austeretony.oxygen_core.common.privilege.PrivilegedGroup;
import austeretony.oxygen_core.common.privilege.PrivilegedGroupImpl;

public class PrivilegesManagerClient {

    private long groupId;

    private PrivilegedGroup group = PrivilegedGroupImpl.DEFAULT_GROUP;  

    public void init(long groupId) {
        this.groupId = groupId;
        PrivilegesLoaderClient.loadPrivilegeDataAsync();
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void groupSynced(PrivilegedGroup group) {
        this.setPrivelegedGroup(group);
        PrivilegesLoaderClient.savePrivilegedGroupAsync();
    }

    public void setPrivelegedGroup(PrivilegedGroup group) {
        this.group = group;
        OxygenMain.LOGGER.info("Privileged group set to <{}>.", group.getName());
    }

    public PrivilegedGroup getPrivilegedGroup() {
        return this.group;
    }

    public void requestGroupSync() {
        OxygenMain.network().sendToServer(new SPOxygenRequest(SPOxygenRequest.EnumRequest.SYNC_PRIVILEGED_GROUP));
        OxygenMain.LOGGER.info("Privileged group sync requested.");
    }
}
