package austeretony.oxygen.common.privilege;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.server.SPOxygenRequest;
import austeretony.oxygen.common.privilege.io.PrivilegeLoaderClient;

public class PrivilegeManagerClient {

    private final OxygenManagerClient manager;

    private PrivilegeLoaderClient privilegeIO;

    private IPrivilegedGroup group;  

    public PrivilegeManagerClient(OxygenManagerClient manager) {
        this.manager = manager;
    }

    public void setPrivelegedGroup(IPrivilegedGroup group) {
        this.group = group;
        OxygenMain.PRIVILEGE_LOGGER.info("Group set to <{}>.", group.getName());
    }

    public IPrivilegedGroup getPrivilegedGroup() {
        return this.group;
    }

    public void requestGroupSync() {
        OxygenMain.network().sendToServer(new SPOxygenRequest(SPOxygenRequest.EnumRequest.SYNC_PRIVILEGED_GROUP));
        OxygenMain.PRIVILEGE_LOGGER.info("Privileged group sync requested.");
    }
}
