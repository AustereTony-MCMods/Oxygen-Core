package austeretony.oxygen_core.client.sync.observed;

import austeretony.oxygen_core.common.util.value.TypedValue;
import austeretony.oxygen_core.common.util.value.ValueType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ObservedEntitiesDataSyncManagerClient {

    private final Map<Integer, ObservedValue> valuesMap = new LinkedHashMap<>();

    public <T> void registerObservedValue(int id, ValueType type) {
        valuesMap.put(id, new ObservedValue(type));
    }

    public <T> void registerObservedValue(int id, Supplier<? extends TypedValue> valueSupplier) {
        valuesMap.put(id, new ObservedValue(valueSupplier));
    }

    public <T> T getObservedValue(int id, int entityId, T defaultValue) {
        ObservedValue observed = valuesMap.get(id);
        if (observed != null) {
            return (T) observed.getOrCreate(entityId).getValue();
        }
        return defaultValue;
    }

    public void update(byte[] data) {
        ByteBuf buffer = null;
        try {
            buffer = Unpooled.wrappedBuffer(data);
            if (buffer.readableBytes() != 0) {
                int amount = buffer.readShort();
                for (int i = 0; i < amount; i++) {
                    int entityId = buffer.readInt();
                    for (ObservedValue observed : valuesMap.values()) {
                        observed.getOrCreate(entityId).read(buffer);
                    }
                }
            }
        } finally {
            if (buffer != null) {
                buffer.release();
            }
        }
    }

    static class ObservedValue<T> {

        private final ValueType type;
        @Nullable
        private final Supplier<? extends TypedValue> valueSupplier;
        private final Map<Integer, TypedValue<T>> entitiesMap = new HashMap<>();

        ObservedValue(ValueType type) {
            this.type = type;
            valueSupplier = null;
        }

        ObservedValue(Supplier<? extends TypedValue> valueSupplier) {
            type = ValueType.CUSTOM;
            this.valueSupplier = valueSupplier;
        }

        TypedValue<T> getOrCreate(int entityId) {
            TypedValue<T> value = entitiesMap.get(entityId);
            if (value == null) {
                if (valueSupplier == null) {
                    value = ValueType.createValue(type);
                } else {
                    value = valueSupplier.get();
                }
                entitiesMap.put(entityId, value);
            }
            return value;
        }
    }
}
