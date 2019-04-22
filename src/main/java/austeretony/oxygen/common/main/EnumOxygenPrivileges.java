package austeretony.oxygen.common.main;

import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;

public enum EnumOxygenPrivileges {

    PREVENT_IGNORE(":preventIgnore"),
    EXPOSE_PLAYERS_OFFLINE(":exposePlayersOffline");

    private final String name;

    EnumOxygenPrivileges(String name) {
        this.name = name;
        PrivilegeProviderServer.registerPrivilege(OxygenMain.MODID + name, OxygenMain.NAME);
    }

    @Override
    public String toString() {
        return OxygenMain.MODID + this.name;
    }
}
