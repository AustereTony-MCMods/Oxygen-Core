package austeretony.oxygen_core.server.sync.watcher;

import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.packets.client.CPSyncWatcherValues;
import austeretony.oxygen_core.common.util.value.TypedValue;
import austeretony.oxygen_core.common.util.value.ValueType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class WatcherManagerServer {

    private final Map<Integer, WatcherValue> valuesMap = new HashMap<>();

    public void registerValue(int id, ValueType type) {
        valuesMap.put(id, new WatcherValue(type));
    }

    public <T> void updateValue(UUID playerUUID, int id, T newValue) {
        synchronized (playerUUID) { //TODO Proper synchronization
            WatcherValue watcherValue = valuesMap.get(id);
            if (watcherValue != null) {
                watcherValue.update(playerUUID, newValue);
            }
        }
    }

    public <T> T getValue(UUID playerUUID, int id, T def) {
        synchronized (playerUUID) {
            WatcherValue watcherValue = valuesMap.get(id);
            if (watcherValue != null) {
                return (T) watcherValue.getOrCreate(playerUUID).getValue();
            }
            return def;
        }
    }

    public void syncData(EntityPlayerMP playerMP) {
        UUID playerUUID = MinecraftCommon.getEntityUUID(playerMP);
        synchronized (playerUUID) {
            ByteBuf buffer = null;
            try {
                buffer = Unpooled.buffer();
                for (WatcherValue watcherValue : valuesMap.values()) {
                    watcherValue.write(playerUUID, buffer);
                }

                byte[] bytes = new byte[buffer.writerIndex()];
                buffer.getBytes(0, bytes);
                OxygenMain.network().sendTo(new CPSyncWatcherValues(bytes), playerMP);
            } finally {
                if (buffer != null) {
                    buffer.release();
                }
            }
        }
    }

    public void playerLoggedOut(UUID playerUUID) {
        synchronized (playerUUID) {
            for (WatcherValue watcherValue : valuesMap.values()) {
                watcherValue.playerLoggedOut(playerUUID);
            }
        }
    }

    private static class WatcherValue<T> {

        private final ValueType type;
        private final Map<UUID, TypedValue<T>> playersMap = new HashMap<>();

        WatcherValue(ValueType type) {
            this.type = type;
        }

        private TypedValue<T> getOrCreate(UUID playerUUID) {
            TypedValue<T> value = playersMap.get(playerUUID);
            if (value == null) {
                value = ValueType.createValue(type);
                playersMap.put(playerUUID, value);
            }
            return value;
        }

        void update(UUID playerUUID, T value) {
            getOrCreate(playerUUID).setValue(value);
        }

        void write(UUID playerUUID, ByteBuf buffer) {
            getOrCreate(playerUUID).write(buffer);
        }

        void playerLoggedOut(UUID playerUUID) {
            playersMap.remove(playerUUID);
        }
    }
}
