package austeretony.oxygen_core.common.privileges;

import austeretony.oxygen_core.common.util.value.ValueType;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class PrivilegeRegistry {

    private static final Map<Integer, Entry> PRIVILEGES = new HashMap<>();

    public static Entry register(int id, String displayName, ValueType valueType) {
        Entry privilegeEntry = new Entry(id, displayName, valueType);
        PRIVILEGES.put(id, privilegeEntry);
        return privilegeEntry;
    }

    public static Map<Integer, Entry> getRegistryMap() {
        return PRIVILEGES;
    }

    @Nullable
    public static Entry getEntry(int id) {
        return getRegistryMap().get(id);
    }

    public static class Entry {

        private final int id;
        private final String displayName;
        private final ValueType valueType;

        public Entry(int id, String displayName, ValueType valueType) {
            this.id = id;
            this.displayName = displayName;
            this.valueType = valueType;
        }

        public int getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public ValueType getValueType() {
            return valueType;
        }
    }
}
