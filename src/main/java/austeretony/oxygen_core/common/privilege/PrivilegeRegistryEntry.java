package austeretony.oxygen_core.common.privilege;

import austeretony.oxygen_core.common.EnumValueType;

public class PrivilegeRegistryEntry {

    public final String name;

    public final EnumValueType type;

    public PrivilegeRegistryEntry(String name, EnumValueType type) {
        this.name = name;
        this.type = type;
    }
}
