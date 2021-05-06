package austeretony.oxygen_core.server.sync.observed;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.packets.client.CPSyncObservedEntitiesData;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.util.value.TypedValue;
import austeretony.oxygen_core.common.util.value.ValueType;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ObservedEntitiesDataSyncManagerServer {

    private final Map<Integer, ObservedValue> values = new LinkedHashMap<>();
    private final Multimap<UUID, Integer> observers = HashMultimap.create();

    public <T> void registerObservedValue(int id, ValueType type, Function<Integer, T> extractor) {
        values.put(id, new ObservedValue(type, extractor));
    }

    public <T> void registerObservedValue(int id, Supplier<? extends TypedValue> valueSupplier, Function<Integer, T> extractor) {
        values.put(id, new ObservedValue(valueSupplier, extractor));
    }

    public void startObservingEntity(UUID playerUUID, int observedEntityId) {
        synchronized (observers) {
            observers.put(playerUUID, observedEntityId);
            for (ObservedValue observed : values.values()) {
                observed.getOrCreate(observedEntityId);
            }
        }
    }

    public void stopObservingEntity(UUID playerUUID, int observedEntityId) {
        synchronized (observers) {
            observers.get(playerUUID).remove(observedEntityId);
            if (!observers.containsValue(observedEntityId)) {
                for (ObservedValue observed : values.values()) {
                    observed.remove(observedEntityId);
                }
            }
        }
    }

    public void playerLoggedIn(EntityPlayerMP playerMP) {
        startObservingEntity(MinecraftCommon.getEntityUUID(playerMP), MinecraftCommon.getEntityId(playerMP));
    }

    public void playerLoggedOut(EntityPlayerMP playerMP) {
        synchronized (observers) {
            Collection<Integer> entities = observers.removeAll(MinecraftCommon.getEntityUUID(playerMP));
            if (!entities.isEmpty()) {
                for (int entityId : entities) {
                    if (!observers.containsValue(entityId)) {
                        for (ObservedValue observed : values.values()) {
                            observed.remove(entityId);
                        }
                    }
                }
            }
        }
    }

    public void update() {
        if (MinecraftCommon.getServer() == null) return;
        final Runnable task = () -> {
            synchronized (observers) {
                collectData();
                sync();
            }
        };
        MinecraftCommon.delegateToServerThread(task);
    }

    private void collectData() {
        for (ObservedValue observed : values.values()) {
            observed.collectData();
        }
    }

    private void sync() {
        for (Map.Entry<UUID, Collection<Integer>> entry : observers.asMap().entrySet()) {
            EntityPlayerMP playerMP = MinecraftCommon.getPlayerByUUID(entry.getKey());
            if (playerMP != null) {
                ByteBuf buffer = null;
                try {
                    buffer = Unpooled.buffer();

                    buffer.writeShort(entry.getValue().size());
                    for (int entityId : entry.getValue()) {
                        buffer.writeInt(entityId);
                        for (ObservedValue value : values.values()) {
                            value.getOrCreate(entityId).write(buffer);
                        }
                    }

                    byte[] dataRaw = new byte[buffer.readableBytes()];
                    buffer.getBytes(0, dataRaw);
                    OxygenMain.network().sendTo(new CPSyncObservedEntitiesData(dataRaw), playerMP);
                } finally {
                    if (buffer != null) {
                        buffer.release();
                    }
                }
            }
        }
    }

    static class ObservedValue<T> {

        private final ValueType type;
        @Nullable
        private final Supplier<? extends TypedValue> valueSupplier;
        private final Function<Integer, T> extractor;
        private final Map<Integer, TypedValue<T>> entities = new HashMap<>();

        ObservedValue(ValueType type, Function<Integer, T> extractor) {
            this.type = type;
            valueSupplier = null;
            this.extractor = extractor;
        }

        ObservedValue(@Nullable Supplier<? extends TypedValue> valueSupplier, Function<Integer, T> extractor) {
            type = ValueType.CUSTOM;
            this.valueSupplier = valueSupplier;
            this.extractor = extractor;
        }

        TypedValue<T> getOrCreate(int entityId) {
            TypedValue<T> value = entities.get(entityId);
            if (value == null) {
                if (valueSupplier == null) {
                    value = ValueType.createValue(type);
                } else {
                    value = valueSupplier.get();
                }
                entities.put(entityId, value);
            }
            return value;
        }

        void remove(int entityId) {
            entities.remove(entityId);
        }

        void collectData() {
            for (Map.Entry<Integer, TypedValue<T>> entry : entities.entrySet()) {
                T value = extractor.apply(entry.getKey());
                if (value != null) {
                    entry.getValue().setValue(value);
                }
            }
        }
    }
}
