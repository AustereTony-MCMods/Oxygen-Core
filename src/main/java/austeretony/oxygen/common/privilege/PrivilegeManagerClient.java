package austeretony.oxygen.common.privilege;

import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.server.SPOxygenRequest;

public class PrivilegeManagerClient {

    private IPrivilegedGroup group;  

    public void setPrivelegedGroup(IPrivilegedGroup group) {
        this.group = group;
        OxygenMain.OXYGEN_LOGGER.info("Privileged group set to <{}>.", group.getName());
    }

    public IPrivilegedGroup getPrivilegedGroup() {
        return this.group;
    }

    public void requestGroupSync() {
        OxygenMain.network().sendToServer(new SPOxygenRequest(SPOxygenRequest.EnumRequest.SYNC_PRIVILEGED_GROUP));
        OxygenMain.OXYGEN_LOGGER.info("Privileged group sync requested.");
    }
}
