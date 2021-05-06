package austeretony.oxygen_core.server;

import austeretony.oxygen_core.server.api.OxygenServer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TimeoutManagerServer {

    private final Map<Integer, TimeoutEntry> timeouts = new HashMap<>(5);

    public void registerTimeout(int id, int timeoutMillis) {
        timeouts.put(id, new TimeoutEntry(timeoutMillis));
    }

    public void resetTimeout(int id, UUID playerUUID) {
        TimeoutEntry timeout = timeouts.get(id);
        if (timeout != null) {
            timeout.resetTimeout(playerUUID);
        }
    }

    public boolean isTimeout(int id, UUID playerUUID) {
        TimeoutEntry timeout = timeouts.get(id);
        if (timeout != null) {
            return timeout.isTimeout(playerUUID);
        }
        return false;
    }

    public void playerLoggedOut(UUID playerUUID) {
        for (TimeoutEntry timeout : timeouts.values()) {
            timeout.playerLoggedOut(playerUUID);
        }
    }

    private static class TimeoutEntry {

        private final int timeoutMillis;

        private final Map<UUID, Long> players = new HashMap<>();

        TimeoutEntry(int timeoutMillis) {
            this.timeoutMillis = timeoutMillis;
        }

        void resetTimeout(UUID playerUUID) {
            players.put(playerUUID, OxygenServer.getCurrentTimeMillis());
        }

        boolean isTimeout(UUID playerUUID) {
            long timeStamp = players.getOrDefault(playerUUID, 0L);
            long now = OxygenServer.getCurrentTimeMillis();
            return now > timeStamp + timeoutMillis;
        }

        void playerLoggedOut(UUID playerUUID) {
            players.remove(playerUUID);
        }
    }
}
