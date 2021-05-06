package austeretony.oxygen_core.server.network;

import austeretony.oxygen_core.server.api.OxygenServer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class NetworkManagerServer {

    private final Map<Integer, RequestEntry> requestsMap = new HashMap<>();

    public void registerRequest(int id, int coolDownMillis) {
        requestsMap.put(id, new RequestEntry(coolDownMillis));
    }

    public boolean isRequestAvailable(int id, UUID playerUUID) {
        RequestEntry request = requestsMap.get(id);
        if (request != null) {
            return request.isRequestAvailable(playerUUID);
        }
        return false;
    }

    public void playerLoggedOut(UUID playerUUID) {
        for (RequestEntry request : requestsMap.values()) {
            request.playerLoggedOut(playerUUID);
        }
    }

    private static class RequestEntry {

        private final int coolDownMillis;

        private final Map<UUID, Long> playersMap = new ConcurrentHashMap<>();

        RequestEntry(int coolDownMillis) {
            this.coolDownMillis = coolDownMillis;
        }

        boolean isRequestAvailable(UUID playerUUID) { //TODO Proper synchronization
            long timeStamp = playersMap.getOrDefault(playerUUID, 0L);
            long now = OxygenServer.getCurrentTimeMillis();
            if (now > timeStamp + coolDownMillis) {
                playersMap.put(playerUUID, now);
                return true;
            }
            return false;
        }

        void playerLoggedOut(UUID playerUUID) {
            playersMap.remove(playerUUID);
        }
    }
}
