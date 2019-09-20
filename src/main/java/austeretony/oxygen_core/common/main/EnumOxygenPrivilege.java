package austeretony.oxygen_core.common.main;

import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.privilege.PrivilegeRegistry;
import austeretony.oxygen_core.server.api.PrivilegeProviderServer;

public enum EnumOxygenPrivilege {

    EXPOSE_PLAYERS_OFFLINE("exposePlayersOffline", EnumValueType.BOOLEAN);

    private final String name;

    private final EnumValueType type;

    EnumOxygenPrivilege(String name, EnumValueType type) {
        this.name = "core:" + name;
        this.type = type;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static void register() {
        for (EnumOxygenPrivilege privilege : EnumOxygenPrivilege.values())
            PrivilegeRegistry.registerPrivilege(privilege.name, privilege.type);
    }
}
