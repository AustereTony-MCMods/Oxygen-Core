package austeretony.oxygen_core.common.privilege;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import austeretony.oxygen_core.common.EnumValueType;

public class PrivilegeRegistry {

    private static final Map<Integer, PrivilegeRegistryEntry> REGISTRY = new HashMap<>();

    public static Collection<PrivilegeRegistryEntry> getRegisteredPrivileges() {
        return REGISTRY.values();
    }

    public static void registerPrivilege(String name, int id, EnumValueType type) {
        REGISTRY.put(id, new PrivilegeRegistryEntry(name, id, type));
    }

    public static PrivilegeRegistryEntry getRegistryEntry(int id) {
        return REGISTRY.get(id);
    }

    public static class PrivilegeRegistryEntry {

        public final String name;

        public final int id;

        public final EnumValueType type;

        PrivilegeRegistryEntry(String name, int id, EnumValueType type) {
            this.name = name;
            this.id = id;
            this.type = type;
        }
    }
}
