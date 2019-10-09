package austeretony.oxygen_core.server;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.server.api.OxygenHelperServer;

public class RequestsFilterServer {

    private final Map<UUID, RequestsContainer> containers = new ConcurrentHashMap<>();

    private static final Set<RegistryData> REGISTRY = new HashSet<>(); 

    public void registerRequest(int requestId, int expireTimeSeconds) {
        REGISTRY.add(new RegistryData(requestId, expireTimeSeconds));
    }

    public void registerPlayer(UUID playerUUID) {
        RequestsContainer container = new RequestsContainer();
        container.registerRequests();
        this.containers.put(playerUUID, container);
    }

    public void unregisterPlayer(UUID playerUUID) {
        this.containers.remove(playerUUID);
    }

    public boolean getLock(UUID playerUUID, int requestId) {
        return this.containers.get(playerUUID).getLock(requestId);
    }

    protected void process() {
        OxygenHelperServer.addRoutineTask(()->{
            for (RequestsContainer container : this.containers.values())
                container.process();
        });
    }

    private static class RegistryData {

        private final int requestId, expireTimeSeconds;

        public RegistryData(int requestId, int expireTimeSeconds) {
            this.requestId = requestId;
            this.expireTimeSeconds = expireTimeSeconds;
        }
    }

    private static class RequestsContainer {

        private final Map<Integer, RequestEntry> requests = new ConcurrentHashMap<>();

        private void registerRequests() {
            REGISTRY.forEach((data)->this.requests.put(data.requestId, new RequestEntry(data.requestId, data.expireTimeSeconds)));
        }

        private boolean getLock(int requestId) {
            return this.requests.get(requestId).getLock();
        }

        private void process() {
            for (RequestEntry entry : this.requests.values())
                entry.process();
        }
    }

    private static class RequestEntry {

        private final int requestId, lockTimeMillis;

        private volatile long expireTimeMillis;

        private volatile boolean unlocked;

        public RequestEntry(int requestId, int expireTimeSeconds) {
            this.requestId = requestId;
            this.lockTimeMillis = expireTimeSeconds * 1000;
        }

        private boolean getLock() {
            boolean unlocked = this.unlocked;
            if (unlocked)
                this.lock();
            return unlocked;
        }

        private void lock() {
            this.unlocked = false;
            this.expireTimeMillis = System.currentTimeMillis() + this.lockTimeMillis;
        }

        private void process() {
            if (!this.unlocked) {
                if (System.currentTimeMillis() >= this.expireTimeMillis)
                    this.unlocked = true;
            }
        }
    }
}