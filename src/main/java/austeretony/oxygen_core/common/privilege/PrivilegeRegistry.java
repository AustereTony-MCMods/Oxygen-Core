package austeretony.oxygen_core.common.privilege;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import austeretony.oxygen_core.common.EnumValueType;

public class PrivilegeRegistry {

    private static final Map<String, PrivilegeRegistryEntry> REGISTRY = new HashMap<>();

    public static Set<String> getRegisteredPrivileges() {
        return REGISTRY.keySet();
    }

    public static boolean privilegeExist(String name) {
        return getRegisteredPrivileges().contains(name);
    }

    public static void registerPrivilege(String name, EnumValueType type) {
        REGISTRY.put(name, new PrivilegeRegistryEntry(name, type));
    }

    public static PrivilegeRegistryEntry getRegistryEntry(String name) {
        return REGISTRY.get(name);
    }
}
