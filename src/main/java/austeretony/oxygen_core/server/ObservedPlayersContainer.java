package austeretony.oxygen_core.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.common.util.StreamUtils;

public class ObservedPlayersContainer {

    private final Map<UUID, Integer> observed = new ConcurrentHashMap<>();

    public int getObservedPlayersAmount() {
        return this.observed.size();
    }

    public Set<UUID> getObservedPlayers() {
        return this.observed.keySet();
    }

    public boolean isEmpty() {
        return this.observed.isEmpty();
    }

    public void addObservedPlayer(UUID playerUUID) {
        if (this.observed.containsKey(playerUUID)) {
            int count = this.observed.get(playerUUID) + 1;
            this.observed.put(playerUUID, count);
        } else
            this.observed.put(playerUUID, 1);
    }

    public void removeObservedPlayer(UUID playerUUID) {
        if (this.observed.containsKey(playerUUID)) {
            int count = this.observed.get(playerUUID) - 1;
            if (count > 0)
                this.observed.put(playerUUID, count);
            else
                this.observed.remove(playerUUID);
        }
    }

    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write((short) this.observed.size(), bos);
        for (Map.Entry<UUID, Integer> entry : this.observed.entrySet()) {
            StreamUtils.write(entry.getKey(), bos);
            StreamUtils.write(entry.getValue().byteValue(), bos);
        }
    }

    public static ObservedPlayersContainer read(BufferedInputStream bis) throws IOException {
        ObservedPlayersContainer container = new ObservedPlayersContainer();
        int amount = StreamUtils.readShort(bis);
        for (int i = 0; i < amount; i++)
            container.observed.put(StreamUtils.readUUID(bis), StreamUtils.readByte(bis));
        return container;
    }
}