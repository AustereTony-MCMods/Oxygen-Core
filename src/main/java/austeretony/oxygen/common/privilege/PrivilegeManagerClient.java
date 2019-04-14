package austeretony.oxygen.common.privilege;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.server.SPGroupSyncRequest;
import austeretony.oxygen.common.privilege.io.PrivilegeIOClient;

public class PrivilegeManagerClient {

    private final OxygenManagerClient manager;

    private PrivilegeIOClient privilegeIO;

    private IPrivilegedGroup group;  

    public PrivilegeManagerClient(OxygenManagerClient manager) {
        this.manager = manager;
    }

    public void initIO() {
        this.privilegeIO = PrivilegeIOClient.create();
    }

    public PrivilegeIOClient getIO() {
        return this.privilegeIO;
    }

    public void setPrivelegedGroup(IPrivilegedGroup group) {
        this.group = group;
        OxygenMain.PRIVILEGE_LOGGER.info("Group set to <{}>.", group.getName());
    }

    public IPrivilegedGroup getPrivilegedGroup() {
        return this.group;
    }

    public void requestGroupSync() {
        OxygenMain.network().sendToServer(new SPGroupSyncRequest());
        OxygenMain.PRIVILEGE_LOGGER.info("Privileged group sync requested.");
    }
}
