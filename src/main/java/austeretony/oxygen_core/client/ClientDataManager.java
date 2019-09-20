package austeretony.oxygen_core.client;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.server.SPChangeActivityStatus;
import austeretony.oxygen_core.server.OxygenPlayerData.EnumActivityStatus;

public class ClientDataManager {

    public void changeActivityStatusSynced(EnumActivityStatus status) {
        OxygenMain.network().sendToServer(new SPChangeActivityStatus(status));
    }
}