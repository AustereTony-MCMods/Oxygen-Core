package austeretony.oxygen.common.main;

import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;

public enum EnumOxygenPrivilege {

    EXPOSE_PLAYERS_OFFLINE(":exposePlayersOffline");

    private final String name;

    EnumOxygenPrivilege(String name) {
        this.name = name;
        PrivilegeProviderServer.registerPrivilege(OxygenMain.MODID + name, OxygenMain.NAME);
    }

    @Override
    public String toString() {
        return OxygenMain.MODID + this.name;
    }
}
