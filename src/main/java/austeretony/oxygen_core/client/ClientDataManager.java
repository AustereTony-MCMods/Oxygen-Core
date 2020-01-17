package austeretony.oxygen_core.client;

import austeretony.oxygen_core.common.EnumActivityStatus;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.server.SPSetActivityStatus;

public class ClientDataManager {

    public void changeActivityStatusSynced(EnumActivityStatus status) {
        OxygenMain.network().sendToServer(new SPSetActivityStatus(status));
    }
}
