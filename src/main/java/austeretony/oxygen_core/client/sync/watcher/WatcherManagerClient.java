package austeretony.oxygen_core.client.sync.watcher;

import austeretony.oxygen_core.common.util.value.TypedValue;
import austeretony.oxygen_core.common.util.value.ValueType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Map;

public final class WatcherManagerClient {

    private final Map<Integer, TypedValue> valuesMap = new HashMap<>(3);

    public void registerValue(int id, ValueType type) {
        valuesMap.put(id, ValueType.createValue(type));
    }

    public <T> void setValue(int id, T value) {
        TypedValue val = valuesMap.get(id);
        if (val != null) {
            val.setValue(value);
        }
    }

    public <T> T getValue(int id, T defaultValue) {
        TypedValue<T> value = valuesMap.get(id);
        if (value != null) {
            return value.getValue();
        }
        return defaultValue;
    }

    public void update(byte[] bytes) {
        ByteBuf buffer = null;
        try {
            buffer = Unpooled.wrappedBuffer(bytes);
            for (TypedValue value : valuesMap.values()) {
                value.read(buffer);
            }
        } finally {
            if (buffer != null) {
                buffer.release();
            }
        }
    }
}
