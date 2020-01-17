package austeretony.oxygen_core.common.main;

import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.privilege.PrivilegeRegistry;

public enum EnumOxygenPrivilege {

    EXPOSE_OFFLINE_PLAYERS("core:exposeOfflinePlayers", 10, EnumValueType.BOOLEAN);

    private final String name;

    private final int id;

    private final EnumValueType type;

    EnumOxygenPrivilege(String name, int id, EnumValueType type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public int id() {
        return id;
    }

    public static void register() {
        for (EnumOxygenPrivilege privilege : EnumOxygenPrivilege.values())
            PrivilegeRegistry.registerPrivilege(privilege.name, privilege.id, privilege.type);
    }
}
