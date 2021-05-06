package austeretony.oxygen_core.common.sync;

import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.util.value.*;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public final class SyncMeta {

    private final Map<String, TypedValue> valuesMap = new HashMap<>(1);

    private SyncMeta() {}

    public static Builder builder() {
        return new SyncMeta().new Builder();
    }

    public void write(ByteBuf buffer) {
        buffer.writeByte(valuesMap.size());
        for (Map.Entry<String, TypedValue> entry : valuesMap.entrySet()) {
            ByteBufUtils.writeString(entry.getKey(), buffer);
            ValueType.write(entry.getValue(), buffer);
        }
    }

    public static SyncMeta read(ByteBuf buffer) {
        SyncMeta meta = new SyncMeta();
        int amount = buffer.readByte();
        for (int i = 0; i < amount; i++) {
            meta.valuesMap.put(ByteBufUtils.readString(buffer), ValueType.read(buffer));
        }
        return meta;
    }

    public <T> T getValue(String id, T defaultValue) {
        TypedValue<T> value = valuesMap.get(id);
        if (value != null) {
            return value.getValue();
        }
        return defaultValue;
    }

    public class Builder {

        public Builder withBoolean(String id, boolean value) {
            SyncMeta.this.valuesMap.put(id, new BooleanValue(value));
            return this;
        }

        public Builder withByte(String id, int value) {
            SyncMeta.this.valuesMap.put(id, new ByteValue(value));
            return this;
        }

        public Builder withShort(String id, int value) {
            SyncMeta.this.valuesMap.put(id, new ShortValue(value));
            return this;
        }

        public Builder withInt(String id, int value) {
            SyncMeta.this.valuesMap.put(id, new IntegerValue(value));
            return this;
        }

        public Builder withLong(String id, long value) {
            SyncMeta.this.valuesMap.put(id, new LongValue(value));
            return this;
        }

        public Builder withFloat(String id, float value) {
            SyncMeta.this.valuesMap.put(id, new FloatValue(value));
            return this;
        }

        public Builder withDouble(String id, double value) {
            SyncMeta.this.valuesMap.put(id, new DoubleValue(value));
            return this;
        }

        public Builder withString(String id, String value) {
            SyncMeta.this.valuesMap.put(id, new StringValue(value));
            return this;
        }

        public SyncMeta build() {
            return SyncMeta.this;
        }
    }
}
