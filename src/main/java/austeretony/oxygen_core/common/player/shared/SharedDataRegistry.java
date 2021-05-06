package austeretony.oxygen_core.common.player.shared;

import austeretony.oxygen_core.common.util.value.ValueType;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class SharedDataRegistry {

    private static final Map<Integer, ValueType> SHARED_VALUES = new LinkedHashMap<>();

    public static void register(int id, ValueType type) {
        SHARED_VALUES.put(id, type);
    }

    public static Map<Integer, ValueType> getRegistryMap() {
        return SHARED_VALUES;
    }

    @Nullable
    public static ValueType getEntry(int id) {
        return getRegistryMap().get(id);
    }
}
