package austeretony.oxygen.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.util.StreamUtils;

public class ObservedPlayersContainer {

    private final Map<UUID, Integer> observed = new ConcurrentHashMap<UUID, Integer>();

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
            OxygenMain.OXYGEN_LOGGER.info("Adding observed player <{}> - {} time.", playerUUID, count);//debug
        } else {
            this.observed.put(playerUUID, 1);
            OxygenMain.OXYGEN_LOGGER.info("Adding observed player <{}> - first time.", playerUUID);//debug
        }
    }

    public void removeObservedPlayer(UUID playerUUID) {
        if (this.observed.containsKey(playerUUID)) {
            int count = this.observed.get(playerUUID) - 1;
            if (count > 0) {
                this.observed.put(playerUUID, count);
                OxygenMain.OXYGEN_LOGGER.info("Removing observed player <{}>, {} entries left.", playerUUID, count);//debug
            } else {
                this.observed.remove(playerUUID);
                OxygenMain.OXYGEN_LOGGER.info("Removing observed player <{}>, no entries left.", playerUUID);//debug
            }
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
            container.observed.put(StreamUtils.readUUID(bis), (int) StreamUtils.readByte(bis));
        return container;
    }
}
