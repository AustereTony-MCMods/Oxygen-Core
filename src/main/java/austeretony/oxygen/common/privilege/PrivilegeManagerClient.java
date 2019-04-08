package austeretony.oxygen.common.privilege;

import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenManagerClient;
import austeretony.oxygen.common.network.server.SPGroupSyncRequest;
import austeretony.oxygen.common.privilege.io.PrivilegeIOClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PrivilegeManagerClient {

    private PrivilegeIOClient privilegeIO;

    private IPrivilegedGroup group;  

    private PrivilegeManagerClient() {}

    public static PrivilegeManagerClient create() {
        return new PrivilegeManagerClient();
    }

    public static PrivilegeManagerClient instance() {
        return OxygenManagerClient.instance().getPrivilegeManager();
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
