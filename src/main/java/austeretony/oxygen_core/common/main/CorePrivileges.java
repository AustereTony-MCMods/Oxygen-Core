package austeretony.oxygen_core.common.main;

import austeretony.oxygen_core.common.privileges.PrivilegeRegistry;
import austeretony.oxygen_core.common.util.value.ValueType;

public final class CorePrivileges {

    public static final PrivilegeRegistry.Entry EXPOSE_OFFLINE_PLAYERS =
            PrivilegeRegistry.register(0, "core:expose_offline_players", ValueType.BOOLEAN);

    private CorePrivileges() {}

    public static void register() {}
}
